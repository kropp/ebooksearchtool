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
    
    private int myCounter = 0;
    
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
            
            ourNetwork = new Network(proxy, connectionTimeout, userAgent);
            myRobots = new ManyFilesRobotsExclusion(ourNetwork);
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
                System.err.println(" analyzer connected on port " + analyzerSocket.getPort());
                analyzerWriter = new BufferedWriter(new OutputStreamWriter(analyzerSocket.getOutputStream()));
            } catch (IOException e) {
                try {
                    analyzerSocket.close();
                } catch (IOException f) { }
                analyzerSocket = null;
                analyzerWriter = null;
                System.err.println(" error: connect to analyzer failed!");
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
    
    public synchronized int getCounter() {
        return myCounter++;
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
        
        
        Thread[] thread = new Thread[ourThreadsCount];
        for (int i = 0; i < ourThreadsCount; i++) {
            thread[i] = new CrawlerThread(this, i);
            thread[i].start();
        }
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) { }
        for (int i = 0; i < ourThreadsCount; i++) {
            thread[i].interrupt();
        }
        for (int i = 0; i < ourThreadsCount; i++) {
            try {
                thread[i].join();
            } catch (InterruptedException e) { }
        }
        
        myRobots.finish();
        myOutput.println("</books>");
        System.out.println("finished");
        if (ourAnalyzerEnabled) {
            try {
                if (myAnalyzerSocket != null) {
                    myAnalyzerWriter.close();
                }
            } catch (IOException e) { }
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
                System.err.println(" error: output to analyzer failed, " + source.hashCode());
                System.err.println(" " + e.getMessage());
            }
        }
    }
    

    
}
