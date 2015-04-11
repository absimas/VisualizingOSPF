package com.simas.Node;

import com.simas.MainFrame;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;

/**
 * Created by Simas Abramovas on 2015 Apr 09.
 */

public class Switch extends Node {

	private static final File IMAGE = new File("switch.png");
	public static final Dimension SIZE = new Dimension(100, 78);
	protected static Image sImage;
	public List<Router> routers = new ArrayList<>();
	public Set<String> twoWayConnections = new HashSet<String>() {
		@Override
		public boolean add(String o) {
			boolean added = super.add(o);
			if (added) updateList();
			return added;
		}

		@Override
		public boolean remove(Object o) {
			boolean removed = super.remove(o);
			if (removed) updateList();
			return removed;
		}

		private void updateList() {
			String[] items = twoWayConnections.toArray(new String[twoWayConnections.size()]);
			// Sort by first number of the networkMask i.e.:
				// 1.1.1.1-2.2.2.2 < 1.1.1.1-10.10.10.10
			Arrays.sort(items, (o1, o2) -> {
				Integer int1 = Integer.parseInt(o1.substring(0, o1.indexOf('.')));
				Integer int2 = Integer.parseInt(o2.substring(0, o2.indexOf('.')));
				if(int1.equals(int2)) {
					// Compare 2nd mask
					int secondIndex = o1.indexOf('-') + 1;
					int1 = Integer.parseInt(o1.substring(secondIndex,
							o1.indexOf('.', secondIndex)));
					secondIndex = o2.indexOf('-') + 1;
					int2 = Integer.parseInt(o2.substring(secondIndex,
							o2.indexOf('.', secondIndex)));
					return int1 - int2;
				}
				return int1 - int2;
			});
			MainFrame.sTwoWayList.setListData(items);
		}
	};

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
		addLabel("0", SIZE.width);
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
				// Don't resend to self or dead routers
				if (router.routerId == helloPacket.routerId || router.isDead()) {
					continue;
				}
				Point switchLocation = getCenterLocation();
				ProgressDot dot = new ProgressDot(switchLocation, router.getCenterLocation(), () -> {
					// On completion, notify router about the received packet
					router.receivePacket(packet);
				});
				dot.setColor(ProgressDot.DotColor.GREEN);
				dot.start();
				getParent().add(dot);
				// Bring dot to top, after it was created
				getParent().setComponentZOrder(dot, 0);
			}
		}
	}

}
