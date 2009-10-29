package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import org.ebooksearchtool.crawler.AbstractLinksQueue;

public class LinksQueue extends AbstractLinksQueue {

    private final BlockingQueue<URI> myQueue = new LinkedBlockingQueue<URI>();
    private final int myMaxSize;
    
    public LinksQueue(int maxSize) {
        myMaxSize = maxSize;
    }
    
    public void offer(URI uri) {
        if (myQueue.size() < myMaxSize) {
            try {
                myQueue.put(uri);
            } catch (InterruptedException e) { }
        }
    }
    
    public URI poll() {
        try {
            return myQueue.take();
        } catch (InterruptedException e) {
            return null;
        }
    }
    
    public boolean isEmpty() {
        return myQueue.isEmpty();
    }
    
    public int size() {
        return myQueue.size();
    }
    
}
