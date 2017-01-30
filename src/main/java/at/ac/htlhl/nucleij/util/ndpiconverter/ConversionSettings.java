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

import java.awt.RenderingHints;
import java.util.HashMap;

import loci.formats.tiff.TiffCompression;

/**
 * Stores and delivers the conversion settings for OME-TIFFs. Additionally, the
 * output directory for converted and exported images is stored.
 * 
 * <p>
 * This class uses the singleton design pattern to ensure that only one instance
 * of this class is created. The {@link #getInstance()} method must be called to
 * get the instance of this class.
 * </p>
 * 
 * @author Matthias Baldauf
 * 
 */
public class ConversionSettings {

	public static final byte STRIPS = 1;
	public static final byte TILES = 2;
	@SuppressWarnings("serial")
	private static final HashMap<String, Object> DOWNSCALING_ALGORITHMS = new HashMap<String, Object>() {
		{
			put("bicubic", RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			put("bilinear", RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			put("nearest-neighbour", RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		}
	};
	private static final int DOWNSCALING_FACTOR = 2;
	private int compressionAlgorithm;
	private String outputDir;
	private boolean downscalingSelected;
	private Object downscalingAlgorithm;
	private boolean bigTiff;
	private byte imgDataOrganization;
	private boolean multipageTiff;
	private boolean storeInSubdirectory;
	private boolean isLibjpegTurboAvailable;
	private static final ConversionSettings instance = new ConversionSettings();

	/**
	 * Private constructor to prevent instantiation from other classes. Creates
	 * a new conversion settings instance with default settings for
	 * standard-compliant OME-TIFFs.
	 * 
	 */
	private ConversionSettings() {
		bigTiff = false;
		storeInSubdirectory = true;
		downscalingSelected = false;
		downscalingAlgorithm = DOWNSCALING_ALGORITHMS.get("bicubic");
		compressionAlgorithm = TiffCompression.LZW.getCode();

		// Settings to create standard-compliant OME-TIFFs
		imgDataOrganization = STRIPS;
		multipageTiff = true;
	}

	/**
	 * Returns {@code true} if libjpeg-turbo was found on the system, and
	 * {@code false} otherwise. If libjpeg-turbo is available, it will be used
	 * to provide fast JPEG-decompression.
	 * 
	 * @return {@code true} if libjpeg-turbo is available, {@code false}
	 *         otherwise
	 */
	public boolean isLibjpegTurboAvailable() {
		return isLibjpegTurboAvailable;
	}

	/**
	 * Sets whether libjpeg-turbo is available for fast JPEG-decompression.
	 * 
	 * @param isLibjpegTurboAvailable
	 *            whether libjpeg-turbo is available
	 */
	public void setLibjpegTurboAvailable(boolean isLibjpegTurboAvailable) {
		this.isLibjpegTurboAvailable = isLibjpegTurboAvailable;
	}

	/**
	 * Returns the downscaling factor used to downscale the selected image.
	 * 
	 * @return the downscaling factor
	 */
	public int getDownscalingFactor() {
		return DOWNSCALING_FACTOR;
	}

	/**
	 * Returns the downscaling fraction. The downscaling fraction is determined
	 * by 1 / downscaling factor.
	 * 
	 * @return the downscaling fraction
	 */
	public float getDownscalingFraction() {
		return 1f / getDownscalingFactor();
	}

	/**
	 * Returns the available downscaling interpolation algorithms as an array of
	 * Strings.
	 * 
	 * @return the available downscaling interpolation algorithms
	 */
	public String[] getAvailableDownscalingAlgorithms() {
		return DOWNSCALING_ALGORITHMS.keySet().toArray(new String[0]);
	}

	/**
	 * Returns {@code true} if selected image should be downscaled to half size,
	 * and {@code false} otherwise.
	 * 
	 * @return {@code true} if selected image should be downscaled to half size,
	 *         {@code false} otherwise
	 */
	public boolean isDownscalingSelected() {
		return downscalingSelected;
	}

	/**
	 * Sets whether selected image should be downscaled to half size.
	 * 
	 * @param downscalingSelected
	 *            whether selected image should be downscaled to half size
	 */
	public void setDownscalingSelected(boolean downscaling) {
		this.downscalingSelected = downscaling;
	}

	/**
	 * Returns the downscaling interpolation algorithm which should be used for
	 * downscaling of the selected image.
	 * 
	 * @return the downscaling interpolation algorithm to use
	 */
	public Object getDownscalingAlgorithm() {
		return downscalingAlgorithm;
	}

	/**
	 * Returns the name of the downscaling interpolation algorithm which should
	 * be used for downscaling of the selected image.
	 * 
	 * @return the name of the downscaling interpolation algorithm to use
	 */
	public String getDownscalingAlgorithmAsString() {
		for (String key : DOWNSCALING_ALGORITHMS.keySet()) {
			if (DOWNSCALING_ALGORITHMS.get(key).equals(downscalingAlgorithm))
				return key;
		}
		return null;
	}

	/**
	 * Sets the downscaling interpolation algorithm which should be used for
	 * downscaling of the selected image.
	 * 
	 * @param algorithmName
	 *            the name of the downscaling interpolation algorithm to use
	 */
	public void setDownscalingAlgorithm(String algorithmName) {
		this.downscalingAlgorithm = DOWNSCALING_ALGORITHMS.get(algorithmName);
	}

	/**
	 * Returns {@code true} if BigTIFF file format should be used for the
	 * resulting OME-TIFF, and {@code false} if standard TIFF file format should
	 * be used.
	 * 
	 * @return {@code true} if BigTIFF file format should be used, {@code false}
	 *         if standard TIFF file format should be used
	 */
	public boolean isBigTiffSelected() {
		return bigTiff;
	}

	/**
	 * Sets whether BigTIFF file format should be used for resulting OME-TIFF.
	 * 
	 * @param bigTiff
	 *            whether BigTIFF file format should be used for resulting
	 *            OME-TIFF
	 */
	public void setBigTiffSelected(boolean bigTiff) {
		this.bigTiff = bigTiff;
	}

	/**
	 * Returns the preferred image data organization method for the resulting
	 * OME-TIFF. Image data can be organized into strips or tiles.
	 * 
	 * @return image data organization method ({@code ConversionSettings.STRIPS}
	 *         for striped OME-TIFF, {@code ConversionSettings.TILES} for tiled
	 *         OME-TIFF)
	 */
	public byte getImgDataOrganization() {
		return imgDataOrganization;
	}

	/**
	 * Returns the name of the preferred image data organization method for the
	 * resulting OME-TIFF.
	 * 
	 * @return name of the preferred image data organization method ("Strips",
	 *         "Tiles" or "NA")
	 */
	public String getImgDataOrganizationAsString() {
		if (imgDataOrganization == STRIPS)
			return "Strips";
		else if (imgDataOrganization == TILES)
			return "Tiles";
		else
			return "NA";
	}

	/**
	 * Sets the preferred image data organization method for the resulting
	 * OME-TIFF. Image data can be organized into strips or tiles.
	 * 
	 * <p>
	 * Note: Only striped OME-TIFFs are standard-compliant.
	 * </p>
	 * 
	 * @param imgDataOrganization
	 *            the image data organization method (
	 *            {@code ConversionSettings.STRIPS} for striped OME-TIFF,
	 *            {@code ConversionSettings.TILES} for tiled OME-TIFF)
	 */
	public void setImgDataOrganization(byte imgDataOrganization) {
		this.imgDataOrganization = imgDataOrganization;
	}

	/**
	 * Returns {@code true} if a multipage OME-TIFF (containing three images -
	 * one for each color-channel), and {@code false} if a singlepage OME-TIFF
	 * (containing one RGB image) should be created.
	 * 
	 * @return {@code true} if multipage OME-TIFF, {@code false} if a singlepage
	 *         OME-TIFF should be created
	 */
	public boolean isMultipageOMETiffSelected() {
		return multipageTiff;
	}

	/**
	 * Sets whether multipage OME-TIFF (containing three images - one for each
	 * color-channel), or whether singlepage OME-TIFF (containing one RGB image)
	 * should be created.
	 * 
	 * <p>
	 * Note: Only multipage OME-TIFFs are standard-compliant.
	 * </p>
	 * 
	 * @param multipageTiff
	 *            whether multipage OME-TIFF ({@code true}), or singlepage
	 *            OME-TIFF ({@code false}) should be created
	 */
	public void setMultipageOMETiffSelected(boolean multipageTiff) {
		this.multipageTiff = multipageTiff;
	}

	/**
	 * Returns the compression algorithm which should be used to compress the
	 * resulting OME-TIFF.
	 * 
	 * @return the compression algorithm to use
	 */
	public int getCompressionAlgorithm() {
		return compressionAlgorithm;
	}

	/**
	 * Returns the name of the compression algorithm which should be used to
	 * compress the resulting OME-TIFF.
	 * 
	 * @return the name of the compression algorithm to use
	 */
	public String getCompressionAlgorithmAsString() {
		if (compressionAlgorithm == TiffCompression.UNCOMPRESSED.getCode()) {
			return "Uncompressed";
		} else if (compressionAlgorithm == TiffCompression.LZW.getCode()) {
			return "LZW";
		} else if (compressionAlgorithm == TiffCompression.DEFLATE.getCode()) {
			return "Deflate";
		} else if (compressionAlgorithm == TiffCompression.PROPRIETARY_DEFLATE.getCode()) {
			return "Adobe Deflate";
		} else if (compressionAlgorithm == TiffCompression.JPEG_2000.getCode()) {
			return "JPEG 2000";
		} else {
			return "(NA)";
		}
	}

	/**
	 * Sets the compression algorithm which should be used to compress the
	 * resulting OME-TIFF.
	 * 
	 * @param compression
	 *            the compression algorithm to use
	 */
	public void setCompressionAlgorithm(int compressionAlgorithm) {
		this.compressionAlgorithm = compressionAlgorithm;
	}

	/**
	 * Returns the output directory in which converted or exported images should
	 * be stored.
	 * 
	 * @return the output directory to use
	 */
	public String getOutputDirectory() {
		return outputDir;
	}

	/**
	 * Returns <code>true</code> if the converted and exported images should be
	 * automatically stored in a subdirectory of the selected NDPI file, and
	 * <code>false</code> if the images should be stored in a user-defined
	 * directory. If {@code true}, converted images will be stored in the
	 * subdirectory {@code Constants.SUBDIRECTORY_CONVERTED} and exported images
	 * in the subdirectory {@code Constants.SUBDIRECTORY_EXPORTED} of the
	 * selected NDPI file.
	 * 
	 * @return <code>true</code> if the converted and exported images should be
	 *         automatically stored in a subdirectory, <code>false</code> if the
	 *         images should be stored in a user-defined directory
	 */
	public boolean isStoreInSubdirectorySelected() {
		return storeInSubdirectory;
	}

	/**
	 * Sets whether the converted and exported images should be automatically
	 * stored in a subdirectory of the selected NDPI file, or whether the images
	 * should be stored in a user-defined directory. If {@code true}, converted
	 * images will be stored in the subdirectory
	 * {@code Constants.SUBDIRECTORY_CONVERTED} and exported images in the
	 * subdirectory {@code Constants.SUBDIRECTORY_EXPORTED} of the
	 * 
	 * @param storeInSubdirectory
	 *            if OME-TIFF should be automatically saved in a subdirectory or
	 *            in a user-defined directory
	 */
	public void setStoreInSubdirectorySelected(boolean storeInSubdirectory) {
		this.storeInSubdirectory = storeInSubdirectory;
	}

	/**
	 * Sets the output directory in which converted or exported images should be
	 * stored if no user-defined output directory was chosen. Converted images
	 * will be stored in the subdirectory
	 * {@code Constants.SUBDIRECTORY_CONVERTED} and exported images in the
	 * subdirectory {@code Constants.SUBDIRECTORY_EXPORTED} of the given output
	 * directory.
	 * 
	 * @param outputDir
	 *            the output directory (path to selected NDPI file) to which the
	 *            appropriate subdirectory will be added
	 * @param convertImage
	 *            whether selected image gets converted and therefore
	 *            {@code Constants.SUBDIRECTORY_CONVERTED} will be added to the
	 *            given output directory, or whether selected image gets
	 *            exported and therefore {@code Constants.SUBDIRECTORY_EXPORTED}
	 *            will be added to the given output directory
	 */
	public void setOutputDirectory(String outputDir, boolean convertImage) {
		this.outputDir = outputDir + System.getProperty("file.separator") + (convertImage ? Constants.SUBDIRECTORY_CONVERTED : Constants.SUBDIRECTORY_EXPORTED);
	}

	/**
	 * Sets the user-defined output directory in which converted and exported
	 * images should be stored. Both converted and exported images will be
	 * stored in the given output directory.
	 * 
	 * @param outputDir
	 *            the user-defined output directory to use
	 */
	public void setOutputDirectory(String outputDir) {
		this.outputDir = outputDir;
	}

	/**
	 * Returns the singleton instance of the conversion settings
	 * 
	 * @return the conversion settings instance
	 */
	public static ConversionSettings getInstance() {
		return instance;
	}

}