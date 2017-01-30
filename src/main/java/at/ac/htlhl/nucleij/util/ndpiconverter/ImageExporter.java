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

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import javax.imageio.ImageIO;
import javax.swing.SwingWorker;

import at.ac.htlhl.nucleij.util.ndpiconverter.NDPIConverterGUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Exports a selected image of the NDPI file.
 * 
 * <p>
 * A NDPI file contains two different image file formats. On the one hand, the
 * scanned image in different resolutions and the macro image are stored as JPEG
 * images. On the other hand, the map image is stored as a TIFF image. The
 * export of a JPEG image is fast as data has to be only read from the start
 * until the end offset in the NDPI file, without any modification of the data.
 * This preserves all the metadata of the image. The export of the TIFF image
 * preserving all metadata is complex, because the image is interwoven with the
 * NDPI TIFF structure. Therefore, only the image data of the TIFF file get read
 * and exported to a bitmap image. Metadata of the TIFF image get not extracted.
 * </p>
 * 
 * @author Matthias Baldauf
 * 
 */
public class ImageExporter extends SwingWorker<Void, Void> {

	private NDPIConverterGUI gui;
	private File ndpiFile;
	private ImageInfo imgInfo;
	private boolean exportFailed;
	/** The buffer size (bytes) of the buffered output stream */
	private static final int BUFFER_SIZE = 8 * 1024;
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageExporter.class);
	private ConversionSettings settings = ConversionSettings.getInstance();

	@SuppressWarnings("unused")
	private ImageExporter() {
	}

	/**
	 * Creates a new image exporter instance to export an image of a NDPI file.
	 * 
	 * @param gui
	 *            the underlying GUI which will be updated to inform the user
	 *            about the export or occurred errors
	 * @param ndpiFile
	 *            the NDPI file which contains the image to export
	 * @param imgInfo
	 *            the image information object of the image to export
	 */
	public ImageExporter(NDPIConverterGUI gui, File ndpiFile, ImageInfo imgInfo) {
		this.gui = gui;
		this.ndpiFile = ndpiFile;
		this.imgInfo = imgInfo;
		this.exportFailed = false;
	}

	/**
	 * Exports the selected image. JPEG images gets exported by reading the
	 * whole JPEG image from the start to the end offset in the NDPI file. TIFF
	 * images gets exported by reading the image data only and creating a
	 * buffered image which gets stored as a bitmap image. Therefore, metadata
	 * of TIFF images gets not extracted.
	 */
	@Override
	protected Void doInBackground() {
		LOGGER.info("Start exporting selected image");

		LOGGER.info("Output file:");
		LOGGER.info(imgInfo.getExportFilename());

		RandomAccessFile in = null;
		OutputStream out = null;
		try {
			// Set up reader for the NDPI file
			in = new RandomAccessFile(ndpiFile, "r");

			// Set up output stream
			out = new BufferedOutputStream(new FileOutputStream(settings.getOutputDirectory() + System.getProperty("file.separator") + imgInfo.getExportFilename()), BUFFER_SIZE);

			if (imgInfo.getCompressionScheme().equals("JPEG")) {
				// Export JPEG image (images and macro)
				long dataChunks = imgInfo.getFileSize() / BUFFER_SIZE;
				in.seek(imgInfo.getOffsetImage());
				byte[] dataChunk = new byte[BUFFER_SIZE];

				for (long d = 1; d < dataChunks; d++) {
					in.read(dataChunk);
					out.write(dataChunk);
				}

				int remainingData = (int) ((imgInfo.getOffsetImage() + imgInfo.getFileSize()) - in.getFilePointer());
				if (remainingData > 0) {
					dataChunk = new byte[remainingData];
					in.read(dataChunk);
					out.write(dataChunk);
				}
			} else if (imgInfo.getCompressionScheme().equals("TIFF")) {
				// Export TIFF image (map)
				byte[] image = new byte[(int) imgInfo.getFileSize()];
				in.seek(imgInfo.getOffsetImage());
				in.readFully(image);

				// Create grayscale image
				BufferedImage buffImg = new BufferedImage(imgInfo.getWidth(), imgInfo.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
				buffImg.getRaster().setDataElements(0, 0, imgInfo.getWidth(), imgInfo.getHeight(), image);

				ImageIO.write(buffImg, "bmp", out);
			} else {
				if (gui != null)
					gui.displayErrorMessage("Image format not supported", "Could not export image because the image format '" + imgInfo.getCompressionScheme() + "' is not supported.");
				LOGGER.error("Could not export image because the image format '" + imgInfo.getCompressionScheme() + "' is not supported");
			}

		} catch (Exception e) {
			if (gui != null)
				gui.displayErrorMessage("Error", "Error occured during export:\n" + e.toString());
			LOGGER.error("Error occured during export:", e);
			exportFailed = true;
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**
	 * Executed after the {@code doInBackground} method is finished. Disposes
	 * the exporting information dialog and displays, if no errors occurred
	 * during export, an information message in the GUI.
	 */
	@Override
	protected void done() {
		if (gui != null)
			gui.disposeExportingDialog();

		if (!exportFailed) {
			if (gui != null)
				gui.displayInfoMessage("Finished", "Image successfully exported");
			LOGGER.info("Image successfully exported");
		} else {
			LOGGER.info("Image could not be exported");
		}
	}

}