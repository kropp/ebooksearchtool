package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import org.ebooksearchtool.crawler.*;

public class LinksQueue extends AbstractLinksQueue {

    private final BlockingQueue<URI> myQueue = new LinkedBlockingQueue<URI>();
    
    public void offer(URI uri) {
        try {
            myQueue.put(uri);
        } catch (InterruptedException e) { }
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
    
}
