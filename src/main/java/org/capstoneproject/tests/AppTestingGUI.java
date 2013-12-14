/**
 *	Class used to create the Graphical User Interface (GUI) for the application
 *	used to test the Shuttle Web Server. It extends JPanel, and adds plenty of
 *	GUI components such as buttons, text fields, and events.
*/
package org.capstoneproject.tests;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppTestingGUI extends JPanel 
{
	/** Instance variables accessible throughout the class */
	private final JTextArea resultsTxtArea;	/** Used to displays statistics from test runs */
	private final JTextField numRequestsTf;	/** Used to gather number of Requests to generate */
	
	/**
	 *	Constructor
	 * */
	public AppTestingGUI() 
	{
		/** sets the layout of the panel (this) */
		setLayout(new BorderLayout());
		
		/** creates a JPanel used to hold a textfield, message, and button for input collection. */
		JPanel panel1 = new JPanel(new GridLayout(1,3));
		panel1.add(new JLabel("Please enter the number of Requests: "));
		
		// text field where # of requests is entered
		numRequestsTf = new JTextField(3);
		panel1.add(numRequestsTf);
		
		// button clicked to run test
		JButton runTestsBt = new JButton("Run Test!");
		
		/** Defining actions for the Run Test Button. Clicking on the button will
		 *	cause the specified number of requests to be generated and sent to the
		 *	web server. An error Message is displayed if a non-numeric value or null
		 *	is entered. */
		runTestsBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				int numRequests = 0;
				
				/** retrieves an instance of the Runtime (CMD-Line), used to execute commands */
				Runtime rt = Runtime.getRuntime();
				
				/** retrieve number of requests to generate */
				try {
					numRequests = Integer.parseInt(numRequestsTf.getText());
				} catch(NumberFormatException nfe) {
					JOptionPane.showMessageDialog(null, "Please Enter A Valid Number!");
				}
				
				/** Generate a web request using CURL (An automatic web request tool)
				 *	for the number of times requested by the Tester */
				for (int i = 0; i < numRequests; i++) 
				{
					long start = System.currentTimeMillis();
					resultsTxtArea.append("Start Time: " + start + "\n");
					
					try {
						//Process proc = rt.exec("curl http://localhost:3000/index.html");
						rt.exec("curl http://localhost:3000/index.html");
						
/* 						BufferedReader bR = new BufferedReader(new InputStreamReader(proc.getInputStream()));
						String line = null, output = "";
						while ((line = bR.readLine()) != null) {
								output += line + "\n"; 
						}*/
						
/* 						bR.close();
						resultsTxtArea.append(output); */
				
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					long end = System.currentTimeMillis();
					resultsTxtArea.append("End Time: " + end + "\n");
					resultsTxtArea.append("Length: " + (end-start) + "\n");
					resultsTxtArea.append("==============================================\n");
				}
			}
		});
		
		/** After creating the button, adding action, finally add it to the first panel*/
		panel1.add(runTestsBt);
		
		/** Add the first panel to this GUI object */
		add(panel1, BorderLayout.NORTH);
		
		// text area used to display test results
		resultsTxtArea = new JTextArea();
		
		/** Add a JScrollPane around the Text Area used to display results b/c of potentially
		 *	long outputs. Then add it to this GUI object */
		add(new JScrollPane(resultsTxtArea), BorderLayout.CENTER);
		
		/** create a second panel used to display buttons at the bottom of the page */
		JPanel panel2 = new JPanel(new GridLayout(1,3));
		panel2.add(new JLabel("Click Here To Clear Results Area! "));
		
		// button used to clear test results area
		JButton clearTxtAreaBt = new JButton("Clear Text Area!");
		JButton traverseDirBt = new JButton("Traverse Directory!");
		
		/** Add action to the button that causes a directory traversal */
		traverseDirBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					new TraverseDirectoryAndTestPages(resultsTxtArea).run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		/** Adding action to the button used to clear test results Text Area */
		clearTxtAreaBt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				resultsTxtArea.setText("");
			}
		});
		
		/** Add the buttons to the second Panel, and add the panel to
		 *	this GUI object */
		panel2.add(clearTxtAreaBt);
		panel2.add(traverseDirBt);
		
		add(panel2, BorderLayout.SOUTH);
	}
}
