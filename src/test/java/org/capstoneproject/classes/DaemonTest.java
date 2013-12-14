package org.capstoneproject.classes;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/*
* Class: DaemonTest
* Author: Kyle Nickel
* Last Modified: 2013-11-29
*/
@RunWith(JUnit4.class)
public class DaemonTest {
	
	@Test
	public void testConfigShortHand() {
		String [] args = {"-p", "5000", "-m", "Debug", "-d", "/tmp",
						"-l", "/tmp/shuttle2.log"};
		WebServer server = new WebServer(args);
		assertEquals("Test port setting", "5000", server.getPort());
		assertEquals("Test mode setting", "Debug", server.getMode());
		assertEquals("Test directory setting", "/tmp", server.getDirectory());
		assertEquals("Test log setting", "/tmp/shuttle2.log", server.getLogPath());
	}
	
	@Test
	public void testConfigLongHand() {
		String [] args = {"--port", "5467", "--mode", "Production", "--directory", "/Users",
						"--log", "/tmp/daemon.log"};
		WebServer server2 = new WebServer(args);
		assertEquals("Test port setting long hand", "5467", server2.getPort());
		assertEquals("Test mode setting long hand", "Production", server2.getMode());
		assertEquals("Test directory setting long hand", "/Users", server2.getDirectory());
		assertEquals("Test log setting long hand", "/tmp/daemon.log", server2.getLogPath());
	}
	
	@Test
	public void testConfigFile() {
		String [] args = {"-c", "/Users/Kyle/Documents/School/CMSC-495/shuttle/src/main/java/org/capstoneproject/classes/shuttle.cfg"};
		WebServer server3 = new WebServer(args);
		assertEquals("Test port setting config file", "4567", server3.getPort());
		assertEquals("Test mode setting config file", "Production", server3.getMode());
		assertEquals("Test directory setting config file", "/var", server3.getDirectory());
		assertEquals("Test log setting config file", "shuttle_daemon.log", server3.getLogPath());
	}

}