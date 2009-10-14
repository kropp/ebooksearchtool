package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.impl.*;

public class Crawler {

//    public static final Proxy PROXY = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.0.2", 3128));
    public static final Proxy PROXY = Proxy.NO_PROXY;
    public static final String USER_AGENT = "ebooksearchtool";
    public static final int CONNECTION_TIMEOUT = 3000;
    public static final int LIMIT = 500000000;
    public static final int ANALYZER_PORT = 9999;
    
    private Socket myAnalyzerSocket = null;
    private BufferedWriter myAnalyzerWriter = null;
    private final PrintWriter myOutput;
    private volatile String myAction;
    
    private final AbstractRobotsExclusion myRobots = new ManyFilesRobotsExclusion();
    
    private int myRunning = 0;
    
    Crawler(PrintWriter output) {
        myOutput = output;
        myAction = "doing nothing";
        try {
            myAnalyzerSocket = new Socket(InetAddress.getLocalHost().getHostName(), ANALYZER_PORT);
            System.err.println(" analyzer connected on port " + myAnalyzerSocket.getPort());
            myAnalyzerWriter = new BufferedWriter(new OutputStreamWriter(myAnalyzerSocket.getOutputStream()));
        } catch (Exception e) {
            System.err.println(" error: connect to analyzer failed!");
        }
    }
    
    public String getAction() {
        return myAction;
    }
    
    public void crawl(String[] starts) {
        myRunning = 1;
        final AbstractVisitedLinksSet were = new VisitedLinksSet();
        final AbstractLinksQueue queue = new LinksQueue();
        for (String start : starts) {
            myAction = "prechecking if i can go to " + start;
            if (myRobots.canGo(start)) {
                were.add(start);
                queue.offer(start);
            }
        }
        myOutput.println("<books>");
        int iteration = 0;
        while (myRunning == 1 && !queue.isEmpty()) {
            String s = queue.poll();
            myAction = "downloading page at " + s;
            String page = getPage(s);
            if (page == null) continue;
            System.out.println((++iteration) + " " + s + " " + page.length());
            myAction = "getting all links out of " + s;
            List<String> links = HTMLParser.parseLinks(Util.getServerNameFromURL(s), page);
            for (String link : links) {
                if (myRunning != 1) break;
                myAction = "checking if already visited " + link;
                if (!were.contains(Util.createSimilarLinks(link)) && were.size() < LIMIT) {
                    were.add(link);
                    if (isBook(link)) {
                        myAction = "writing information about visited book " + link;
                        writeBookToOutput(link, s, page);
                        continue;
                    }
                    myAction = "checking if i can go to " + link;
                    boolean permitted = myRobots.canGo(link);
                    if (permitted) {
                        myAction = "adding to queue " + link;
                        queue.offer(link);
                    }
                }
            }
        }
        myOutput.println("</books>");
        myAction = "doing nothing";
        System.out.println("finished; input something to exit");
        myRunning = 0;
        try {
            if (myAnalyzerSocket != null) {
                myAnalyzerWriter.close();
            }
        } catch (IOException e) { }
    }
    
    public void stop() {
        if (myRunning == 1) myRunning = 2;
    }
    
    public boolean isRunning() {
        return myRunning != 0;
    }
    
    private boolean isBook(String url) {
        return
            url.endsWith(".epub") ||
            url.endsWith(".pdf") ||
            url.endsWith(".txt") ||
            url.endsWith(".doc");
    }
    
    private void writeBookToOutput(String url, String referrer, String referrerPage) {
        myOutput.println("\t<book>");
        myOutput.println("\t\t<link src=\"" + url + "\" />");
        myOutput.println("\t\t<referrer src=\"" + referrer + "\" />");
        myOutput.println("\t</book>");
        myOutput.flush();
        try {
            if (myAnalyzerSocket != null) {
                referrerPage = referrerPage.replaceAll("]]>", "]]]]><![CDATA[>");
                myAnalyzerWriter.write("<root>"); myAnalyzerWriter.newLine();
                myAnalyzerWriter.write("\t<link src=\"" + url + "\" />"); myAnalyzerWriter.newLine();
                myAnalyzerWriter.write("\t<![CDATA["); myAnalyzerWriter.newLine();
                myAnalyzerWriter.write(referrerPage); myAnalyzerWriter.newLine();
                myAnalyzerWriter.write("\t]]>"); myAnalyzerWriter.newLine();
                myAnalyzerWriter.write("</root>");
                myAnalyzerWriter.flush();
            }
        } catch (IOException e) {
            System.err.println(" error: output to analyzer failed, " + url.hashCode());
            System.err.println(" " + e.getMessage());
        }
    }
    
    private static String getPage(String url) {
        try {
            URLConnection connection = new URL(url).openConnection(Crawler.PROXY);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestProperty("User-Agent", USER_AGENT);
            InputStream is = connection.getInputStream();
            if (is == null || !connection.getHeaderField("Content-Type").startsWith("text/html")) {
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuilder page = new StringBuilder();
            char[] buf = new char[1024];
            int r = 0;
            while ((r = br.read(buf, 0, 1024)) >= 0) {
                for (int i = 0; i < r; i++) {
                    page.append(buf[i]);
                }
            }
            String ans = page.toString();
            br.close();
            return ans;
        } catch (Exception e) {
            System.err.println(" error on " + url);
            System.err.println(" " + e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }
    

    
}
