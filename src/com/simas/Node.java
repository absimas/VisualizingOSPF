package com.simas;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import static com.simas.BaseFrame.log;

/**
 * Created by Simas Abramovas on 2015 Apr 08.
 */

public class Node extends JLabel {

	private static final String IMAGE_FILENAME = "router.png";
	public static final Dimension SIZE = new Dimension(100, 68);
	private static Image sImage;
	private static int sNodeCount = 0;
	// Label
	private static final int LABEL_SIZE = 26;
	private static final float LABEL_BG_OPACITY = 0.2f;
	private static final Color LABEL_COLOR = Color.WHITE;

	static {
		try {
			Image image = ImageIO.read(new File(IMAGE_FILENAME));
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

	public Node() {
		super(new ImageIcon(sImage));
		setSize(SIZE);
		addLabel();
	}

	/**
	 * Sets the size, location and adds itself to given parent automatically
	 * @param container    node will be added to this automatically
	 * @param x            x axis location
	 * @param y            y axis location
	 */
	public Node(Container container, int x, int y) {
		super(new ImageIcon(sImage));
		setSize(SIZE);
		setLocation(x, y);
		container.add(this);
		addLabel();
	}

	private void addLabel() {
		// Create a text label
		JLabel text = new JLabel(String.valueOf(++sNodeCount));
		text.setOpaque(true);
		text.setFont(new Font("Helvetica", Font.PLAIN, LABEL_SIZE));

		// Measure
		FontMetrics metrics = text.getFontMetrics(text.getFont());
		Dimension size = new Dimension(metrics.stringWidth(text.getText()), metrics.getHeight());
		text.setSize(size);

		// Center
		Point labelCenter = getCenter();
		labelCenter.translate(-size.width / 2, -size.height / 2);
		text.setLocation(labelCenter);
		text.setForeground(LABEL_COLOR);
		text.setBackground(new Color(0, 0, 0, LABEL_BG_OPACITY));
		add(text);
		setVisible(true);
	}

	public Point getCenter() {
		return new Point(getSize().width / 2, getSize().height / 2);
	}

	public Point getCenterLocation() {
		Point location = getLocation();
		// Move by parent's spacing
		Point parent = getParent().getLocation();
		location.translate(parent.x,  parent.y + 24);
		// Move to center of node
		location.translate(getCenter().x, getCenter().y);
		return location;
	}

}