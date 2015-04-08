package com.simas;

		import java.awt.Dimension;
		import java.awt.Toolkit;
		import java.awt.Window;
		import javax.swing.JFrame;
		import javax.swing.WindowConstants;

/**
 * Created by Simas Abramovas on 2015 Apr 08.
 */

public abstract class BaseFrame extends JFrame {

	public static final String APP_NAME = "Visualizing OSPF";

	public BaseFrame() {
		super(APP_NAME);
		customizeFrame();
		addComponents();
	}

	public abstract void addComponents();

	/**
	 * Basic {@code Frame} customization
	 */
	private void customizeFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setUndecorated(false);
		resizeToFitInScreen(this, (double) 2 / 5);
		setLocationRelativeTo(null);
	}

	/**
	 * Resize the{@code }JFrame} to fit the screen at specified proportions.
	 * @param frame     Window that will be resized
	 * @param atMost    Part of the window that can be covered at most.
	 */
	static void resizeToFitInScreen(Window frame, double atMost) {
		// Check image sizes
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double maxWidth = screenSize.getWidth() * atMost;
		double maxHeight = screenSize.getHeight() * atMost;
		frame.setSize((int) maxWidth, (int) maxHeight);
		frame.setLocationRelativeTo(null); // Center frame on the screen
	}

	static void log(Object obj) {
		System.out.println(obj);
	}

}
