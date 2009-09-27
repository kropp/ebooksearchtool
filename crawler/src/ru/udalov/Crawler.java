package ru.udalov;

import java.io.*;
import java.net.*;
import java.util.*;

class Crawler {

	private static final int LIMIT = 500000;
	private final PrintWriter myOutput;
	
	Crawler(PrintWriter output) {
		myOutput = output;
	}
	
	public void go(String start) {
		final Set<String> were = new HashSet<String>();
		final Queue<String> queue = new LinkedList<String>();
		were.add(start);
		queue.offer(start);
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
					queue.offer(link);
				}
			}
		}
		System.out.println("finished");
	}
	
	private static List<String> getLinks(String page) {
		final List<String> links = new LinkedList<String>();
		String href = "href=\"http://";
		int k = -1;
		while (true) {
			k = page.indexOf(href, k + 1);
			if (k < 0) break;
			int x = page.indexOf("\"", k + href.length() - 7);
			if (x < 0) break;
			String t = page.substring(k + href.length() - 7, x);
			links.add(t);
		}
		return links;
	}
	
	private static String getPage(String s) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new URL(s).openStream()));
			String line = "";
			StringBuilder page = new StringBuilder();
			while ((line = br.readLine()) != null) {
				page.append(line);
				page.append("\n");
			}
			String ans = page.toString();
			return ans;
		} catch (Exception e) {
			System.err.println("error on URL =\n" + s);
			System.err.println(e.getMessage());
			//e.printStackTrace();
			return null;
		}
	}
	
	
}
