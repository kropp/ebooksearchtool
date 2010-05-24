package org.ebooksearchtool.crawler;

public abstract class AbstractLinksQueue {

    public abstract boolean offer(Link s);
    
    public abstract Link poll() throws InterruptedException;
    
    public abstract boolean isEmpty();
    
    public abstract int size();
    
    
    public abstract void hostHasOneMoreBook(String host);
    
    public abstract boolean isLargeSource(String host);
    public abstract boolean isGoodSite(Link link);
    
}
