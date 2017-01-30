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

import java.text.DecimalFormat;

/**
 * Stores and delivers important properties about a single image of a NDPI file.
 * This information is used to export the image, to determine if the image can
 * be converted and to convert the image.
 * 
 * @author Matthias Baldauf
 * 
 */
public class ImageInfo {

	private String ndpiFilename;
	private String make;
	private String model;
	private String software;
	private String dateTime;
	private int width;
	private int height;
	private double resolutionX;
	private double resolutionY;
	private int resolutionUnit;
	private long offsetIFD;
	private long offsetImage;
	private long offsetImageData;
	private long offsetImageHeight;
	private long[] offsetRestartMarkers;
	private long filesize;
	private int compressionScheme;
	private int restartInterval;
	private int components;
	private int subsamplingY;
	private int subsamplingCb;
	private int subsamplingCr;
	private float magnification;
	private boolean isValidFile;
	private boolean hasRestartInterval;
	private boolean hasRestartMarkerOffsets;
	private boolean hasThreeComponents;

	public static final int COMPRESSION_NONE = 1;
	public static final int COMPRESSION_JPEG_OLD_STYLE = 6;
	public static final int COMPRESSION_JPEG_NEW_STYLE = 7;
	public static final String SUBSAMPLING_1x1 = "4:4:4";
	public static final String SUBSAMPLING_NA = "(NA)";
	public static final String RESOLUTION_UNIT_NONE = "None";
	public static final String RESOLUTION_UNIT_INCH = "Inch";
	public static final String RESOLUTION_UNIT_CENTIMETER = "Centimeter";

	@SuppressWarnings("unused")
	private ImageInfo() {
	}

	/**
	 * Creates a new image information object for storing and delivering
	 * important image properties.
	 * 
	 * @param ndpiFilename
	 *            the filename of the NDPI file
	 */
	public ImageInfo(String ndpiFilename) {
		this.ndpiFilename = ndpiFilename;
		this.hasRestartInterval = false;
		this.hasRestartMarkerOffsets = false;
	}

	/**
	 * Returns the filename of the underlying NDPI file.
	 * 
	 * @return the filename of the underlying NDPI file
	 */
	public String getInputFilename() {
		return ndpiFilename;
	}

	/**
	 * Returns the filename of the output-file (OME-TIFF). The filename is
	 * composed of the NDPI filename, the magnification of the image and the
	 * filename extension (ome.tif).
	 * 
	 * @return the filename of the OME-TIFF
	 */
	public String getOMETiffFilename() {
		int dot = ndpiFilename.lastIndexOf(".");
		return (dot >= 0 ? ndpiFilename.substring(0, dot) : ndpiFilename) + "_" + getMagnification() + ".ome.tif";
	}

	/**
	 * Returns the the filename of the export-file. The filename is composed of
	 * the NDPI filename, the magnification of the image (if available), the
	 * width and height of the image and the filename extension (.jpeg for JPEG
	 * images and .bmp for TIFF images).
	 * 
	 * @return the filename of the export-file
	 */
	public String getExportFilename() {
		int dot = ndpiFilename.lastIndexOf(".");
		return (dot >= 0 ? ndpiFilename.substring(0, dot) : ndpiFilename) + "_" + (getMagnification() != null ? getMagnification() + "_" : "") + getWidth() + "x" + getHeight()
				+ (getCompressionScheme().equals("TIFF") ? ".bmp" : "." + getCompressionScheme().toLowerCase());
	}

	/**
	 * Returns the filename of the temporary file which holds the image data for
	 * the green color channel. The temporary file is needed for the creation of
	 * multipage OME-TIFFs. The filename is composed of the NDPI filename, the
	 * magnification and the filename extension (.green.tmp).
	 * 
	 * @return the filename of the temporary file for the green color channel
	 */
	public String getTempGreenFilename() {
		int dot = ndpiFilename.lastIndexOf(".");
		return (dot >= 0 ? ndpiFilename.substring(0, dot) : ndpiFilename) + "_" + getMagnification() + ".green.tmp";
	}

	/**
	 * Returns the filename of the temporary file which holds the image data for
	 * the blue color channel. The temporary file is needed for the creation of
	 * multipage OME-TIFFs. The filename is composed of the NDPI filename, the
	 * magnification and the filename extension (.blue.tmp).
	 * 
	 * @return the filename of the temporary file for the blue color channel
	 */
	public String getTempBlueFilename() {
		int dot = ndpiFilename.lastIndexOf(".");
		return (dot >= 0 ? ndpiFilename.substring(0, dot) : ndpiFilename) + "_" + getMagnification() + ".blue.tmp";
	}

	/**
	 * Returns the make of the slide scanner.
	 * 
	 * @return the make of the slide scanner
	 */
	public String getMake() {
		return make;
	}

	/**
	 * Sets the make of the slide scanner.
	 * 
	 * @param make
	 *            the make of the slide scanner
	 */
	public void setMake(String make) {
		this.make = make;
	}

	/**
	 * Returns the model of the slide scanner.
	 * 
	 * @return the model of the slide scanner
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets the model of the slide scanner.
	 * 
	 * @param model
	 *            the model of the slide scanner
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Returns the name of the software used to create the NDPI file.
	 * 
	 * @return the name of the software
	 */
	public String getSoftware() {
		return software;
	}

	/**
	 * Sets the name of the software used to create the NDPI file.
	 * 
	 * @param software
	 *            the name of the software
	 */
	public void setSoftware(String software) {
		this.software = software;
	}

	/**
	 * Returns the date and time of the scan.
	 * 
	 * @return date and time of the scan
	 */
	public String getDateTime() {
		return dateTime;
	}

	/**
	 * Sets the date and time of the scan.
	 * 
	 * @param dateTime
	 *            date and time of the scan
	 */
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * Returns the downscaled width of the image. The image width gets divided
	 * by the downscaling factor.
	 * 
	 * @param downscalingFactor
	 *            the downscale factor
	 * @return the downscaled width of the image; {@code -1} if image width is
	 *         not divisible without a remainder
	 */
	public int getDownscaledWidth(int downscalingFactor) {
		if (getWidth() % downscalingFactor > 0)
			return -1;
		else
			return getWidth() / downscalingFactor;
	}

	/**
	 * Returns the downscaled height of the image. The image height gets divided
	 * by the downscaling factor.
	 * 
	 * @param downscalingFactor
	 *            the downscale factor
	 * @return the downscaled height of the image; {@code -1} if image height is
	 *         not divisible without a remainder
	 */
	public int getDownscaledHeight(int downscaleFactor) {
		if (getHeight() % downscaleFactor > 0)
			return -1;
		else
			return getHeight() / downscaleFactor;
	}

	/**
	 * Returns the downscaled strip height of the image. The strip height gets
	 * divided by the downscaling factor.
	 * 
	 * @param downscalingFactor
	 *            the downscale factor
	 * @return the downscaled strip height of the image; {@code -1} if strip
	 *         height is not divisible without a remainder
	 */
	public int getDownscaledStripHeight(int downscaleFactor) {
		if (getStripHeight() % downscaleFactor > 0)
			return -1;
		else
			return getStripHeight() / downscaleFactor;
	}

	/**
	 * Returns the width of the image.
	 * 
	 * @return the width of the image
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width of the image.
	 * 
	 * @param width
	 *            the width of the image
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Returns the height of the image.
	 * 
	 * @return the height of the image
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height of the image.
	 * 
	 * @param height
	 *            the height of the image
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Returns the image resolution (number of pixels per unit) in horizontal
	 * direction.
	 * 
	 * @return the image resolution in horizontal direction
	 */
	public double getResolutionX() {
		return resolutionX;
	}

	/**
	 * Sets the image resolution (number of pixels per unit) in horizontal
	 * direction
	 * 
	 * @param resolutionX
	 *            the image resolution in horizontal direction
	 */
	public void setResolutionX(double resolutionX) {
		this.resolutionX = resolutionX;
	}

	/**
	 * Returns the image resolution (number of pixels per unit) in vertical
	 * direction
	 * 
	 * @return the image resolution in vertical direction
	 */
	public double getResolutionY() {
		return resolutionY;
	}

	/**
	 * Sets the image resolution (number of pixels per unit) in vertical
	 * direction
	 * 
	 * @param resolutionY
	 *            the image resolution in vertical direction
	 */
	public void setResolutionY(double resolutionY) {
		this.resolutionY = resolutionY;
	}

	/**
	 * Returns the resolution unit of the image.
	 * 
	 * @return the resolution unit
	 */
	public String getResolutionUnit() {
		switch (resolutionUnit) {
		case 2:
			return RESOLUTION_UNIT_INCH;
		case 3:
			return RESOLUTION_UNIT_CENTIMETER;
		default:
			return RESOLUTION_UNIT_NONE;
		}
	}

	/**
	 * Sets the resolution unit of the image.
	 * 
	 * @param resolutionUnit
	 *            the resolution unit
	 */
	public void setResolutionUnit(int resolutionUnit) {
		this.resolutionUnit = resolutionUnit;
	}

	/**
	 * Returns the physical size of one pixel in microns in horizontal
	 * direction.
	 * 
	 * @return physical size of one pixel in microns in horizontal direction;
	 *         {@code -1} if the physical size can't be calculated
	 */
	public double getPixelsPhysicalSizeX() {
		switch (resolutionUnit) {
		case 2:
			// INCH (1 inch = 2.54 cm)
			return (double) 25400 / getResolutionX();
		case 3:
			// CENTIMETER
			return (double) 10000 / getResolutionX();
		default:
			return -1;
		}
	}

	/**
	 * Returns the physical size of one pixel in microns in vertical direction.
	 * 
	 * @return physical size of one pixel in microns in vertical direction;
	 *         {@code -1} if the physical size can't be calculated
	 */
	public double getPixelsPhysicalSizeY() {
		switch (resolutionUnit) {
		case 2:
			// INCH (1 inch = 2.54 cm)
			return (double) 25400 / getResolutionY();
		case 3:
			// CENTIMETER
			return (double) 10000 / getResolutionY();
		default:
			return -1;
		}
	}

	/**
	 * Returns the offset where the IFD of the image in the NDPI file starts.
	 * 
	 * @return the offset of the IFD
	 */
	public long getOffsetIFD() {
		return this.offsetIFD;
	}

	/**
	 * Sets the offset where the IFD of the image in the NDPI file starts.
	 * 
	 * @param offsetFile
	 *            the offset of the IFD
	 */
	public void setOffsetIFD(long offsetIFD) {
		this.offsetIFD = offsetIFD;
	}

	/**
	 * Returns the offset where the image in the NDPI file starts.
	 * 
	 * @return the offset of the image
	 */
	public long getOffsetImage() {
		return this.offsetImage;
	}

	/**
	 * Sets the offset where the image in the NDPI file starts.
	 * 
	 * @param offsetFile
	 *            the offset of the image
	 */
	public void setOffsetImage(long offsetImage) {
		this.offsetImage = offsetImage;
	}

	/**
	 * Returns the offset where the pure image data of the JPEG file in the NDPI
	 * file starts.
	 * 
	 * @return the offset of the pure image data
	 */
	public long getOffsetImageData() {
		return this.offsetImageData;
	}

	/**
	 * Sets the offset where the pure image data of the JPEG file in the NDPI
	 * file starts.
	 * 
	 * @param offsetImageData
	 *            the offset of the pure image data
	 */
	public void setOffsetImageData(long offsetImageData) {
		this.offsetImageData = offsetImageData;
	}

	/**
	 * Returns the offset of the image-height field in the SOF0-marker of the
	 * JPEG image.
	 * 
	 * @return offset of the image-height field
	 */
	public long getOffsetImageHeight() {
		return this.offsetImageHeight;
	}

	/**
	 * Sets the offset of the image-height field in the SOF0-marker of the JPEG
	 * image.
	 * 
	 * @param offsetImageHeight
	 *            the offset of the image-height field
	 */
	public void setOffsetImageHeight(long offsetImageHeight) {
		this.offsetImageHeight = offsetImageHeight;
	}

	/**
	 * Returns the file size of the image in bytes.
	 * 
	 * @return file size of the image in bytes
	 */
	public long getFileSize() {
		return this.filesize;
	}

	/**
	 * Returns the file size formated with an appropriate binary prefix (KB or
	 * MB).
	 * 
	 * @return formated file size
	 */
	public String getFileSizeFormated() {
		DecimalFormat fdf = new DecimalFormat(".##");
		if (filesize > (1024 * 1024)) {
			double imgFileSize = (double) filesize / (1024 * 1024);
			return fdf.format(imgFileSize) + " MB";
		} else {
			double imgFileSize = (double) filesize / 1024;
			return fdf.format(imgFileSize) + " KB";
		}
	}

	/**
	 * Sets the file size of the image in bytes.
	 * 
	 * @param filesize
	 *            the file size of the image in bytes
	 */
	public void setFileSize(long filesize) {
		this.filesize = filesize;
	}

	/**
	 * Returns the compression scheme of the image.
	 * 
	 * @return the compression scheme of the image; "(NA)" if the compression
	 *         scheme is not JPEG or TIFF
	 */
	public String getCompressionScheme() {
		switch (compressionScheme) {
		case COMPRESSION_JPEG_OLD_STYLE:
		case COMPRESSION_JPEG_NEW_STYLE:
			return "JPEG";
		case COMPRESSION_NONE:
			return "TIFF";
		default:
			// Not supported
			return "(NA)";
		}
	}

	/**
	 * Sets the compression scheme of the image.
	 * 
	 * @param compression
	 *            the compression scheme of the image
	 */
	public void setCompressionScheme(int compressionScheme) {
		this.compressionScheme = compressionScheme;
	}

	/**
	 * Returns {@code true} if image is a JPEG image, and {@code false}
	 * otherwise.
	 * 
	 * @return {@code tue} if image is a JPEG image, {@code false} otherwise
	 */
	public boolean isJPEG() {
		if (compressionScheme == COMPRESSION_JPEG_OLD_STYLE || compressionScheme == COMPRESSION_JPEG_NEW_STYLE)
			return true;
		else
			return false;
	}

	/**
	 * Returns {@code true} if image is an uncompressed image (uncompressed
	 * TIFF), and {@code false} otherwise.
	 * 
	 * @return {@code true} if uncompressed, {@code false} otherwise
	 */
	public boolean isUncompressed() {
		return compressionScheme == COMPRESSION_NONE;
	}

	/**
	 * Returns the restart interval of the restart markers in the JPEG image.
	 * 
	 * @return the restart interval of the restart markers
	 */
	public int getRestartInterval() {
		return restartInterval;
	}

	/**
	 * Returns {@code true} if JPEG image has a restart interval for restart
	 * markers, and {@code false} otherwise.
	 * 
	 * @return {@code true} if restart interval existent, {@code false}
	 *         otherwise
	 */
	public boolean hasRestartInterval() {
		return hasRestartInterval;
	}

	/**
	 * Sets the restart interval for restart markers of the JPEG image.
	 * 
	 * @param restartInterval
	 *            the restart interval for restart markers
	 */
	public void setRestartInterval(int restartInterval) {
		this.restartInterval = restartInterval;
		hasRestartInterval = true;
	}

	/**
	 * Returns the offsets of the restart markers in the JPEG image as an array.
	 * First value: offset of pure image data. Last value: offset of EOI-marker.
	 * 
	 * @return the offsets of the restart markers
	 */
	public long[] getRestartMarkerOffsets() {
		return offsetRestartMarkers;
	}

	/**
	 * Sets the offsets of the restart markers in the JPEG image.
	 * 
	 * @param offsetRestartMarkers
	 *            the offsets of the restart markers
	 */
	public void setRestartMarkerOffsets(long[] offsetRestartMarkers) {
		this.offsetRestartMarkers = offsetRestartMarkers;
		hasRestartMarkerOffsets = true;
	}

	/**
	 * Returns {@code true} if image is a valid image file, and {@code false}
	 * otherwise.
	 * 
	 * @return {@code true} if image is a valid image file, {@code false}
	 *         otherwise
	 */
	public boolean isValidFile() {
		return isValidFile;
	}

	/**
	 * Sets whether image is a valid image file.
	 * 
	 * @param isValidFile
	 *            whether image is a valid image file
	 */
	public void setValidFile(boolean isValidFile) {
		this.isValidFile = isValidFile;
	}

	/**
	 * Returns the number of color components of the JPEG image.
	 * 
	 * @return the number of color components of the JPEG image
	 */
	public int getNumComponents() {
		return components;
	}

	/**
	 * Returns {@code true} if JPEG image color space is YCbCr, and
	 * {@code false} otherwise.
	 * 
	 * @return {@code true} if JPEG-image color space is YCbCr, {@code false}
	 *         otherwise
	 */
	public boolean isYCbCr() {
		if (hasThreeComponents)
			return true;
		else
			return false;
	}

	/**
	 * Sets the number of color components of the JPEG image.
	 * 
	 * @param components
	 *            the number of color components of the JPEG image
	 */
	public void setNumComponents(int components) {
		this.components = components;
		if (components == 3)
			this.hasThreeComponents = true;
		else
			this.hasThreeComponents = false;
	}

	/**
	 * Returns the YCbCr subsampling of the JPEG image. Only 4:4:4 subsampling
	 * is supported.
	 * 
	 * @return subsampling of the JPEG image; "(NA)" if subsampling of the JPEG
	 *         image is not supported
	 */
	public String getSubsampling() {
		// 1h1v,1h1v,1h1v (also called 4:4:4 or 1x1 sampling)
		if ((subsamplingY == 0x11) && (subsamplingCb == 0x11) && (subsamplingCr == 0x11)) {
			return SUBSAMPLING_1x1;
		} else {
			// Not supported
			return SUBSAMPLING_NA;
		}
	}

	/**
	 * Sets the Y-subsampling of the JPEG image.
	 * 
	 * @param subsamplingY
	 *            the Y-subsampling of the JPEG image
	 */
	public void setYSubsampling(int subsamplingY) {
		this.subsamplingY = subsamplingY;
	}

	/**
	 * Sets the Cb-subsampling of the JPEG image.
	 * 
	 * @param subsamplingCb
	 *            the Cb-subsampling of the JPEG image
	 */
	public void setCbSubsampling(int subsamplingCb) {
		this.subsamplingCb = subsamplingCb;
	}

	/**
	 * Sets the Cr-subsampling of the JPEG image.
	 * 
	 * @param subsamplingCr
	 *            the Cr-subsampling of the JPEG image
	 */
	public void setCrSubsampling(int subsamplingCr) {
		this.subsamplingCr = subsamplingCr;
	}

	/**
	 * Returns the tile width in pixels. If YCbCr subsampling is 4:4:4, the tile
	 * width is calculated as (8 * restart interval).
	 * 
	 * @return tile width in pixels; {@code -1} if tile width could not be
	 *         determined (unsupported YCbCr subsampling or no restart interval)
	 */
	public int getTileWidth() {
		if (hasRestartInterval) {
			if (getSubsampling().equals(SUBSAMPLING_1x1)) {
				// With 4:4:4 Subsampling a MCU-unit consists of 8x8 pixel
				// Therefore multiply the number of MCU-units (= restart
				// interval by 8 to get the tile width
				return getRestartInterval() * 8;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Returns the tile height in pixels. The calculation is identical to the
	 * tile width.
	 * 
	 * @return tile height in pixels; {@code -1} if tile width could not be
	 *         determined (unsupported YCbCr subsampling or no restart interval)
	 * @see #getTileWidth()
	 */
	public int getTileHeight() {
		// Tile width is equal tile height
		return getTileWidth();
	}

	/**
	 * Returns the number of tiles in horizontal direction.
	 * 
	 * @return the number of tiles in horizontal direction
	 */
	public int getTilesX() {
		return getWidth() / getTileWidth();
	}

	/**
	 * Returns the number of tiles in vertical direction.
	 * 
	 * @return the number of tiles in vertical direction
	 */
	public int getTilesY() {
		// Round up, because number of vertical tiles might not be an integer
		return (int) Math.ceil((double) height / (double) getTileHeight());
	}

	/**
	 * Returns the number of MCU-rows per tile. If YCbCr subsampling is 4:4:4,
	 * the height of one MCU is 8 pixels. Therefore, the number of MCU-rows per
	 * tile is calculated as (tile height / 8).
	 * 
	 * @return number of MCU-rows per tile; {@code -1} if MCU-rows per tile
	 *         could not be determined (unsupported YCbCr subsampling)
	 */
	public int getRowsPerTile() {
		if (getSubsampling().equals(SUBSAMPLING_1x1)) {
			// With 4:4:4 Subsampling a MCU-unit consists of 8x8 pixel
			// Therefore divide the tile height by 8 to get the number of
			// MCU-rows per tile
			return getTileHeight() / 8;
		} else {
			return -1;
		}
	}

	/**
	 * Returns the strip width in pixels. The strip width is equal to the image
	 * height.
	 * 
	 * @return the strip width in pixels
	 */
	public int getStripWidth() {
		// Strip width is equal to the image width
		return getWidth();
	}

	/**
	 * Returns the strip height in pixels. If YCbCr subsampling is 4:4:4, the
	 * strip height is 8 pixels (height of a MCU).
	 * 
	 * @return the strip height in pixels; {@code -1} if strip height could not
	 *         be determined (unsupported YCbCr subsampling or no restart
	 *         interval)
	 */
	public int getStripHeight() {
		if (hasRestartInterval) {
			if (getSubsampling().equals(SUBSAMPLING_1x1)) {
				// With 4:4:4 Subsampling a MCU-unit consists of 8x8 pixel
				// Therefore the strip height is 8 pixels
				return 8;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}

	/**
	 * Returns the number of strips in vertical direction.
	 * 
	 * @return the number of strips in vertical direction
	 */
	public int getStripsY() {
		return height / getStripHeight();
	}

	/**
	 * Returns the number of tiles per strip. A tile in this context is the part
	 * of the image between two restart markers.
	 * 
	 * @return the number of tiles per strip; {@code -1} if the number of tiles
	 *         could not be determined (unsupported YCbCr subsampling)
	 */
	public int getNumberOfTilesPerStrip() {
		if (getSubsampling().equals(SUBSAMPLING_1x1)) {
			// Divide the image width by the width of one tile
			// because the tile with is the number of pixels
			// between two restart markers
			return getWidth() / getTileWidth();
		} else {
			return -1;
		}
	}

	/**
	 * Returns the magnification of the image.
	 * 
	 * @return the magnification of the image; {@code null} if no magnification
	 *         information is available
	 */
	public String getMagnification() {
		if (magnification > 0) {
			if (magnification % 1 == 0)
				return "x" + Integer.toString((int) magnification);
			else
				return "x" + Float.toString(magnification);
		} else {
			return null;
		}
	}

	/**
	 * Sets the magnification of the image.
	 * 
	 * @param magnification
	 *            the magnification of the image
	 */
	public void setMagnification(float magnification) {
		this.magnification = magnification;
	}

	/**
	 * Returns the image type. The image type can be:
	 * <ul>
	 * <li>Image: shows the scanned image</li>
	 * <li>Macro: shows a overall picture of the scanned slide</li>
	 * <li>Map: probably shows the scanned area on the slide</li>
	 * </ul>
	 * 
	 * @return the image type
	 */
	public String getImageType() {
		if (magnification == -1) {
			// '-1' is the NDPI-marker that this image is the macro-image:
			// It shows a overall picture of the scanned slide.
			return "Macro";
		} else if (magnification == -2) {
			// '-2' is the NDPI-marker that this image is a map-image:
			// It probably shows the scanned area on the slide.
			// The scanned area is indicated by the color #010101
			// and the omitted area has the color #000000.
			return "Map";
		} else {
			return "Image";
		}
	}

	/**
	 * Returns {@code true} if the image is convertible to an OME-TIFF, and
	 * {@code false} otherwise.
	 * 
	 * @return {@code true} if the image is convertible to an OME-TIFF,
	 *         {@code false} otherwise
	 */
	public boolean isConvertible() {
		if (isValidFile && isJPEG() && getSubsampling().equals(SUBSAMPLING_1x1) && isYCbCr() && hasRestartInterval && hasRestartMarkerOffsets) {
			return true;
		} else {
			return false;
		}
	}

}