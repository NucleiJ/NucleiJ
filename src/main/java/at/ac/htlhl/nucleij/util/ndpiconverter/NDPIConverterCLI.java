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

import gnu.getopt.Getopt;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import loci.formats.tiff.TiffCompression;

import org.libjpegturbo.turbojpeg.TJDecompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides the command-line interface for the NDPI to OME-TIFF Converter.
 * 
 * @author Matthias Baldauf
 * @see NDPIConverterGUI
 * 
 */
public class NDPIConverterCLI {

	private CLArgumentsStore argStore;
	private ArrayList<ImageInfo> imgInfos;
	private File ndpiFile;
	private static final Logger LOGGER = LoggerFactory.getLogger(NDPIConverterCLI.class);
	private ConversionSettings settings = ConversionSettings.getInstance();

	/**
	 * Creates a new command-line NDPI to OME-TIFF Converter with the given
	 * command-line argument store.
	 * 
	 * @param argStore
	 *            the command-line argument store
	 * @see CLArgumentsStore
	 */
	public NDPIConverterCLI(CLArgumentsStore argStore) {
		this.argStore = argStore;
		imgInfos = new ArrayList<ImageInfo>();
	}

	/**
	 * Starts the command-line NDPI to OME-TIFF Converter. The following
	 * operations will be carried out:
	 * <ul>
	 * <li>Checks if libjpeg-turbo is installed</li>
	 * <li>Checks if the NDPI file and output directory exists</li>
	 * <li>Reads the contents of the NDPI file</li>
	 * <li>If requested: displays information about containing images of the
	 * NDPI file and exit</li>
	 * <li>If image number selected: checks if given image number is correct</li>
	 * <li>Processes the selected image (export or convert)</li>
	 * </ul>
	 * 
	 * @return the return code ({@code 0} on success, {@code 1} on argument
	 *         failure, {@code 2} on exception during conversion/export)
	 */
	public int start() {
		LOGGER.info(Constants.TOOL_TITLE + " " + Constants.TOOL_VERSION);

		// Check if libjpeg-turbo is installed
		try {
			new TJDecompressor();
			ConversionSettings.getInstance().setLibjpegTurboAvailable(true);
		} catch (UnsatisfiedLinkError e) {
			LOGGER.warn("libjpeg-turbo was not found on your system:");
			LOGGER.warn("Java built-in JPEG-decoder will be used");
			ConversionSettings.getInstance().setLibjpegTurboAvailable(false);
		} catch (Exception e) {
			LOGGER.warn("libjpeg-turbo could not be started");
			LOGGER.warn("Java built-in JPEG-decoder will be used", e);
			ConversionSettings.getInstance().setLibjpegTurboAvailable(false);
		}

		ndpiFile = new File(argStore.getNDPIFilename());

		// Check if NDPI file exists
		if (!ndpiFile.exists()) {
			LOGGER.error("NDPI file not found: " + argStore.getNDPIFilename());
			return 1;
		}

		// Check if output directory exists
		if (settings.getOutputDirectory() == null) {
			// Set output directory to subfolder of given NDPI file
			settings.setStoreInSubdirectorySelected(true);
			String absPath = ndpiFile.getAbsolutePath();
			settings.setOutputDirectory(absPath.substring(0, absPath.lastIndexOf(File.separator)), !argStore.isExportOnly());
		} else if (!new File(settings.getOutputDirectory()).exists()) {
			LOGGER.error("Output directory does not exist:");
			LOGGER.error(settings.getOutputDirectory());
			return 1;
		}

		// Read content of NDPI file
		ImageInfoRetriever swingWorker = new ImageInfoRetriever(null, ndpiFile, imgInfos);
		try {
			SwingUtilities.invokeAndWait(swingWorker);
		} catch (InterruptedException e) {
			LOGGER.error("File information retriever was interrupted:", e);
			return 2;
		} catch (InvocationTargetException e) {
			LOGGER.error("An exception occured while retrieving file information:", e);
			return 2;
		}

		if (argStore.isFileContentDisplayOnly()) {
			displayFileContent();
		} else {
			// Check if given image number is correct
			if (!argStore.isMacroSelected() && !argStore.isMapSelected()) {
				if ((argStore.getImagePositionNumber() < 1) || (argStore.getImagePositionNumber() - 1 >= imgInfos.size())) {
					LOGGER.error("Wrong image number '" + argStore.getImagePositionNumber() + "': must be between 1 and " + imgInfos.size());
					return 1;
				}
			}

			return processSelectedImage(argStore.isExportOnly());
		}

		return 0;
	}

	/**
	 * Processes the selected image by either converting or exporting the image:
	 * Gets the image information object for the selected image, checks if the
	 * output file already exists (exits if true) and starts the conversion or
	 * export of the selected image.
	 * 
	 * @param exportImage
	 *            whether selected image should be exported, or whether selected
	 *            image should be converted
	 * @return the return code ({@code 0} on success, {@code 2} on exception
	 *         during conversion/export)
	 */
	private int processSelectedImage(boolean exportImage) {
		ImageInfo imgInfo = null;
		if (argStore.isMacroSelected()) {
			for (ImageInfo i : imgInfos) {
				if (i.getImageType().equals("Macro"))
					imgInfo = i;
			}
		} else if (argStore.isMapSelected()) {
			for (ImageInfo i : imgInfos) {
				if (i.getImageType().equals("Map"))
					imgInfo = i;
			}
		} else {
			imgInfo = imgInfos.get(argStore.getImagePositionNumber() - 1);
		}

		if (imgInfo == null) {
			LOGGER.error("Can't process selected image: file information object not found");
		} else {
			// Check if image can be converted
			if(!exportImage && !imgInfo.isConvertible()) {
				LOGGER.error("The selected image can't be converted to an OME-TIFF with this tool");
				LOGGER.error("Please select another image");
				return 1;
			}
			
			// Check if the output-directory exists
			if (!new File(settings.getOutputDirectory()).exists()) {
				try {
					if ((new File(settings.getOutputDirectory())).mkdir()) {
						LOGGER.info("Output directory sucessfully created:");
						LOGGER.info(settings.getOutputDirectory());
					} else {
						LOGGER.error("Output directory could not be created");
						return 2;
					}
				} catch (Exception e) {
					LOGGER.error("Failed to create output directory:", e);
					return 2;
				}
			}

			// Check if the output file already exists
			String outputFilename = (exportImage ? imgInfo.getExportFilename() : imgInfo.getOMETiffFilename());
			File outFile = new File(settings.getOutputDirectory() + System.getProperty("file.separator") + outputFilename);
			if (outFile.exists()) {
				LOGGER.error("Output file already exists:");
				LOGGER.error(outputFilename);
				return 2;
			}

			if (exportImage) {
				// Create a new SwingWorker thread for exporting the selected image
				ImageExporter exporterWorker = new ImageExporter(null, ndpiFile, imgInfo);
				try {
					SwingUtilities.invokeAndWait(exporterWorker);
				} catch (InterruptedException e) {
					LOGGER.error("Exporter was interrupted:", e);
					return 2;
				} catch (InvocationTargetException e) {
					LOGGER.error("An exception occured while exporting selected image:", e);
					return 2;
				}
			} else {
				// Create a new SwingWorker thread for converting the selected image to an OME-TIFF
				ImageConverter converterWorker = new ImageConverter(null, ndpiFile, imgInfo);
				try {
					SwingUtilities.invokeAndWait(converterWorker);
				} catch (InterruptedException e) {
					LOGGER.error("Converter was interrupted:", e);
					return 2;
				} catch (InvocationTargetException e) {
					LOGGER.error("An exception occured while converting selected image:", e);
					return 2;
				}
			}

		}

		return 0;
	}

	/**
	 * Displays the file content of the NDPI file in a table-like manner.
	 * Comprises general information about the NDPI file and information about
	 * each of the containing images.
	 * 
	 * <p>
	 * General information about the NDPI file:
	 * <ul>
	 * <li>Make of the slide scanner</li>
	 * <li>Model the slide scanner</li>
	 * <li>Software used to create the NDPI file</li>
	 * <li>Magnification of the image</li>
	 * <li>Date and time of the scan</li>
	 * </ul>
	 * </p>
	 * 
	 * <p>
	 * Information about each image in the NDPI file:
	 * <ul>
	 * <li>Number of the image in the NDPI file</li>
	 * <li>Image type (Image, Macro or Map)</li>
	 * <li>Image size (width and height) of the image</li>
	 * <li>Magnification of the image</li>
	 * <li>File format of the image</li>
	 * <li>File size of the image</li>
	 * </ul>
	 * </p>
	 */
	private void displayFileContent() {
		if (imgInfos == null || imgInfos.size() == 0) {
			LOGGER.warn("No images found in NDPI file");
		} else {
			System.out.println("\nFile Information for NDPI file\n'" + ndpiFile.getName() + "'");
			System.out.println("---------------------------------------------------------------------------");
			ImageInfo ndpiInfo = imgInfos.get(0);
			double fileSize = (double) ndpiFile.length() / (1024 * 1024);
			DecimalFormat df = new DecimalFormat(".##");
			System.out.print(formatString("File size: " + df.format(fileSize) + " MB", 40));
			System.out.println(formatString("Make: " + ndpiInfo.getMake(), 35));
			System.out.print(formatString("Images: " + imgInfos.size(), 40));
			System.out.println(formatString("Model: " + ndpiInfo.getModel(), 35));
			System.out.print(formatString("Software: " + ndpiInfo.getSoftware(), 40));
			System.out.println(formatString("Date & Time: " + ndpiInfo.getDateTime(), 35));
			System.out.println("---------------------------------------------------------------------------");
			System.out.println(formatString("#", 4) + formatString("Type", 10) + formatString("Image Size (w x h)", 22) + formatString("Mag.", 9) + formatString("File format", 15) + formatString("File size", 15));
			System.out.println("---------------------------------------------------------------------------");
			int counter = 0;
			for (ImageInfo imgInfo : imgInfos) {
				counter++;
				System.out.print(formatString(Integer.toString(counter), 4));
				System.out.print(formatString(imgInfo.getImageType(), 10));
				System.out.print(formatString(NumberFormat.getIntegerInstance().format(imgInfo.getWidth()) + " x " + NumberFormat.getIntegerInstance().format(imgInfo.getHeight()), 22));
				System.out.print(formatString((imgInfo.getMagnification() != null ? imgInfo.getMagnification() : ""), 9));
				System.out.print(formatString(imgInfo.getCompressionScheme(), 15));
				System.out.println(formatString(imgInfo.getFileSizeFormated(), 15));
				System.out.println("---------------------------------------------------------------------------");
			}
		}
	}

	/**
	 * Formats a given string by adding whitespace characters until the given
	 * length of the string is reached.
	 * 
	 * @param input
	 *            the input string
	 * @param length
	 *            the length of the formatted string
	 * @return the formatted string
	 */
	private String formatString(String input, int length) {
		int inputLength = input.length();
		StringBuffer buffer = new StringBuffer(length);
		buffer.append(input);
		while (inputLength++ < length) {
			buffer.append(" ");
		}
		return buffer.toString();
	}

	/**
	 * Displays the usage text for the command-line NDPI to OME-TIFF Converter
	 * and exits with return code {@code 1}.
	 */
	public static void usage() {
		String downscalingAlgorithms = Arrays.toString(ConversionSettings.getInstance().getAvailableDownscalingAlgorithms());
		downscalingAlgorithms = downscalingAlgorithms.replace("[", "'").replace("]", "'").replace(", ", "', '");

		System.out.println(String.format("\nUsage: %s [OPTIONS] [NDPI-FILE] \n"
				+ "   or: %s [OPTIONS] [NDPI-FILE] [OUTPUT-DIR]\n\n"
				+ "[OPTIONS]\n"
				+ "-f          Display information about all images of the NDPI file and exit\n\n"
				+ "-i <image>  Use <image> of the NDPI file to convert or export\n"
				+ "            If <image> is a number: select the # image of the NDPI file\n"
				+ "            If <image> is 'macro': select the macro image of the NDPI file\n"
				+ "            If <image> is 'map': select the map image of the NDPI file\n"
				+ "            If this option is not specified,\n"
				+ "            the first image of the NDPI file will be selected\n\n"
				+ "-e          Export selected image\n"
				+ "            If this option is specified,\n"
				+ "            all further options (except -i) will be ignored\n"
				+ "            If this option is not specified,\n"
				+ "            the selected image will be converted\n\n"
				+ "-s          Create singlepage OME-TIFF\n"
				+ "            An OME-TIFF (not standard-compliant) with\n"
				+ "            one RGB-image will be created\n"
				+ "            If this option is not specified,\n"
				+ "            a multipage OME-TIFF will be created\n\n"
				+ "-t          Create a tiled OME-TIFF\n"
				+ "            Image data of the OME-TIFF gets organized into tiles\n"
				+ "            If this option is not specified,\n"
				+ "            image data gets organized into strips\n\n"
				+ "-d <algo>   Downscale selected image to half size with\n"
				+ "            the specified interpolation algorithm\n"
				+ "            Valid values for <algo> are:\n"
				+ "            %s\n"
				+ "            Downscaling is only possible for striped OME-TIFFs\n"
				+ "            If this option is not specified, no downscaling will be applied\n\n"
				+ "-c <compr>  Compress OME-TIFF with the specified compression algorithm\n"
				+ "            Valid values for <compr> are:\n"
				+ "            'lzw', 'deflate', 'adobe-deflate', 'jpeg2000'\n"
				+ "            If this option is not specified,\n"
				+ "            an uncompressed OME-TIFF will be created\n\n"
				+ "-b          Use BigTIFF file format (to write OME-TIFFs larger than 4 GB)\n"
				+ "            If this option is not specified,\n"
				+ "            standard TIFF file format will be used\n\n"
				+ "-h          Display this help and exit\n\n"
				+ "-v          Display further information about this tool and exit\n\n"
				+ "[NDPI-FILE]\n"
				+ "The NDPI file to use\n"
				+ "Note: Quote the filename if it contains spaces\n\n"
				+ "[OUTPUT-DIR]\n"
				+ "The output directory in which OME-TIFFs and exported images will be stored\n"
				+ "Note: Quote the output directory if it contains spaces\n"
				+ "If the output directory is not specified,\n"
				+ "    - OME-TIFFs will be stored in the subfolder '%s'\n"
				+ "    - exported images will be stored in the subfolder '%s'\n"
				+ "of the given NDPI file.\n\n"
				+ "EXAMPLE CONVERT: %s -i 1 -d bicubic -c lzw file.ndpi\n"
				+ "Select first image of NDPI file (file.ndpi), downscale image\n"
				+ "by using bicubic interpolation, use LZW compression and store\n"
				+ "OME-TIFF in subfolder '%s'.\n\n"
				+ "EXAMPLE EXPORT: %s -i macro \"test file.ndpi\" D:\\tmp\\\n"
				+ "Select macro image of NDPI file (test file.ndpi) and export it to D:\\tmp\\",
				Constants.CONVERTER_JAR, Constants.CONVERTER_JAR,
				downscalingAlgorithms, Constants.SUBDIRECTORY_CONVERTED,
				Constants.SUBDIRECTORY_EXPORTED, Constants.CONVERTER_JAR,
				Constants.SUBDIRECTORY_CONVERTED, Constants.CONVERTER_JAR));

		System.exit(1);
	}

	/**
	 * Displays further information about the NDPI to OME-TIFF Converter and
	 * exits with return code {@code 1}.
	 */
	public static void info() {
		System.out.println("\n" + Constants.TOOL_TITLE + " " + Constants.TOOL_VERSION);
		System.out.println("This tool reads the content of a NDPI file and converts");
		System.out.println("a selected image to a standard-compliant OME-TIFF or exports a selected image.\n");
		System.out.println(Constants.TOOL_COPYRIGHT);
		System.out.println(Constants.TOOL_LICENSE + "\n");
		System.out.println(Constants.TOOL_WEBSITE + "\n\n");
		System.out.println("This tool uses the following icon-sets:\n");
		System.out.println(Constants.TOOL_USED_ICONSETS + "\n\n");
		System.out.println("This tool uses the following packages/software:\n");
		System.out.println(Constants.TOOL_USED_PACKAGES);

		System.exit(1);
	}

	/**
	 * Application entry point for NDPI to OME-TIFF Converter with command-line
	 * interface. Checks if update for installed Java is necessary, parses given
	 * arguments and passes them as a {@code CLAgumentStore} object to the NDPI
	 * to OME-TIFF Converter.
	 * 
	 * <p>
	 * Return codes are:
	 * <ul>
	 * <li>0 on success</li>
	 * <li>1 on argument failure</li>
	 * <li>2 on exception during conversion/export</li>
	 * </ul>
	 * </p>
	 * 
	 * @param args
	 *            the command-line arguments
	 */
	public static void main(String[] args) {
		// Check if installed Java Version is Version 6 Update 10 (1.6.0_10) or greater
		System.out.println("Im Konverter");

		System.out.println("KONVERTER erfolgreich  gestartet: ");
		for ( int index= 0 ; index < args.length; index++ ){
			System.out.println(args[index]);
		}

		try {
			String version = System.getProperty("java.version");
			int major = Integer.parseInt(version.split("\\.")[0]);
			int minor = Integer.parseInt(version.split("\\.")[1]);
			int update = Integer.parseInt((version.split("\\.")[2]).split("\\_")[1]);
			if (major < 1 || (major == 1 && minor < 6) || (major == 1 && minor == 6 && update < 10)) {
				System.err.println("Please update Java to Version 6 Update 10 or greater");
				System.err.println("Installed: Java Version " + minor + " Update " + update + " (" + version + ")");
				System.exit(1);
			}
		} catch (Exception e) {
			System.err.println("Could not determine Java Version");
		}

		//if (args.length == 0)
			//System.out.println("keine ARGS");


		// Create store for command-line arguments which can't be stored in conversion settings
		CLArgumentsStore argStore = new CLArgumentsStore();

		ConversionSettings settings = ConversionSettings.getInstance();

		// Set default compression algorithm to uncompressed
		settings.setCompressionAlgorithm(TiffCompression.UNCOMPRESSED.getCode());

		// Parse arguments
		Getopt g = new Getopt(Constants.CONVERTER_JAR, args, ":fi:estd:c:bhv", null);
		//Getopt g = new Getopt("TestConvertername", args, ":fi:estd:c:bhv", null);
		g.setOpterr(true);

		int option;
		String arg;

		// Loop through the options
		// Check if argument is 'macro', 'map' or an image number
		System.out.println("Fall i");
		try {
			argStore.setImagePositionNumber(2);
		} catch (NumberFormatException e) {
			System.err.println("Error - Usage of -i <image>: Given image-number is not a valid number");
			usage();
		}


		//case 's':
		// Singlepage OME-TIFF (do not split color channels)
		settings.setMultipageOMETiffSelected(false);

		//case 'c':
		// Compress OME-TIFF
		arg = g.getOptarg();
		boolean validCompression = false;
		settings.setCompressionAlgorithm(TiffCompression.LZW.getCode());
		validCompression = true;


		// Get NDPI filename
		try {
			argStore.setNDPIFilename( "C:\\Users\\Stefan\\Documents\\stapel\\test.ndpi" );
		} catch (ArrayIndexOutOfBoundsException e) {
			System.err.println("Error - No NDPI file given");
			usage();
		}


		// Get user-defined output directory
		try {
			settings.setStoreInSubdirectorySelected(false);
			settings.setOutputDirectory(args[g.getOptind() + 1]);
		} catch (ArrayIndexOutOfBoundsException e) {
			// Do nothing because this is an optional argument
		}

		// Check if any image was selected
		if (argStore.getImagePositionNumber() == -1 && !argStore.isMacroSelected() && !argStore.isMapSelected()) {
			// No image was selected
			// Therefore, set selected image to image number 1 (largest image)
			argStore.setImagePositionNumber(2);
		}

		// Check if downscaling is allowed
		if (!argStore.isFileContentDisplayOnly() && settings.isDownscalingSelected() && settings.getImgDataOrganization() == ConversionSettings.TILES) {
			System.err.println("Error - Downscaled images have to be stored as striped OME-TIFFs");
			usage();
		}

		// Start NDPI to OME-TIFF Converter
		NDPIConverterCLI converter = new NDPIConverterCLI(argStore);
		int returnCode = converter.start();

		//System.exit(returnCode);
	}

}