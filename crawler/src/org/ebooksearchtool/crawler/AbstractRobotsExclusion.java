package org.ebooksearchtool.crawler;

public abstract class AbstractRobotsExclusion {
	
	private static String getServerNameFromURL(String url) {
		if (!url.startsWith("http://")) return null;
		///TODO: try {URL u = new URL(url);} catch (Exception e) {return false;}
		int x = url.indexOf("/", 7);
		return x < 0 ? url : url.substring(0, x);
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
		String server = getServerNameFromURL(url);
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
