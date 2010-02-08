package org.ebooksearchtool.crawler.impl;

import java.util.Map;
import java.util.HashMap;

import java.net.URI;
import java.util.BitSet;
import java.util.Random;
import org.ebooksearchtool.crawler.AbstractVisitedLinksSet;

public class VisitedLinksSet extends AbstractVisitedLinksSet {
    
    private final int myMaxLinksFromHost;
    private final int myMaxLinksFromLargeSource;
    private final long myHostStatsCleanupPeriod;
    
    private Map<String, Integer> myHostCount;
    private long myLastCleanupTime = 0;
    
    private final BitSet myBitSet;
    private final int myMaxNumberOfElements;
    private final int myHashCount = 10;
    private final int myMaxSize = 1 << 30;
    
    private int myNumberOfElements = 0;
    private int mySize;
    private long[] myHashBases;
    
    public VisitedLinksSet(int maxNumberOfElements, int maxLinksFromHost, int maxLinksFromLargeSource, long hostStatsCleanupPeriod) {
        myMaxNumberOfElements = maxNumberOfElements;
        myMaxLinksFromHost = maxLinksFromHost;
        myMaxLinksFromLargeSource = maxLinksFromLargeSource;
        myHostStatsCleanupPeriod = hostStatsCleanupPeriod;
        long size = 15L * maxNumberOfElements; // some empiric results
        if (size > myMaxSize) {
            size = myMaxSize;
        }
        mySize = (int)size;
        myBitSet = new BitSet(mySize);
        myHashBases = new long[myHashCount];
        Random rnd = new Random();
        for (int i = 0; i < myHashCount; i++) {
            while (myHashBases[i] % 2 == 0) {
                myHashBases[i] = rnd.nextLong();
            }
        }
    }
    
    private long hash(String s, long base) {
        int n = s.length();
        long answer = 0;
        for (int i = 0; i < n; i++) {
            answer = (answer * base + s.charAt(i));
        }
        return answer;
    }
    
    private boolean add(URI uri) {
        if (myNumberOfElements == myMaxNumberOfElements) {
            return false;
        }
        String s = uri.toString();
        for (int i = 0; i < myHashCount; i++) {
            long h = hash(s, myHashBases[i]);
            myBitSet.set((int)(Math.abs(h) % mySize));
        }
        myNumberOfElements++;
        Integer thisCount = myHostCount.get(uri.getHost());
        if (thisCount == null) {
            thisCount = 0;
        }
        myHostCount.put(uri.getHost(), thisCount + 1);
        return true;
    }
    
    public synchronized boolean contains(URI uri) {
        String s = uri.toString();
        for (int i = 0; i < myHashCount; i++) {
            long h = hash(s, myHashBases[i]);
            if (!myBitSet.get((int)(Math.abs(h) % mySize))) {
                return false;
            }
        }
        return true;
    }
    
    public synchronized boolean addIfNotContains(URI uri, boolean isLargeSource, boolean isGoodSite) {
        if (contains(uri)) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (myLastCleanupTime + myHostStatsCleanupPeriod < now) {
            myLastCleanupTime = now;
            myHostCount = new HashMap<String, Integer>();
        }
        int maxLinks = isLargeSource ? myMaxLinksFromLargeSource : myMaxLinksFromHost;
        Integer thisCount = myHostCount.get(uri.getHost());
        if (!isGoodSite && thisCount != null && thisCount > maxLinks) {
            return false;
        }
        return add(uri);
    }
    
    public int size() {
        return myNumberOfElements;
    }
    
}
