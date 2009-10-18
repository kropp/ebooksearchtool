package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.impl.*;

public class Crawler {

    private static Proxy ourProxy;
    private static String ourUserAgent;
    private static int ourConnectionTimeout;
    private static int ourMaxLinksCount;
    
    private static boolean ourAnalyzerEnabled;
    private static int ourAnalyzerPort;
    
    private static int ourMaxLinksFromPage;
    
    
    private Socket myAnalyzerSocket = null;
    private BufferedWriter myAnalyzerWriter = null;
    private final PrintWriter myOutput;
    private volatile String myAction;
    
    private final AbstractRobotsExclusion myRobots = new ManyFilesRobotsExclusion();
    
    private int myRunning = 0;
    
    Crawler(Properties properties, PrintWriter output) {
        myOutput = output;
        myAction = "doing nothing";
        try {
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
                ourProxy = new Proxy(proxyType, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
            } else {
                ourProxy = Proxy.NO_PROXY;
            }
            if ("true".equals(properties.getProperty("analyzer_enabled"))) {
                String port = properties.getProperty("analyzer_port");
                ourAnalyzerEnabled = true;
                ourAnalyzerPort = Integer.parseInt(port);
            } else {
                ourAnalyzerEnabled = false;
                ourAnalyzerPort = -1;
            }
            ourMaxLinksCount = Integer.parseInt(properties.getProperty("max_links_count"));
            ourConnectionTimeout = Integer.parseInt(properties.getProperty("connection_timeout"));
            ourUserAgent = properties.getProperty("user_agent");
            int maxLinksFromPage = Integer.parseInt(properties.getProperty("max_links_from_page"));
            ourMaxLinksFromPage = maxLinksFromPage == 0 ? Integer.MAX_VALUE : maxLinksFromPage;
        } catch (Exception e) {
            throw new RuntimeException("bad format of properties file: " + e.getMessage());
        }
        if (ourAnalyzerEnabled) {
            try {
                myAnalyzerSocket = new Socket(InetAddress.getLocalHost().getHostName(), ourAnalyzerPort);
                System.err.println(" analyzer connected on port " + myAnalyzerSocket.getPort());
                myAnalyzerWriter = new BufferedWriter(new OutputStreamWriter(myAnalyzerSocket.getOutputStream()));
            } catch (Exception e) {
                System.err.println(" error: connect to analyzer failed!");
            }
        }
    }
    
    public String getAction() {
        return myAction;
    }
    
    public static int getConnectionTimeout() {
        return ourConnectionTimeout;
    }
    
    public static String getUserAgent() {
        return ourUserAgent;
    }
    
    public static Proxy getProxy() {
        return ourProxy;
    }
    
    public static int getMaxLinksFromPage() {
        return ourMaxLinksFromPage;
    }
    
    
    public void crawl(String[] starts) {
        myRunning = 1;
        final AbstractVisitedLinksSet were = new VisitedLinksSet();
        final AbstractLinksQueue queue = new LinksQueue();
        for (String start : starts) {
            myAction = "prechecking if i can go to " + start;
            URI uri = createURI(start);
            if (myRobots.canGo(uri)) {
                were.add(uri);
                queue.offer(uri);
            }
        }
        myOutput.println("<books>");
        int iteration = 0;
        while (myRunning == 1 && !queue.isEmpty()) {
            URI uri = queue.poll();
            myAction = "downloading page at " + uri;
            String page = getPage(uri);
            if (page == null) continue;
            System.out.println((++iteration) + " " + uri + " " + page.length());
            myAction = "getting all links out of " + uri;
            List<URI> links = HTMLParser.parseLinks(uri, page);
            for (URI link : links) {
                if (myRunning != 1) break;
                myAction = "checking if already visited " + link;
                if (!were.contains(Util.createSimilarLinks(link)) && were.size() < ourMaxLinksCount) {
                    were.add(link);
                    if (isBook(link)) {
                        myAction = "writing information about visited book " + link;
                        writeBookToOutput(link, uri, page);
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
        if (ourAnalyzerEnabled) {
            try {
                if (myAnalyzerSocket != null) {
                    myAnalyzerWriter.close();
                }
            } catch (IOException e) { }
        }
    }
    
    public void stop() {
        if (myRunning == 1) myRunning = 2;
    }
    
    public boolean isRunning() {
        return myRunning != 0;
    }
    
    private boolean isBook(URI uri) {
        String s = uri.getPath();
        return s != null && (s.endsWith(".epub") || s.endsWith(".pdf") || s.endsWith(".txt") || s.endsWith(".doc"));
    }
    
    private void writeBookToOutput(URI source, URI referrer, String referrerPage) {
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
    
    private static URI createURI(String s) {
        try {
            URI tmp = new URI(s.replaceAll(" ", "%20"));
            return new URI(tmp.getScheme(), tmp.getHost(), tmp.getPath(), null);
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    private static String getPage(URI uri) {
        try {
            URLConnection connection = uri.toURL().openConnection(ourProxy);
            connection.setConnectTimeout(ourConnectionTimeout);
            connection.setRequestProperty("User-Agent", ourUserAgent);
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
            System.err.println(" error on " + uri);
            System.err.println(" " + e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }
    

    
}
