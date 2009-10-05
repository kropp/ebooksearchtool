package org.ebooksearchtool.crawler.impl;

import java.io.*;
import java.net.*;
import org.ebooksearchtool.crawler.*;

public class ManyFilesRobotsExclusion extends AbstractRobotsExclusion {
	
	private static final File ROBOTS_DIR = new File("robotscache");
	private static final int FILES_NUMBER = 256;
	private File[] cacheFile;
	
	/*  stores all cached robots.txt in a number of files:
	    0.txt, 1.txt, ..., {FILES_NUMBER - 1}.txt,
	    where the number chosen for the given server is its name's hashcode  */
	public ManyFilesRobotsExclusion() {
		try {
			if (!ROBOTS_DIR.exists()) {
				boolean success = ROBOTS_DIR.mkdir();
				if (!success) throw new Exception();
			}
			cacheFile = new File[FILES_NUMBER];
			for (int i = 0; i < FILES_NUMBER; i++) {
				cacheFile[i] = new File(ROBOTS_DIR + "/" + String.format("%03d", i) + ".txt");
				if (!cacheFile[i].exists()) {
					new PrintWriter(cacheFile[i]).close();
				}
			}
		} catch (Exception e) {
			System.err.println(ROBOTS_DIR + " cannot be initialized");
			System.exit(0);
		}
	}
	
	protected int isDisallowed(String server, String url) {
		File file = cacheFile[Math.abs(server.hashCode()) % FILES_NUMBER];
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (IOException ioe) {
			System.err.println(file + " cannot be read");
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
			System.err.println("error while reading " + file);
		}
		return serverFound ? 0 : -1;
	}
	
	protected void downloadRobotsTxt(String server) {
		File file = cacheFile[Math.abs(server.hashCode()) % FILES_NUMBER];
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
		} catch (IOException e) {
			System.err.println(file + " cannot be written");
			return;
		}
		BufferedReader br = null;
		try {
			URLConnection connection = new URL(server + "/robots.txt").openConnection(Crawler.PROXY);
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
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

