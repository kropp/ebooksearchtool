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
    
    private CrawlerThread[] myThread;
    
    Crawler(Properties properties, String[] starts, PrintWriter output) {
        myOutput = output;
        myStarts = starts;
        try {
            Proxy proxy;
            if ("true".equals(properties.getProperty("proxy_enabled"))) {
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
            if ("true".equals(properties.getProperty("analyzer_enabled"))) {
                String port = properties.getProperty("analyzer_port");
                ourAnalyzerEnabled = true;
                ourAnalyzerPort = Integer.parseInt(port);
            } else {
                ourAnalyzerEnabled = false;
                ourAnalyzerPort = -1;
            }
            int connectionTimeout = Integer.parseInt(properties.getProperty("connection_timeout"));
            String userAgent = properties.getProperty("user_agent");
            int maxLinksCount = Integer.parseInt(properties.getProperty("max_links_count"));
            ourMaxLinksCount = maxLinksCount == 0 ? Integer.MAX_VALUE : maxLinksCount;
            int maxLinksFromPage = Integer.parseInt(properties.getProperty("max_links_from_page"));
            ourMaxLinksFromPage = maxLinksFromPage == 0 ? Integer.MAX_VALUE : maxLinksFromPage;
            int maxQueueSize = Integer.parseInt(properties.getProperty("max_queue_size"));
            ourMaxQueueSize = maxQueueSize == 0 ? Integer.MAX_VALUE : maxQueueSize;
            ourThreadsCount = Integer.parseInt(properties.getProperty("threads_count"));
            ourThreadTimeoutForLink = Integer.parseInt(properties.getProperty("thread_timeout_for_link"));
            ourThreadFinishTime = Integer.parseInt(properties.getProperty("thread_finish_time"));
            boolean logToScreenEnabled = "true".equals(properties.getProperty("log_to_screen"));
            String loggerOutput = properties.getProperty("log_file");
            Map<Logger.MessageType, Boolean> logOptions = new HashMap<Logger.MessageType, Boolean>();
            logOptions.put(Logger.MessageType.DOWNLOADED_ROBOTS_TXT, "true".equals(properties.getProperty("log_downloaded_robots_txt")));
            logOptions.put(Logger.MessageType.CRAWLED_PAGES, "true".equals(properties.getProperty("log_crawled_pages")));
            logOptions.put(Logger.MessageType.FOUND_BOOKS, "true".equals(properties.getProperty("log_found_books")));
            logOptions.put(Logger.MessageType.ERRORS, "true".equals(properties.getProperty("log_errors")));
            logOptions.put(Logger.MessageType.MISC, "true".equals(properties.getProperty("log_misc")));
            
            myLogger = new Logger(loggerOutput, logToScreenEnabled, logOptions);
            ourNetwork = new Network(proxy, connectionTimeout, userAgent, myLogger);
            myRobots = new ManyFilesRobotsExclusion(ourNetwork, myLogger);
            myQueue = new LinksQueue(ourMaxQueueSize);
            myVisited = new VisitedLinksSet(ourMaxLinksCount);
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
                } catch (IOException f) { }
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
    

    private long myCrawledPagesNumber = 0;
    public synchronized long getCrawledPagesNumber() {
        return myCrawledPagesNumber++;
    }
    
    private long myFoundBooksNumber = 0;
    public synchronized long getFoundBooksNumber() {
        return myFoundBooksNumber++;
    }
    
    @SuppressWarnings("deprecation")
    public void run() {
        for (String start : myStarts) {
            URI uri = Util.createURI(start);
            if (myRobots.canGo(uri)) {
                myVisited.add(uri);
                myQueue.offer(uri);
            }
        }
        myOutput.println("<books>");
        
        
        myThread = new CrawlerThread[ourThreadsCount];
        for (int i = 0; i < ourThreadsCount; i++) {
            myThread[i] = new CrawlerThread(this, i);
            myThread[i].start();
        }
        
        URI[] downloadingURI = new URI[ourThreadsCount];
        try {
            while (true) {
                Thread.sleep(ourThreadTimeoutForLink);
                for (int i = 0; i < ourThreadsCount; i++) {
                    if (downloadingURI[i] != null && downloadingURI[i].equals(myThread[i].getDownloadingURI())) {
                        myLogger.log(Logger.MessageType.ERRORS, " thread #" + i + " is waiting too long for: " + downloadingURI[i]);
                        myThread[i].finish();
                        Thread.sleep(ourThreadFinishTime);
                        if (myThread[i].isAlive()) {
                            myThread[i].stop();
                        }
                        myThread[i] = new CrawlerThread(this, i);
                        myThread[i].start();
                        myLogger.log(Logger.MessageType.ERRORS, " thread #" + i + " restarted");
                    }
                    downloadingURI[i] = myThread[i].getDownloadingURI();
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
        
        myRobots.finish();
        myOutput.println("</books>");
        System.out.println();
        System.out.println("finished");
        myLogger.finish();
        if (ourAnalyzerEnabled) {
            try {
                if (myAnalyzerSocket != null) {
                    myAnalyzerWriter.close();
                }
            } catch (IOException e) { }
        }
    }
    
    public boolean dumpCurrentState(File file) {
        try {
            StringBuffer sb = new StringBuffer();
            sb.append("     queue size = " + myQueue.size() + "\n");
            sb.append("       set size = " + myVisited.size() + "\n");
            sb.append("  pages crawled = " + myCrawledPagesNumber + "\n");
            sb.append("    books found = " + myFoundBooksNumber + "\n");
            for (int i = 0; i < ourThreadsCount; i++) {
                sb.append(String.format("%4d  %s\n", i, myThread[i].getAction()));
            }
            System.out.println(sb);
            PrintWriter pw = new PrintWriter(file);
            pw.println(sb);
            pw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    
    synchronized void writeBookToOutput(URI source, URI referrer, String referrerPage) {
        String link = source.toString().replaceAll("&", "&amp;");
        String from = referrer.toString().replaceAll("&", "&amp;");
        myOutput.println("\t<book>");
        myOutput.println("\t\t<link src=\"" + link + "\" />");
        myOutput.println("\t\t<referrer src=\"" + from + "\" />");
        myOutput.println("\t</book>");
        myOutput.flush();
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
                myLogger.log(Logger.MessageType.ERRORS, " error: output to analyzer failed, " + link.hashCode());
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
