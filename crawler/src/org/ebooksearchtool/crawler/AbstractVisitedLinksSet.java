package org.ebooksearchtool.crawler;

import java.net.URI;

public abstract class AbstractVisitedLinksSet {
    
    public abstract boolean contains(URI uri);
    
    public abstract boolean addIfNotContains(URI uri, boolean isLargeSource, boolean isGoodSite);
    
    public abstract int size();
    
}
