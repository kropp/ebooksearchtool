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
        if (!link.getPath().startsWith("/product/")) return false;
        try {
            Long.parseLong(link.getPath().substring(9));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public void apply(Link link, String page, Collection<Link> result) {
        if (!thisCase(link)) return;
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
