package org.ebooksearchtool.crawler;

import javax.swing.text.html.parser.*;
import javax.swing.text.html.*;
import javax.swing.text.*;
import java.io.*;
import java.util.*;

class HTMLParser extends HTMLEditorKit.ParserCallback {
	
	private final List<String> myLinks = new ArrayList<String>();
	private final String myServer;
	
	private HTMLParser(String server) {
		myServer = server;
	}
	
	private List<String> getLinks() {
		return myLinks;
	}
	
	private void maybeAddLink(String url) {
		if (url.startsWith("javascript:")) return;
		if (url.indexOf("://") < 0) {
			if (url.charAt(0) != '/') url = "/" + url;
			url = myServer + url;
		}
		myLinks.add(url);
	}
	
	public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attr, int pos) {
		super.handleSimpleTag(tag, attr, pos);
		if (tag == HTML.Tag.A) {
			Object href = attr.getAttribute(HTML.Attribute.HREF);
			if (href != null) {
				maybeAddLink(href.toString());
			}
		}
	}
	
	
	static List<String> parseLinks(String server, String page) {
		HTMLParser parser = new HTMLParser(server);
		try {
			DocumentParser dp = new DocumentParser(DTD.getDTD("html"));
			dp.parse(new StringReader(page), parser, true);
		} catch (Exception e) {
System.err.println("o_O " + e.getMessage());
			return new ArrayList<String>();
		}
		return parser.getLinks();
	}
	
}
