package org.ebooksearchtool.crawler;

import java.net.URI;
import java.util.Collection;

public abstract class AbstractVisitedLinksSet {
    
    public abstract boolean add(URI uri);
    
    public abstract boolean contains(URI uri);
    
    public abstract boolean addIfNotContains(URI uri);
    
    public abstract int size();
    
}
