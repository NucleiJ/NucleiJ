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

package at.ac.htlhl.nucleij.util.ndpiconverter.utilities;

import java.awt.Cursor;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Creates a custom toolbar button with a given icon and rollover icon.
 * 
 * @author Matthias Baldauf
 * 
 */
public class CustomToolBarButton extends JButton {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new custom tool bar button with a given icon and rollover icon.
	 * 
	 * @param icon
	 *            the icon image to display on the button
	 * @param rolloverIcon
	 *            the rollover icon used as the "rollover" image
	 */
	public CustomToolBarButton(Icon icon, Icon rolloverIcon) {
		super(icon);
		setRolloverEnabled(true);
		setRolloverIcon(rolloverIcon);

		setContentAreaFilled(false);
		// Set border to avoid automatically added space around the buttons
		setBorder(BorderFactory.createEmptyBorder());
		setBorderPainted(false);

		setMargin(new Insets(0, 0, 0, 0));
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

}