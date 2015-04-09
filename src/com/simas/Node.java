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

public abstract class Node extends JLabel {

	// Label
	private static final int LABEL_SIZE = 26;
	private static final float LABEL_BG_OPACITY = 0.2f;
	private static final Color LABEL_COLOR = Color.WHITE;

	/**
	 * Sets the size, location and adds itself to given parent automatically
	 * @param container    node will be added to this automatically
	 * @param x            x axis location
	 * @param y            y axis location
	 */
	public Node(Container container, int x, int y, Image image) {
		super(new ImageIcon(image));
		setLocation(x, y);
		container.add(this);
	}

	protected void addLabel(String name) {
		// Create a text label
		JLabel text = new JLabel(name);
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

	public abstract Point getCenter();

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