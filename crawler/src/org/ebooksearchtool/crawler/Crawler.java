package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.impl.*;

public class Crawler {

	public static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128));
//	public static final Proxy PROXY = Proxy.NO_PROXY;
	
	public static final String USER_AGENT = "ebooksearchtool";
	
	private static final int LIMIT = 500000;
	private final PrintWriter myOutput;
	private volatile String myAction;
	
	private final AbstractRobotsExclusion myRobots = new RobotsExclusion();
	
	Crawler(PrintWriter output) {
		myOutput = output;
		myAction = "nothing";
	}
	
	public String whatAmIDoing() {
		return myAction;
	}
	
	public void go(String[] starts) {
		final AbstractVisitedLinksSet were = new VisitedLinksSet();
		final AbstractLinksQueue queue = new LinksQueue();
		for (String start : starts) {
			myAction = "prechecking if i can go to " + start;
			if (myRobots.canGo(start)) {
				were.add(start);
				queue.offer(start);
			}
		}
		int iteration = 0;
		while (!queue.isEmpty()) {
			String s = queue.poll();
			myAction = "downloading page at " + s;
			String page = getPage(s);
			if (page == null) continue;
			System.out.println((++iteration) + " " + s + " " + page.length());
			myOutput.println(s);
			myAction = "getting all links out of " + s;
			List<String> links = getLinks(getServerNameFromURL(s), page);
			for (String link : links) {
				myAction = "checking if already visited " + link;
				if (!were.contains(link) && were.size() < LIMIT) {
					were.add(link);
					myAction = "checking if i can go to " + link;
					boolean permitted = myRobots.canGo(link);
					if (permitted) {
//System.out.println("allowed: " + link);
						myAction = "adding to queue " + link;
						queue.offer(link);
					} else {
//System.out.println("disallowed: " + link);
					}
				}
			}
		}
		myAction = "nothing";
		System.out.println("finished");
	}
	
	private List<String> getLinks(String server, String page) {
		return HTMLParser.parseLinks(server, page);
	}
	
	private String getPage(String s) {
		try {
			URLConnection connection = new URL(s).openConnection(Crawler.PROXY);
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = "";
			StringBuilder page = new StringBuilder();
			char[] buf = new char[1024];
			int r = 0;
			while ((r = br.read(buf, 0, 1024)) >= 0) {
				for (int i = 0; i < r; i++) {
					page.append(buf[i]);
				}
			}
			String ans = page.toString();
			br.close();
			return ans;
		} catch (Exception e) {
			System.err.println("error on URL =\n" + s);
			System.err.println(e.getMessage());
			//e.printStackTrace();
			return null;
		}
	}
	


	public static String getServerNameFromURL(String url) {
		if (!url.startsWith("http://")) return null;
		try {
			URL u = new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
		int x = url.indexOf("/", 7);
		return x < 0 ? url : url.substring(0, x);
	}
	
	
}
