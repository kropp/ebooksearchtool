package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.impl.*;

class Crawler {

	private static final int LIMIT = 500000;
	private final PrintWriter myOutput;
	
	private final AbstractRobotsExclusion myRobots = new RobotsExclusion();
	
	Crawler(PrintWriter output) {
		myOutput = output;
	}
	
	public void go(String[] starts) {
		final AbstractVisitedLinksSet were = new VisitedLinksSet();
		final AbstractLinksQueue queue = new LinksQueue();
		for (String start : starts) {
			if (myRobots.isPermitted(start)) {
				were.add(start);
				queue.offer(start);
			}
		}
		int iteration = 0;
		while (!queue.isEmpty()) {
			String s = queue.poll();
			String page = getPage(s);
			if (page == null) continue;
			System.out.println((++iteration) + " " + s + " " + page.length());
			myOutput.println(s);
			List<String> links = getLinks(page);
			for (String link : links) {
				if (!were.contains(link) && were.size() < LIMIT) {
					were.add(link);
					boolean permitted = myRobots.isPermitted(link);
					if (permitted) {
System.out.println("allowed: " + link);
						queue.offer(link);
					} else {
System.out.println("disallowed: " + link);
					}
				}
			}
		}
		System.out.println("finished");
	}
	
	private List<String> getLinks(String page) {
		final List<String> links = new LinkedList<String>();
		String href = "href=";
		int k = -1;
		while (true) {
			k = page.indexOf(href, k + 1);
			if (k < 0) break;
			k += 5;
			if (page.length() > k && page.charAt(k) == '\"') {
				int x = page.indexOf("\"", k + 1);
				if (x < 0) break;
				String t = page.substring(k + 1, x);
				links.add(t);
			}
		}
		return links;
	}
	
	private String getPage(String s) {
		try {
			URLConnection connection = new URL(s).openConnection(Main.PROXY);
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
	
	
}