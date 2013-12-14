/**
 *	The Response class is used to hold information commonly
 *	associated with a response, such as the content type, other
 *	necessary header information, and the resource content.
*/
package org.capstoneproject.classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.capstoneproject.caching.Cacher;

public class Response 
{
	/** Accessors */
	public String getStatus() {
		return status;
	}
	public String getContentType() {
		return contentType;
	}
	public String getContentLength() {
		return contentLength;
	}
	public byte[] getResourceContent() throws IOException {
		String key = request.getRequestedResource() + request.getClientAddress();
		
		Object cacheRes = cache.cacheRequest(key);
				
		// if cache contains
		if (cacheRes != null) {
			return cache.cacheRequest(key);
		}
		else {
			resourceContent = convertToBytes();
			cache.cacheAdd(key, resourceContent);
			return resourceContent;
		}	
	}
	public ErrorCode getErrCode() {
		return errCode;
	}
	public Request getRequest() {
		return request;
	}
	public Cacher getCacher() { return this.cache; }

	/** Mutators */
	public void setStatus(String status) {
		this.status = status;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public void setContentLength(String contentLength) {
		this.contentLength = contentLength;
	}
	public void setResourceContent(byte[] resourceContent) {
		this.resourceContent = resourceContent;
	}
	public void setErrCode(ErrorCode errorCode) {
		this.errCode = errCode;
	}
	
	public void setRequest(Request request) {
		this.request = request;
	}

	/** Instance variables */
	private String status;
	private String contentType;
	private String contentLength;
	private byte[] resourceContent;
	private ErrorCode errCode;
	private Request request;
	private Cacher cache;
	
	/**
	 *	Constructor
	 *	@param request holds information related to a request
	 *	@param cache The cache used to store content of previously
	 *	retrieved web pages.
	*/
	public Response(Request request, Cacher cache) {
		this.request = request;
		this.cache = cache;
	}
	
	/**
	 *	Method used to convert the content of a text file into
	 *	a byte stream.
	 *	@throws IOException
	 *	@return byte[] which is a byte array holding the file content.
	*/
	private byte[] convertToBytes() throws IOException {
		// read file into byte array, Then Send!!!!!!
		File f = new File(request.getRequestedResource());
		FileInputStream fis = new FileInputStream(request.getRequestedResource());
		byte[] resourceContent = new byte[(int)f.length()];
		fis.read(resourceContent);
		fis.close();
		return resourceContent;
	}
}