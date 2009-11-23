package org.ebooksearchtool.crawler;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

class CrawlerThread extends Thread {
    
    private final Crawler myCrawler;
    private final int myIndex;
    
    private String myAction;
    private boolean myStopping = false;
    private boolean myWaitingForQueue = false;
    private URI myDownloadingURI = null;
    private boolean myDoNotInterruptInAnyCase = false;
    
    CrawlerThread(Crawler crawler, int index) {
        myCrawler = crawler;
        myIndex = index;
    }
    
    public String getAction() {
        return myAction;
    }
    
    public boolean isWaitingForQueue() {
        return myWaitingForQueue;
    }
    
    public URI getDownloadingURI() {
        return myDownloadingURI;
    }
    
    public void setDoNotInterruptInAnyCase(boolean doNotInterruptInAnyCase) {
        myDoNotInterruptInAnyCase = doNotInterruptInAnyCase;
    }
    
    public boolean getDoNotInterruptInAnyCase() {
        return myDoNotInterruptInAnyCase;
    }
    
    private static class URICrawlingOrderComparator implements Comparator<URI> {
        private Set<String> myOtherThreadsHosts;
        
        public void setOtherThreadsHosts(Set<String> otherThreadsHosts) {
            myOtherThreadsHosts = otherThreadsHosts;
        }
        
        public int compare(URI first, URI second) {
            if (first == null) {
                return second == null ? 0 : 1;
            } else if (second == null) {
                return -1;
            }
            boolean firstBusy = myOtherThreadsHosts.contains(first);
            boolean secondBusy = myOtherThreadsHosts.contains(second);
            if (firstBusy) {
                return secondBusy ? 0 : 1;
            } else if (secondBusy) {
                return -1;
            }
            return 0;
        }
    }
    
    public void run() {
        myAction = "preparing";
        final AbstractLinksQueue queue = myCrawler.getQueue();
        final AbstractVisitedLinksSet visited = myCrawler.getVisited();
        final AbstractRobotsExclusion robots = myCrawler.getRobots();
        final Network network = myCrawler.getNetwork();
        final Logger logger = myCrawler.getLogger();
        final int maxLinksFromPage = myCrawler.getMaxLinksFromPage();
        final URICrawlingOrderComparator myCrawlingOrderComparator = new URICrawlingOrderComparator();
        while (true) {
            myAction = "taking an URI from the queue";
            URI uri = null;
            myWaitingForQueue = true;
            while (uri == null) {
                myCrawlingOrderComparator.setOtherThreadsHosts(myCrawler.getThreadsHosts());
                uri = queue.poll();
                synchronized (queue) {
                    if (uri != null) {
                        myWaitingForQueue = false;
                        break;
                    }
                }
                if (myCrawler.allThreadsAreWaitingForQueue()) break;
            }
            if (uri == null) break;
            myAction = "downloading the page at: " + uri;
            myDownloadingURI = uri;
            String page = network.download(uri, "text/html", true, myIndex);
            myDownloadingURI = null;
            if (myStopping) break;
            if (page == null) continue;
            logger.log(Logger.MessageType.CRAWLED_PAGES, String.format("% 4d %d %s %d", myIndex, myCrawler.getCrawledPagesNumber(), uri, page.length()));
            myAction = "getting links out of: " + uri;
            List<URI> links = HTMLParser.parseLinks(uri, page);
            int canAddMoreLinks = maxLinksFromPage;
            if (myStopping) break;
            for (URI link : links) {
                if (canAddMoreLinks == 0) break;
                myAction = "normalizing link: " + link;
                link = Util.normalize(link);
                if (myStopping) break;
                if (link == null) continue;
                myAction = "adding the link to the set: " + link;
                boolean success = visited.addIfNotContains(link);
                if (success) {
                    myAction = "checking if link is probably a book: " + link;
                    if (Util.isBook(link)) {
                        myAction = "writing information about a book: " + link;
                        myCrawler.writeBookToOutput(link, uri, page);
                        logger.log(Logger.MessageType.FOUND_BOOKS, String.format("% 4d  book #%d: %s", myIndex, myCrawler.getFoundBooksNumber(), link));
                    } else {
                        myAction = "checking if i can go to: " + link;
                        myDownloadingURI = link;
                        boolean permitted = robots.canGo(link);
                        myDownloadingURI = null;
                        if (myStopping) break;
                        if (permitted) {
                            myAction = "adding the link to the queue: " + link;
                            if (queue.offer(link)) {
                                canAddMoreLinks--;
                            }
                        }
                    }
                }
            }
            if (myStopping) break;
        }
        myAction = "finished";
//        System.out.print("#" + myIndex + " ");
    }
    
    public void finish() {
        myStopping = true;
    }
    
}
