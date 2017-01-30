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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.border.AbstractBorder;

/**
 * Creates a custom rounded border with a given corner radius. This is achieved
 * by overriding the {@code paintBorder} method.
 * 
 * @author Matthias Baldauf
 * 
 */
public class CustomRoundedBorder extends AbstractBorder {

	private static final long serialVersionUID = 1L;
	private int cornerRadius;
	private Color bgColor;

	/**
	 * Creates a new custom rounded border with a given corner radius and given
	 * background color in the corners.
	 * 
	 * @param cornerRadius
	 *            the corner radius of the border
	 * @param bgColor
	 *            the background color of the corners
	 */
	public CustomRoundedBorder(int cornerRadius, Color bgColor) {
		this.cornerRadius = cornerRadius;
		this.bgColor = bgColor;
	}

	/**
	 * Paints the rounded border.
	 * 
	 * @param c
	 *            the component for which this border is being painted
	 * @param g
	 *            the paint graphics
	 * @param x
	 *            the x position of the painted border
	 * @param y
	 *            the y position of the painted border
	 * @param width
	 *            the width of the painted border
	 * @param height
	 *            the height of the painted border
	 */
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Paint background of the rounded corners
		for (int r = 0; r <= cornerRadius; r++) {
			g2.setColor(bgColor);
			g2.drawRoundRect(x, y, width, height, r, r);
		}

		// Paint rounded corners
		Color color = deriveColorBrightness(c.getBackground(), 0.3f);
		g2.setColor(deriveColorAlpha(color, 40));
		g2.drawRoundRect(x, y + 2, width - 1, height - 3, cornerRadius, cornerRadius);
		g2.setColor(deriveColorAlpha(color, 90));
		g2.drawRoundRect(x, y + 1, width - 1, height - 2, cornerRadius, cornerRadius);
		g2.setColor(deriveColorAlpha(color, 255));
		g2.drawRoundRect(x, y, width - 1, height - 1, cornerRadius, cornerRadius);

		g2.dispose();
	}

	/**
	 * Derives a color by changing the alpha component of the given color.
	 * 
	 * @param color
	 *            the color whose alpha component gets changed
	 * @param alpha
	 *            the alpha component
	 * @return the color with modified alpha component
	 */
	private Color deriveColorAlpha(Color base, int alpha) {
		return new Color(base.getRed(), base.getGreen(), base.getBlue(), alpha);
	}

	/**
	 * Derives a color by changing the brightness of the given color.
	 * 
	 * @param color
	 *            the color whose brightness gets changed
	 * @param amount
	 *            the amount to change the brightness the color (between
	 *            {@code -1} and {@code 1}). {@code -1} changes the color to
	 *            white, {@code 0} leaves the color unchanged and {@code 1}
	 *            changes the color to black.
	 * @return the color with modified brightness
	 */
	private Color deriveColorBrightness(Color color, float amount) {
		int red = (int) ((color.getRed() * (1 - amount) / 255) * 255);
		int green = (int) ((color.getGreen() * (1 - amount) / 255) * 255);
		int blue = (int) ((color.getBlue() * (1 - amount) / 255) * 255);
		return new Color(red > 255 ? 255 : red, green > 255 ? 255 : green, blue > 255 ? 255 : blue);
	}

}