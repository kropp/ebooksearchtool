package org.ebooksearchtool.crawler;

import java.net.URI;

public abstract class AbstractLinksQueue {

    public abstract void offer(URI s);
    
    public abstract URI poll();
    
    public abstract boolean isEmpty();
    
    public abstract int size();
    
}
