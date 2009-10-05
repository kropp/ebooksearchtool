package org.ebooksearchtool.crawler;

import java.net.*;

public abstract class AbstractRobotsExclusion {
	
	protected static boolean matches(String url, String pattern) {
		// regular expressions?...
		return url.startsWith(pattern);
	}
	
	/*  returns -1 if no robots.txt is stored for this server,
	             0 if url is not disallowed,
	             1 if url is disallowed                       */
	protected abstract int isDisallowed(String server, String url);
	
	/*  creates information about robots.txt located on the server.
	    if there is no robots.txt, information about total allowance
	    must be generated anyway.                             */
	protected abstract void downloadRobotsTxt(String server);
	
	boolean canGo(String url) {
		String server = Crawler.getServerNameFromURL(url);
		if (server == null) return false;
		int disallowed = isDisallowed(server, url);
		if (disallowed == -1) {
			downloadRobotsTxt(server);
			disallowed = isDisallowed(server, url);
		}
		assert disallowed >= 0;
		return disallowed == 0;
	}
	
}
