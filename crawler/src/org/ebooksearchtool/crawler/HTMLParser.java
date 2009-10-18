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
                    String tagName = tag.getName().toLowerCase();
                    if ("a".equals(tagName)) {
                        String href = attributes.getValue("href");
                        if (href != null) {
                            maybeAddLink(answer, server, href);
                            if (answer.size() == Crawler.getMaxLinksFromPage()) return answer;
                        }
                    } else if ("meta".equals(tagName)) {
                        for (Attribute attribute : attributes) {
                            String name = attributes.getValue("name");
                            if (name != null && "robots".equals(name.toLowerCase())) {
                                String content = attributes.getValue("content");
                                String[] terms = content.split(" *, *");
                                for (String term : terms) {
                                    if ("noindex".equals(term.toLowerCase()) || "nofollow".equals(term.toLowerCase())) {
                                        return new ArrayList<String>();
                                    }
                                }
                            }
                        }
                    }
                    // (there may be some other tags with links)
                }
            }
        }
        return answer;
    }
    
}
