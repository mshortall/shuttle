/**
 *	The class containing the main method. It creates an instance of
 *	the AppTestingGUI class, adds it to a frame, and displays the
 *	GUI (frame).
*/
package org.capstoneproject.tests;

import javax.swing.*;

public class ApplicationTesting 
{
	/** Constants used to hold the dimensions of the GUI */
	private final static int LENGTH = 500;
	private final static int WIDTH = 700;
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Application Testing");
		frame.add(new AppTestingGUI());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, LENGTH);
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
	}
}
