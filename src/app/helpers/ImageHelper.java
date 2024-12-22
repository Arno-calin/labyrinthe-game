package app.helpers;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Set of static functions allowing to manipulate images
 *
 * @author Adrien Krähenbühl
 */
public class ImageHelper {
	/**
	 *  Generate a new image from a background image and a foreground image
	 *
	 * @param bg is the path of the background image
	 * @param fg is the list of path of the other images
	 * @return an image combining the foreground image over the background image
	 */
	public static BufferedImage merge(BufferedImage bg, BufferedImage fg ) throws IOException {
		// Check for null images
		if (bg == null || fg == null) {
			throw new IllegalArgumentException("Background or foreground image cannot be null");
		}

		// Create new image with background dimensions
		BufferedImage mergedImage = new BufferedImage(bg.getWidth(), bg.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = mergedImage.createGraphics();

		// Enable antialiasing for better quality
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw background
		g2d.drawImage(bg, 0, 0, null);

		// Scale foreground to match background size
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
		g2d.drawImage(fg, 0, 0, bg.getWidth(), bg.getHeight(), null);

		g2d.dispose();
		return mergedImage;
	}

	/**
	 *  Rotate an original image from the center by a defined angle.
	 *  The original image MUST HAVE the same width and height.
	 *
	 * @param original is the input image to rotate
	 * @param angle is the angle of rotation in radian
	 * @return a new image representing the original image rotated of angle radian from its center.
	 */
	public static BufferedImage rotate( final BufferedImage original, double angle ) throws IllegalArgumentException {
		if ( original.getWidth() != original.getHeight() )
			throw new IllegalArgumentException("Original image must have same width and height.");
		BufferedImage rotated = new BufferedImage(original.getWidth(), original.getHeight(), original.getType());
		Graphics2D graphic = rotated.createGraphics();
		graphic.rotate(angle, original.getWidth()/2., original.getHeight()/2.);
		graphic.drawRenderedImage(original, null);
		graphic.dispose();
		return rotated;
	}

	/**
	 *  Rotate an original image from the center by 90 degrees in clockwise
	 *
	 * @param original is the input image to rotate
	 * @return a new image representing the original image rotated by 90 degrees from its center.
	 */
	public static BufferedImage rotateClockwise( final BufferedImage original ) throws IllegalArgumentException {
		return rotate( original, 0.5*Math.PI );
	}

	/**
	 *  Rotate an original image from the center by 90 degrees in counterclockwise
	 *
	 * @param original is the input image to rotate
	 * @return a new image representing the original image rotated by 90 degrees from its center.
	 */
	public static BufferedImage rotateCounterClockwise( final BufferedImage original ) throws IllegalArgumentException {
		return rotate( original, 1.5*Math.PI );
	}
}
