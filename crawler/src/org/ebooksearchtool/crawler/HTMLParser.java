package org.ebooksearchtool.crawler;

import java.net.URI;
import java.util.*;
import net.htmlparser.jericho.*;

class HTMLParser {
    
    private static void maybeAddLink(List<URI> links, URI referrer, String link) {
        if (link.length() == 0 || link.startsWith("javascript:")) return;
        URI uri = null;
        try {
            uri = new URI(link);
            uri = referrer.resolve(uri);
        } catch (Exception e) {
            return;
        }
        links.add(uri);
    }
    
    
    static List<URI> parseLinks(URI referrer, String page) {
        List<URI> answer = new ArrayList<URI>();
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
                                        return new ArrayList<URI>();
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
