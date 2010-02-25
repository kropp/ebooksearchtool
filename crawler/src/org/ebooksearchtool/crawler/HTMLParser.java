package org.ebooksearchtool.crawler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.Tag;

class HTMLParser {
    
    private static void maybeAddLink(List<Link> links, Link referrer, String link) {
        if (link.length() == 0 || link.startsWith("javascript:")) return;
        Link uri = null;
        try {
            uri = Util.createLink(link);
            String referrerPath = referrer.getPath();
            boolean addSlash = referrerPath == null || "".equals(referrerPath);
            if (!addSlash) {
                int lastSlash = referrerPath.lastIndexOf('/');
                addSlash = lastSlash < 0 || referrerPath.substring(lastSlash).indexOf('.') < 0;
            }
            if (addSlash) {
                referrer = new Link(referrer + "/");
            }
            uri = referrer.resolve(uri);
            String checkDots = uri.toString();
            if (checkDots.indexOf("../") >= 0) {
                uri = new Link(checkDots.replaceAll("\\.\\./", ""));
            }
        } catch (Exception e) {
            return;
        }
        links.add(uri);
    }
    
    
    static List<Link> parseLinks(Link referrer, String page) {
        List<Link> answer = new ArrayList<Link>();
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
                            maybeAddLink(answer, referrer, href);
                        }
                    } else if ("meta".equals(tagName)) {
                        for (Attribute attribute : attributes) {
                            String name = attributes.getValue("name");
                            if (name != null && "robots".equals(name.toLowerCase())) {
                                String content = attributes.getValue("content");
                                if (content != null) {
                                    String[] terms = content.split(" *, *");
                                    for (String term : terms) {
                                        if ("noindex".equals(term.toLowerCase()) || "nofollow".equals(term.toLowerCase())) {
                                            return new ArrayList<Link>();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // (there may be some other tags with links)
                }
            }
        }
        Collections.shuffle(answer);
        return answer;
    }
    
}
