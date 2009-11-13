package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import org.ebooksearchtool.crawler.AbstractLinksQueue;

public class LinksQueue extends AbstractLinksQueue {
    
    private final LinksComparator myLinksComparator = new LinksComparator();
    private final SortedSet<URI> mySet;
    private final int myMaxSize;
    
    public LinksQueue(int maxSize) {
        myMaxSize = maxSize;
        mySet = new TreeSet<URI>(myLinksComparator);
    }
    
    public synchronized boolean offer(URI uri) {
        if (mySet.size() < myMaxSize) {
            mySet.add(uri);
            notify();
            return true;
        } else {
            URI last = mySet.last();
            if (myLinksComparator.compare(uri, last) < 0) {
                mySet.remove(last);
                mySet.add(uri);
                notify();
                return true;
            }
        }
        return false;
    }
    
    public synchronized URI poll() {
        URI uri = null;
        try {
            uri = mySet.first();
        } catch (NoSuchElementException e) {
            try {
                wait();
            } catch (InterruptedException ie) { }
            try {
                uri = mySet.first();
            } catch (NoSuchElementException ee) { }
        }
        if (uri != null) {
            mySet.remove(uri);
        }
        return uri;
    }
    
    public boolean isEmpty() {
        return mySet.isEmpty();
    }
    
    public int size() {
        return mySet.size();
    }
    
}
