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
 * Wrapper around the graphical user interface and command-line interface of the
 * NDPI to OME-TIFF Converter.
 * 
 * @author Matthias Baldauf
 * 
 */
public class NDPIConverter {

	private String[] args;

	public NDPIConverter(String[] args)
	{
		this.args = args;
	}

	/**
	 * Starts the command-line interface or graphical user interface depending
	 * if arguments are passed or not. If arguments are passed, the command-line
	 * interface gets started. If no arguments are passed, the graphical user
	 * interface gets started.
	 * 
	 * @param args
	 *            the command-line arguments
	 */
	//public static void main(String[] args) {
	public static void main(String[] args) {
		if (args.length >= 1) {
			System.out.println("KONVERTER GESTARTET");
			NDPIConverterCLI.main(args);
			return;
		}
		else {
			NDPIConverterGUI.main(args);
			return;
		}
	}

}