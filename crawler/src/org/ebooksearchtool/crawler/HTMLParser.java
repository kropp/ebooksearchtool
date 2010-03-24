package org.ebooksearchtool.crawler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.htmlparser.jericho.Attribute;
import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.StartTag;
import net.htmlparser.jericho.Tag;
import org.ebooksearchtool.crawler.cases.SpecialCase;

public class HTMLParser {
    
    private static List<SpecialCase> cases = new ArrayList<SpecialCase>();
    
    public static void addCase(SpecialCase caze) {
        cases.add(caze);
    }
    
    public static void maybeAddLink(Collection<Link> links, Link pageLink, String link) {
        if (link.length() == 0 || link.startsWith("javascript:")) return;
        Link uri = null;
        try {
            uri = Util.createLink(link);
            String path = pageLink.getPath();
            boolean addSlash = path == null || "".equals(path);
            if (!addSlash) {
                int lastSlash = path.lastIndexOf('/');
                addSlash = lastSlash < 0 || path.substring(lastSlash).indexOf('.') < 0;
            }
            if (addSlash) {
                pageLink = new Link(pageLink + "/");
            }
            uri = pageLink.resolve(uri);
            String checkDots = uri.toString();
            if (checkDots.indexOf("../") >= 0) {
                uri = new Link(checkDots.replaceAll("\\.\\./", ""));
            }
        } catch (Exception e) {
            return;
        }
        links.add(uri);
    }
    
    
    public static List<Link> parseLinks(Link pageLink, String page) {
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
                            maybeAddLink(answer, pageLink, href);
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
    
    
    
    public static List<Link> parse(Link pageLink, String page) {
        List<Link> answer = parseLinks(pageLink, page);
        for (SpecialCase caze : cases) {
            try {
                caze.apply(pageLink, page, answer);
            } catch (Exception e) {
                Logger.getDefaultLogger().log(Logger.MessageType.ERRORS, " special case (" + caze + ") error on " + pageLink);
            }
        }
        return answer;
    }
    
}
