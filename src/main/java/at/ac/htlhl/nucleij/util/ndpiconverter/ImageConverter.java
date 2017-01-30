/* 
NDPI to OME-TIFF Converter
Tool for converting a selected image of a NDPI file to an OME-TIFF.

Copyright (C) 2012  Matthias Baldauf & Andreas Dander

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.ac.htlhl.nucleij.util.ndpiconverter;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import loci.common.services.ServiceFactory;
import loci.formats.FormatTools;
import loci.formats.MetadataTools;
import loci.formats.meta.IMetadata;
import loci.formats.out.OMETiffWriter;
import loci.formats.out.TiffWriter;
import loci.formats.services.OMEXMLService;
import loci.formats.tiff.IFD;
import ome.xml.model.primitives.Color;
import ome.xml.model.primitives.PositiveFloat;

import org.libjpegturbo.turbojpeg.TJ;
import org.libjpegturbo.turbojpeg.TJDecompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.MultiStepRescaleOp;

/**
 * Converts a selected JPEG image of the NDPI file to an OME-TIFF.
 * 
 * <p>
 * <u>Common problem with large NDPI files:</u> NDPI keeps the images as JPEG
 * files. The maximum edge length of a JPEG image by specification is 65536
 * pixels (2^16 bit). When using large magnifications or scanning large areas of
 * the slide, the image size easily exceeds this limit, resulting in an invalid
 * JPEG image as width and/or height of the image are set to zero. JPEG-decoders
 * can't process these images without modification.
 * </p>
 * 
 * <p>
 * <u>Approach:</u> Restart markers of a JPEG image allow reading and
 * decompressing the image in tiles. Only the largest images in the NDPI file
 * have restart markers. Therefore, only these images can be converted. This
 * class reads the JPEG image tile per tile, decompressing only one tile at once
 * and not the whole image (which would need large amounts of memory). As the
 * JPEG images use 4:4:4 YCbCr subsampling, a minimum coded unit (MCU) consists
 * of 8x8 pixels. Therefore, the height of the smallest possible tile to read
 * from the JPEG image is 8 pixels. The width of the tile is 8x restart interval
 * (number of MCUs between restart markers). The tiles are valid JPEG images and
 * are created by using a modified JPEG header of the image (height and width
 * are set to tile width and height). In order to create the OME-TIFF, the tiles
 * get decompressed and reassembled. The the resulting OME-TIFF is a valid TIFF
 * and can be viewed with any image viewer which supports the TIFF format and
 * the used TIFF compression.
 * </p>
 * 
 * <p>
 * <u>This class supports the conversion of the selected JPEG image to the
 * following OME-TIFF formats:</u>
 * <ul>
 * <li>Multipage, striped OME-TIFF (downscaled or original size)</li>
 * <li>Singlepage, striped OME-TIFF (downscaled or original size)</li>
 * <li>Multipage, tiled OME-TIFF</li>
 * <li>Singlepage, tiled OME-TIFF</li>
 * </ul>
 * 
 * <u>Multipage:</u> Resulting OME-TIFF consists of three grayscale images (one
 * image for each RGB color channel).<br>
 * <u>Singlepage:</u> Resulting OME-TIFF consists of one RGB image.<br>
 * <u>Strips:</u> Image data get organized into strips. Strip width: image
 * width, strip height: MCU height.<br>
 * <u>Tiles:</u> Image data get organizes into tiles. Tile width and height: 8x
 * restart interval.
 * </p>
 * 
 * @author Matthias Baldauf
 * 
 */
public class ImageConverter extends SwingWorker<Void, String> {

	private NDPIConverterGUI gui;
	private ImageInfo imgInfo;
	private File ndpiFile;
	private byte[] jpegTileHeader;
	private int outputHeight;
	private int outputWidth;
	private int outputStripHeight;
	private int samplesPerPixel;
	private boolean conversionFailed;
	private TiffWriter writer;
	private RandomAccessFile in;
	private TJDecompressor decompressor;
	private long startTime;
	private long endTime;
	/** EOI (end of image) marker of JPEG images */
	private static final byte[] EOI = { (byte) 0xff, (byte) 0xd9 };
	/** Pixel type of the resulting OME-TIFF */
	private static final int PIXEL_TYPE = FormatTools.UINT8;
	/** Number of color channels of the resulting OME-TIFF */
	private static final int COMPONENTS = 3;
	/** XML annotation namespace for original metadata annotations */
	private static final String XML_ANNOTATION_NAMESPACE = "openmicroscopy.org/OriginalMetadata";
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageConverter.class);
	/** Output settings for the resulting OME-TIFF */
	private ConversionSettings settings = ConversionSettings.getInstance();

	@SuppressWarnings("unused")
	private ImageConverter() {
	}

	/**
	 * Creates a new image converter instance to convert a JPEG file of a NDPI
	 * file to an OME-TIFF under consideration of the selected output settings.
	 * 
	 * @param gui
	 *            the underlying GUI which will be updated during the conversion
	 *            to inform the user about the conversion progress or occurred
	 *            errors
	 * @param ndpiFile
	 *            the NDPI file which contains the image to convert
	 * @param imgInfo
	 *            the image information object of the image to convert
	 */
	public ImageConverter(NDPIConverterGUI gui, File ndpiFile, ImageInfo imgInfo) {
		this.gui = gui;
		this.imgInfo = imgInfo;
		this.ndpiFile = ndpiFile;
		this.conversionFailed = false;

		// Get some important information to process the image
		outputWidth = (settings.isDownscalingSelected() ? imgInfo.getDownscaledWidth(settings.getDownscalingFactor()) : imgInfo.getWidth());
		outputHeight = (settings.isDownscalingSelected() ? imgInfo.getDownscaledHeight(settings.getDownscalingFactor()) : imgInfo.getHeight());
		outputStripHeight = (settings.isDownscalingSelected() ? imgInfo.getDownscaledStripHeight(settings.getDownscalingFactor()) : imgInfo.getStripHeight());
		samplesPerPixel = settings.isMultipageOMETiffSelected() ? 1 : 3;
	}

	/**
	 * Makes all necessary preparations by setting up the NDPI file reader and
	 * the OME-TIFF writer and starts conversion of the selected image by
	 * calling the appropriate method to create the OME-TIFF.
	 */
	@Override
	protected Void doInBackground() {
		LOGGER.info("Start converting selected NDPI image to OME-TIFF");
		startTime = System.nanoTime();
		setProgress(0);

		try {
			// Set up NDPI file reader
			in = new RandomAccessFile(ndpiFile, "r");

			// Set up OME-TIFF writer
			writer = setupOMETiffWriter();

			// Set up libjpeg-turbo JPEG-decompressor (if available)
			if (settings.isLibjpegTurboAvailable())
				decompressor = new TJDecompressor();

			LOGGER.info("Input file:");
			LOGGER.info(imgInfo.getInputFilename());
			LOGGER.info(imgInfo.getCompressionScheme() + ", " + imgInfo.getWidth() + " x " + imgInfo.getHeight() + (imgInfo.getMagnification() != null ? ", " + imgInfo.getMagnification() : ""));
			LOGGER.info("Output file:");
			LOGGER.info(imgInfo.getOMETiffFilename());
			LOGGER.info((settings.isMultipageOMETiffSelected() ? "Multipage OME-TIFF" : "Singlepage OME-TIFF") + ", " + outputWidth + " x " + outputHeight);

			if (settings.getImgDataOrganization() == ConversionSettings.STRIPS) {
				if (settings.isDownscalingSelected())
					LOGGER.info("downscaled (" + settings.getDownscalingAlgorithmAsString() + ")");

				LOGGER.info("strips (" + outputWidth + " x " + outputStripHeight + "), " + settings.getCompressionAlgorithmAsString() + ", " + (settings.isBigTiffSelected() ? "BigTIFF" : "TIFF"));

				if (settings.isMultipageOMETiffSelected())
					createStripedMultipageOMETiff(settings.isDownscalingSelected());
				else
					createStripedSinglepageOMETiff(settings.isDownscalingSelected());

			} else if (settings.getImgDataOrganization() == ConversionSettings.TILES) {

				LOGGER.info("tiles (" + imgInfo.getTileWidth() + " x " + imgInfo.getTileHeight() + "), " + settings.getCompressionAlgorithmAsString() + ", " + (settings.isBigTiffSelected() ? "BigTIFF" : "TIFF"));

				if (settings.isMultipageOMETiffSelected())
					createTiledMultipageOMETiff();
				else
					createTiledSinglepageOMETiff();
			}

			endTime = System.nanoTime();
			LOGGER.info("End converting selected NDPI image to OME-TIFF");

		} catch (Exception e) {
			if (gui != null)
				gui.displayErrorMessage("Error", "<b>Error occured during conversion:</b><br>" + e.toString());

			LOGGER.error("Error occured during conversion:", e);
			conversionFailed = true;
		} finally {
			try {
				writer.close();
				if (decompressor != null)
					decompressor.close();
				in.close();
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**
	 * Creates a striped, singlepage OME-TIFF.
	 * 
	 * @param downscaleImage
	 *            whether image should be downscaled
	 * @return {@code true} if conversion finished normally (not canceled by the
	 *         user), {@code false} if conversion was canceled by the user
	 * @throws Exception
	 *             if creation of the OME-TIFF failed
	 */
	private boolean createStripedSinglepageOMETiff(boolean downscaleImage) throws Exception {
		// Set up IFD for creating a striped, singlepage OME-TIFF
		IFD ifdRGB = new IFD();
		ifdRGB.put(IFD.COMPRESSION, settings.getCompressionAlgorithm());
		ifdRGB.put(IFD.ROWS_PER_STRIP, outputStripHeight);

		MultiStepRescaleOp downscaler = null;
		BufferedImage resultImage = null;
		BufferedImage resizedImage = null;
		if (downscaleImage)
			downscaler = new MultiStepRescaleOp(DimensionConstrain.createRelativeDimension(settings.getDownscalingFraction()), settings.getDownscalingAlgorithm());

		// Set up array for storing the pixel values of one strip
		byte[] pixelsRGB = new byte[outputStripHeight * outputWidth * COMPONENTS];

		// Check if image should be downscaled
		if (downscaleImage) {
			// Process first strip
			if (gui != null)
				publish("Processing strip 1 of " + imgInfo.getStripsY());

			resultImage = getImageTile(imgInfo.getTilesX(), 2, 0, 0);
			resizedImage = downscaler.filter(resultImage, null);
			getPixelValues(pixelsRGB, resizedImage, 0, outputStripHeight);
			writer.saveBytes(0, pixelsRGB, ifdRGB, 0, 0, outputWidth, outputStripHeight);
			setProgress(1, imgInfo.getStripsY());

			// Process second strip until second last strip
			for (int strip = 1; strip < (imgInfo.getStripsY() - 1); strip++) {
				if (isCancelled()) {
					LOGGER.info("Conversion canceled by user");
					return false;
				}
				if (gui != null)
					publish("Processing strip " + (strip + 1) + " of " + imgInfo.getStripsY());

				resultImage = getImageTile(imgInfo.getTilesX(), 3, 0, strip - 1);
				resizedImage = downscaler.filter(resultImage, null);
				getPixelValues(pixelsRGB, resizedImage, outputStripHeight, outputStripHeight);
				writer.saveBytes(0, pixelsRGB, ifdRGB, 0, strip * outputStripHeight, outputWidth, outputStripHeight);
				setProgress(strip + 1, imgInfo.getStripsY());
			}

			// Process last strip
			if (isCancelled()) {
				LOGGER.info("Conversion canceled by user");
				return false;
			}
			if (gui != null)
				publish("Processing strip " + imgInfo.getStripsY() + " of " + imgInfo.getStripsY());

			resultImage = getImageTile(imgInfo.getTilesX(), 2, 0, imgInfo.getStripsY() - 2);
			resizedImage = downscaler.filter(resultImage, null);
			getPixelValues(pixelsRGB, resizedImage, outputStripHeight, outputStripHeight);
			writer.saveBytes(0, pixelsRGB, ifdRGB, 0, (imgInfo.getStripsY() - 1) * outputStripHeight, outputWidth, outputStripHeight);
			setProgress(imgInfo.getStripsY(), imgInfo.getStripsY());
		} else {
			// Convert image at original size
			for (int strip = 0; strip < imgInfo.getStripsY(); strip++) {
				if (isCancelled()) {
					LOGGER.info("Conversion canceled by user");
					return false;
				}
				if (gui != null)
					publish("Processing strip " + (strip + 1) + " of " + imgInfo.getStripsY());

				resultImage = getImageTile(imgInfo.getTilesX(), 1, 0, strip);
				getPixelValues(pixelsRGB, resultImage, 0, outputStripHeight);
				writer.saveBytes(0, pixelsRGB, ifdRGB, 0, strip * outputStripHeight, outputWidth, outputStripHeight);
				setProgress(strip + 1, imgInfo.getStripsY());
			}
		}

		return true;
	}

	/**
	 * Creates a striped, multipage OME-TIFF.
	 * 
	 * @param downscaleImage
	 *            whether image should be downscaled
	 * @return {@code true} if conversion finished normally (not canceled by the
	 *         user), {@code false} if conversion was canceled by the user
	 * @throws Exception
	 *             if creation of the OME-TIFF failed
	 */
	private boolean createStripedMultipageOMETiff(boolean downscaleImage) throws Exception {
		// Set up IFD for creating a striped, multipage OME-TIFF
		IFD ifdRed = new IFD();
		IFD ifdGreen = new IFD();
		IFD ifdBlue = new IFD();
		ifdRed.put(IFD.COMPRESSION, settings.getCompressionAlgorithm());
		ifdRed.put(IFD.ROWS_PER_STRIP, outputStripHeight);
		ifdGreen.put(IFD.COMPRESSION, settings.getCompressionAlgorithm());
		ifdGreen.put(IFD.ROWS_PER_STRIP, outputStripHeight);
		ifdBlue.put(IFD.COMPRESSION, settings.getCompressionAlgorithm());
		ifdBlue.put(IFD.ROWS_PER_STRIP, outputStripHeight);

		// Set up output streams for two temporary files which hold the green and blue color channel of the image
		String tempFilenameGreen = settings.getOutputDirectory() + "/" + imgInfo.getTempGreenFilename();
		String tempFilenameBlue = settings.getOutputDirectory() + "/" + imgInfo.getTempBlueFilename();
		BufferedOutputStream greenOut = new BufferedOutputStream(new FileOutputStream(tempFilenameGreen));
		BufferedOutputStream blueOut = new BufferedOutputStream(new FileOutputStream(tempFilenameBlue));

		MultiStepRescaleOp downscaler = null;
		BufferedImage resultImage = null;
		BufferedImage resizedImage = null;
		if (downscaleImage)
			downscaler = new MultiStepRescaleOp(DimensionConstrain.createRelativeDimension(settings.getDownscalingFraction()), settings.getDownscalingAlgorithm());

		// Set up arrays for storing the pixel values of one strip
		byte[] pixelsRed = new byte[outputWidth * outputStripHeight];
		byte[] pixelsGreen = new byte[outputWidth * outputStripHeight];
		byte[] pixelsBlue = new byte[outputWidth * outputStripHeight];

		// Process the RED color channel:
		// Loop through the strips of the image, write the red color channel and store the green and blue color channels in temp-file
		LOGGER.info("Processing RED color channel");

		// Check if image should be downscaled
		if (downscaleImage) {
			// Process first strip
			if (gui != null)
				publish("RED color channel: processing strip 1 of " + imgInfo.getStripsY());

			resultImage = getImageTile(imgInfo.getTilesX(), 2, 0, 0);
			resizedImage = downscaler.filter(resultImage, null);
			getPixelValues(pixelsRed, pixelsGreen, pixelsBlue, resizedImage, 0, outputStripHeight);
			writer.saveBytes(0, pixelsRed, ifdRed, 0, 0, outputWidth, outputStripHeight);
			greenOut.write(pixelsGreen);
			blueOut.write(pixelsBlue);
			setProgress(1, imgInfo.getStripsY() * 3);

			// Process second strip until second last strip
			for (int strip = 1; strip < (imgInfo.getStripsY() - 1); strip++) {
				if (isCancelled()) {
					LOGGER.info("Conversion canceled by user");
					greenOut.close();
					blueOut.close();
					(new File(tempFilenameGreen)).delete();
					(new File(tempFilenameBlue)).delete();
					return false;
				}
				if (gui != null)
					publish("RED color channel: processing strip " + (strip + 1) + " of " + imgInfo.getStripsY());

				resultImage = getImageTile(imgInfo.getTilesX(), 3, 0, strip - 1);
				resizedImage = downscaler.filter(resultImage, null);
				getPixelValues(pixelsRed, pixelsGreen, pixelsBlue, resizedImage, outputStripHeight, outputStripHeight);
				writer.saveBytes(0, pixelsRed, ifdRed, 0, strip * outputStripHeight, outputWidth, outputStripHeight);
				greenOut.write(pixelsGreen);
				blueOut.write(pixelsBlue);
				setProgress(strip + 1, imgInfo.getStripsY() * 3);
			}

			// Process last strip
			if (isCancelled()) {
				LOGGER.info("Conversion canceled by user");
				greenOut.close();
				blueOut.close();
				(new File(tempFilenameGreen)).delete();
				(new File(tempFilenameBlue)).delete();
				return false;
			}
			if (gui != null)
				publish("RED color channel: processing strip " + imgInfo.getStripsY() + " of " + imgInfo.getStripsY());

			resultImage = getImageTile(imgInfo.getTilesX(), 2, 0, imgInfo.getStripsY() - 2);
			resizedImage = downscaler.filter(resultImage, null);
			getPixelValues(pixelsRed, pixelsGreen, pixelsBlue, resizedImage, outputStripHeight, outputStripHeight);
			writer.saveBytes(0, pixelsRed, ifdRed, 0, (imgInfo.getStripsY() - 1) * outputStripHeight, outputWidth, outputStripHeight);
			setProgress(imgInfo.getStripsY(), imgInfo.getStripsY() * 3);
		} else {
			// Process strips without downscaling
			for (int strip = 0; strip < imgInfo.getStripsY(); strip++) {
				if (isCancelled()) {
					LOGGER.info("Conversion canceled by user");
					greenOut.close();
					blueOut.close();
					(new File(tempFilenameGreen)).delete();
					(new File(tempFilenameBlue)).delete();
					return false;
				}
				if (gui != null)
					publish("RED color channel: processing strip " + (strip + 1) + " of " + imgInfo.getStripsY());

				resultImage = getImageTile(imgInfo.getTilesX(), 1, 0, strip);
				getPixelValues(pixelsRed, pixelsGreen, pixelsBlue, resultImage, 0, outputStripHeight);
				writer.saveBytes(0, pixelsRed, ifdRed, 0, strip * outputStripHeight, outputWidth, outputStripHeight);
				greenOut.write(pixelsGreen);
				blueOut.write(pixelsBlue);
				setProgress(strip + 1, imgInfo.getStripsY() * 3);
			}
		}

		// Output finished, close output streams
		greenOut.close();
		blueOut.close();

		// Process the GREEN color channel:
		// Read the strips from the temp-file and write them into the OME-TIFF
		LOGGER.info("Processing GREEN color channel");
		DataInputStream greenInput = new DataInputStream(new FileInputStream(tempFilenameGreen));
		for (int y = 0; y < imgInfo.getStripsY(); y++) {
			if (isCancelled()) {
				LOGGER.info("Conversion canceled by user");
				greenInput.close();
				(new File(tempFilenameGreen)).delete();
				(new File(tempFilenameBlue)).delete();
				return false;
			}

			if (gui != null)
				publish("GREEN color channel: processing strip " + (y + 1) + " of " + imgInfo.getStripsY());

			greenInput.read(pixelsGreen);
			writer.saveBytes(1, pixelsGreen, ifdGreen, 0, y * outputStripHeight, outputWidth, outputStripHeight);
			setProgress((y + 1) + imgInfo.getStripsY(), imgInfo.getStripsY() * 3);
		}
		greenInput.close();

		// Process the BLUE color channel:
		// Read the strips from the temp-file and write them into the OME-TIFF
		LOGGER.info("Processing BLUE color channel");
		DataInputStream blueInput = new DataInputStream(new FileInputStream(tempFilenameBlue));
		for (int y = 0; y < imgInfo.getStripsY(); y++) {
			if (isCancelled()) {
				LOGGER.info("Conversion canceled by user");
				blueInput.close();
				(new File(tempFilenameGreen)).delete();
				(new File(tempFilenameBlue)).delete();
				return false;
			}

			if (gui != null)
				publish("BLUE color channel: processing strip " + (y + 1) + " of " + imgInfo.getStripsY());

			blueInput.read(pixelsBlue);
			writer.saveBytes(2, pixelsBlue, ifdBlue, 0, y * outputStripHeight, outputWidth, outputStripHeight);
			setProgress((y + 1) + imgInfo.getStripsY() * 2, imgInfo.getStripsY() * 3);
		}
		blueInput.close();

		// Delete temp-files
		(new File(tempFilenameGreen)).delete();
		(new File(tempFilenameBlue)).delete();

		return true;
	}

	/**
	 * Creates a tiled, singlepage OME-TIFF.
	 * 
	 * @return {@code true} if conversion finished normally (not canceled by the
	 *         user), {@code false} if conversion was canceled by the user
	 * @throws Exception
	 *             if creation of the OME-TIFF failed
	 */
	private boolean createTiledSinglepageOMETiff() throws Exception {
		// Set up IFD for creating a tiled, singlepage OME-TIFF
		IFD ifdRGB = new IFD();
		ifdRGB.put(IFD.COMPRESSION, settings.getCompressionAlgorithm());
		ifdRGB.put(IFD.TILE_WIDTH, imgInfo.getTileWidth());
		ifdRGB.put(IFD.TILE_LENGTH, imgInfo.getTileHeight());

		BufferedImage resultImage = null;
		int numMCURows = 0;

		// Set up array for storing the pixel values of one tile
		byte[] pixelsRGB = new byte[imgInfo.getTileWidth() * imgInfo.getTileHeight() * COMPONENTS];

		// Loop through the tiles of the image and write tile
		for (int tileY = 0; tileY < imgInfo.getTilesY(); tileY++) {
			for (int tileX = 0; tileX < imgInfo.getTilesX(); tileX++) {
				if (isCancelled()) {
					LOGGER.info("Conversion canceled by user");
					return false;
				}

				if (gui != null)
					publish("Processing tile " + (tileY * imgInfo.getTilesX() + tileX + 1) + " of " + (imgInfo.getTilesY() * imgInfo.getTilesX()));

				// Check if last row of tiles gets processed (tiles of last row might be smaller in height)
				if (tileY < imgInfo.getTilesY() - 1)
					numMCURows = imgInfo.getRowsPerTile();
				else
					numMCURows = (imgInfo.getHeight() / imgInfo.getStripHeight()) - (imgInfo.getRowsPerTile() * (imgInfo.getTilesY() - 1));

				resultImage = getImageTile(1, numMCURows, tileX, tileY * imgInfo.getRowsPerTile());
				getPixelValues(pixelsRGB, resultImage, 0, imgInfo.getStripHeight() * numMCURows);
				writer.saveBytes(0, pixelsRGB, ifdRGB, tileX * imgInfo.getTileWidth(), tileY * imgInfo.getTileHeight(), imgInfo.getTileWidth(), numMCURows * imgInfo.getStripHeight());
				setProgress(tileY * imgInfo.getTilesX() + tileX + 1, imgInfo.getTilesY() * imgInfo.getTilesX());
			}
		}

		return true;
	}

	/**
	 * Creates a tiled, multipage OME-TIFF.
	 * 
	 * @return {@code true} if conversion finished normally (not canceled by the
	 *         user), {@code false} if conversion was canceled by the user
	 * @throws Exception
	 *             if creation of the OME-TIFF failed
	 */
	private boolean createTiledMultipageOMETiff() throws Exception {
		// Set up IFD for creating a tiled, multipage OME-TIFF
		IFD ifdRed = new IFD();
		IFD ifdGreen = new IFD();
		IFD ifdBlue = new IFD();
		ifdRed.put(IFD.COMPRESSION, settings.getCompressionAlgorithm());
		ifdRed.put(IFD.TILE_WIDTH, imgInfo.getTileWidth());
		ifdRed.put(IFD.TILE_LENGTH, imgInfo.getTileHeight());
		ifdGreen.put(IFD.COMPRESSION, settings.getCompressionAlgorithm());
		ifdGreen.put(IFD.TILE_WIDTH, imgInfo.getTileWidth());
		ifdGreen.put(IFD.TILE_LENGTH, imgInfo.getTileHeight());
		ifdBlue.put(IFD.COMPRESSION, settings.getCompressionAlgorithm());
		ifdBlue.put(IFD.TILE_WIDTH, imgInfo.getTileWidth());
		ifdBlue.put(IFD.TILE_LENGTH, imgInfo.getTileHeight());

		// Set up output streams for two temporary files which hold the green and blue color channel of the image
		String tempFilenameGreen = settings.getOutputDirectory() + "/" + imgInfo.getTempGreenFilename();
		String tempFilenameBlue = settings.getOutputDirectory() + "/" + imgInfo.getTempBlueFilename();
		BufferedOutputStream greenOut = new BufferedOutputStream(new FileOutputStream(tempFilenameGreen));
		BufferedOutputStream blueOut = new BufferedOutputStream(new FileOutputStream(tempFilenameBlue));

		BufferedImage resultImage = null;
		int numMCURowsTile = imgInfo.getRowsPerTile();

		// Get number of MCU rows of tiles of last row to calculate their actual height
		// (tiles of last row might be smaller in height)
		int numMCURowsLastTile = (imgInfo.getHeight() / imgInfo.getStripHeight()) - (imgInfo.getRowsPerTile() * (imgInfo.getTilesY() - 1));

		// Set up arrays for storing the pixel values of one tile
		byte[] pixelsRed = new byte[imgInfo.getTileWidth() * imgInfo.getTileHeight()];
		byte[] pixelsGreen = new byte[imgInfo.getTileWidth() * imgInfo.getTileHeight()];
		byte[] pixelsBlue = new byte[imgInfo.getTileWidth() * imgInfo.getTileHeight()];

		// Process the RED color channel:
		// Loop through the tiles of the image, write the red color channel and store the green and blue color channel in temp-files
		LOGGER.info("Processing RED color channel");
		for (int tileY = 0; tileY < imgInfo.getTilesY(); tileY++) {
			for (int tileX = 0; tileX < imgInfo.getTilesX(); tileX++) {

				if (isCancelled()) {
					LOGGER.info("Conversion canceled by user");
					greenOut.close();
					blueOut.close();
					(new File(tempFilenameGreen)).delete();
					(new File(tempFilenameBlue)).delete();
					return false;
				}

				if (gui != null)
					publish("RED color channel: processing tile " + (tileY * imgInfo.getTilesX() + tileX + 1) + " of " + (imgInfo.getTilesY() * imgInfo.getTilesX()));

				resultImage = getImageTile(1, (tileY < imgInfo.getTilesY() - 1 ? numMCURowsTile : numMCURowsLastTile), tileX, tileY * imgInfo.getRowsPerTile());
				getPixelValues(pixelsRed, pixelsGreen, pixelsBlue, resultImage, 0, imgInfo.getStripHeight() * (tileY < imgInfo.getTilesY() - 1 ? numMCURowsTile : numMCURowsLastTile));
				writer.saveBytes(0, pixelsRed, ifdRed, tileX * imgInfo.getTileWidth(), tileY * imgInfo.getTileHeight(), imgInfo.getTileWidth(), (tileY < imgInfo.getTilesY() - 1 ? numMCURowsTile : numMCURowsLastTile) * imgInfo.getStripHeight());
				greenOut.write(pixelsGreen);
				blueOut.write(pixelsBlue);
				setProgress(tileY * imgInfo.getTilesX() + tileX + 1, imgInfo.getTilesY() * imgInfo.getTilesX() * 3);
			}
		}

		// Output finished, close output streams
		greenOut.close();
		blueOut.close();

		// Process the GREEN color channel:
		// Read the tiles from the temp-file and write them into the OME-TIFF
		LOGGER.info("Processing GREEN color channel");
		DataInputStream greenInput = new DataInputStream(new FileInputStream(tempFilenameGreen));
		for (int tileY = 0; tileY < imgInfo.getTilesY(); tileY++) {
			for (int tileX = 0; tileX < imgInfo.getTilesX(); tileX++) {
				if (isCancelled()) {
					LOGGER.info("Conversion canceled by user");
					greenInput.close();
					(new File(tempFilenameGreen)).delete();
					(new File(tempFilenameBlue)).delete();
					return false;
				}

				if (gui != null)
					publish("GREEN color channel: processing tile " + (tileY * imgInfo.getTilesX() + tileX + 1) + " of " + (imgInfo.getTilesY() * imgInfo.getTilesX()));

				greenInput.read(pixelsGreen);
				writer.saveBytes(1, pixelsGreen, ifdGreen, tileX * imgInfo.getTileWidth(), tileY * imgInfo.getTileHeight(), imgInfo.getTileWidth(), (tileY < imgInfo.getTilesY() - 1 ? numMCURowsTile : numMCURowsLastTile) * imgInfo.getStripHeight());
				setProgress(tileY * imgInfo.getTilesX() + imgInfo.getTilesY() * imgInfo.getTilesX() + tileX + 1, imgInfo.getTilesY() * imgInfo.getTilesX() * 3);
			}
		}
		greenInput.close();

		// Process the BLUE color channel:
		// Read the tiles from the temp-file and write them into the OME-TIFF
		LOGGER.info("Processing BLUE color channel");
		DataInputStream blueInput = new DataInputStream(new FileInputStream(tempFilenameBlue));
		for (int tileY = 0; tileY < imgInfo.getTilesY(); tileY++) {
			for (int tileX = 0; tileX < imgInfo.getTilesX(); tileX++) {
				if (isCancelled()) {
					LOGGER.info("Conversion canceled by user");
					blueInput.close();
					(new File(tempFilenameGreen)).delete();
					(new File(tempFilenameBlue)).delete();
					return false;
				}

				if (gui != null)
					publish("BLUE color channel: processing tile " + (tileY * imgInfo.getTilesX() + tileX + 1) + " of " + (imgInfo.getTilesY() * imgInfo.getTilesX()));

				blueInput.read(pixelsBlue);
				writer.saveBytes(2, pixelsBlue, ifdBlue, tileX * imgInfo.getTileWidth(), tileY * imgInfo.getTileHeight(), imgInfo.getTileWidth(), (tileY < imgInfo.getTilesY() - 1 ? numMCURowsTile : numMCURowsLastTile) * imgInfo.getStripHeight());
				setProgress(tileY * imgInfo.getTilesX() + imgInfo.getTilesY() * imgInfo.getTilesX() * 2 + tileX + 1, imgInfo.getTilesY() * imgInfo.getTilesX() * 3);
			}
		}
		blueInput.close();

		// Delete temp-files
		(new File(tempFilenameGreen)).delete();
		(new File(tempFilenameBlue)).delete();

		return true;
	}

	/**
	 * Sets up the OME-TIFF writer by storing important metadata (OME-XML) and
	 * output settings. In addition to the automatically created metadata, as
	 * required by the minimum specification for OME-XML, the following metadata
	 * gets stored in the OME-XML:
	 * 
	 * <ul>
	 * <li>Physical size of the pixels in microns</li>
	 * <li>Color channels of the image</li>
	 * <li>Make of the slide scanner</li>
	 * <li>Model the slide scanner</li>
	 * <li>Software used to create the NDPI file</li>
	 * <li>Magnification of the image</li>
	 * <li>Date and time of the scan</li>
	 * </ul>
	 * 
	 * @return the OME-TIFF writer
	 * @throws Exception
	 *             if setting up the OME-TIFF writer failed
	 */
	private TiffWriter setupOMETiffWriter() throws Exception {
		// Create OME-XML for the OME-TIFF
		ServiceFactory factory = new ServiceFactory();
		OMEXMLService service = factory.getInstance(OMEXMLService.class);
		IMetadata meta = service.createOMEXMLMetadata();

		// Add the physical size of the pixels in microns
		int imageIndex = meta.getImageCount();
		if (imgInfo.getPixelsPhysicalSizeX() != -1) {
			meta.setPixelsPhysicalSizeX(new PositiveFloat(imgInfo.getPixelsPhysicalSizeX()), imageIndex);
		}
		if (imgInfo.getPixelsPhysicalSizeY() != -1) {
			meta.setPixelsPhysicalSizeY(new PositiveFloat(imgInfo.getPixelsPhysicalSizeY()), imageIndex);
		}

		// Set up the color channels of the image
		meta.setChannelID("Channel:0:0", 0, 0);
		meta.setChannelName("Red", 0, 0);
		meta.setChannelColor(new Color(255, 0, 0, 255), 0, 0);

		meta.setChannelID("Channel:0:1", 0, 1);
		meta.setChannelName("Green", 0, 1);
		meta.setChannelColor(new Color(0, 255, 0, 255), 0, 1);

		meta.setChannelID("Channel:0:2", 0, 2);
		meta.setChannelName("Blue", 0, 2);
		meta.setChannelColor(new Color(0, 0, 255, 255), 0, 2);

		// Appending metadata from the original NDPI file
		int xmlAnnotationIndex = 0;

		if (imgInfo.getMake() != null) {
			String lsid = MetadataTools.createLSID("Annotation", xmlAnnotationIndex);
			meta.setXMLAnnotationID(lsid, xmlAnnotationIndex);
			meta.setXMLAnnotationNamespace(XML_ANNOTATION_NAMESPACE, xmlAnnotationIndex);
			meta.setXMLAnnotationValue(createOriginalMetadata("Make", imgInfo.getMake()), xmlAnnotationIndex);
			meta.setImageAnnotationRef(lsid, 0, 0);
			xmlAnnotationIndex++;
		}
		if (imgInfo.getModel() != null) {
			String lsid = MetadataTools.createLSID("Annotation", xmlAnnotationIndex);
			meta.setXMLAnnotationID(lsid, xmlAnnotationIndex);
			meta.setXMLAnnotationNamespace(XML_ANNOTATION_NAMESPACE, xmlAnnotationIndex);
			meta.setXMLAnnotationValue(createOriginalMetadata("Model", imgInfo.getModel()), xmlAnnotationIndex);
			meta.setImageAnnotationRef(lsid, 0, 0);
			xmlAnnotationIndex++;
		}
		if (imgInfo.getSoftware() != null) {
			String lsid = MetadataTools.createLSID("Annotation", xmlAnnotationIndex);
			meta.setXMLAnnotationID(lsid, xmlAnnotationIndex);
			meta.setXMLAnnotationNamespace(XML_ANNOTATION_NAMESPACE, xmlAnnotationIndex);
			meta.setXMLAnnotationValue(createOriginalMetadata("Software", imgInfo.getSoftware()), xmlAnnotationIndex);
			meta.setImageAnnotationRef(lsid, 0, 0);
			xmlAnnotationIndex++;
		}
		if (imgInfo.getMagnification() != null) {
			String lsid = MetadataTools.createLSID("Annotation", xmlAnnotationIndex);
			meta.setXMLAnnotationID(lsid, xmlAnnotationIndex);
			meta.setXMLAnnotationNamespace(XML_ANNOTATION_NAMESPACE, xmlAnnotationIndex);
			meta.setXMLAnnotationValue(createOriginalMetadata("Magnification", imgInfo.getMagnification()), xmlAnnotationIndex);
			meta.setImageAnnotationRef(lsid, 0, 0);
			xmlAnnotationIndex++;
		}
		if (imgInfo.getDateTime() != null) {
			String lsid = MetadataTools.createLSID("Annotation", xmlAnnotationIndex);
			meta.setXMLAnnotationID(lsid, xmlAnnotationIndex);
			meta.setXMLAnnotationNamespace(XML_ANNOTATION_NAMESPACE, xmlAnnotationIndex);
			meta.setXMLAnnotationValue(createOriginalMetadata("DateTime", imgInfo.getDateTime()), xmlAnnotationIndex);
			meta.setImageAnnotationRef(lsid, 0, 0);
		}

		MetadataTools.populateMetadata(meta, 0, imgInfo.getInputFilename(), false, "XYZCT", FormatTools.getPixelTypeString(PIXEL_TYPE), outputWidth, outputHeight, 1, COMPONENTS, 1, samplesPerPixel);

		// Set up the writer for OME-TIFF
		TiffWriter writer = new OMETiffWriter();
		writer.setMetadataRetrieve(meta);
		writer.setInterleaved(Boolean.TRUE);
		writer.setBigTiff(settings.isBigTiffSelected());
		writer.setId(settings.getOutputDirectory() + System.getProperty("file.separator") + imgInfo.getOMETiffFilename());

		return writer;
	}

	/**
	 * Returns the JPEG tile at the given position of the underlying JPEG image.
	 * Tiling of the underlying JPEG image is enabled by using the restart
	 * markers of the image.
	 * 
	 * @param numTilesX
	 * @param numTilesY
	 * @param startTileX
	 * @param startTileY
	 * @return
	 * @throws Exception
	 */
	private BufferedImage getImageTile(int numTilesX, int numTilesY, int startTileX, int startTileY) throws Exception {
		BufferedImage resultImage = new BufferedImage(numTilesX * imgInfo.getTileWidth(), numTilesY * imgInfo.getStripHeight(), BufferedImage.TYPE_INT_RGB);
		for (int y = 0; y < numTilesY; y++) {
			for (int x = 0; x < numTilesX; x++) {
				// Get JPEG-tile
				byte[] strip = getTileAsJPEG(x + startTileX, y + startTileY);

				// Decompress tile
				BufferedImage bi;
				if (settings.isLibjpegTurboAvailable()) {
					decompressor.setJPEGImage(strip, strip.length);
					bi = decompressor.decompress(imgInfo.getTileWidth(), imgInfo.getStripHeight(), BufferedImage.TYPE_INT_RGB, TJ.PF_RGB);
				} else {
					bi = ImageIO.read(new ByteArrayInputStream(strip));
				}

				// Draw current tile
				Graphics g = resultImage.getGraphics();
				g.drawImage(bi, x * imgInfo.getTileWidth(), y * imgInfo.getStripHeight(), null);
			}
		}
		return resultImage;
	}

	private void getPixelValues(byte[] pixelsRGB, BufferedImage image, int numPixelRowStart, int numRows) {
		int[] pixel = new int[4];
		for (int posy = 0; posy < numRows; posy++) {
			for (int posx = 0; posx < image.getWidth(); posx++) {
				image.getRaster().getPixel(posx, posy + numPixelRowStart, pixel);
				pixelsRGB[(image.getWidth() * posy * 3) + (posx * 3)] = (byte) (pixel[0]);
				pixelsRGB[(image.getWidth() * posy * 3) + (posx * 3) + 1] = (byte) (pixel[1]);
				pixelsRGB[(image.getWidth() * posy * 3) + (posx * 3) + 2] = (byte) (pixel[2]);
			}
		}
	}

	private void getPixelValues(byte[] pixelsR, byte[] pixelsG, byte[] pixelsB, BufferedImage image, int numPixelRowStart, int numRows) {
		int[] pixel = new int[4];
		for (int posy = 0; posy < numRows; posy++) {
			for (int posx = 0; posx < image.getWidth(); posx++) {
				image.getRaster().getPixel(posx, posy + numPixelRowStart, pixel);
				pixelsR[(image.getWidth() * posy) + posx] = (byte) (pixel[0]);
				pixelsG[(image.getWidth() * posy) + posx] = (byte) (pixel[1]);
				pixelsB[(image.getWidth() * posy) + posx] = (byte) (pixel[2]);
			}
		}
	}

	private void setProgress(int current, int total) {
		// Update the Progress-Bar in the GUI
		setProgress(Math.round((((float) current) / (float) total) * 100));
	}

	/**
	 * Creates the JPEG-Header which is used to read the NDPI JPEG-image in
	 * tiles. The header of the original JPEG is read an modified. Height and
	 * width of the created header are set to strip-height and tile-width. This
	 * leads to smallest possible JPEG-tiles, because strip-height is the
	 * MCU-height and tile-width is the number of pixels until the next set
	 * restart-marker.
	 * 
	 * @return the modified JPEG-Header as a byte-array
	 * @throws IOException
	 *             if reading the original JPEG-Header (in the NDPI-File) fails
	 */
	private byte[] createJPEGHeader(boolean isLastRow) throws IOException {
		// Read the JPEG-header
		byte[] jpegHeader = new byte[(int) (imgInfo.getOffsetImageData() - imgInfo.getOffsetImage())];
		in.seek(imgInfo.getOffsetImage());
		in.readFully(jpegHeader);

		// Modify the JPEG-header (set height and width)
		jpegHeader[(int) (imgInfo.getOffsetImageHeight() - imgInfo.getOffsetImage())] = (byte) ((imgInfo.getStripHeight() >> 8) & 0xff);
		jpegHeader[(int) (imgInfo.getOffsetImageHeight() - imgInfo.getOffsetImage()) + 1] = (byte) (imgInfo.getStripHeight() & 0xff);
		jpegHeader[(int) (imgInfo.getOffsetImageHeight() - imgInfo.getOffsetImage()) + 2] = (byte) ((imgInfo.getTileWidth() >> 8) & 0xff);
		jpegHeader[(int) (imgInfo.getOffsetImageHeight() - imgInfo.getOffsetImage()) + 3] = (byte) (imgInfo.getTileWidth() & 0xff);

		return jpegHeader;
	}

	/**
	 * Returns the JPEG tile at the given position of the underlying JPEG image.
	 * Tiling of the underlying JPEG image is enabled by using the offsets of
	 * the restart markers of the image. In order to create the JPEG tile, the
	 * JPEG header of the underlying image gets modified by setting the width
	 * and height of the image to the tile width and height. The second step
	 * consists of reading the image data between the two determined restart
	 * markers and adding the data after the JPEG header. The last step is
	 * adding the EOI (end of image) marker to the JPEG tile to make it a valid
	 * JPEG image.
	 * 
	 * @param desiredTileX
	 *            the horizontal position of the desired tile
	 * @param desiredTileY
	 *            the vertical position of the desired tile
	 * @return the JPEG tile as a byte-array
	 * @throws IOException
	 *             if reading the data from the NDPI file failed
	 */
	private byte[] getTileAsJPEG(int desiredTileX, int desiredTileY) throws IOException {
		ByteArrayOutputStream imageTile = new ByteArrayOutputStream();

		// Write the JPEG header
		if (jpegTileHeader == null)
			jpegTileHeader = createJPEGHeader(false);
		imageTile.write(jpegTileHeader);

		// Write the desired tile
		int indexOffsetStart = (desiredTileY * imgInfo.getTilesX()) + desiredTileX;
		int indexOffsetEnd = indexOffsetStart + 1;

		try {
			long offsetStart = imgInfo.getRestartMarkerOffsets()[indexOffsetStart];
			long offsetEnd = imgInfo.getRestartMarkerOffsets()[indexOffsetEnd];

			byte imgData[] = new byte[(int) (offsetEnd - offsetStart)];
			in.seek(imgInfo.getOffsetImage() + offsetStart);
			in.readFully(imgData);

			imageTile.write(imgData);
		} catch (ArrayIndexOutOfBoundsException e) {
			LOGGER.error("Could not create requested JPEG-tile (x=" + desiredTileX + ", y=" + desiredTileY + "):", e);
			return null;
		}

		// Write the EOI-marker
		imageTile.write(EOI);
		imageTile.close();

		return imageTile.toByteArray();
	}

	/**
	 * Creates an XML-string for appending metadata from the original NDPI file
	 * as original metadata (key-value pair) in the resulting OME-TIFF.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @return the created XML-string
	 */
	private String createOriginalMetadata(String key, String value) {
		return "<OriginalMetadata><Key>" + key + "</Key><Value>" + value + "</Value></OriginalMetadata>";
	}

	/**
	 * Receives information messages of conversion progress from the
	 * {@code publish} method and displays the newest message in the GUI.
	 * 
	 * @param infoMessages
	 *            the list of information messages to process
	 */
	@Override
	protected void process(List<String> infoMessages) {
		if (gui != null)
			gui.updateConversionProgress(infoMessages.get(infoMessages.size() - 1));
	}

	/**
	 * Executed after the {@code doInBackground} method is finished. Disposes
	 * the converting information dialog and displays, if no errors occurred
	 * during conversion, an information message in the GUI.
	 */
	@Override
	protected void done() {
		if (gui != null)
			gui.disposeConvertingDialog();

		if (!conversionFailed && !isCancelled()) {
			String elapsedTime = String.format("%d min, %d sec", TimeUnit.NANOSECONDS.toMinutes(endTime - startTime),
					TimeUnit.NANOSECONDS.toSeconds(endTime - startTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(endTime - startTime)));
			LOGGER.info("OME-TIFF successfully created");
			LOGGER.info("Duration: " + elapsedTime);

			if (gui != null)
				gui.displayInfoMessage("Finished", "<b>OME-TIFF successfully created</b><br>Duration: " + elapsedTime);
		}
	}

}