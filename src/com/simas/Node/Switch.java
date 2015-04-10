package com.simas.Node;

import com.simas.Packet.HelloPacket;
import com.simas.Packet.Packet;
import com.simas.ProgressDot;

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

public class Switch extends Node {

	private static final File IMAGE = new File("switch.png");
	public static final Dimension SIZE = new Dimension(100, 78);
	protected static Image sImage;
	public List<Router> routers = new ArrayList<>();

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

	/**
	 * Resend the given packet to every router contained in {@code routers}
	 * @param packet    packet to be resent
	 */
	public void resendPacket(Packet packet) {
		// Resend hello packets only
		if (packet instanceof HelloPacket) {
			HelloPacket helloPacket = (HelloPacket) packet;

			for (Router router : routers) {
				// Don't resend to itself
				if (router.routerId == helloPacket.routerId) continue;
				// Animate a ProgressDot
				Point switchLocation = getCenterLocation();
				ProgressDot dot = new ProgressDot(switchLocation, router.getCenterLocation(), () -> {
					// Notify router about the received packet only when the ProgressDot finishes
					router.receivePacket(packet);
				});
				getParent().add(dot);
			}
		}
	}

}
