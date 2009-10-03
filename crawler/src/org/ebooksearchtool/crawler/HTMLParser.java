package org.ebooksearchtool.crawler;

import javax.swing.text.ChangedCharSetException;
import javax.swing.text.html.parser.*;
import javax.swing.text.html.*;
import java.io.*;
import java.util.*;

class HTMLParser extends Parser {
	
	private final List<String> myLinks = new ArrayList<String>();
	
	HTMLParser(DTD dtd) {
		super(dtd);
	}
	
	private List<String> getLinks() {
		return myLinks;
	}
	
	protected void startTag(TagElement tag) {
		try {
			super.startTag(tag);
		} catch (ChangedCharSetException e) { }
		if (tag.getHTMLTag() == HTML.Tag.A) {
			Element e = tag.getElement();
			AttributeList atts = e.atts;
			while (atts != null) {
				System.err.println(atts.getName() + " " + e.getName());
				atts = atts.next;
			}
		}
	}
	
	
	static List<String> parseLinks(String page) {
		HTMLParser parser = null;
		try {
			parser = new HTMLParser(DTD.getDTD(""));
			parser.parse(new StringReader(page));
		} catch (Exception e) {
			System.err.println("o_O");
			return new ArrayList<String>();
		}
		return parser.getLinks();
	}
	
}
