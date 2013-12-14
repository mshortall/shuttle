/**
 *	This is a very trivial class. It was designed to help generate a log entry.
 *	It is used by the RequestHandler class to log events into the system log.
 *	@author Daman
*/

package org.capstoneproject.logging;

import java.util.Date;

public class Entry {

	public String getClientAddr() {
		return clientAddr;
	}

	public void setClientAddr(String clientAddr) {
		this.clientAddr = clientAddr;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public String getRequestedResource() {
		return requestedResource;
	}

	public void setRequestedResource(String requestedResource) {
		this.requestedResource = requestedResource;
	}

	private String clientAddr;
	private Date date;
	private String status;
	private int size;
	private String requestedResource;
	private String componentName;
	
	@Override
	public String toString() {
		Date now = new Date();
		String out = "";
		
		out += " ";
		out += requestedResource + " ";
		out += status;
		
		//out += clientAddr + " [" + new Date() + "] " + status + " 22";
		return out;
	}
}
