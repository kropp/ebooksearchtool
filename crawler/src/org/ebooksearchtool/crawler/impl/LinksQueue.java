package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import org.ebooksearchtool.crawler.AbstractLinksQueue;

public class LinksQueue extends AbstractLinksQueue {
    
    private final LinksComparator myLinksComparator = new LinksComparator();
    private final Object myLock = new Object();
    private final SortedSet<URI> mySet;
    private final int myMaxSize;
    
    public LinksQueue(int maxSize) {
        myMaxSize = maxSize;
        mySet = new TreeSet<URI>(myLinksComparator);
    }
    
    public boolean offer(URI uri) {
        boolean answer;
        synchronized (myLock) {
            if (mySet.size() < myMaxSize) {
                answer = mySet.add(uri);
            } else {
                URI last = mySet.last();
                if (myLinksComparator.compare(uri, last) < 0) {
                    mySet.remove(last);
                    answer = mySet.add(uri);
                } else {
                    answer = false;
                }
            }
        }
        return answer;
    }
    
    public URI poll() {
        URI uri = null;
        while (true) {
            try {
                synchronized (myLock) {
                    uri = mySet.first();
                    mySet.remove(uri);
                }
            } catch (NoSuchElementException e) {
                break;
            }
            break;
        }
        return uri;
    }
    
    public boolean isEmpty() {
        synchronized (myLock) {
            return mySet.isEmpty();
        }
    }
    
    public int size() {
        synchronized (myLock) {
            return mySet.size();
        }
    }
    
}
