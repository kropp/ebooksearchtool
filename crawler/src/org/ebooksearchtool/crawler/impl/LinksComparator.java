package org.ebooksearchtool.crawler.impl;

import java.util.Comparator;
import java.net.URI;

public class LinksComparator implements Comparator<URI> {

    public int compare(URI a, URI b) {
        int diff = linkValue(b) - linkValue(a);
        if (diff != 0) return diff;
        /*
        diff = a.hashCode() - b.hashCode();
        if (diff != 0) return diff;
        */
        return a.compareTo(b);
    }
    
    
    private static final String[] BAD_SITES = new String[]
    {"facebook", "wikipedia", "tumblr", "rutube", "endless"};
    private static final String[] GOOD_DOMAINS = new String[]
    {"com", "net", "org", "info", "edu", "gov", "biz", "ru", "uk", "us"};
    
    public static int linkValue(URI uri) {
        String s = uri.toString();
        for (String badSite : BAD_SITES) {
            if (s.indexOf(badSite) >= 0) return -100;
        }
        String host = uri.getHost();
        int lastPoint = host.lastIndexOf('.');
        if (lastPoint >= 0) {
            String domain = host.substring(lastPoint + 1);
            boolean isGoodDomain = false;
            for (String goodDomain : GOOD_DOMAINS) {
                if (goodDomain.equals(domain)) {
                    isGoodDomain = true;
                    break;
                }
            }
            if (!isGoodDomain) return -5;
        }
        if (s.indexOf("epub") >= 0 || s.indexOf("book") >= 0) return 5;
        return 0;
    }
    
}
