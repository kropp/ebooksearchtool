package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import org.ebooksearchtool.crawler.*;

public class LinksQueue extends AbstractLinksQueue {

    private final Queue<URI> myQueue = new LinkedList<URI>();
    
    public void offer(URI uri) {
        myQueue.offer(uri);
    }
    
    public URI poll() {
        return myQueue.poll();
    }
    
    public boolean isEmpty() {
        return myQueue.isEmpty();
    }
    
}
