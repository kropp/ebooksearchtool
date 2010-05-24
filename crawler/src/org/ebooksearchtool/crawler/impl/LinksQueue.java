package org.ebooksearchtool.crawler.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Iterator;
import org.ebooksearchtool.crawler.AbstractLinksQueue;
import org.ebooksearchtool.crawler.Link;

public class LinksQueue extends AbstractLinksQueue {

public String DEBUG() { return
"LinksQueue:\n" + 
"SortedSet<URI> mySet: " + mySet.size() + "\n" +
"Map<String, Integer> myHasBooks: " + myHasBooks.size() + "\n" +
"Map<String, Set<URI>> myLinksFromHost: " + myLinksFromHost.size() + "\n"; }

    private final long delay = 2000;
    private final int myMaxPageCountFromLargeSource;
    private final int myMaxPageCountFromOtherSource;
    
    private final int myLargeAmountOfBooks;
    
    private final LinksComparator myLinksComparator;
    private final TreeSet<Link> mySet;
    private final int myMaxSize;
    private final Map<String, Integer> myHasBooks; ///TODO: some different data structure?..
    private final Map<String, Set<Link>> myLinksFromHost;
    private final Random myRandom;

    private final Map<String, Integer> myHostPageCounter;
    private final Map<String, Long> lastVisitTime;
    private final Map<String, HostData> myHostDataMap;

    class HostData {
        public int pageCount = 0;
        public long lastVisitTime= 0;
    }

   
    public LinksQueue(int maxSize, int largeAmountOfBooks, Set<String> goodDomains, Collection<String> goodSites, Collection<String> badSites) {
        myMaxSize = maxSize;
        myMaxPageCountFromLargeSource = maxSize / 10;
        myMaxPageCountFromOtherSource = maxSize / 20;
        
        myLargeAmountOfBooks = largeAmountOfBooks;
        myHasBooks = new HashMap<String, Integer>();
        myLinksComparator = new LinksComparator(myHasBooks, goodDomains, goodSites, badSites);
        mySet = new TreeSet<Link>(myLinksComparator);
        myLinksFromHost = new HashMap<String, Set<Link>>();
        myRandom = new Random();

        lastVisitTime = new HashMap<String, Long>();
        myHostPageCounter = new HashMap<String, Integer>();
        myHostDataMap = new HashMap<String, HostData>();
    }
    
    private void remove(Link link) {
//        String host = link.getHost();
//        Set<Link> set = myLinksFromHost.get(host);
//        set.remove(link);
//        if (set.isEmpty()) {
//            myLinksFromHost.remove(host);
//        }
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

        String ahost = link.getHost();

        if (add) {
            // check if the pages from host in the queue less than limit
            HostData hostData = myHostDataMap.get(ahost);
            if (hostData == null) {
                hostData = new HostData();
                hostData.pageCount = 1;
                myHostDataMap.put(ahost, hostData);
            } else {
                int limit = 0;
                if (isLargeSource(ahost))
                    limit = myMaxPageCountFromLargeSource;
                else
                    limit = myMaxPageCountFromOtherSource;
    
                if (hostData.pageCount < limit)
                    hostData.pageCount++;
                else {
                    add = false;
                    System.err.println("REFUSE  count = " + hostData.pageCount + " URI " + link);
                }
            }
        }

        if (add) {
            Set<Link> aset = myLinksFromHost.get(ahost);
            if (aset == null) {
                aset = new TreeSet<Link>();
                myLinksFromHost.put(ahost, aset);
            }
            aset.add(link);
            mySet.add(link);
            
            notify();
    //        System.err.println("queue get " + link);
            return true;
        }
        return false;
    }
    

   /**
    * Return the next old link (the time of last visit this host is less than delay).
    * Or null if there are not the such links.
    * @return the link or null.
    */
    private Link nextLink() {
        for (Iterator<Link> iter = mySet.descendingIterator(); iter.hasNext(); ) {
            final Link link = iter.next();
            final HostData hostData = myHostDataMap.get(link.getHost());
//            System.err.println("delay = " + delay);
//            System.err.println("curre = " + System.currentTimeMillis());
//            System.err.println("hostData.lastVisitTime = " + hostData.lastVisitTime);

            if (System.currentTimeMillis() - hostData.lastVisitTime > delay) {
                // mySet.remove(link);
                remove(link);
                
                hostData.lastVisitTime = System.currentTimeMillis();
                hostData.pageCount--;

                if (hostData.pageCount == 0)
                    myHostDataMap.remove(link.getHost());

                return link;
            }
            System.err.println("TOO FRESH " + link);
        }
        return null;
    }

    public synchronized Link poll() throws InterruptedException {

        if (mySet.isEmpty())
            // TODO throw normal exception here
            throw new RuntimeException();

        Link link = null;
        while (true) {
            link = nextLink();
            if (link != null)
                return link;
            System.err.println("ALL URI's ARE TOO FRESH");
            Thread.sleep(1000);
        }

//        Link link = null;
//        try {
//            link = nextLink();
//        } catch (NoSuchElementException e) {
//            try {
//                // TODO: check if it is needed to wait with delay
//                //wait(2000);
//                wait();
//            } catch (InterruptedException ie) { }
//            try {
//                link = nextLink();
//            } catch (NoSuchElementException ee) {
//            }
//        }
//
//
//        if (link != null) {
//            remove(link);
//
//        }
//        return link;
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

    class LimitLinksQueue {
        private final int myDelay;
        private final int myMaxPageLargeSource;
        private final int myMaxPageOtherSource;
        
        private final SortedSet<Link> myLinkSet;
        private final Map<String, Integer> myHostPageCounter;

        public LimitLinksQueue(int delay, int maxPageLargeSource, int maxPageOtherSource, LinksComparator linksComparator) {
            myDelay = delay;
            myMaxPageLargeSource = maxPageLargeSource;
            myMaxPageOtherSource = maxPageOtherSource;
            
            myLinkSet = new TreeSet<Link>(linksComparator);
            myHostPageCounter = new HashMap<String, Integer>();
        }

//        public size() {
//            return myLinkSet.size();
//        }
//
//        public isEmpty() {
//            return myLinkSet.isEmpty();
//        }



    }
    
}
