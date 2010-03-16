package org.ebooksearchtool.crawler.cases;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.List;
import org.ebooksearchtool.crawler.HTMLParser;
import org.ebooksearchtool.crawler.Link;
import org.ebooksearchtool.crawler.Network;

public class LuluCase implements SpecialCase {
    
    private static Link ourActionLink;
    
    private final Network myNetwork;
    
    public LuluCase(Network network) {
        myNetwork = network;
        try {
            ourActionLink = new Link("http://www.lulu.com/browse/product_preview_endpoint.php");
        } catch (URISyntaxException e) { }
    }
    
    private boolean thisCase(Link link) {
        if (!"www.lulu.com".equals(link.getHost())) return false;
        String path = link.getPath();
        if (!path.startsWith("/product/")) return false;
        path = path.substring(9);
        try {
            Long.parseLong(path);
        } catch (Exception e) {
            if (!path.startsWith("download")) return false;
        }
        return true;
    }
    
    public void apply(Link link, String page, Collection<Link> result) {
        if (!thisCase(link)) return;
        if (link.getPath().startsWith("/product/download")) {
        	String s = "http://www.lulu.com/content/content_download_redirect.php?metaId=";
        	int si = page.indexOf(s) + s.length();
            int sj = si;
            while (Character.isDigit(page.charAt(sj))) sj++;
            String uri = s + page.substring(si, sj);
            try {
                result.add(new Link(uri));
            } catch (Exception e) { }
            return;
        }
        String pd_ = "previewDialog_";
        int cdi = page.indexOf(pd_) + pd_.length();
        int cdj = cdi;
        while (Character.isDigit(page.charAt(cdj))) cdj++;
        int contentId = Integer.parseInt(page.substring(cdi, cdj));
        String previewBlock = myNetwork.sendPOST(ourActionLink, "contentId=" + contentId);
        List<Link> links = HTMLParser.parseLinks(link, previewBlock);
        for (Link l : links) {
            result.add(l);
System.out.println("!!!! " + l);
        }
    }
    
    public String toString() {
        return "Lulu.com";
    }
    
}
