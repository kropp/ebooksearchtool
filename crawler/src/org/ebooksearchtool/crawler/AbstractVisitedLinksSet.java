package org.ebooksearchtool.crawler;

public abstract class AbstractVisitedLinksSet {
    
    public abstract boolean contains(Link link);
    
    public abstract boolean addIfNotContains(Link link, boolean isLargeSource, boolean isGoodSite);
    
    public abstract int size();
    
}
