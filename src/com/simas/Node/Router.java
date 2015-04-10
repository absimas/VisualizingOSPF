package com.simas.Node;

import com.simas.MainFrame;
import com.simas.Packet.HelloPacket;
import com.simas.Packet.Packet;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Created by Simas Abramovas on 2015 Apr 09.
 */
public class Router extends Node {

	// OSPF
		// http://ciscoiseasy.blogspot.com/2011/02/lesson-39-ospf-fundamentals-part2-hello.html

	// ToDo check if text fits, otherwise skew

	private static final File IMAGE = new File("router.png");
	public static final Dimension SIZE = new Dimension(100, 68);
	private static int sNodeCount = 0;
	private static Image sImage;
	public static Switch sSwitch;

	// Settings
	final String networkMask;
	final int routerId;
	final List<Integer> neighbor = new ArrayList<>();
	int helloInterval = MainFrame.DEFAULT_HELLO_INTERVAL;
	int deadInterval = MainFrame.DEFAULT_DEAD_INTERVAL;


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
	 * Convenience method that accepts a {@code Point} instead of separate coordinates
	 * @param container node will be added to this automatically
	 * @param position  x and y coordinates
	 */
	public Router(Container container, Point position) {
		this(container, position.x, position.y);
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
		routerId = ++sNodeCount;
		networkMask = String.format("%s.%s.%s.%s", sNodeCount, sNodeCount, sNodeCount, sNodeCount);
		addLabel(networkMask);
		setSize(SIZE);
	}

	@Override
	public Point getCenter() {
		return new Point(SIZE.width / 2, SIZE.height / 2);
	}

	public void receivePacket(Packet packet) {
		// ToDo
		// Check for new neighbours
		// Check for current neighbours death
	}

	/**
	 * Sends a packet via the {@code sSwitch} to every other router.
	 */
	public void sendPacket() {
		// ToDo should only send Hello packets to 224.0.0.5
		// Send hello packet
		HelloPacket packet = new HelloPacket(routerId, networkMask);
		packet.neighbor = neighbor;
		sSwitch.resendPacket(packet);
	}

}
