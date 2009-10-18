package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import org.ebooksearchtool.crawler.*;

public class VisitedLinksSet extends AbstractVisitedLinksSet {

    private final Set<URI> mySet = new HashSet<URI>();
    
    public void add(URI uri) {
        mySet.add(uri);
    }
    
    public boolean contains(URI uri) {
        return mySet.contains(uri);
    }
    
    public boolean contains(Collection<? extends URI> c) {
        for (URI uri : c) {
            if (contains(uri)) {
                return true;
            }
        }
        return false;
    }
    
    public int size() {
        return mySet.size();
    }
    
}
