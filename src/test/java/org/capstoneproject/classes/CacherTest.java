/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.capstoneproject.classes;

import org.capstoneproject.logging.*;
import org.capstoneproject.caching.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author matth_000
 */
public class CacherTest 
{
	@Test
	public void trivial() {}
	
/* 	private Logger logger;
	private Cacher cache;
  
	@Before
	public void setUp() {
		logger = new Logger();
        cache = new Cacher(logger,1);
	}
    
	@Test
	public void testCacheFault() {
		String name = "n1";
		byte[] b = new byte[1];
		b[0] = 0;

		String name2 = "n2";
		byte[] b2 = new byte[1];
		b2[0] = 1;

		assertEquals(cache.cacheRequest(name), null);
		assertEquals(cache.cacheRequest(name2), null);
	}

	@Test
	public void testCacheHit() 
	{
		logger = new Logger();
        cache = new Cacher(logger,1);
		
		String name = "n1";
		byte[] b = name.getBytes();

		String name2 = "n2";
		byte[] b2 = name2.getBytes();
		
		cache.cacheAdd(name, b);
		cache.cacheAdd(name2, b2);

		byte[] res1 = cache.cacheRequest(name);
		byte[] res2 = cache.cacheRequest(name2);
		
		assertTrue( (res1 != null) );
		assertTrue( (res2 != null) );
    } */
    
}
