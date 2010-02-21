package org.ebooksearchtool.crawler.impl;

import java.util.*;
import org.ebooksearchtool.crawler.AbstractLinksQueue;
import org.ebooksearchtool.crawler.Link;

public class LinksQueue extends AbstractLinksQueue {

public String DEBUG() { return
"LinksQueue:\n" + 
"SortedSet<URI> mySet: " + mySet.size() + "\n" +
"Map<String, Integer> myHasBooks: " + myHasBooks.size() + "\n" +
"Map<String, Set<URI>> myLinksFromHost: " + myLinksFromHost.size() + "\n"; }
    
    private final int myLargeAmountOfBooks;
    
    private final LinksComparator myLinksComparator;
    private final SortedSet<Link> mySet;
    private final int myMaxSize;
    private final Map<String, Integer> myHasBooks; ///TODO: some different data structure?..
    private final Map<String, Set<Link>> myLinksFromHost;
    private final Random myRandom;
    
    public LinksQueue(int maxSize, int largeAmountOfBooks, Set<String> goodDomains, Collection<String> goodSites, Collection<String> badSites) {
        myMaxSize = maxSize;
        myLargeAmountOfBooks = largeAmountOfBooks;
        myHasBooks = new HashMap<String, Integer>();
        myLinksComparator = new LinksComparator(myHasBooks, goodDomains, goodSites, badSites);
        mySet = new TreeSet<Link>(myLinksComparator);
        myLinksFromHost = new HashMap<String, Set<Link>>();
        myRandom = new Random();
    }
    
    private void remove(Link link) {
        String host = link.getHost();
        Set<Link> set = myLinksFromHost.get(host);
        set.remove(link);
        if (set.isEmpty()) {
            myLinksFromHost.remove(host);
        }
        mySet.remove(link);
    }
    
    public synchronized boolean offer(Link link) {
        boolean add = false;
        if (mySet.size() < myMaxSize) {
            add = true;
        } else {
            Link last = mySet.last();
            int cmp = myLinksComparator.compare(link, last);
            // add our link, if it is better than the worst link in the set,
            // or, if it is as good as the worst link, add in 50% cases
            if (cmp < 0 || (cmp == 0 && myRandom.nextBoolean())) {
                remove(last);
                add = true;
            }
        }
        if (add) {
            String ahost = link.getHost();
            Set<Link> aset = myLinksFromHost.get(ahost);
            if (aset == null) {
                aset = new TreeSet<Link>();
                myLinksFromHost.put(ahost, aset);
            }
            aset.add(link);
            mySet.add(link);
            notify();
            return true;
        }
        return false;
    }
    
    public synchronized Link poll() {
        Link link = null;
        try {
            link = mySet.first();
        } catch (NoSuchElementException e) {
            try {
                wait();
            } catch (InterruptedException ie) { }
            try {
                link = mySet.first();
            } catch (NoSuchElementException ee) { }
        }
        if (link != null) {
            remove(link);
        }
        return link;
    }
    
    public boolean isEmpty() {
        return mySet.isEmpty();
    }
    
    public int size() {
        return mySet.size();
    }
    
    
    public synchronized void hostHasOneMoreBook(String host) {
        // we need to restructure mySet, because some links may have now higher priority
        Set<Link> links = myLinksFromHost.get(host);
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
    
    public synchronized boolean isGoodSite(Link link) {
        return myLinksComparator.isGoodSite(link.toString());
    }
    
}
