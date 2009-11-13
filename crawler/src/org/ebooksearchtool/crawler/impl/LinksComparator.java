package org.ebooksearchtool.crawler.impl;

import java.util.*;
import java.net.*;

class LinksComparator implements Comparator<URI> {

    public int compare(URI a, URI b) {
        return linkValue(b) - linkValue(a);
    }
    
    
    String[] badSites = new String[] {"facebook", "wikipedia", "tumblr", "rutube"};
    
    private int linkValue(URI uri) {
        String s = uri.toString();
        for (String badSite : badSites) {
            if (s.indexOf(badSite) >= 0) return -100;
        }
        if (uri.toString().indexOf("epub") >= 0) return 10;
        if (uri.toString().indexOf("book") >= 0) return 5;
        return 0;
    }
    
}
