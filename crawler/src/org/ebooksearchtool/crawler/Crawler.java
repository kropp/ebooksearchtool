package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.impl.*;

public class Crawler {

//	public static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128));
	public static final Proxy PROXY = Proxy.NO_PROXY;
	public static final String USER_AGENT = "ebooksearchtool";
	public static final int CONNECTION_TIMEOUT = 4000;
	public static final int LIMIT = 500000;
	
	private final PrintWriter myOutput;
	private volatile String myAction;
	
	private final AbstractRobotsExclusion myRobots = new ManyFilesRobotsExclusion();
	
	Crawler(PrintWriter output) {
		myOutput = output;
		myAction = "doing nothing";
	}
	
	public String getAction() {
		return myAction;
	}
	
	public void crawl(String[] starts) {
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
			myAction = "getting all links out of " + s;
			List<String> links = HTMLParser.parseLinks(Util.getServerNameFromURL(s), page);
			for (String link : links) {
				myAction = "checking if already visited " + link;
				if (!were.contains(Util.createSimilarLinks(link)) && were.size() < LIMIT) {
					were.add(link);
					if (isBook(link)) {
						myAction = "writing information about visited book " + link;
						writeBookToOutput(link, s);
						continue;
					}
					myAction = "checking if i can go to " + link;
					boolean permitted = myRobots.canGo(link);
					if (permitted) {
						myAction = "adding to queue " + link;
						queue.offer(link);
					}
				}
			}
		}
		myAction = "doing nothing";
		System.out.println("finished; input something to exit");
	}
	
	private boolean isBook(String url) {
		return url.endsWith(".epub") || url.endsWith(".pdf");
	}
	
	private void writeBookToOutput(String url, String referrer) {
		myOutput.println(url + " from " + referrer);
		myOutput.flush();
	}
	
	private static String getPage(String url) {
		try {
			URLConnection connection = new URL(url).openConnection(Crawler.PROXY);
			if (!connection.getHeaderField("Content-Type").startsWith("text/html;")) {
				return null;
			}
			connection.setConnectTimeout(CONNECTION_TIMEOUT);
//			connection.setRequestProperty("User-Agent", USER_AGENT);
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
			System.err.println("error on " + url);
			System.err.println(e.getMessage());
			//e.printStackTrace();
			return null;
		}
	}
	

	
}
