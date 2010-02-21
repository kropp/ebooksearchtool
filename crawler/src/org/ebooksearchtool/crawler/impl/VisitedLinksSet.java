package org.ebooksearchtool.crawler.impl;

import java.util.Map;
import java.util.HashMap;

import java.util.BitSet;
import java.util.Random;
import org.ebooksearchtool.crawler.AbstractVisitedLinksSet;
import org.ebooksearchtool.crawler.Link;

public class VisitedLinksSet extends AbstractVisitedLinksSet {
    
public String DEBUG() { return
"VisitedLinksSet:\n" +
"Map<String, Integer> myHostCount: " + myHostCount.size() + "\n" +
"Map<String, Integer> mySLDCount: " + mySLDCount.size() + "\n" +
"BitSet myBitSet: " + myBitSet.size() + "\n"; }

    private final int myMaxLinksFromHost;
    private final int myMaxLinksFromLargeSource;
    private final int myMaxLinksFromSecondLevelDomain;
    private final long myHostStatsCleanupPeriod;
    
    private Map<String, Integer> myHostCount;
    private Map<String, Integer> mySLDCount;
    private long myLastCleanupTime = 0;
    
    private final BitSet myBitSet;
    private final int myMaxNumberOfElements;
    private final int myHashCount = 10;
    private final int myMaxSize = 1 << 30;
    
    private int myNumberOfElements = 0;
    private int mySize;
    private long[] myHashBases;
    
    public VisitedLinksSet(int maxNumberOfElements, int maxLinksFromHost, int maxLinksFromLargeSource, int maxLinksFromSecondLevelDomain, long hostStatsCleanupPeriod) {
        myMaxNumberOfElements = maxNumberOfElements;
        myMaxLinksFromHost = maxLinksFromHost;
        myMaxLinksFromLargeSource = maxLinksFromLargeSource;
        myMaxLinksFromSecondLevelDomain = maxLinksFromSecondLevelDomain;
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
    
    private boolean add(Link link) {
        if (myNumberOfElements == myMaxNumberOfElements) {
            return false;
        }
        String s = link + "";
        for (int i = 0; i < myHashCount; i++) {
            long h = hash(s, myHashBases[i]);
            myBitSet.set((int)(Math.abs(h) % mySize));
        }
        myNumberOfElements++;
        
        String host = link.getHost();
        Integer x = myHostCount.get(host);
        if (x == null) x = 0;
        myHostCount.put(host, x + 1);
        
        String sld = link.getSecondLevelDomain();
        x = mySLDCount.get(sld);
        if (x == null) x = 0;
        mySLDCount.put(sld, x + 1);
        return true;
    }
    
    public synchronized boolean contains(Link link) {
        String s = link + "";
        for (int i = 0; i < myHashCount; i++) {
            long h = hash(s, myHashBases[i]);
            if (!myBitSet.get((int)(Math.abs(h) % mySize))) {
                return false;
            }
        }
        return true;
    }
    
    public synchronized boolean addIfNotContains(Link link, boolean isLargeSource, boolean isGoodSite) {
        if (contains(link)) {
            return false;
        }
        long now = System.currentTimeMillis();
        if (myLastCleanupTime + myHostStatsCleanupPeriod < now) {
            myLastCleanupTime = now;
            myHostCount = new HashMap<String, Integer>();
            mySLDCount = new HashMap<String, Integer>();
        }
        int maxLinks = isLargeSource ? myMaxLinksFromLargeSource : myMaxLinksFromHost;
        Integer h = myHostCount.get(link.getHost());
        Integer s = mySLDCount.get(link.getSecondLevelDomain());
        if (!isGoodSite && (h != null && h >= maxLinks || s != null && s >= myMaxLinksFromSecondLevelDomain)) {
            return false;
        }
        return add(link);
    }
    
    public int size() {
        return myNumberOfElements;
    }
    
}
