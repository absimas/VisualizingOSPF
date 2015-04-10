package com.simas;

import com.simas.Node.Node;
import com.simas.Node.Router;
import com.simas.Node.Switch;
import com.simas.Packet.HelloPacket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// ToDo scrollable area kad galima butu dafiga node'u
// ToDo spacing based on node count?
// ToDo Each router, based on its id, has a delay to send the first hello packet. => so all r visibl

public class MainFrame extends BaseFrame {

    private static final int NODE_SPACING = 170;
    private static final int NODE_ROWS = 3;
    private static final int NODE_COLS = 3;
	private static final Color CONNECTION_COLOR = new Color(0, 0, 0, 0.4f);
    private static final Dimension INTERVAL_FIELD_SIZE = new Dimension(300, 20);
    public static final int DEFAULT_HELLO_INTERVAL = 5000; // Milliseconds
    public static final int DEFAULT_DEAD_INTERVAL = 10000; // Milliseconds

	private Switch mSwitch;
    private JPanel mContainer;
    public static int helloInterval = DEFAULT_HELLO_INTERVAL;
    public static int deadInterval = DEFAULT_DEAD_INTERVAL;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }

    @Override
    public void addComponents() {

        // Will handle sizes and positions manually
        setLayout(null);

        // Node container
        int containerWidth = Router.SIZE.width * NODE_COLS + NODE_SPACING * (NODE_COLS - 1);
        int containerHeight = Router.SIZE.height * NODE_ROWS + NODE_SPACING * (NODE_ROWS - 1);
        int horizontalBorder = (getSize().width - containerWidth) / 2;
        int verticalBorder = (getSize().height - containerHeight) / 2;

        mContainer = new JPanel(null) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                final Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw connecting lines
                g.setColor(CONNECTION_COLOR);
                Point switchCenter = mSwitch.getCenterLocation();
                for (Node node : mSwitch.routers) {
                    Point nodeCenter = node.getCenterLocation();
                    g.drawLine(nodeCenter.x, nodeCenter.y, switchCenter.x, switchCenter.y);
                }
            }
        };
        mContainer.setLocation(horizontalBorder, verticalBorder);
        mContainer.setSize(containerWidth, containerHeight);
        mContainer.setOpaque(false);
        add(mContainer);

        // Switch
        mSwitch = Router.sSwitch = new Switch(mContainer, (containerWidth - Switch.SIZE.width) / 2,
                (containerHeight - Switch.SIZE.height) / 2);
        Rectangle switchRect = mSwitch.getRect();

        // Routers
        // Loop rows
        for (int row = 0; row < NODE_ROWS; ++row) {
            // Loop cols
            for (int col = 0; col < NODE_COLS; ++col) {
                // Rectangle imitating the node
                Point nodePos = new Point((Router.SIZE.width + NODE_SPACING) * col,
                        (Router.SIZE.height + NODE_SPACING) * row);
                Rectangle nodeRect = new Rectangle(nodePos, Router.SIZE);

                // Don't draw nodes on top of the switch
                if (!switchRect.intersects(nodeRect)) {
                    mSwitch.routers.add(new Router(mContainer, nodePos));
                }
            }
        }

        mSwitch.resendPacket(new HelloPacket(1, ""));
    }







    //        // Options
//        // JTextField for hello interval
//        JLabel intervalLabel = new JLabel("Hello interval (in ms):   ");
//        JTextField intervalField = new JTextField(String.valueOf(helloInterval));
//        intervalField.getDocument().addDocumentListener(new FieldParser(intervalField));
//
//        JPanel helloInterval = new JPanel();
//        helloInterval.setLayout(new BoxLayout(helloInterval, BoxLayout.X_AXIS));
//        helloInterval.add(intervalLabel);
//        helloInterval.add(intervalField);
//        helloInterval.setSize(INTERVAL_FIELD_SIZE);
//
//        // JTextField for hello interval
//        intervalLabel = new JLabel("Death interval (in ms): ");
//        intervalField = new JTextField(String.valueOf(deadInterval));
//        intervalField.getDocument().addDocumentListener(new FieldParser(intervalField));
//
//        JPanel deadInterval = new JPanel();
//        deadInterval.setLayout(new BoxLayout(deadInterval, BoxLayout.X_AXIS));
//        deadInterval.add(intervalLabel);
//        deadInterval.add(intervalField);
//        deadInterval.setSize(INTERVAL_FIELD_SIZE);
//
//        // Option fields
//        JPanel options = new JPanel();
//        options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
//        options.add(helloInterval);
//        options.add(deadInterval);
//        options.setLocation((getWidth() - INTERVAL_FIELD_SIZE.width) / 2, 0);
//        options.setSize(INTERVAL_FIELD_SIZE.width, INTERVAL_FIELD_SIZE.height * 3);
//        add(options);

//    private final class FieldParser implements DocumentListener {
//
//        private final JTextField mField;
//        private final Border mDefaultBorder;
//        private final Border mRedBorder = BorderFactory.createLineBorder(Color.RED);
//
//        public FieldParser(JTextField component) {
//            mField = component;
//            mDefaultBorder = mField.getBorder();
//        }
//
//        @Override
//        public void insertUpdate(DocumentEvent e) {
//            parseField();
//        }
//
//        @Override
//        public void removeUpdate(DocumentEvent e) {
//            parseField();
//        }
//
//        @Override
//        public void changedUpdate(DocumentEvent e) {
//            parseField();
//        }
//
//        private void parseField() {
//            try {
//                helloInterval = Integer.parseInt(mField.getText());
//                mField.setBorder(mDefaultBorder);
//            } catch (NumberFormatException e) {
//                helloInterval = DEFAULT_HELLO_INTERVAL;
//                mField.setBorder(mRedBorder);
//            }
//        }
//
//    }

}
