package org.ebooksearchtool.crawler;

import javax.swing.text.html.parser.*;
import javax.swing.text.html.*;
import javax.swing.text.*;
import java.io.*;
import java.net.*;
import java.util.*;

class Util {

	public static List<String> createSimilarLinks(String url) {
		List<String> answer = new ArrayList<String>();
		final String HTTP = "http://";
		if (url.startsWith(HTTP)) {
			url = url.substring(HTTP.length());
		}
		String server = null;
		int slash = url.indexOf('/');
		if (slash < 0) {
			server = url;
			url = "";
		} else {
			server = url.substring(0, slash);
			url = url.substring(slash);
		}
		//TODO: /index.html, /index.htm, /index.php, #...
		for (int iteration = 0; iteration <= 1; iteration++) {
			if (iteration == 1) {
				///TODO: make this customizable (because www.site.com is not always equal to site.com)
				if (server.startsWith("www.")) {
					server = server.substring("www.".length());
				} else {
					server = "www." + server;
				}
			}
			String toAdd = HTTP + server + url;
			answer.add(toAdd);
			if (toAdd.endsWith("/")) {
				answer.add(toAdd.substring(0, toAdd.length() - 1));
			} else {
				answer.add(toAdd + "/");
			}
		}
		return answer;
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
