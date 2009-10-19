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
            if (isInterrupted()) break;
            System.out.println(String.format("% 4d %d %s %d", myIndex, myCrawler.getCounter(), uri, page.length()));
            List<URI> links = HTMLParser.parseLinks(uri, page);
            for (URI link : links) {
                Collection<URI> similarLinks = Util.createSimilarLinks(link);
                if (isInterrupted()) break;
                boolean success = visited.addIfNotContains(similarLinks, link);
                if (success) {
                    if (Util.isBook(link)) {
                        myCrawler.writeBookToOutput(link, uri, page);
                    } else {
                        boolean permitted = robots.canGo(link);
                        if (permitted) {
                            queue.offer(link);
                        }
                    }
                }
            }
        }
        System.out.println("thread #" + myIndex + " finished");
    }
    
}
