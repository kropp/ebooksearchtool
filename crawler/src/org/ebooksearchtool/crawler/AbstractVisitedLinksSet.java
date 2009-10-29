package org.ebooksearchtool.crawler;

import java.net.URI;
import java.util.Collection;

public abstract class AbstractVisitedLinksSet {
    
    public abstract boolean add(URI uri);
    
    public abstract boolean contains(URI uri);
    
    /* returns true iff at least one element of the collection is present in the set */
    public abstract boolean contains(Collection<? extends URI> c);
    
    public abstract boolean addIfNotContains(Collection<? extends URI> c, URI uri);
    
    public abstract int size();
    
}
