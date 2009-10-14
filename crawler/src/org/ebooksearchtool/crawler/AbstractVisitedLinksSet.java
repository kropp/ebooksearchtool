package org.ebooksearchtool.crawler;

import java.util.Collection;

public abstract class AbstractVisitedLinksSet {
    
    public abstract void add(String s);
    
    public abstract boolean contains(String s);
    
    /* returns true iff at least one element of the collection is present in the set */
    public abstract boolean contains(Collection<? extends String> c);
    
    public abstract int size();
    
}
