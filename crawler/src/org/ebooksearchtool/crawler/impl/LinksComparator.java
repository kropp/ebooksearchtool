package org.ebooksearchtool.crawler.impl;

import java.util.*;
import org.ebooksearchtool.crawler.Link;

class LinksComparator implements Comparator<Link> {
    
    private final Map<String, Integer> myHasBooks;
    
    private final Set<String> myGoodDomains;
    private final Collection<String> myGoodSites;
    private final Collection<String> myBadSites;
    
    LinksComparator(Map<String, Integer> hasBooks, Set<String> goodDomains, Collection<String> goodSites, Collection<String> badSites) {
        myHasBooks = hasBooks;
        myGoodDomains = goodDomains;
        myGoodSites = goodSites;
        myBadSites = badSites;
    }
    
    public int compare(Link a, Link b) {
        int fa, fb;
        fa = linkValue(a);
        fb = linkValue(b);
        if (fa != fb) {
            return fb < fa ? -1 : 1;
        }
        fa = a.hashCode();
        fb = b.hashCode();
        if (fa != fb) {
            return fa < fb ? -1 : 1;
        }
        return a.compareTo(b);
    }
    
    private int linkValue(Link link) {
        String s = link + "";
        for (String badSite : myBadSites) {
            if (s.indexOf(badSite) >= 0) return -1000000000;
        }
        if (isGoodSite(s)) return 1000000000;
        String host = link.getHost();
        Integer thisHostHasBooks = myHasBooks.get(host);
        if (thisHostHasBooks != null) {
            return thisHostHasBooks * 20;
        }
        int lastPoint = host.lastIndexOf('.');
        if (lastPoint >= 0) {
            String domain = host.substring(lastPoint + 1);
            if (!myGoodDomains.contains(domain)) return -5;
        }
        if (s.indexOf("epub") >= 0 || s.indexOf("ebook") >= 0) return 10;
        if (s.indexOf("book") >= 0) return 5;
        return 0;
    }
    
    boolean isGoodSite(String s) {
        for (String goodSite : myGoodSites) {
            if (s.indexOf(goodSite) >= 0) return true;
        }
        return false;
    }
    
}
