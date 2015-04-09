package com.simas;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Created by Simas Abramovas on 2015 Apr 09.
 */
public class Router extends Node {

	// OSPF
		// http://ciscoiseasy.blogspot.com/2011/02/lesson-39-ospf-fundamentals-part2-hello.html

	private static final File IMAGE = new File("router.png");
	public static final Dimension SIZE = new Dimension(100, 68);
	private static int sNodeCount = 0;
	private static Image sImage;


	static {
		try {
			Image image = ImageIO.read(IMAGE);
			// Scale
			sImage = image.getScaledInstance(SIZE.width, SIZE.height,
					Image.SCALE_SMOOTH);
			// Force image measuring
			sImage.getWidth(null);
			sImage.getHeight(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sets the size, location and adds itself to given parent automatically
	 *
	 * @param container node will be added to this automatically
	 * @param x         x axis location
	 * @param y         y axis location
	 */
	public Router(Container container, int x, int y) {
		super(container, x, y, sImage);
		addLabel(String.valueOf(++sNodeCount));
		setSize(SIZE);
	}

	@Override
	public Point getCenter() {
		return new Point(SIZE.width / 2, SIZE.height / 2);
	}

}
