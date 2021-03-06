package com.simas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * Created by Simon on 2015 Apr 10.
 */
public class ProgressDot extends JLabel implements ActionListener {

	private static final File IMAGE = new File("circle.png");
	private static final Dimension SIZE = new Dimension(15, 15);
	private static final int ITERATION_DELAY = 50; // ms
	public static final int DEFAULT_DURATION = 1000;

	private static BufferedImage sRedImage;
	private static BufferedImage sGreenImage;
	private final Timer mTimer;
	private final Point mFrom;
	private final Point mTo;
	private final int mStepLength;
	private final CompletionListener mCompletionListener;

	private int mIteration = 1;

	public enum DotColor {
		RED, GREEN
	}

	static {
		try {
			Image image = ImageIO.read(IMAGE);
			// Scale
			image = image.getScaledInstance(SIZE.width, SIZE.height, Image.SCALE_SMOOTH);
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			sRedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics g = sRedImage.getGraphics();
			g.drawImage(image, 0, 0, null);
			g.dispose();

			sGreenImage = colorImage(Color.GREEN, sRedImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Convenience method which uses the default step length
	 * @param from    origin point
	 * @param to      destination point
	 */
	public ProgressDot(Point from, Point to, CompletionListener listener) {
		this(from, to, DEFAULT_DURATION, listener);
	}

	/**
	 *
	 * @param from        origin point
	 * @param to          destination point
	 * @param duration    the duration
	 */
	public ProgressDot(Point from, Point to, int duration, CompletionListener listener) {
		super(new ImageIcon(sRedImage));
		mCompletionListener = listener;
		setSize(SIZE);
		// Positions
		mFrom = from;
		mFrom.translate(-SIZE.width / 2, -SIZE.height / 2);
		mTo = to;
		mTo.translate(-SIZE.width / 2, -SIZE.height / 2);
		setLocation(mFrom);

		// Calculate step length
		double distance = Math.sqrt(Math.pow(mTo.x - mFrom.x, 2) + Math.pow(mTo.y - mFrom.y, 2));
		mStepLength = (int) (distance / Math.max(1, duration / ITERATION_DELAY));

		mTimer = new Timer(ITERATION_DELAY, this);
	}

	public void setColor(DotColor color) {
		switch (color) {
			case GREEN:
				setIcon(new ImageIcon(sGreenImage));
				break;
			default:
				// Default is RED
				setIcon(new ImageIcon(sRedImage));
				break;
		}

	}

	public void start() {
		mTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Vector AB
		Point vab = new Point(mTo.x - mFrom.x, mTo.y - mFrom.y);
		double len = Math.sqrt(Math.pow(vab.x, 2) + Math.pow(vab.y, 2));

		// Unit vector
		double unitX = vab.x / len;
		double unitY = vab.y / len;

		// Request length vector
		double reqX = unitX * mStepLength * mIteration;
		double reqY = unitY * mStepLength * mIteration;


		getParent().repaint();
		Point target = new Point(mFrom.x + (int) reqX, mFrom.y + (int) reqY);
		if (isBetween(mFrom, mTo, target)) {
			setLocation(target);
			++mIteration;
		} else {
			mTimer.stop();
			setLocation(mTo);
			getParent().remove(this);

			if (mCompletionListener != null) {
				mCompletionListener.onCompleted();
			}
		}
	}

	private static boolean isBetween(Point a, Point b, Point c) {
		return almostEqual(distance(a, c) + distance(c, b), distance(a, b), 0.1);
	}

	private static double distance(Point a, Point b) {
		return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
	}

	private static boolean almostEqual(double a, double b, double eps){
		return Math.abs(a - b) < eps && a != 0 && b != 0;
	}

	/**
	 * Creates a copy of the given image with visible pixels replaced by given color
	 * @param color       color that will be written over each visible pixel
	 * @param original    image that will be copied and redrawn
	 * @return colored copy of the {@code original} image
	 */
	private static BufferedImage colorImage(Color color, BufferedImage original) {
		BufferedImage image = new BufferedImage(original.getColorModel(), original.copyData(null),
				original.isAlphaPremultiplied(), null);
		int width = image.getWidth();
		int height = image.getHeight();
		WritableRaster raster = image.getRaster();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int[] pixels = raster.getPixel(x, y, (int[]) null);
				pixels[0] = color.getRed();
				pixels[1] = color.getGreen();
				pixels[2] = color.getBlue();
				raster.setPixel(x, y, pixels);
			}
		}
		return image;
	}

	public interface CompletionListener {
		void onCompleted();
	}

}
