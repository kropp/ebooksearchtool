package org.ebooksearchtool.crawler.impl;

import java.util.*;
import org.ebooksearchtool.crawler.*;

public class LinksQueue extends AbstractLinksQueue {

    private final Queue<String> myQueue = new LinkedList<String>();
    
    public void offer(String s) {
        myQueue.offer(s);
    }
    
    public String poll() {
        return myQueue.poll();
    }
    
    public boolean isEmpty() {
        return myQueue.isEmpty();
    }
    
}
