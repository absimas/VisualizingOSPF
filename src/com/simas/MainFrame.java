package com.simas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends BaseFrame {

    private static final int NODE_SPACING = 100;
    private static final int NODE_ROWS = 2;
    private static final int NODE_COLS = 2;
	private static final Color CONNECTION_COLOR = new Color(0, 0, 0, 0.4f);

    private List<Node> mRouters;
	private Node mSwitch;
    private JPanel mContainer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    @Override
    public void addComponents() {
        // Init node array (from parent class)
        mRouters = new ArrayList<>();
        // Will handle sizes and positions manually
        setLayout(null);

        // Calculate router container pos
        int containerWidth = Router.SIZE.width * NODE_COLS + NODE_SPACING * (NODE_COLS - 1);
        int containerHeight = Router.SIZE.height * NODE_ROWS + NODE_SPACING * (NODE_ROWS - 1);

        int horizontalBorder = (getSize().width - containerWidth) / 2;
        int verticalBorder = (getSize().height - containerHeight) / 2;


        // Init router container // Will handle sizes and positions manually
        mContainer = new JPanel(null);
        mContainer.setLocation(horizontalBorder, verticalBorder);
        mContainer.setSize(containerWidth, containerHeight);
        add(mContainer);

	    // Init switch at the center
	    mSwitch = new Switch(mContainer, (containerWidth - Switch.SIZE.width) / 2,
			    (containerHeight - Switch.SIZE.height) / 2);

        // Init routers
        // First row
        mRouters.add(new Router(mContainer, 0, 0));
        mRouters.add(new Router(mContainer, Router.SIZE.width + NODE_SPACING, 0));

        // Second row
        mRouters.add(new Router(mContainer, 0, Router.SIZE.height + NODE_SPACING));
        mRouters.add(new Router(mContainer, Router.SIZE.width + NODE_SPACING,
		        Router.SIZE.height + NODE_SPACING));


//        // Init router container // Will handle sizes and positions manually
//        JPanel lineContainer = new JPanel(null);
//        lineContainer.setLocation(horizontalBorder, verticalBorder);
//        lineContainer.setSize(totalRouterWidth, totalRouterHeight);
//        lineContainer.setBackground(new Color(0, 0, 0, 0.2f));
////        lineContainer.setOpaque(true);
//        add(lineContainer);
//        setComponentZOrder(lineContainer, 2);

        // Draw connecting lines
        // From 0 to 1
        log(mRouters.get(0).getCenterLocation());
        log(mRouters.get(1).getCenterLocation());
        log(mRouters.get(2).getCenterLocation());
        log(mRouters.get(3).getCenterLocation());

        log("Finished adding components");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Make sure nodes are initialized
        if (mContainer != null && mRouters.size() == NODE_ROWS * NODE_COLS) {
            log("Drawing lines");
            final Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // ToDo abstractize

            // Get node center points
	        Point node0 = mSwitch.getCenterLocation();
            Point node1 = mRouters.get(0).getCenterLocation();
            Point node2 = mRouters.get(1).getCenterLocation();
            Point node3 = mRouters.get(2).getCenterLocation();
            Point node4 = mRouters.get(3).getCenterLocation();

            // Draw lines
            g.setColor(CONNECTION_COLOR);
            g.drawLine(node1.x, node1.y, node0.x, node0.y);
            g.drawLine(node2.x, node2.y, node0.x, node0.y);
            g.drawLine(node3.x, node3.y, node0.x, node0.y);
            g.drawLine(node4.x, node4.y, node0.x, node0.y);
        }
    }
}
