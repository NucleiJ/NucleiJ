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

import java.awt.Color;

/**
 * Defines constants used by various classes of the NDPI to OME-TIFF Converter.
 * All members of this class are immutable and need to be accessed in a static
 * way, for example {@code Constants.TOOL_TITLE}.
 * 
 * @author Matthias Baldauf
 * 
 */
public final class Constants {

	public static final String TOOL_TITLE = "NDPI to OME-TIFF Converter";
	public static final String TOOL_PURPOSE = "Tool for converting a selected image of a NDPI file to an OME-TIFF";
	public static final String TOOL_VERSION = "v1.5 (Nov 15, 2012)";
	public static final String TOOL_COPYRIGHT = "Copyright (c) 2012 Matthias Baldauf & Andreas Dander";
	public static final String TOOL_WEBSITE = "http://www.matthias-baldauf.at/software/ndpi-converter";
	public static final String TOOL_LICENSE = "Released under the GNU General Public License";
	public static final String TOOL_LICENSE_TEXT = "This program is free software: you can redistribute it and/or modify\n"
			+ "it under the terms of the GNU General Public License as published by\n"
			+ "the Free Software Foundation, either version 3 of the License, or\n"
			+ "(at your option) any later version.\n\n"
			+ "This program is distributed in the hope that it will be useful,\n"
			+ "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
			+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
			+ "GNU General Public License for more details.\n\n"
			+ "You should have received a copy of the GNU General Public License\n"
			+ "along with this program. If not, see <http://www.gnu.org/licenses/>.";
	public static final String TOOL_USED_ICONSETS = "WPZOOM Developer Icon Set by WPZOOM\n"
			+ "http://www.wpzoom.com/wpzoom/new-freebie-wpzoom-developer-icon-set-154-free-icons\n\n"
			+ "Iconza by Turbomilk\n" + "http://www.turbomilk.com";
	public static final String TOOL_USED_PACKAGES = "LOCI Tools complete bundle 4.4.4 (stable release - Sept 25, 2012)\n"
			+ "http://loci.wisc.edu/software/bio-formats\n\n"
			+ "libjpeg-turbo Java wrapper 1.2.0\n"
			+ "http://www.libjpeg-turbo.org\n\n"
			+ "java-image-scaling 0.8.5\n"
			+ "http://code.google.com/p/java-image-scaling\n\n"
			+ "GNU getopt - Java port (release 1.0.14)\n"
			+ "http://www.urbanophile.com/arenn/hacking/download.html";

	public static final String RESOURCES_DIR = "/at/matthiasbaldauf/ndpiconverter/resources/";
	public static final String WINDOW_IMAGE = RESOURCES_DIR + "icon_window.png";
	public static final String LOGO_IMAGE = RESOURCES_DIR + "logo.png";
	public static final String FOOTER_IMAGE = RESOURCES_DIR + "footer.png";
	public static final String WELCOME_BACKGROUND_IMAGE = RESOURCES_DIR + "welcome_background.png";
	public static final String TITLE_BACKGROUND_IMAGE = RESOURCES_DIR + "title_background.png";
	public static final String FILE_ICON = RESOURCES_DIR + "icon_file.png";
	public static final String THUMBNAIL_IMAGE = RESOURCES_DIR + "thumbnail.png";
	public static final String EXPORT_ICON = RESOURCES_DIR + "icon_export.png";
	public static final String CONVERT_ICON = RESOURCES_DIR + "icon_save.png";
	public static final String DIRECTORY_ICON = RESOURCES_DIR + "icon_directory.png";
	public static final String PROCESSING_IMAGE = RESOURCES_DIR + "processing.gif";
	public static final String CANCEL_IMAGE = RESOURCES_DIR + "icon_cancel.png";
	public static final String HELP_FILE = RESOURCES_DIR + "help.html";

	public static final String TOOLBAR_IMAGE_CONVERT = RESOURCES_DIR + "toolbar_button_convert.png";
	public static final String TOOLBAR_IMAGE_CONVERT_HOVER = RESOURCES_DIR + "toolbar_button_convert_hover.png";
	public static final String TOOLBAR_IMAGE_SETTINGS = RESOURCES_DIR + "toolbar_button_settings.png";
	public static final String TOOLBAR_IMAGE_SETTINGS_HOVER = RESOURCES_DIR + "toolbar_button_settings_hover.png";
	public static final String TOOLBAR_IMAGE_HELP = RESOURCES_DIR + "toolbar_button_help.png";
	public static final String TOOLBAR_IMAGE_HELP_HOVER = RESOURCES_DIR + "toolbar_button_help_hover.png";

	public static final Color DARK_GREY_COLOR = new Color(201, 201, 201);
	public static final Color MEDIUM_GREY_COLOR = new Color(235, 235, 235);
	public static final Color LIGHT_GREY_COLOR = new Color(249, 249, 249);
	public static final Color DARK_GREEN_COLOR = new Color(140, 206, 55);
	public static final Color DARK_TURQUOISE_COLOR = new Color(150, 204, 212);
	public static final Color LIGHT_TURQUOISE_COLOR = new Color(170, 233, 243);
	public static final Color MOUSE_OVER_COLOR = new Color(223, 241, 244);

	public static final String SUBDIRECTORY_EXPORTED = "exported";
	public static final String SUBDIRECTORY_CONVERTED = "converted";

	public static final String CONVERTER_JAR = "ndpi-converter.jar";

	/**
	 * Private constructor to prevent instantiation of this class.
	 */
	private Constants() {
		throw new AssertionError();
	}

}