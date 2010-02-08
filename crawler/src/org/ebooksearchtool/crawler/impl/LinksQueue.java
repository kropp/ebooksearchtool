package org.ebooksearchtool.crawler.impl;

import java.net.URI;
import java.util.*;
import org.ebooksearchtool.crawler.AbstractLinksQueue;

public class LinksQueue extends AbstractLinksQueue {
    
    private final int myLargeAmountOfBooks;
    
    private final LinksComparator myLinksComparator;
    private final SortedSet<URI> mySet;
    private final int myMaxSize;
    private final Map<String, Integer> myHasBooks; ///TODO: some different data structure?..
    private final Map<String, Set<URI>> myLinksFromHost;
    private final Random myRandom;
    
    public LinksQueue(int maxSize, int largeAmountOfBooks) {
        myMaxSize = maxSize;
        myLargeAmountOfBooks = largeAmountOfBooks;
        myHasBooks = new HashMap<String, Integer>();
        myLinksComparator = new LinksComparator(myHasBooks);
        mySet = new TreeSet<URI>(myLinksComparator);
        myLinksFromHost = new HashMap<String, Set<URI>>();
        myRandom = new Random();
    }
    
    private void remove(URI uri) {
        String host = uri.getHost();
        Set<URI> set = myLinksFromHost.get(host);
        set.remove(uri);
        if (set.size() == 0) {
            myLinksFromHost.remove(host);
        }
        mySet.remove(uri);
    }
    
    public synchronized boolean offer(URI uri) {
        boolean add = false;
        if (mySet.size() < myMaxSize) {
            add = true;
        } else {
            URI last = mySet.last();
            int cmp = myLinksComparator.compare(uri, last);
            // add our link, if it is better than the worst link in the set,
            // or, if it is as good as the worst link, add in 50% cases
            if (cmp < 0 || (cmp == 0 && myRandom.nextBoolean())) {
                remove(last);
                add = true;
            }
        }
        if (add) {
            String ahost = uri.getHost();
            Set<URI> aset = myLinksFromHost.get(ahost);
            if (aset == null) {
                aset = new TreeSet<URI>();
                myLinksFromHost.put(ahost, aset);
            }
            aset.add(uri);
            mySet.add(uri);
            notify();
            return true;
        }
        return false;
    }
    
    public synchronized URI poll() {
        URI uri = null;
        try {
            uri = mySet.first();
        } catch (NoSuchElementException e) {
            try {
                wait();
            } catch (InterruptedException ie) { }
            try {
                uri = mySet.first();
            } catch (NoSuchElementException ee) { }
        }
        if (uri != null) {
            remove(uri);
        }
        return uri;
    }
    
    public boolean isEmpty() {
        return mySet.isEmpty();
    }
    
    public int size() {
        return mySet.size();
    }
    
    
    public synchronized void hostHasOneMoreBook(String host) {
        // we need to restructure mySet, because some links may have now higher priority
        Set<URI> links = myLinksFromHost.get(host);
        if (links != null) {
            mySet.removeAll(links);
        }
        
        Integer x = myHasBooks.get(host);
        if (x == null) x = 0;
        myHasBooks.put(host, x + 1);
        
        if (links != null) {
            mySet.addAll(links);
        }
    }
    
    public synchronized boolean isLargeSource(String host) {
        Integer x = myHasBooks.get(host);
        return x != null && x > myLargeAmountOfBooks;
    }
    
}
