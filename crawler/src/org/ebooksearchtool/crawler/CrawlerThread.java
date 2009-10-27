package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.impl.*;

class CrawlerThread extends Thread {
    
    private final Crawler myCrawler;
    private final int myIndex;
    
    private String myAction;
    private boolean myStopping = false;
    
    CrawlerThread(Crawler crawler, int index) {
        myCrawler = crawler;
        myIndex = index;
    }
    
    public String getAction() {
        return myAction;
    }
    
    public void run() {
        myAction = "preparing";
        AbstractLinksQueue queue = myCrawler.getQueue();
        AbstractVisitedLinksSet visited = myCrawler.getVisited();
        AbstractRobotsExclusion robots = myCrawler.getRobots();
        Network network = myCrawler.getNetwork();
        while (true) {
            myAction = "taking an URI from the queue";
            URI uri = queue.poll();
            if (uri == null) break;
            myAction = "downloading the page at: " + uri;
            String page = network.download(uri, "text/html");
            if (myStopping) break;
            if (page == null) continue;
            Logger.log(String.format("% 4d %d %s %d", myIndex, myCrawler.getCounter(), uri, page.length()));
            myAction = "getting links out of: " + uri;
            List<URI> links = HTMLParser.parseLinks(uri, page, myCrawler.getMaxLinksFromPage());
            if (myStopping) break;
            for (URI link : links) {
                myAction = "creating similar links to: " + link;
                Collection<URI> similarLinks = Util.createSimilarLinks(link);
                if (myStopping) break;
                myAction = "adding the link to the set: " + link;
                boolean success = visited.addIfNotContains(similarLinks, link);
                if (success) {
                    myAction = "checking if link is probably a book: " + link;
                    if (Util.isBook(link)) {
                        myAction = "writing information about a book: " + link;
                        myCrawler.writeBookToOutput(link, uri, page);
                    } else {
                        myAction = "checking if i can go to: " + link;
                        boolean permitted = robots.canGo(link);
                        if (myStopping) break;
                        if (permitted) {
                            myAction = "adding the link to the queue: " + link;
                            queue.offer(link);
                        }
                    }
                }
            }
            if (myStopping) break;
        }
        myAction = "finished";
        System.out.print("#" + myIndex + " ");
    }
    
    public void finish() {
        myStopping = true;
    }
    
}
