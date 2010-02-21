package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.impl.*;

public class Crawler implements Runnable {

    private static Network ourNetwork;

    private static boolean ourAnalyzerEnabled;
    private static int ourAnalyzerPort;
    
    private static int ourMaxLinksCount;
    private static int ourMaxLinksFromPage;
    private static int ourMaxQueueSize;
    private static int ourThreadsCount;
    private static int ourThreadTimeoutForLink;
    private static int ourThreadFinishTime;
    
    private final Socket myAnalyzerSocket;
    private final BufferedWriter myAnalyzerWriter;
    private final PrintWriter myOutput;
    private final String[] myStarts;
    
    private final AbstractRobotsExclusion myRobots;
    private final AbstractLinksQueue myQueue;
    private final AbstractVisitedLinksSet myVisited;
    private final Logger myLogger;
    
    private final CrawlerThread[] myThread;
    
    Crawler(Properties properties, String[] starts, PrintWriter output) {
        myOutput = output;
        myStarts = starts;
        try {
            Proxy proxy;
            if (Boolean.parseBoolean(properties.getProperty("proxy_enabled"))) {
                String type = properties.getProperty("proxy_type").toLowerCase();
                Proxy.Type proxyType = null;
                if ("http".equals(type)) {
                    proxyType = Proxy.Type.HTTP;
                } else if ("socks".equals(type)) {
                    proxyType = Proxy.Type.SOCKS;
                }
                String proxyHost = properties.getProperty("proxy_host");
                String proxyPort = properties.getProperty("proxy_port");
                proxy = new Proxy(proxyType, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
            } else {
                proxy = Proxy.NO_PROXY;
            }
            if (Boolean.parseBoolean(properties.getProperty("analyzer_enabled"))) {
                String port = properties.getProperty("analyzer_port");
                ourAnalyzerEnabled = true;
                ourAnalyzerPort = Integer.parseInt(port);
            } else {
                ourAnalyzerEnabled = false;
                ourAnalyzerPort = -1;
            }
            String userAgent = properties.getProperty("user_agent");
            int connectionTimeout = Integer.parseInt(properties.getProperty("connection_timeout"));
            int readTimeout = Integer.parseInt(properties.getProperty("read_timeout"));
            int maxLinksCount = Integer.parseInt(properties.getProperty("max_links_count"));
            
            ourMaxLinksCount = maxLinksCount == 0 ? Integer.MAX_VALUE : maxLinksCount;
            int maxLinksFromPage = Integer.parseInt(properties.getProperty("max_links_from_page"));
            ourMaxLinksFromPage = maxLinksFromPage == 0 ? Integer.MAX_VALUE : maxLinksFromPage;
            int maxQueueSize = Integer.parseInt(properties.getProperty("max_queue_size"));
            ourMaxQueueSize = maxQueueSize == 0 ? Integer.MAX_VALUE : maxQueueSize;
            ourThreadsCount = Integer.parseInt(properties.getProperty("threads_count"));
            ourThreadTimeoutForLink = Integer.parseInt(properties.getProperty("thread_timeout_for_link"));
            ourThreadFinishTime = Integer.parseInt(properties.getProperty("thread_finish_time"));
            int waitingForAccessTimeout = Integer.parseInt(properties.getProperty("waiting_for_access_timeout"));
            
            int largeAmountOfBooks = Integer.parseInt(properties.getProperty("large_amount_of_books"));
            int maxLinksFromHost = Integer.parseInt(properties.getProperty("max_links_from_host"));
            int maxLinksFromLargeSource = Integer.parseInt(properties.getProperty("max_links_from_large_source"));
            long hostStatsCleanupPeriod = Long.parseLong(properties.getProperty("host_stats_cleanup_period"));
            
            String[] goodDomainsArray = properties.getProperty("good_domains").split(" +");
            String[] goodSitesArray = properties.getProperty("good_sites").split(" +");
            String[] badSitesArray = properties.getProperty("bad_sites").split(" +");
            if (goodSitesArray.length == 1 && "".equals(goodSitesArray[0])) goodSitesArray = new String[]{};
            if (badSitesArray.length == 1 && "".equals(badSitesArray[0])) badSitesArray = new String[]{};
            Set<String> goodDomains = new HashSet<String>(Arrays.asList(goodDomainsArray));
            List<String> goodSites = Arrays.asList(goodSitesArray);
            List<String> badSites = Arrays.asList(badSitesArray);
            
            boolean logToScreenEnabled = Boolean.parseBoolean(properties.getProperty("log_to_screen"));
            String loggerOutput = properties.getProperty("log_file");
            Map<Logger.MessageType, Boolean> logOptions = new HashMap<Logger.MessageType, Boolean>();
            logOptions.put(Logger.MessageType.DOWNLOADED_ROBOTS_TXT, Boolean.parseBoolean(properties.getProperty("log_downloaded_robots_txt")));
            logOptions.put(Logger.MessageType.CRAWLED_PAGES, Boolean.parseBoolean(properties.getProperty("log_crawled_pages")));
            logOptions.put(Logger.MessageType.FOUND_BOOKS, Boolean.parseBoolean(properties.getProperty("log_found_books")));
            logOptions.put(Logger.MessageType.ERRORS, Boolean.parseBoolean(properties.getProperty("log_errors")));
            logOptions.put(Logger.MessageType.MISC, Boolean.parseBoolean(properties.getProperty("log_misc")));
            
            
            myLogger = new Logger(loggerOutput, logToScreenEnabled, logOptions);
            ourNetwork = new Network(this, proxy, connectionTimeout, readTimeout, waitingForAccessTimeout, userAgent, myLogger);
            myRobots = new ManyFilesRobotsExclusion(ourNetwork, myLogger);
            myQueue = new LinksQueue(ourMaxQueueSize, largeAmountOfBooks, goodDomains, goodSites, badSites);
            myVisited = new VisitedLinksSet(ourMaxLinksCount, maxLinksFromHost, maxLinksFromLargeSource, hostStatsCleanupPeriod);
            myThread = new CrawlerThread[ourThreadsCount];
        } catch (Exception e) {
            throw new RuntimeException("bad format of properties file: " + e.getMessage());
        }
        if (ourAnalyzerEnabled) {
            Socket analyzerSocket = null;
            BufferedWriter analyzerWriter = null;
            try {
                analyzerSocket = new Socket(InetAddress.getLocalHost().getHostName(), ourAnalyzerPort);
                myLogger.log(Logger.MessageType.MISC, " analyzer connected on port " + analyzerSocket.getPort());
                analyzerWriter = new BufferedWriter(new OutputStreamWriter(analyzerSocket.getOutputStream()));
            } catch (IOException e) {
                try {
                    analyzerSocket.close();
                } catch (Exception f) { }
                analyzerSocket = null;
                analyzerWriter = null;
                myLogger.log(Logger.MessageType.ERRORS, " error: connect to analyzer failed, continuing without it");
            }
            myAnalyzerSocket = analyzerSocket;
            myAnalyzerWriter = analyzerWriter;
        } else {
            myAnalyzerSocket = null;
            myAnalyzerWriter = null;
        }
    }

    public static Network getNetwork() {
        return ourNetwork;
    }
    
    public static int getMaxLinksFromPage() {
        return ourMaxLinksFromPage;
    }
    
    AbstractLinksQueue getQueue() {
        return myQueue;
    }
    
    AbstractVisitedLinksSet getVisited() {
        return myVisited;
    }
    
    AbstractRobotsExclusion getRobots() {
        return myRobots;
    }
    
    Logger getLogger() {
        return myLogger;
    }
    
    CrawlerThread getCrawlerThread(int index) {
        return myThread[index];
    }
    

    private volatile long myCrawledPagesNumber = 0;
    public synchronized long getCrawledPagesNumber() {
        return myCrawledPagesNumber++;
    }
    
    private volatile long myFoundBooksNumber = 0;
    public synchronized long getFoundBooksNumber() {
        return myFoundBooksNumber++;
    }
    
    @SuppressWarnings("deprecation")
    public void run() {
        for (String start : myStarts) {
            Link link = Util.createLink(start);
            if (myRobots.canGo(link)) {
                myVisited.addIfNotContains(link, false, true);
                myQueue.offer(link);
            }
        }
        if (myOutput != null) {
            myOutput.println("<books>");
        }
        
        
        for (int i = 0; i < ourThreadsCount; i++) {
            myThread[i] = new CrawlerThread(this, i);
            myThread[i].start();
        }
        
        Link[] downloadingLink = new Link[ourThreadsCount];
        boolean[] toKill = new boolean[ourThreadsCount];
        try {
            while (true) {
                Thread.sleep(ourThreadTimeoutForLink);
                Arrays.fill(toKill, false);
                boolean killSomeone = false;
                for (int i = 0; i < ourThreadsCount; i++) {
                    if (downloadingLink[i] != null && downloadingLink[i].equals(myThread[i].getDownloadingLink()) && !myThread[i].getDoNotInterruptInAnyCase()) {
                        myLogger.log(Logger.MessageType.ERRORS, " thread #" + i + " is waiting too long for: " + downloadingLink[i]);
                        myThread[i].interrupt();
                        myThread[i].finish();
                        toKill[i] = true;
                        killSomeone = true;
                    }
                    downloadingLink[i] = myThread[i].getDownloadingLink();
                }
                if (!killSomeone) continue;
                Thread.sleep(ourThreadFinishTime);
                for (int i = 0; i < ourThreadsCount; i++) {
                    if (toKill[i]) {
                        if (myThread[i].isAlive()) {
                            myThread[i].stop();
                        }
                        myThread[i] = new CrawlerThread(this, i);
                        myThread[i].start();
                        downloadingLink[i] = null;
                        myLogger.log(Logger.MessageType.ERRORS, " thread #" + i + " is restarted");
                    }
                }
            }
        } catch (InterruptedException e) { }
        
        for (int i = 0; i < ourThreadsCount; i++) {
            myThread[i].finish();
        }
        try {
            Thread.sleep(ourThreadFinishTime);
        } catch (InterruptedException e) { }
        for (int i = 0; i < ourThreadsCount; i++) {
            myThread[i].stop();
        }
        
        if (myOutput != null) {
            myOutput.println("</books>");
        }
        System.out.println();
        System.out.println("finished");
        myLogger.finish();
        ourNetwork.finish();
        if (ourAnalyzerEnabled) {
            try {
                if (myAnalyzerSocket != null) {
                    myAnalyzerWriter.close();
                }
            } catch (IOException e) { }
        }
    }
    
    public boolean dumpCurrentState(String file) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("     queue size = " + myQueue.size() + "\n");
            sb.append("       set size = " + myVisited.size() + "\n");
            sb.append("  pages crawled = " + myCrawledPagesNumber + "\n");
            sb.append("    books found = " + myFoundBooksNumber + "\n");
            for (int i = 0; i < ourThreadsCount; i++) {
                if (myThread[i] != null) {
                    sb.append(String.format("%4d  %s\n", i, myThread[i].getAction()));
                }
            }
sb.append(((LinksQueue)myQueue).DEBUG());
sb.append(((VisitedLinksSet)myVisited).DEBUG());
            System.out.println(sb);
            if (!"".equals(file)) {
                PrintWriter pw = new PrintWriter(file);
                pw.println(sb);
                pw.close();
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    synchronized void writeBookToOutput(Link source, Link referrer, String referrerPage) {
        String link = source.toString().replaceAll("&", "&amp;");
        String from = referrer.toString().replaceAll("&", "&amp;");
        if (myOutput != null) {
            myOutput.println("\t<book>");
            myOutput.println("\t\t<link src=\"" + link + "\" />");
            myOutput.println("\t\t<referrer src=\"" + from + "\" />");
            myOutput.println("\t</book>");
            myOutput.flush();
        }
        if (ourAnalyzerEnabled) {
            try {
                if (myAnalyzerSocket != null) {
                    referrerPage = referrerPage.replaceAll("]]>", "]]]]><![CDATA[>");
                    myAnalyzerWriter.write("<root>"); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write("\t<link src=\"" + link + "\" />"); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write("\t<![CDATA["); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write(referrerPage); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write("\t]]>"); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write("</root>");
                    myAnalyzerWriter.flush();
                }
            } catch (IOException e) {
                myLogger.log(Logger.MessageType.ERRORS, " error: output to analyzer failed, " + link.hashCode() % 1000);
                myLogger.log(Logger.MessageType.ERRORS, " " + e.getMessage());
            }
        }
    }
    
    boolean allThreadsAreWaitingForQueue() {
        for (int i = 0; i < ourThreadsCount; i++) {
            if (!myThread[i].isWaitingForQueue()) {
                return false;
            }
        }
        return true;
    }
    
}
