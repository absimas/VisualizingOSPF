package com.simas;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class MainFrame extends BaseFrame {

    private static final int NODE_SPACING = 100;
    private static final int NODE_ROWS = 2;
    private static final int NODE_COLS = 2;

    private List<Node> mNodes;
    private JPanel mRouterContainer;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    @Override
    public void addComponents() {
        // Init node array (from parent class)
        mNodes = new ArrayList<>();
        // Will handle sizes and positions manually
        setLayout(null);

        // Calculate router container pos
        int totalRouterWidth = Node.SIZE.width * NODE_COLS + NODE_SPACING * (NODE_COLS - 1);
        int totalRouterHeight = Node.SIZE.height * NODE_ROWS + NODE_SPACING * (NODE_ROWS - 1);

        int horizontalBorder = (getSize().width - totalRouterWidth) / 2;
        int verticalBorder = (getSize().height - totalRouterHeight) / 2;


        // Init router container // Will handle sizes and positions manually
        mRouterContainer = new JPanel(null);
        mRouterContainer.setLocation(horizontalBorder, verticalBorder);
        mRouterContainer.setSize(totalRouterWidth, totalRouterHeight);
        add(mRouterContainer);

        // Init routers
        // First row
        mNodes.add(new Node(mRouterContainer, 0, 0));
        mNodes.add(new Node(mRouterContainer, Node.SIZE.width + NODE_SPACING, 0));

        // Second row
        mNodes.add(new Node(mRouterContainer, 0, Node.SIZE.height + NODE_SPACING));
        mNodes.add(new Node(mRouterContainer, Node.SIZE.width + NODE_SPACING,
                Node.SIZE.height + NODE_SPACING));


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
        log(mNodes.get(0).getCenterLocation());
        log(mNodes.get(1).getCenterLocation());
        log(mNodes.get(2).getCenterLocation());
        log(mNodes.get(3).getCenterLocation());

        log("Finished adding components");
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // Make sure nodes are initialized
        if (mRouterContainer != null && mNodes.size() == NODE_ROWS * NODE_COLS) {
            log("Drawing lines");
            final Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // ToDo abstractize

            // Get node center points
            Point node1 = mNodes.get(0).getCenterLocation();
            Point node2 = mNodes.get(1).getCenterLocation();
            Point node3 = mNodes.get(2).getCenterLocation();
            Point node4 = mNodes.get(3).getCenterLocation();

            // Draw lines
            g.setColor(new Color(0, 0, 0, 0.4f));
            g.drawLine(node1.x, node1.y, node2.x, node2.y);
            g.drawLine(node2.x, node2.y, node3.x, node3.y);
            g.drawLine(node3.x, node3.y, node4.x, node4.y);
            g.drawLine(node4.x, node4.y, node1.x, node1.y);
        }
    }
}
