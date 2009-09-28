package org.ebooksearchtool.crawler.impl;

import java.io.*;
import java.net.*;
import org.ebooksearchtool.crawler.*;

public class RobotsExclusion implements AbstractRobotsExclusion {

	private static String getServerNameFromURL(String url) {
		int x = url.indexOf("/", 7);
		return x < 0 ? url : url.substring(0, x);
	}
	
	public boolean isPermitted(String url) {
//System.out.println("checking = " + url);
		String server = null;
		try {
			server = getServerNameFromURL(url);
		} catch (StringIndexOutOfBoundsException aioobe) {
			return false;
		}
		BufferedReader br = null;
		try {
			URLConnection connection = new URL(server + "/robots.txt").openConnection(Main.PROXY);
			br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		} catch (MalformedURLException mue) {
			return true;
		} catch (IOException ioe) {
			return true;
		}
		String s = "";
		boolean me = false;
		try {
			while ((s = br.readLine()) != null) {
				s = s.trim().replaceAll(" +", " ").toLowerCase();
				if (s.startsWith("user-agent:")) {
					s = s.substring(11).trim();
					me = "*".equals(s) || "ebooksearchtool".equals(s);
				} else if (me && s.startsWith("disallow:")) {
					s = s.substring(9).trim();
					if (url.startsWith(server + s)) {
						return true;
					}
				}
			}
		} catch (IOException ioe) {
			return false;
		}
		
		return true;
	}
	
}

