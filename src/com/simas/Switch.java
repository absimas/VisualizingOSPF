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

public class Switch extends Node {

	private static final File IMAGE = new File("switch.png");
	public static final Dimension SIZE = new Dimension(100, 78);
	protected static Image sImage;

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
	public Switch(Container container, int x, int y) {
		super(container, x, y, sImage);
		addLabel("0");
		setSize(SIZE);
	}

	@Override
	public Point getCenter() {
		return new Point(SIZE.width / 2, SIZE.height / 2);
	}

}
