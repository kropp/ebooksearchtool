package org.ebooksearchtool.crawler.impl;

import java.io.*;
import java.net.*;
import org.ebooksearchtool.crawler.*;

public class OneFileRobotsExclusion extends AbstractRobotsExclusion {
	
	private static final File ROBOTS_FILE = new File("robotscache.txt");
	
	/*  stores all cached robots.txt in one file
	    very inefficient and slow                */
	public OneFileRobotsExclusion() {
		if (!ROBOTS_FILE.exists()) {
			try {
				new PrintWriter(ROBOTS_FILE).close();
			} catch (FileNotFoundException e) {
				System.err.println(ROBOTS_FILE + " cannot be created");
				System.exit(0);
			}
		}
	}
	
	protected int isDisallowed(String server, String url) {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(ROBOTS_FILE));
		} catch (IOException ioe) {
			System.err.println(ROBOTS_FILE + " cannot be read");
			return 1;
		}
		String s = "";
		boolean serverFound = false;
		url = url.substring(server.length());
		try {
			while ((s = br.readLine()) != null) {
				if (s.length() == 0) continue;
				if (s.charAt(0) == ' ') {
					if (serverFound && matches(url, s.substring(1))) {
						br.close();
						return 1;
					}
				} else {
					if (s.equals(server)) {
						serverFound = true;
					} else {
						if (serverFound) {
							br.close();
							return 0;
						}
						serverFound = false;
					}
				}
			}
			br.close();
		} catch (IOException ioe) {
			System.err.println("error while reading " + ROBOTS_FILE);
		}
		return serverFound ? 0 : -1;
	}
	
	protected void downloadRobotsTxt(String server) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(ROBOTS_FILE, true));
		} catch (IOException e) {
			System.err.println(ROBOTS_FILE + " cannot be written");
			return;
		}
		BufferedReader br = null;
		try {
			URLConnection connection = new URL(server + "/robots.txt").openConnection(Crawler.PROXY);
			connection.setConnectTimeout(Crawler.CONNECTION_TIMEOUT);
			InputStream is = connection.getInputStream();
			if (is == null) throw new IOException();
			if (!connection.getHeaderField("Content-Type").startsWith("text/plain")) {
				throw new IOException();
			}
			br = new BufferedReader(new InputStreamReader(is));
		} catch (MalformedURLException mue) {
			System.err.println("malformed URL: " + server);
			try {
				bw.close();
			} catch (IOException ioe) { }
			return;
		} catch (IOException ioe) {
			try {
				bw.write(server);
				bw.newLine();
				bw.close();
			} catch (IOException ioe2) { }
			return;
		}
		String s = "";
		boolean me = false;
		try {
			bw.write(server);
			bw.newLine();
			while ((s = br.readLine()) != null) {
				s = s.trim().replaceAll(" +", " ").toLowerCase();
				if (s.startsWith("user-agent:")) {
					s = s.substring(11).trim();
					me = "*".equals(s) || Crawler.USER_AGENT.equals(s);
				} else if (me && s.startsWith("disallow:")) {
					int end = s.indexOf('#');
					if (end < 0) end = s.length();
					s = s.substring(9, end).trim();
					if (s.length() > 0) {
						bw.write(" " + s);
						bw.newLine();
					}
				}
			}
			br.close();
			bw.close();
		} catch (IOException ioe) {
			return;
		}
	}
	
	
}

