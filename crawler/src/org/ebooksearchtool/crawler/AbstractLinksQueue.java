package org.ebooksearchtool.crawler;

import java.net.URI;

public abstract class AbstractLinksQueue {

    public abstract boolean offer(URI s);
    
    public abstract URI poll();
    
    public abstract boolean isEmpty();
    
    public abstract int size();
    
    
    public abstract void hostHasOneMoreBook(String host);
    
    public abstract boolean isLargeSource(String host);
    
}
