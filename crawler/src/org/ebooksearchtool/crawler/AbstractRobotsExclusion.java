package org.ebooksearchtool.crawler;

public abstract class AbstractRobotsExclusion {
	
	private static String getServerNameFromURL(String url) {
		int x = url.indexOf("/", 7);
		return x < 0 ? url : url.substring(0, x);
	}
	
	/*  returns -1 if no robots.txt is stored for this server,
	             0 if url is not disallowed,
	             1 if url is disallowed                       */
	protected abstract int isDisallowed(String server, String url);
	
	protected abstract void downloadRobotsTxt(String server);
	
	boolean canGo(String url) {
		///TODO: try {URL u = new URL(url);} catch (Exception e) {return false;}
		String server = getServerNameFromURL(url);
		int disallowed = isDisallowed(server, url);
		if (disallowed == -1) {
			downloadRobotsTxt(server);
			disallowed = isDisallowed(server, url);
		}
		return disallowed == 0;
	}
	
}
