/**
 * 	The RequestHandler class is responsible for handling user requests.
 * 	It contains several methods and makes use of different classes to implement
 * 	the different functionalities required when handling a Request.
 */	
package org.capstoneproject.classes;

import java.io.*;
import java.net.*;
import java.util.*;
import org.capstoneproject.logging.*;

public class RequestHandler implements Runnable 
{
	/** Instance variables */
	private DataOutputStream dos;	/** Used to send data to client browser */
	private Request request;		/** Used to hold information pertinent to a Request */
	private Response response;		/** Used to aggregate information sent back to the client */
	private HashMap<String, String> cfg;
	
	/**
	 * 	The constructor for the class
	 * 	@param request representing information related to a request 
	 *	@param response	representing information related to a response
	 * 	@throws IOException
	 * */
	public RequestHandler(Request request, Response response,
		HashMap<String, String> cfg) throws IOException 
	{
		this.request = request;
		this.response = response;
		this.cfg = cfg;
		
		try {
			dos = new DataOutputStream(request.getClientSocket().getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 	The overriden run() method from the Runnable class.
	 */	
	public void run() 
	{
		try {
			handleRequest();
		} catch (Exception ex) { 
			ex.printStackTrace();
		}
	}
	
	/**
	 * 	Handles a user request. It parses the Request, then determines the content
	 *	type of the Request. If the Request is valid, then appropriate header
	 *	information is set, and the requested resource is returned to the Client.
	 *	Similarly if the request is invalid, an appropriate error page is returned.
	 * 	@throws Exception
	 */	
	private void handleRequest() throws Exception 
	{
		parseRequest(request);
		
		if (request.isValid()) 
		{
			/** Determine content type requested which can be html, image, text file */
			String contentType = getContentType(request.getRequestedResource());
			
			switch (contentType) {
				case "text/html": 
					response.setContentType("Content-Type: text/html\r\n"); break;
				case "image/gif": 
					response.setContentType("Content-Type: image/gif\r\n"); break;
				case "image/jpeg": 
					response.setContentType("Content-Type: image/jpeg\r\n"); break;
				case "text/plain": 
					response.setContentType("Content-Type: text/plain\r\n"); break;
			}
			
			response.setStatus("HTTP/1.1 200 OK" + "\r\n");
		}
		else 
		{
			switch(request.getErrCode()) {
				case REQUEST_BAD:
					response.setStatus("HTTP/1.1 400 Bad Request" + "\r\n");
					request.setRequestedResource("./error_pages/badRequest.html");
					break;
				case REQUEST_FORBIDDEN: 
					response.setStatus("HTTP/1.1 403 Request Forbidden" + "\r\n");
					request.setRequestedResource("./error_pages/requestForbidden.html");
				break;
				case REQUEST_NOT_FOUND: 
					response.setStatus("HTTP/1.1 404 NotFound" + "\r\n");
					request.setRequestedResource("./error_pages/notfound.html");
					break;
				default:
					break;
			}
			response.setContentType("Content-Type: text/html\r\n");
		}
		
		// Log Entry!!!!!!!!!!!!!!
		Logger log = response.getCacher().getLogger();
		Entry entry = new Entry();
		entry.setClientAddr(request.getClientAddress());
		entry.setRequestedResource(request.getRequestedResource());
		entry.setStatus(response.getStatus());
		
		log.log("NORM", "RequestHandler", entry.toString());
		
		sendResponse();
	}
	
	/**
	 * 	Sends response back to the client. It writes the header information
	 *	first, then writes a new line indicator which is necessary for
	 *	functionality. The requested resource content is then sent to the
	 *	client browser.
	 * 	@throws Exception
	 */	
	private void sendResponse()  throws Exception
	{
		dos.writeBytes(response.getStatus());
		dos.writeBytes(response.getContentType());
		dos.writeBytes("\n"); // Necessary! b/c a blank line signals end of headers!!!!!!!!!!!!
		
		dos.write(response.getResourceContent());
		dos.close();
	}
	
	/**
	 * 	Parses a client request
	 * 	@param request the object holding information associated with a client
	 *	request.
	 * 	@throws IOException
	 */	
    private void parseRequest(Request request) throws IOException 
    {
		/** Retrieve and store the web root directory from the configurations variable. */
		String root = cfg.get("directory");
		
		/** Fetch client connection socket from the Request Object */
    	Socket socket = request.getClientSocket();
    	
		/** Create a Buffered reader object, to be used in parsing the client request.
		 *	The Request consist of a few text lines with delimiters.
		*/
		BufferedReader bR = new BufferedReader(
				new InputStreamReader(socket.getInputStream()));
		
		// Get Resource Name!
		String requestLine = bR.readLine();
		
		StringTokenizer sT = new StringTokenizer(requestLine);
		sT.nextToken(); // take the GET token out of sT
		
		// Set Requested Resource Name in Request Object
		String resourceName = sT.nextToken();
		
	 	if ( (!resourceName.contains(".jpg")) &&
				(resourceName.length() == 1 || !resourceName.contains("index.html")))
			resourceName += "index.html";
		
		/** sets the path to be used in retrieving the requested resource */
		request.setRequestedResource(root + resourceName);
		
		// Get Host Name!
		requestLine = bR.readLine();
		sT = new StringTokenizer(requestLine);
		sT.nextToken();
		String hostInfo = sT.nextToken();
		sT = new StringTokenizer(hostInfo, ":");
		
		// Set Client IP Address in the Request Object
		String hostName = sT.nextToken();
		request.setClientAddress(hostName);

		//bR.close();
		response.setRequest(request);
    }
    
	/**
	 * 	Determines the content type of the resource based on file extension.
	 * 	@param resourceName
	 * 	@return contentType
	 */	
	private String getContentType(String resourceName) {
		String contentType = null;
		
		if (resourceName.endsWith(".html") || resourceName.endsWith(".htm")) return "text/html";
		if (resourceName.endsWith(".gif")) return "image/gif";
		if (resourceName.endsWith(".jpeg") || resourceName.endsWith(".jpg")) return "image/jpeg";
		if (resourceName.endsWith(".txt")) return "text/plain";
		
		return contentType;
	}
}