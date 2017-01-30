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

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Creates a custom panel with a given background image. This is achieved by
 * overriding the {@code paintComponent} method. The background image can be
 * changed after creation of the panel.
 * 
 * @author Matthias Baldauf
 * 
 */
public class CustomBackgroundPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private BufferedImage img;
	private boolean stretch;

	/**
	 * Creates a new panel with the given background image.
	 * 
	 * @param filename
	 *            the filename of the background image
	 * @param stretch
	 *            whether background image should be stretched to fill the whole
	 *            panel
	 */
	public CustomBackgroundPanel(String filename, boolean stretch) {
		this.stretch = stretch;
		try {
			img = ImageIO.read(getClass().getResource(filename));
		} catch (IOException e) {
		}
	}

	/**
	 * Paints the background image to the panel. The background image gets
	 * either stretched to fill the panel or gets centered in the panel.
	 * 
	 * @param g
	 *            the {@code Graphics} object
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (stretch)
			// Stretch image
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		else {
			// Center image in the panel
			int posx = 0;
			int posy = 0;
			if ((getWidth() - img.getWidth()) > 0)
				posx = (getWidth() - img.getWidth()) / 2;
			if ((getHeight() - img.getHeight()) > 0)
				posy = (getHeight() - img.getHeight()) / 2;
			g.drawImage(img, posx, posy, img.getWidth(), img.getHeight(), this);
		}
	}

	/**
	 * Sets the background image for the panel and repaints it.
	 * 
	 * @param img
	 *            the background image
	 */
	public void setImage(BufferedImage img) {
		this.img = img;
		repaint();
	}

	/**
	 * Sets the background image for the panel and repaints it.
	 * 
	 * @param filename
	 *            the filename of the background image
	 */
	public void setImage(String filename) {
		try {
			img = ImageIO.read(getClass().getResource(filename));
			repaint();
		} catch (IOException e) {
		}
	}

}