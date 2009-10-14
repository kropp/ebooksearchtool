package org.ebooksearchtool.crawler;

import java.io.*;
import java.util.*;
import net.htmlparser.jericho.*;

class HTMLParser {
	
	private static void maybeAddLink(List<String> links, String server, String url) {
		if (url.length() == 0 || url.startsWith("javascript:")) {
			return;
		}
		if (url.indexOf("://") < 0) {
			if (url.charAt(0) != '/') url = "/" + url;
			url = server + url;
		}
		links.add(url);
	}
	
	
	static List<String> parseLinks(String server, String page) {
		List<String> answer = new ArrayList<String>();
		Source source = new Source(page);
		source.setLogger(null);
		Tag[] tags = source.fullSequentialParse();
		for (Tag tag : tags) {
			if (tag instanceof StartTag) {
				StartTag startTag = (StartTag)tag;
				Attributes attributes = startTag.getAttributes();
				if (attributes != null) {
					if ("a".equals(tag.getName())) {
						String href = attributes.getValue("href");
						if (href != null) {
							maybeAddLink(answer, server, href);
						}
					}
					// (there may be some other tags with links)
				}
			}
		}
		return answer;
	}
	
}
