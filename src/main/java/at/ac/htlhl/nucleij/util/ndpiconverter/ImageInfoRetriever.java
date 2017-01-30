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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads the NDPI file and creates an image information object for every image
 * in the NDPI file. The image information object stores important image
 * properties from the TIFF-tags and JPEG-headers which are necessary to export
 * and/or convert the image. This class provides a simple TIFF and JPEG reader.
 * 
 * @author Matthias Baldauf
 * 
 */
public class ImageInfoRetriever extends SwingWorker<Void, Void> {

	private NDPIConverterGUI gui;
	private String fileMake;
	private String fileModel;
	private String fileSoftware;
	private String fileDateTime;
	private int fileImages;
	private RandomAccessFile in;
	private File ndpiFile;
	private boolean littleEndian;
	private ArrayList<ImageInfo> imgInfos;
	private static final Logger LOGGER = LoggerFactory.getLogger(ImageInfoRetriever.class);

	public static final int TIFF_MAGICAL_NUMBER = 42;
	public static final int TIFF_BYTE_ORDER_LITTLE_ENDIAN = 0x4949;
	public static final int TIFF_BYTE_ORDER_BIG_ENDIAN = 0x4d4d;
	public static final int TIFF_IMAGE_WIDTH = 256;
	public static final int TIFF_IMAGE_LENGTH = 257;
	public static final int TIFF_COMPRESSION = 259;
	public static final int TIFF_MAKE = 271;
	public static final int TIFF_MODEL = 272;
	public static final int TIFF_STRIP_OFFSETS = 273;
	public static final int TIFF_STRIP_BYTE_COUNTS = 279;
	public static final int TIFF_X_RESOLUTION = 282;
	public static final int TIFF_Y_RESOLUTION = 283;
	public static final int TIFF_RESOLUTION_UNIT = 296;
	public static final int TIFF_SOFTWARE = 305;
	public static final int TIFF_DATE_TIME = 306;
	public static final int NDPI_SOURCE_LENS = 65421;
	public static final int NDPI_RESTART_MARKER_OFFSETS = 65426;

	public static final int JPEG_MAGICAL_NUMBER = 0xffd8;
	public static final int JPEG_MARKER_APP0 = 0xffe0;
	public static final int JPEG_MARKER_COM = 0xfffe;
	public static final int JPEG_MARKER_DQT = 0xffdb;
	public static final int JPEG_MARKER_DHT = 0xffc4;
	public static final int JPEG_MARKER_SOF0 = 0xffc0;
	public static final int JPEG_MARKER_DRI = 0xffdd;
	public static final int JPEG_MARKER_SOS = 0xffda;

	@SuppressWarnings("unused")
	private ImageInfoRetriever() {
	}

	/**
	 * Creates a new file information retriever to read the NDPI file and create
	 * an image information object for every image in the NDPI file.
	 * 
	 * @param gui
	 *            the underlying GUI which will be updated to inform the user
	 *            about the image information retrieval or occurred errors
	 * @param ndpiFile
	 *            the input file (NDPI-file)
	 * @param imgInfos
	 *            an {@link ArrayList} in which the created image information
	 *            objects gets stored
	 */
	public ImageInfoRetriever(NDPIConverterGUI gui, File ndpiFile, ArrayList<ImageInfo> imgInfos) {
		this.gui = gui;
		this.ndpiFile = ndpiFile;
		this.imgInfos = imgInfos;
	}

	/**
	 * Reads the NDPI file, retrieves the image information for every image in
	 * the NDPI file and stores it in the given ArrayList.
	 */
	@Override
	protected Void doInBackground() {
		LOGGER.info("Start reading NDPI file information");

		try {
			// Set up NDPI file reader
			in = new RandomAccessFile(ndpiFile, "r");

			long ifdOffset = openImageFileHeader();
			if (ifdOffset == -1) {
				in.close();
				// Not a valid TIFF file
				LOGGER.error("Given file is not a valid NDPI file: no TIFF information found");
				if (gui != null)
					gui.displayErrorMessage("Not a NPDI file", "<b>This file is not a valid NDPI file</b><br>No TIFF information found in this file");
				return null;
			} else {
				while (ifdOffset != 0) {
					fileImages++;

					// Get the information of the current image
					ImageInfo imgInfo = openImageFileDirectory(ifdOffset);

					if (imgInfo != null) {
						imgInfos.add(imgInfo);
						// Determine offset of next IFD
						ifdOffset = ((long) readInt()) & 0xffffffffL;
					} else {
						ifdOffset = 0L;
					}
				}
				in.close();
				LOGGER.info("End reading NDPI file information");
				return null;
			}
		} catch (IOException e) {
			LOGGER.error("Error while reading NDPI file:", e);
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
		}

		return null;
	}

	/**
	 * Opens the TIFF IFH (Image File Header) to determine the byte-order of the
	 * NDPI file and to check if the NDPI file is a TIFF file.
	 * 
	 * @return the offset of the first IFD in the TIFF file; {@code -1} if the
	 *         file is not a valid TIFF file
	 * @throws IOException
	 *             if an I/O error occurred
	 */
	public long openImageFileHeader() throws IOException {
		// Determine the byte order
		int byteOrder = readShort();
		if (byteOrder == TIFF_BYTE_ORDER_LITTLE_ENDIAN) {
			// "II"
			littleEndian = true;
		} else if (byteOrder == TIFF_BYTE_ORDER_BIG_ENDIAN) {
			// "MM"
			littleEndian = false;
		} else {
			// Not a valid TIFF file
			in.close();
			return -1;
		}

		// Magical number
		int magicalNumber = readShort();
		if (magicalNumber != TIFF_MAGICAL_NUMBER) {
			// Not a valid TIFF file
			in.close();
			return -1;
		}

		// Determine offset of first IFD
		return ((long) readInt()) & 0xffffffffL;
	}

	/**
	 * Opens a TIFF IFD (Image File Directory) and reads important properties of
	 * the image. If the image is a JPEG image, further information are read
	 * from the JPEG header.
	 * 
	 * @param offset
	 *            the offset of the IFD
	 * @return an {@link ImageInfo} object containing the read image properties
	 * @throws IOException
	 *             if an I/O error occured
	 */
	public ImageInfo openImageFileDirectory(long offset) throws IOException {
		// Go to the start-byte of the IFD
		in.seek(offset);

		// Create new File-Info
		ImageInfo imgInfo = new ImageInfo(ndpiFile.getName());

		imgInfo.setOffsetIFD(offset);

		int numberOfEntries = readShort();
		for (int i = 0; i < numberOfEntries; i++) {
			int tag = readShort();
			in.skipBytes(2); // skip the data-type
			long numberOfValues = readInt();
			long value = readInt();

			// Only important tags are processed
			switch (tag) {
			case TIFF_IMAGE_WIDTH:
				imgInfo.setWidth((int) value);
				break;
			case TIFF_IMAGE_LENGTH:
				imgInfo.setHeight((int) value);
				break;
			case TIFF_COMPRESSION:
				imgInfo.setCompressionScheme((int) value);
				break;
			case TIFF_MAKE:
				String make = readString(value, (int) numberOfValues);
				if (fileMake == null)
					fileMake = make;
				imgInfo.setMake(make);
				break;
			case TIFF_MODEL:
				String model = readString(value, (int) numberOfValues);
				if (fileModel == null)
					fileModel = model;
				imgInfo.setModel(model);
				break;
			case TIFF_STRIP_OFFSETS:
				imgInfo.setOffsetImage(value);
				break;
			case TIFF_STRIP_BYTE_COUNTS:
				imgInfo.setFileSize(value);
				break;
			case TIFF_X_RESOLUTION:
				imgInfo.setResolutionX(readRational(value));
				break;
			case TIFF_Y_RESOLUTION:
				imgInfo.setResolutionY(readRational(value));
				break;
			case TIFF_RESOLUTION_UNIT:
				imgInfo.setResolutionUnit((int) value);
				break;
			case TIFF_SOFTWARE:
				String software = readString(value, (int) numberOfValues);
				if (fileSoftware == null)
					fileSoftware = software;
				imgInfo.setSoftware(software);
				break;
			case TIFF_DATE_TIME:
				String dateTime = readString(value, (int) numberOfValues);
				if (fileDateTime == null)
					fileDateTime = dateTime;
				imgInfo.setDateTime(dateTime);
				break;
			case NDPI_SOURCE_LENS:
				byte[] magnBytes = ByteBuffer.allocate(8).putLong(value).array();
				ByteBuffer magnOutBuf = ByteBuffer.wrap(magnBytes, 4, 4);
				imgInfo.setMagnification(magnOutBuf.getFloat());
				break;
			case NDPI_RESTART_MARKER_OFFSETS:
				long currentOffset = in.getFilePointer();
				in.seek(value);

				// Get all restart marker offsets
				long[] offsets = new long[(int) numberOfValues + 1];
				for (int y = 0; y < numberOfValues; y++) {
					offsets[y] = readInt();
				}
				// The last restart marker offset (in this case the EOI-marker) is also relevant
				offsets[offsets.length - 1] = imgInfo.getFileSize();

				in.seek(currentOffset);
				imgInfo.setRestartMarkerOffsets(offsets);
				break;
			}

		}

		// Check if the embedded file is a JPEG file
		if (imgInfo.isJPEG()) {
			// Read the JPEG-header
			openJPEGHeader(imgInfo);
		}

		return imgInfo;
	}

	/**
	 * Opens the JPEG header and reads important image properties of the JPEG
	 * image.
	 * 
	 * @param imgInfo
	 *            the {@link ImageInfo} object in which the image properties get
	 *            stored
	 * @throws IOException
	 *             if an I/O error occured
	 */
	public void openJPEGHeader(ImageInfo imgInfo) throws IOException {
		long currentOffset = in.getFilePointer();
		in.seek(imgInfo.getOffsetImage());

		// Magical number
		int magicalNumber = in.readUnsignedShort();
		if (magicalNumber != JPEG_MAGICAL_NUMBER) {
			// Not a valid Jpeg-file
			imgInfo.setValidFile(false);
		} else {
			imgInfo.setValidFile(true);
			int marker = in.readUnsignedShort();
			while (marker != JPEG_MARKER_SOS) {
				switch (marker) {
				case JPEG_MARKER_DRI:
					in.skipBytes(2);
					imgInfo.setRestartInterval(in.readUnsignedShort());
					break;
				case JPEG_MARKER_SOF0:
					int markerLength = in.readUnsignedShort();

					// Set the offset where the image width / height markers are
					imgInfo.setOffsetImageHeight(in.getFilePointer() + 1);

					in.skipBytes(5);
					int numComponents = in.read();
					imgInfo.setNumComponents(numComponents);
					if (numComponents == 3) {
						// Only JPEG-Files with three Components (= YCbCr) can be processed
						in.skipBytes(1);
						imgInfo.setYSubsampling(in.read());
						in.skipBytes(2);
						imgInfo.setCbSubsampling(in.read());
						in.skipBytes(2);
						imgInfo.setCrSubsampling(in.read());
						in.skipBytes(1);
					} else {
						// Skip the remaining bytes of the current marker
						in.skipBytes(markerLength - 8);
					}
					break;
				default:
					// Two bytes after each marker contains the length of the marker
					// Skip this marker because it is not relevant
					in.skipBytes(in.readUnsignedShort() - 2);
					break;
				}

				marker = in.readUnsignedShort();
			}
			// SOS-Marker starts here
			// Set the offset where pure image data begins
			imgInfo.setOffsetImageData(in.getFilePointer() + in.readUnsignedShort());
		}

		in.seek(currentOffset);
	}

	/**
	 * Reads two bytes from the input file.
	 * 
	 * @return two bytes as an {@link Integer} value
	 * @throws IOException
	 *             if an I/O error occurred
	 */
	public int readShort() throws IOException {
		int byte1 = in.read();
		int byte2 = in.read();
		if (littleEndian)
			return ((byte2 << 8) + byte1);
		else
			return ((byte1 << 8) + byte2);
	}

	/**
	 * Reads four bytes from the input file.
	 * 
	 * @return four bytes as a {@link Long} value
	 * @throws IOException
	 *             if an I/O error occurred
	 */
	public long readInt() throws IOException {
		int byte1 = in.read();
		int byte2 = in.read();
		int byte3 = in.read();
		int byte4 = in.read();
		if (littleEndian)
			return ((byte4 << 24) + (byte3 << 16) + (byte2 << 8) + byte1);
		else
			return ((byte1 << 24) + (byte2 << 16) + (byte3 << 8) + byte4);
	}

	/**
	 * Reads a string from the input file, starting at given offset.
	 * 
	 * @param offset
	 *            the offset to start
	 * @param length
	 *            the length of the string
	 * @return the read string
	 * @throws IOException
	 *             if an I/O error occurred
	 */
	public String readString(long offset, int length) throws IOException {
		// skip the zero-byte at the end
		length--;
		long currentOffset = in.getFilePointer();
		in.seek(offset);
		byte[] data = new byte[length];
		in.readFully(data);
		in.seek(currentOffset);
		return new String(data);
	}

	/**
	 * Reads eight bytes from the input file, starting at given offset, and
	 * converts it to a rational number.
	 * 
	 * @param offset
	 *            the offset to start
	 * @return the read rational number
	 * @throws IOException
	 *             if an I/O error occurred
	 */
	public double readRational(long offset) throws IOException {
		long currentOffset = in.getFilePointer();
		in.seek(offset);
		long numerator = readInt();
		long denominator = readInt();
		in.seek(currentOffset);
		return numerator / denominator;
	}

	/**
	 * Executed after the {@code doInBackground} method is finished. Disposes
	 * the information dialog and displays, if no errors occurred during
	 * analysis, an information message in the GUI.
	 */
	@Override
	protected void done() {
		if (gui != null) {
			gui.disposeReadingDialog();
			gui.displayNDPIFileInfo(Integer.toString(fileImages), fileMake, fileModel, fileSoftware, fileDateTime);
		}
	}

}