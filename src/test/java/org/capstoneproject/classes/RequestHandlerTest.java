package org.capstoneproject.classes;

import static org.junit.Assert.*;
import com.thoughtworks.selenium.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RequestHandlerTest extends SeleneseTestBase 
{	
	/** Invoked at the beginning of the test */
	@Before
	public void setUp() throws Exception {
		//selenium = new DefaultSelenium("localhost", 4444, "*chrome", "http://www.google.co.in/");
		selenium = new DefaultSelenium("localhost",
						4444, "*chrome", "http://localhost:3000/index.html");
		selenium.start();
	}

	/** Used to test that a request was successful. It does this by
	 *        fetching the web page, and ascertain that it contains
	 *        expected text.
	*/
	@Test
	public void testRequestOk() throws Exception {
		selenium.open("http://localhost:3000/index.html");
		assertTrue(selenium.isTextPresent("Hello World!"));
	}
	
	/**
	 *        Used to test a cache hit. It does this by making two
	 *        requests, and ascertain that the second request takes
	 *        less time than the second request */
	@Test
	public void testCacheHit() {
		long start = System.currentTimeMillis();
		selenium.open("http://localhost:3000/index.html");
		long end = System.currentTimeMillis();
		long duration1 = end-start;
		
		start = System.currentTimeMillis();
		selenium.open("http://localhost:3000/index.html");
		end = System.currentTimeMillis();
		long duration2 = end-start;
		
		assertTrue(duration2 < duration1);
	}

	/**
	 *        Used to test a cache fault. It does this by making two
	 *        requests, and ascertain that the first request takes
	 *        longer than the second request */
	@Test
	public void testCacheFault() {
		long start = System.currentTimeMillis();
		selenium.open("http://localhost:3000/index.html");
		long end = System.currentTimeMillis();
		long duration1 = end-start;
		
		start = System.currentTimeMillis();
		selenium.open("http://localhost:3000/index.html");
		end = System.currentTimeMillis();
		long duration2 = end-start;
		
		assertTrue(duration1> duration2);
	}
	
	/**
	 *        Used to test the caching system. It does this by making a series
	 *        of requests, and making sure that the duration of each subsequent
	 *        request is shorter than the very first */
	@Test
	public void testCacheFaultAndProperCaching() {
		long start = System.currentTimeMillis();
		selenium.open("http://localhost:3000/index.html");
		long end = System.currentTimeMillis();
		long duration1 = end-start;
		
		start = System.currentTimeMillis();
		selenium.open("http://localhost:3000/index.html");
		end = System.currentTimeMillis();
		long duration2 = end-start;
		
		start = System.currentTimeMillis();
		selenium.open("http://localhost:3000/index.html");
		end = System.currentTimeMillis();
		long duration3 = end-start;
		
		assertTrue( (duration1> duration2) && (duration1>duration3) );
	}
	
	/** Invoked at completion of test run */
	@After
	public void tearDown() throws Exception {
		selenium.stop();
	}
}
