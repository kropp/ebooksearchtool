package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.impl.*;

class CrawlerThread extends Thread {
    
    private final Crawler myCrawler;
    private final int myIndex;
    
    CrawlerThread(Crawler crawler, int index) {
        myCrawler = crawler;
        myIndex = index;
    }
    
    public void run() {
        AbstractLinksQueue queue = myCrawler.getQueue();
        AbstractVisitedLinksSet visited = myCrawler.getVisited();
        AbstractRobotsExclusion robots = myCrawler.getRobots();
        while (true) {
            URI uri = queue.poll();
            if (uri == null) break;
            String page = myCrawler.getPage(uri);
            if (page == null) continue;
            
            System.out.printf("% 4d %d %s %d\n", myIndex, myCrawler.getCounter(), uri, page.length());
            List<URI> links = HTMLParser.parseLinks(uri, page);
            for (URI link : links) {
                Collection<URI> similarLinks = Util.createSimilarLinks(link);
                boolean success = visited.addIfNotContains(similarLinks, link);
                if (success) {
                    if (Util.isBook(link)) {
                        myCrawler.writeBookToOutput(link, uri, page);
                        continue;
                    }
                    boolean permitted = robots.canGo(link);
                    if (permitted) {
                        queue.offer(link);
                    }
                }
            }
        }
    }
    
}
