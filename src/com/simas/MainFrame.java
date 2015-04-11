package com.simas;

import com.simas.Node.Router;
import com.simas.Node.Switch;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;

// OSPF
	// http://ciscoiseasy.blogspot.com/2011/02/lesson-39-ospf-fundamentals-part2-hello.html

// Maybe
// spacing based on node count?
// pause/resume all timers
// clickable packets? This would need a pausable program,
	// which would need accessible timer arrays (including ProgressDot timers...)

public class MainFrame extends BaseFrame {

	private static final String TWO_WAY_STATES = "Two way states:";
	private static final int TWO_WAY_PANEL_SPACING = 20;
	private static final int TWO_WAY_PANEL_WIDTH = 280;

	public static final int TOP_SPACE = 24;
    private static final int NODE_SPACING = 200;
    private static final int NODE_ROWS = 2;
    private static final int NODE_COLS = 2;
	private static final Color DR_CONNECTION_COLOR = new Color(0, 1f, 0, 0.7f);
	private static final Color BDR_CONNECTION_COLOR = new Color(1f, 0, 0, 0.4f);
	private static final Color SWITCH_CONNECTION_COLOR = new Color(0, 0, 0, 0.3f);
    public static final int DEFAULT_HELLO_INTERVAL = 2500; // Milliseconds
    public static final int DEFAULT_DEAD_INTERVAL = 5200; // Milliseconds 10 secs for ethernet

	private Switch mSwitch;
	public static int helloInterval = DEFAULT_HELLO_INTERVAL;
    public static int deadInterval = DEFAULT_DEAD_INTERVAL;
	public static JList<String> sTwoWayList;

	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    @Override
    public void addComponents() {
        // Will handle sizes and positions manually
        setLayout(null);
	    // Increase tooltip dismiss delay
	    ToolTipManager.sharedInstance().setDismissDelay(60000);

        /* Node container */
        int containerWidth = Router.SIZE.width * NODE_COLS + NODE_SPACING * (NODE_COLS - 1);
        int containerHeight = Router.SIZE.height * NODE_ROWS + NODE_SPACING * (NODE_ROWS - 1);
	    JPanel container = new JPanel(null) {
		    @Override
		    public void paint(Graphics g) {
			    super.paint(g);
			    final Graphics2D g2 = (Graphics2D) g;
			    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			    // Draw connecting lines
			    Point switchCenter = mSwitch.getCenterLocation();
			    // Find DR and BSR
			    Point drCenter = (Router.sDR != null) ? Router.sDR.getCenterLocation() : null;
			    Point bdrCenter = (Router.sBDR != null) ? Router.sBDR.getCenterLocation() : null;
			    for (Router router : mSwitch.routers) {
				    // Don't draw lines to dead routers
				    if (router.isDead()) continue;
				    Point routerCenter = router.getCenterLocation();
				    // Connection to switch
				    g2.setColor(SWITCH_CONNECTION_COLOR);
				    g2.drawLine(routerCenter.x, routerCenter.y, switchCenter.x, switchCenter.y);

//				    // Connection to DR (if not self)
//				    if (drCenter != null && router != Router.sDR) {
//					    g2.setColor(DR_CONNECTION_COLOR);
//					    g2.drawLine(routerCenter.x, routerCenter.y, drCenter.x, drCenter.y);
//				    }
//
//				    // Connection to BDR (if not self)
//				    if (bdrCenter != null && router != Router.sBDR) {
//					    g2.setColor(BDR_CONNECTION_COLOR);
//					    g2.drawLine(routerCenter.x, routerCenter.y, bdrCenter.x, bdrCenter.y);
//				    }
			    }
		    }
	    };
	    // Calculate container position
	    Point loc = new Point(0, 0);
	    if (getWidth() > containerWidth + TWO_WAY_PANEL_WIDTH) {
		    loc.x = (getWidth() - containerWidth - TWO_WAY_PANEL_WIDTH) / 2;
	    }
	    if (getHeight() > containerHeight) {
		    loc.y = (getHeight() - containerHeight) / 2;
	    }
        container.setLocation(loc);
        container.setSize(containerWidth, containerHeight);
        container.setOpaque(false);
        add(container);
	    //------------------------------------------------------------------------------------------
	    /* Two way panel */
	    Dimension panelSize = new Dimension(TWO_WAY_PANEL_WIDTH, getSize().height);

	    // JList
	    sTwoWayList = new JList<>();
	    sTwoWayList.setFont(new Font("Arial", Font.PLAIN, 18));
	    sTwoWayList.setSize(panelSize);

	    // Scroll pane
	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setViewportView(sTwoWayList);
	    scrollPane.setSize(panelSize);

	    // Label
	    JLabel twoWayLabel = new JLabel(TWO_WAY_STATES);
	    twoWayLabel.setFont(new Font("Arial", Font.PLAIN, 22));

	    // Containing panel
	    JPanel twoWayPanel = new JPanel();
	    twoWayPanel.setLayout(new BoxLayout(twoWayPanel, BoxLayout.Y_AXIS));
	    twoWayPanel.add(twoWayLabel);
	    twoWayPanel.add(scrollPane);
	    twoWayPanel.setSize(scrollPane.getWidth() - TWO_WAY_PANEL_SPACING,
			    getHeight() - TOP_SPACE - TWO_WAY_PANEL_SPACING);

	    // Calculate panel position
	    if (getWidth() > container.getX() + container.getWidth() + twoWayPanel.getWidth() +
			    TWO_WAY_PANEL_SPACING) {
		    // Router container and 2 way list will fit together
		    loc = new Point(getWidth() - twoWayPanel.getWidth() - TWO_WAY_PANEL_SPACING, 0);
	    } else {
		    loc = new Point(container.getX() + container.getWidth() + TWO_WAY_PANEL_SPACING, 0);
	    }
	    twoWayPanel.setLocation(loc);
	    add(twoWayPanel);
	    //------------------------------------------------------------------------------------------
        /* Nodes */
        mSwitch = Router.sSwitch = new Switch(container, (containerWidth - Switch.SIZE.width) / 2,
                (containerHeight - Switch.SIZE.height) / 2);
        Rectangle switchRect = mSwitch.getRect();

        // Loop router rows
        for (int row = 0; row < NODE_ROWS; ++row) {
            // Loop router cols
            for (int col = 0; col < NODE_COLS; ++col) {
                // Rectangle imitating the node
                Point nodePos = new Point((Router.SIZE.width + NODE_SPACING) * col,
                        (Router.SIZE.height + NODE_SPACING) * row);
                Rectangle nodeRect = new Rectangle(nodePos, Router.SIZE);

                // Don't draw nodes on top of the switch
                if (!switchRect.intersects(nodeRect)) {
                    mSwitch.routers.add(new Router(container, nodePos));
                }
            }
        }
    }

}
