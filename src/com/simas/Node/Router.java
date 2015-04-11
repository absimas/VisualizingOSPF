package com.simas.Node;

import com.simas.MainFrame;
import com.simas.Packet.HelloPacket;
import com.simas.Packet.Packet;
import com.simas.ProgressDot;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.ToolTipManager;

import static com.simas.BaseFrame.log;

/**
 * Created by Simas Abramovas on 2015 Apr 09.
 */
public class Router extends Node implements MouseListener {

	private static final File IMAGE = new File("router.png");
	// Delay until each node gets it's first hello packet sent (node id * difference)
	private static final int NODE_INITIAL_DELAY_DIFFERENCE = 1100;
	private static final int NODE_DELAY = 2500;
	public static final Dimension SIZE = new Dimension(100, 68);
	private static int sNodeCount = 0;
	private static Image sImage;
	public static Switch sSwitch;
	public static Router sDR;
	public static Router sBDR;


	// Settings
	final int routerId;
	final Timer timer;
	final Map<Integer, Timer> neighbor = new HashMap<>();
	int helloInterval = MainFrame.DEFAULT_HELLO_INTERVAL;
	int deadInterval = MainFrame.DEFAULT_DEAD_INTERVAL;
	public boolean dead;


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
		// Set DR and BDR (if not yet set)
		if (sDR == null) {
			sDR = this;
		} else if (sBDR == null) {
			sBDR = this;
		}

		// Settings
		routerId = ++sNodeCount;

		// Appearance
		addLabel(getFullId(routerId), SIZE.width);
		setSize(SIZE);

		// Packet sending timer
		timer = new Timer(NODE_DELAY, e -> sendPacket());
		timer.setInitialDelay(NODE_INITIAL_DELAY_DIFFERENCE * routerId);
		timer.setRepeats(true);
		timer.start();

		// Click listener (killer)
		addMouseListener(this);

		// Set tooltip to display basic information
		setToolTipText(createToolTipText());
	}

	@Override
	public Point getCenter() {
		return new Point(SIZE.width / 2, SIZE.height / 2);
	}

	public void receivePacket(Packet packet) {
		if (packet instanceof HelloPacket) {
			HelloPacket hello = (HelloPacket) packet;
			// Add the hello originator as a neighbor with a death timer
			Timer deathTimer = new Timer(hello.deadInterval, e -> {
				// Remove neighbor
				neighbor.remove(hello.routerId);

				// It will seize being a neighbour when a hello packet from it isn't received for deadInterval
				log(String.format("Death for %s's neighbour %s", getFullId(routerId),
						getFullId(hello.routerId)));

				// Update tooltip
				updateToolTip();

				// Remove 2 way state from list (if it exists)
				sSwitch.twoWayConnections.remove(getStateString(routerId, hello.routerId));
			});
			deathTimer.setRepeats(false);
			Timer previous = neighbor.put(hello.routerId, deathTimer);
			// Stop previous death timer if it was already set
			if (previous != null) previous.stop();
			// Start the new one
			deathTimer.start();


			if (hello.neighbor.contains(routerId)) {
				// The packet received from hello.routerId contains this router as a neighbor.
					// This means, that a a 2-way state has been established
						// (2 routers saw each others hello packets)

				if (sSwitch.twoWayConnections.add(getStateString(routerId, hello.routerId))) {
					log(String.format("2-way state between %s and %s", getFullId(routerId),
							getFullId(hello.routerId)));
				}
			}
		}
		updateToolTip();
	}

	private void updateToolTip() {
		// Update tooltip only if the text changed
		if (!getToolTipText().equals(createToolTipText())) {
			setToolTipText(createToolTipText());
			// Update shown tooltip
			if (sCurrentEnterEvent != null) {
				MouseEvent e = sCurrentEnterEvent;
				ToolTipManager.sharedInstance().mouseMoved(
						new MouseEvent(e.getComponent(), -1, System.currentTimeMillis(), 0,
								e.getX(), e.getY(), e.getXOnScreen(), e.getYOnScreen(), 0, false, 0));
			}
		}
	}

	private static String getFullId(int routerId) {
		return String.format("%s.%s.%s.%s", routerId, routerId, routerId, routerId);
	}

	private static String getStateString(int routerId1, int routerId2) {
		String lowerMask, higherMask;
		if (routerId1 < routerId2) {
			lowerMask = getFullId(routerId1);
			higherMask = getFullId(routerId2);
		} else {
			lowerMask = getFullId(routerId2);
			higherMask = getFullId(routerId1);
		}

		return String.format("%s-%s", lowerMask, higherMask);
	}

	/**
	 * Sends a packet via the {@code sSwitch} to every other router.
	 */
	public void sendPacket() {
		// Send hello packet to switch
		ProgressDot dot = new ProgressDot(getCenterLocation(), sSwitch.getCenterLocation(),
				MainFrame.helloInterval, () -> {
					// On completion switch resends the packet to every other router
					HelloPacket packet = new HelloPacket(routerId);
					packet.helloInterval = MainFrame.helloInterval;
					packet.neighbor = neighbor.keySet();
					sSwitch.resendPacket(packet);
				}
		);
		dot.start();
		getParent().add(dot);
		// Bring dot to top, after it was created
		getParent().setComponentZOrder(dot, 0);
	}

	public void setDead(boolean dead) {
		this.dead = dead;
		setEnabled(!dead);
		if (dead) {
			timer.stop();
			// ToDo remove neighbors or should they die manually after they timeout?
		} else {
			// Remove the initial delay (used when first creating all the routers)
			timer.setInitialDelay(0);
			timer.restart();
		}
		// Update tooltip
		updateToolTip();

		// Redraw connecting lines
		getParent().repaint();
	}

	public boolean isDead() {
		return dead;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		setDead(!isDead());
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	private static MouseEvent sCurrentEnterEvent;

	@Override
	public void mouseEntered(MouseEvent e) {
		sCurrentEnterEvent = e;
		updateToolTip();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		sCurrentEnterEvent = null;
	}

	private String createToolTipText() {
		String neighbors = "-<br>";
		if (neighbor.size() > 0) {
			neighbors = "<br>";
			for (Integer id : neighbor.keySet()) {
				if (id != null) neighbors += "&nbsp;" + getFullId(id) + "<br>";
			}
		}
		return String.format("<html>Id: %s<br> Neighbours: %s Dead: %s",
				getFullId(routerId), neighbors, isDead());
	}

}
