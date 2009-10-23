package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import org.ebooksearchtool.crawler.AbstractVisitedLinksSet;

public class VisitedLinksSet extends AbstractVisitedLinksSet {

    private final Set<URI> mySet = Collections.synchronizedSet(new HashSet<URI>());
    private final int myMaxSize;
    
    public VisitedLinksSet(int maxSize) {
        myMaxSize = maxSize;
    }
    
    public boolean add(URI uri) {
        if (mySet.size() == myMaxSize) {
            return false;
        }
        mySet.add(uri);
        return true;
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
    
    public synchronized boolean addIfNotContains(Collection<? extends URI> c, URI uri) {
        if (contains(c)) {
            return false;
        }
        return add(uri);
    }

}
