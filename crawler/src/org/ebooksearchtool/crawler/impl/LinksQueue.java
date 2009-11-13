package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import org.ebooksearchtool.crawler.AbstractLinksQueue;

public class LinksQueue extends AbstractLinksQueue {

    private final Queue<URI> myQueue;
    private final int myMaxSize;
    
    private static int ourTimeToWait = 200;
    
    public LinksQueue(int maxSize) {
        myMaxSize = maxSize;
        myQueue = new PriorityQueue<URI>(maxSize, new LinksComparator());
    }
    
    public synchronized void offer(URI uri) {
        if (myQueue.size() < myMaxSize) {
            myQueue.offer(uri);
        }
    }
    
    public synchronized URI poll() {
        URI uri = myQueue.poll();
        if (uri == null) {
            try {
                Thread.sleep(ourTimeToWait);
            } catch (InterruptedException ie) { }
            uri = myQueue.poll();
        }
        return uri;
    }
    
    public boolean isEmpty() {
        return myQueue.isEmpty();
    }
    
    public int size() {
        return myQueue.size();
    }
    
}
