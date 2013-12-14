package org.capstoneproject.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.HashMap;

import org.capstoneproject.caching.Cacher;
import org.capstoneproject.classes.Request;
import org.capstoneproject.classes.RequestHandler;
import org.capstoneproject.classes.Response;

import org.capstoneproject.logging.Logger;
import org.capstoneproject.logging.MasterLogger;

public class WebServer
{
	private Socket clientSocket;
	private ServerSocket serverSocket;
	private int port;
	private static Cacher cache = new Cacher(new Logger(), 10);
	private static MasterLogger masterLogger;
	private Logger logger = new Logger();
	private HashMap<String, String> options = new HashMap<String, String>();
	
	/*
	 * Method: WebServer
	 * Args: command line options
	 * Desc: Default constructor to set up all needed objects
	 */
	public WebServer(String [] args) {
		try {
			// parse configurations
			getOpts(args);
			
			// create MasterLogger
			masterLogger = new MasterLogger(options.get("log"));
			
			// create our server socket
			serverSocket = new ServerSocket(new Integer(options.get("port")));
		} catch (IOException e) {
			// output some useful information
			logger.log("Error", "Daemon ", "Houston we have a problem");
			logger.log("Error", "Daemon ", e.toString());
			logger.log("Error", "Daemon ", e.getStackTrace().toString());
			logger.close();
		}
		
			logger.log("NORM", "Daemon ", "We have liftoff on port: " + options.get("port"));
			logger.close();
	}
	
	/*
	 * Method: listen
	 * Args: None
	 * Desc: Loop to listen for incoming connections
	 */
	private final void listen() {
		while (true) {
			try {
				clientSocket = serverSocket.accept();
				Request incomingRequest = new Request(clientSocket);
				Response outgoingResponse = new Response(incomingRequest, cache);
				//new Thread (new RequestHandler(incomingRequest, outgoingResponse)).start();
				new Thread (new RequestHandler(incomingRequest, outgoingResponse, options)).start();
			} catch (Exception e ) {
				logger.log("Error", "Daemon ", "Houston we have a problem");
				logger.log("Error", "Daemon ", e.toString());
				logger.log("Error", "Daemon ", e.getStackTrace().toString());
				logger.close();
			}
		}
	}
	
	/*
	 * Method: getOpts
	 * Args: String array args
	 * Desc: Go through each option and add it to the options hash
	 * 		 call the parseConfig method if necessary
	 */
	private void getOpts(String [] args) {
    	
		String option = new String();
		
		for (int i = 0; i < args.length; i++) {
			
			option = args[i].toString().toLowerCase();
			
			if (option.equals("-c") || option.equals("--config")) {
				options.put("config", args[++i]);
			} else if (option.equals("-m") || option.equals("--mode")) {
				options.put("mode", args[++i]);
			} else if (option.equals("-d") || option.equals("--directory")) {
				options.put("directory", args[++i]);
			} else if (option.equals("-p") || option.equals("--port")) {
				options.put("port", args[++i]);
			} else if (option.equals("-l") || option.equals("--log")) {
				options.put("log", args[++i]);
			} else {
				logger.log("Warn", "Daemon ", "Unknown argument");
				logger.close();
			}
		}
		
		if (options.containsKey("config")) {
			parseConfig();
		}
		
		if (!options.containsKey("mode")) {
			options.put("mode", "debug");
		}
		
		if (!options.containsKey("directory")) {
			//options.put("directory", "/var/www/html");
			options.put("directory", "./htdocs/");
		}
		
		if (!options.containsKey("port")) {
			options.put("port", "3000");
		}
		
		if (!options.containsKey("log")) {
			options.put("log", "/tmp/shuttle.log");
		}
    }
	
	/*
	 * Method: parseConfig
	 * Args: None
	 * Desc: Go through a config file line by line and add the 
	 * 		 options to the options hash
	 */
	private void parseConfig() {
		// declare needed variables
		File config = new File(options.get("config"));
		BufferedReader br;
		String [] parts = new String[2];
		try {
			br = new BufferedReader(new FileReader(config));
			String line = new String();
			while ((line = br.readLine()) != null) {
				parts = line.split(":");
				// go through each part adding it to the options hash but don'e overwrite 
				if (parts[0].equals("mode") && !options.containsKey("mode")) {
					options.put("mode", parts[1]);
				} else if (parts[0].equals("directory") && !options.containsKey("directory")) {
					options.put("directory", parts[1]);
				} else if (parts[0].equals("port") && !options.containsKey("port")) {
					options.put("port", parts[1]);
				} else if (parts[0].equals("log") && !options.containsKey("log")) {
					options.put("log", parts[1]);
				} else {
					logger.log("Warn", "Daemon ", "Unknown option");
					logger.close();
				}
			}
		} catch (Exception e) {
			logger.log("Error", "Daemon ", e.toString());
			logger.log("Error", "Daemon ", e.getStackTrace().toString());
			logger.close();
		}
	
	}
	
	// Accessors
	public String getConfigPath() {
		return options.get("config");
	}
	
	public String getMode() {
		return options.get("mode");
	}
	
	public String getDirectory() {
		return options.get("directory");
	}
	
	public String getPort() {
		return options.get("port");
	}
	
	public String getLogPath() {
		return options.get("log");
	}
	
    public static void main( String[] args )  throws Exception
    {	
    	WebServer shuttle = new WebServer(args);
    	shuttle.listen();
    }
}
