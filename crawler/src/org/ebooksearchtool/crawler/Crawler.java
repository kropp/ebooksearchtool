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
    private static int ourThreadsCount;
    
    
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
            ourThreadsCount = Integer.parseInt(properties.getProperty("threads_count"));
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
            myQueue = new LinksQueue();
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
    

    private int myCrawledPagesNumber = 0;
    public synchronized int getCrawledPagesNumber() {
        return myCrawledPagesNumber++;
    }
    
    private int myFoundBooksNumber = 0;
    public synchronized int getFoundBooksNumber() {
        return myFoundBooksNumber++;
    }
    
    
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
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) { }
        for (int i = 0; i < ourThreadsCount; i++) {
            myThread[i].finish();
        }
        for (int i = 0; i < ourThreadsCount; i++) {
            try {
                myThread[i].join();
            } catch (InterruptedException e) { }
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
            PrintWriter pw = new PrintWriter(file);
            for (int i = 0; i < ourThreadsCount; i++) {
                pw.println(String.format("%4d  %s", i, myThread[i].getAction()));
            }
            pw.println();
            pw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    
    synchronized void writeBookToOutput(URI source, URI referrer, String referrerPage) {
        myOutput.println("\t<book>");
        myOutput.println("\t\t<link src=\"" + source + "\" />");
        myOutput.println("\t\t<referrer src=\"" + referrer + "\" />");
        myOutput.println("\t</book>");
        myOutput.flush();
        if (ourAnalyzerEnabled) {
            try {
                if (myAnalyzerSocket != null) {
                    referrerPage = referrerPage.replaceAll("]]>", "]]]]><![CDATA[>");
                    myAnalyzerWriter.write("<root>"); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write("\t<link src=\"" + source + "\" />"); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write("\t<![CDATA["); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write(referrerPage); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write("\t]]>"); myAnalyzerWriter.newLine();
                    myAnalyzerWriter.write("</root>");
                    myAnalyzerWriter.flush();
                }
            } catch (IOException e) {
                myLogger.log(Logger.MessageType.ERRORS, " error: output to analyzer failed, " + source.hashCode());
                myLogger.log(Logger.MessageType.ERRORS, " " + e.getMessage());
            }
        }
    }
    

    
}
