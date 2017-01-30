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

/**
 * Stores command-line arguments if passed to the NDPI to OME-TIFF Converter.
 * Used to store all arguments which can't be stored in the conversion settings
 * instance. The arguments are needed for the command-line usage of the
 * converter.
 * 
 * @author Matthias Baldauf
 * 
 */
public class CLArgumentsStore {

	private boolean displayFileContentOnly;

	// Arguments only for export
	private boolean exportOnly;

	// Arguments for export and conversion
	private String ndpiFilename;
	private int imgPosNumber;
	private boolean macroSelected;
	private boolean mapSelected;

	/**
	 * Creates a new instance to store the command-line arguments of the NDPI to
	 * OME-TIFF Converter. The available options are initialized with the
	 * default values.
	 */
	public CLArgumentsStore() {
		displayFileContentOnly = false;
		imgPosNumber = -1;
		macroSelected = false;
		mapSelected = false;
	}

	/**
	 * Returns {@code true} if the NDPI file content should be displayed without
	 * any further action, and {@code false} if selected image should be
	 * converted or exported.
	 * 
	 * @return {@code true} if the NPDI file content should be displayed without
	 *         any further action, {@code false} if selected image should be
	 *         converted or exported
	 */
	public boolean isFileContentDisplayOnly() {
		return displayFileContentOnly;
	}

	/**
	 * Sets whether the NPDI file content should be displayed without any
	 * further action.
	 * 
	 * @param displayFileContentOnly
	 *            whether the NPDI file content should be displayed without any
	 *            further action
	 */
	public void setFileContentDisplayOnly(boolean displayFileContentOnly) {
		this.displayFileContentOnly = displayFileContentOnly;
	}

	/**
	 * Returns {@code true} if the macro image of the NDPI file is selected, and
	 * {@code false} otherwise.
	 * 
	 * @return {@code true} if the macro image of the NDPI file is selected,
	 *         {@code false} otherwise
	 */
	public boolean isMacroSelected() {
		return macroSelected;
	}

	/**
	 * Sets whether the macro image of the NDPI file is selected.
	 * 
	 * @param macroSelected
	 *            whether the macro image of the NDPI file is selected
	 */
	public void setMacroSelected(boolean macroSelected) {
		this.macroSelected = macroSelected;
	}

	/**
	 * Returns {@code true} if the map image of the NDPI file is selected, and
	 * {@code false} otherwise.
	 * 
	 * @return {@code true} if the map image of the NDPI file is selected,
	 *         {@code false} otherwise
	 */
	public boolean isMapSelected() {
		return mapSelected;
	}

	/**
	 * Sets whether the map image of the NDPI file is selected.
	 * 
	 * @param mapSelected
	 *            whether the map image of the NDPI file is selected
	 */
	public void setMapSelected(boolean mapSelected) {
		this.mapSelected = mapSelected;
	}

	/**
	 * Returns the position number of the selected image in the NDPI file.
	 * 
	 * @return the position number of the selected image in the NDPI file
	 */
	public int getImagePositionNumber() {
		return imgPosNumber;
	}

	/**
	 * Sets the position number of the selected image in the NDPI file.
	 * 
	 * @param imgPosNumber
	 *            the position number of the selected image in the NDPI file
	 */
	public void setImagePositionNumber(int imgPosNumber) {
		this.imgPosNumber = imgPosNumber;
	}

	/**
	 * Returns {@code true} if selected image should be exported, and
	 * {@code false} if the selected image should be converted.
	 * 
	 * @return {@code true} if selected image should be exported, {@code false}
	 *         if the selected image should be converted
	 */
	public boolean isExportOnly() {
		return exportOnly;
	}

	/**
	 * Sets whether selected image should be exported, or whether selected image
	 * should be converted.
	 * 
	 * @param exportOnly
	 *            whether selected image should be exported ({@code true}), or
	 *            whether selected image should be converted ({@code false})
	 */
	public void setExportOnly(boolean exportOnly) {
		this.exportOnly = exportOnly;
	}

	/**
	 * Returns the filename of the selected NDPI file.
	 * 
	 * @return the filename of the selected NDPI file
	 */
	public String getNDPIFilename() {
		return ndpiFilename;
	}

	/**
	 * Sets the filename of the selected NDPI file.
	 * 
	 * @param ndpiFilename
	 *            the filename of the selected NDPI file
	 */
	public void setNDPIFilename(String ndpiFilename) {
		this.ndpiFilename = ndpiFilename;
	}

}