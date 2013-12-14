/**
 *	The Request class holds information pertinent to a user request.
 *	It contains information pertinent to a Request such as the name
 *	of the requested resource, the client address making the request, etc...
*/
package org.capstoneproject.classes;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.Socket;

public class Request 
{
	/** Accessors */
	
	public String getRequestedResource() {
		return requestedResource;
	}

	public String getClientBrowser() {
		return clientBrowser;
	}
	
	public String getClientAddress() {
		return clientAddress;
	}
	
	public ErrorCode getErrCode() {
		return errCode;
	}
	
	public Socket getClientSocket() {
		return clientSocket;
	}

	/** Mutators */
	
	public void setRequestedResource(String requestedResource) {
		this.requestedResource = requestedResource;
	}
	
	public void setClientBrowser(String clientBrowser) {
		this.clientBrowser = clientBrowser;
	}
	
	public void setClientAddress(String clientAddress) {
		this.clientAddress = clientAddress;
	}
	
	public void setErrCode(ErrorCode errCode) {
		this.errCode = errCode;
	}
	
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	/** Instance variables */
	private String requestedResource;
	private String clientBrowser;
	private String clientAddress;
	private ErrorCode errCode;
	private Socket clientSocket;
	

	/** Constructor */
	public Request(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	/**
	 *	A method used to determine whether a request is valid.
	 *	It determines this by checking to see whether or not the
	 *	requested resource exist, along with necessary file permissions.
	 *	@return boolean indicating whether or not the request
	 *	is valid.
	*/
	public boolean isValid() 
	{
		/** Assume that the request is valid */
		errCode = ErrorCode.REQUEST_OK;
		
		/** If client address is missing then request is bad (could be forged) */
		if (this.clientAddress == null || requestedResource == null)
		{
			this.errCode = ErrorCode.REQUEST_BAD;
		}
		
		/** Perform file operations to determine if file exists, and whether 
		 *	permissions will allow reading.
		*/
		try {
			BufferedReader br = new BufferedReader(
					new FileReader(this.getRequestedResource()));
		} 
		catch (FileNotFoundException e) 
		{
			if (e.getMessage().contains("Access is denied")) {
				errCode = ErrorCode.REQUEST_FORBIDDEN;
			}
			else
				errCode = ErrorCode.REQUEST_NOT_FOUND;
		}
		
		/** for request to be valid, client IP must be set, along with valid resource name */
		if (errCode == ErrorCode.REQUEST_OK)
			return true;
		else
			return false;
	}
}