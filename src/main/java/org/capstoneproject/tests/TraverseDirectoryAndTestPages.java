/**
 *	This class is used by the ApplicationTesting class to perform
 *	a recursive directory traversal. It retrieves every html page (.html)
 *	stored in the webroot and its sub-folders. 
*/
package org.capstoneproject.tests;

import javax.swing.*;
import java.awt.*;

import java.io.*;
import java.util.StringTokenizer;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import static java.nio.file.FileVisitResult.*;

/**
	src: http://docs.oracle.com/javase/tutorial/essential/io/walk.html
  *	Most of the code from this class was retrieved from the above URL.
  *	The changes made to the code are appropriately commented.
  *	The code assumes that the BASE DIRECTORY is called htdocs!!!!!!!!!!!!
*/
public class TraverseDirectoryAndTestPages 
	extends SimpleFileVisitor<Path> 
{
	/** 
	 *	Constructor 
	 *	@param resultsTxtArea - A Text Area used to display statistics from test runs
	*/
	public TraverseDirectoryAndTestPages(JTextArea resultsTxtArea) 
	{
		this.numOfPagesRequested = 0;
		this.numOfPagesReturned = 0;
		this.numOfFailures = 0;
		this.resultsTxtArea = resultsTxtArea;
	}
	
	/**
	 * 	@author Team Shuttle
	 *  Instance variables added to keep track of a single instance of the Runtime, and to 
	 * help build the url for each web page found during a directory traversal.
	 * */
	
	/** URL to default webroot used to build URL for pages to request */
	private String url = "http://localhost:3000/";	
	
	private Runtime rt = Runtime.getRuntime();

	/** Statistics about test run, to be displayed in the end */
	private static long totalTime;
	private static int numOfPagesRequested;
	private static int numOfPagesReturned;
	private static int numOfFailures;
	private JTextArea resultsTxtArea;
	private static int testRunCount = 0;
	
	// Print information about
	// each type of file.
	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
		if (attr.isSymbolicLink()) {
			//System.out.format("Symbolic link: %s ", file);
		} else if (attr.isRegularFile() && (file.toString().endsWith(".html")) ) {
			System.out.format("Regular file: %s ", file);
			StringTokenizer st = new StringTokenizer(file.toString(), "\\");
			
			/**
			 * 	@author Team Shuttle
			 * 	Changes made to Transform each file path into a URL. First token extracted
			 * 	is a (.) representing the current directory. The URL is then built.
			 * */
			st.nextToken();
			while (st.hasMoreTokens()) {
				url += st.nextToken();
				url += (st.hasMoreTokens())? "/" : "";
			}
	
			/**
			 * 	@author Team SHuttle
			 * 	Code used to call the CURL comand with each URL or page encountered
			 * 	during directory traversal.
			 * */
			try {
				String[] urlPts = url.split("/htdocs/");
				url = urlPts[0] + "/" + urlPts[1];
				
				//System.out.println("URL " + url);
				
				Process proc = rt.exec("curl " + url);
				numOfPagesRequested++;	/** Increment counter for # of Pages Requested */
				
				BufferedReader bR = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line = null;
				while ((line = bR.readLine()) != null) {
					if (line.contains("The Requested Resource Was Not Found!!")) {
						numOfFailures++; /** If error page is returned as Request Response, then increment failures counter */
					}
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			url = "http://localhost:3000/";
				
		} else {
			//System.out.format("Other: %s ", file);
		}
		System.out.println("(" + attr.size() + "bytes)");
		return CONTINUE;
	}

	// Print each directory visited.
	@Override
	public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
		//System.out.format("Directory: %s%n", dir);
		return CONTINUE;
	}

	// If there is some error accessing
	// the file, let the user know.
	// If you don't override this method
	// and an error occurs, an IOException 
	// is thrown.
	@Override
	public FileVisitResult visitFileFailed(Path file, IOException exc) 
	{
		System.err.println(exc);
		return CONTINUE;
	}
	
	/**
	 * 	@author Team Shuttle
	 * 	Method added to enable other objects to invoke methods in this Object.
	 * */
	public void run() throws Exception
	{
		//Path startingDir = ...;
		//Path startingDir = FileSystems.getDefault().getPath("logs", "access.log");
		Path startingDir = FileSystems.getDefault().getPath("./htdocs");
		TraverseDirectoryAndTestPages tdatp = new TraverseDirectoryAndTestPages(resultsTxtArea);
		
		long start = System.currentTimeMillis();
		Files.walkFileTree(startingDir, tdatp);
		long end = System.currentTimeMillis();
		
		/** Keeps track of # of directory traversals performed */
		testRunCount++;
		
		/** # of successful requests = (total # of requests - # of failed requests) */ 
		numOfPagesReturned = numOfPagesRequested - numOfFailures;
		
		totalTime = end-start;	/** compute time in milliseconds of directory traversal duration */
		
		/** Display statistics from test run */
		resultsTxtArea.append("\nShuttle Test Run " + testRunCount + "\n");
		resultsTxtArea.append("================\n");
		
		resultsTxtArea.append("Number of Pages Returned " + numOfPagesReturned + "\n");
		resultsTxtArea.append("Number of Pages Requested " + numOfPagesRequested + "\n");
		resultsTxtArea.append("Number of Failures " + numOfFailures + "\n");
		resultsTxtArea.append("Total Time " + totalTime + "\n");
	}
}