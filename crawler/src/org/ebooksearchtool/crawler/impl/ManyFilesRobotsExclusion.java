package org.ebooksearchtool.crawler.impl;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import org.ebooksearchtool.crawler.AbstractRobotsExclusion;
import org.ebooksearchtool.crawler.Network;
import org.ebooksearchtool.crawler.Logger;
import org.ebooksearchtool.crawler.Util;

public class ManyFilesRobotsExclusion extends AbstractRobotsExclusion {
    
    public static final File ROBOTS_CACHE_DIR = Util.CACHE_DIR;
    public static final int FILES_NUMBER = 8192;
        
    private final Network myNetwork;
    private final Logger myLogger;
    
    private File[] myCacheFile;
    
    /*  stores all cached robots.txt in a number of files: 0.txt, 1.txt, ...,
        {FILES_NUMBER - 1}.txt, where the number chosen for the given server
        is its name's hashcode modulo FILES_NUMBER                           */
    public ManyFilesRobotsExclusion(Network network, Logger logger) {
        myNetwork = network;
        myLogger = logger;
        try {
            myCacheFile = new File[FILES_NUMBER];
            int digits = (FILES_NUMBER + "").length();
            if (!ROBOTS_CACHE_DIR.exists()) {
                ROBOTS_CACHE_DIR.mkdir();
            }
            File[] cacheFiles = ROBOTS_CACHE_DIR.listFiles(new FilenameFilter() {
                private final Pattern myPattern = Pattern.compile("[0-9]+\\.txt");
                public boolean accept(File dir, String name) {
                    return myPattern.matcher(name).matches();
                }
            });
            boolean reCreateAllFiles = cacheFiles.length != FILES_NUMBER;
            for (int i = 0; i < FILES_NUMBER; i++) {
                myCacheFile[i] = new File(ROBOTS_CACHE_DIR + "/" + String.format("%0" + digits + "d", i) + ".txt");
                if (reCreateAllFiles && !myCacheFile[i].exists()) {
                    new PrintWriter(myCacheFile[i]).close();
                }
            }
        } catch (Exception e) {
            myLogger.log(Logger.MessageType.ERRORS, ROBOTS_CACHE_DIR + " cannot be initialized, exiting");
            System.exit(1);
        }
    }
    
    protected synchronized int isDisallowed(String host, URI uri) {
        final File file = myCacheFile[Math.abs(host.hashCode()) % FILES_NUMBER];
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (IOException ioe) {
            myLogger.log(Logger.MessageType.ERRORS, file + " cannot be read");
            return 1;
        }
        String s = "";
        boolean hostFound = false;
        String path = uri.getPath();
        try {
            while ((s = br.readLine()) != null) {
                if (s.length() < 2) continue;
                if (s.charAt(0) == ' ') {
                    if (s.charAt(1) == '-') {              // Disallow
                        if (hostFound && matches(path, s.substring(3))) {
                            br.close();
                            return 1;
                        }
                    } else if (s.charAt(1) == '+') {       // Allow
                        if (hostFound && matches(path, s.substring(3))) {
                            br.close();
                            return 0;
                        }
                    } else if (s.charAt(1) == 't') {       // Visit-time
                        String[] ss = s.substring(3).split(" ");
                        int time1 = Integer.parseInt(ss[0]);
                        int time2 = Integer.parseInt(ss[1]);
                        int time = Util.getCurrentTime();
                        if (time < time1 || time > time2) {
                            br.close();
                            return 1;
                        }
                    } else if (s.charAt(1) == 'r') {       // Request-rate
                        String[] ss = s.substring(3).split("[ \\/\\-]");
                        int time1 = Integer.parseInt(ss[2]);
                        int time2 = Integer.parseInt(ss[3]);
                        int time = Util.getCurrentTime();
                        if (time1 <= time && time <= time2) {
                            long docs = Long.parseLong(ss[0]);
                            long seconds = Long.parseLong(ss[1]);
                            long waitBetweenRequests = 1000L * (seconds + docs - 1) / docs;
                            long lastAccess = myNetwork.getLastAccessTime(host);
                            long nextAccess = myNetwork.getNextAccessTime(host);
                            if (nextAccess <= lastAccess) {
                                myNetwork.setNextAccessTime(host, lastAccess + waitBetweenRequests);
                            }
                        }
                    }
                } else {
                    if (s.equals(host)) {
                        hostFound = true;
                    } else {
                        if (hostFound) {
                            // assuming there are no more records about this host further in the cached file
                            br.close();
                            return 0;
                        }
                        hostFound = false;
                    }
                }
            }
            br.close();
        } catch (IOException ioe) {
            myLogger.log(Logger.MessageType.ERRORS, "error while reading " + file);
        }
        return hostFound ? 0 : -1;
    }
    
    protected void downloadRobotsTxt(String host) {
        final File file = myCacheFile[Math.abs(host.hashCode()) % FILES_NUMBER];
        BufferedWriter bw = null;
        URI robotstxt = null;
        try {
            robotstxt = new URI("http://" + host + "/robots.txt");
        } catch (URISyntaxException e) {
            myLogger.log(Logger.MessageType.ERRORS, "URI syntax exception: " + e.getMessage());
            return;
        }
        String content = myNetwork.download(robotstxt, "text/plain", false, -1);
        if (content == null) {
            try {
                synchronized (file) {
                    if (isDisallowed(host, robotstxt) < 0) {
                        bw = new BufferedWriter(new FileWriter(file, true));
                        bw.write(host);
                        bw.newLine();
                        bw.close();
                    }
                }
            } catch (IOException ioe) { }
            return;
        }
        String[] lines = content.split("\n");
        boolean me = false;
        StringBuffer sb = new StringBuffer();
        try {
            sb.append(host).append("\n");
            for (String s : lines) {
                s = s.replaceAll(" +", " ").toLowerCase();
                int end = s.indexOf('#');
                if (end < 0) end = s.length();
                s = s.substring(0, end).trim();
                if (s.startsWith("user-agent:")) {
                    s = s.substring("user-agent:".length()).trim();
                    me = "*".equals(s) || myNetwork.getUserAgent().toLowerCase().equals(s);
                } else if (me) {
                    if (s.startsWith("disallow:")) {
                        s = s.substring("disallow:".length()).trim();
                        if (s.length() > 0) {
                            sb.append(" - ").append(s).append("\n");
                        }
                    } else if (s.startsWith("allow:")) {
                        s = s.substring("allow:".length()).trim();
                        if (s.length() > 0) {
                            sb.append(" + ").append(s).append("\n");
                        }
                    } else if (s.startsWith("request-rate:")) {
                        s = s.substring("request-rate:".length()).trim();
                        if (s.length() > 0) {
                            try {
                                String r = null;
                                String[] ss = s.replaceAll(":", "").split("[ \\/\\-]+");
                                if (ss[1].endsWith("h")) {
                                    long x = Long.parseLong(ss[1].substring(0, ss[1].length() - 1));
                                    ss[1] = (3600 * x) + "";
                                } else if (ss[1].endsWith("m")) {
                                    long x = Long.parseLong(ss[1].substring(0, ss[1].length() - 1));
                                    ss[1] = (60 * x) + "";
                                } else {
                                    Long.parseLong(ss[1]);
                                }
                                Long.parseLong(ss[0]);
                                r = ss[0] + " " + ss[1];
                                String times = null;
                                if (ss.length >= 4) {
                                    try {
                                        int t1 = Integer.parseInt(ss[2]);
                                        int t2 = Integer.parseInt(ss[3]);
                                        if (0 <= t1 && t1 <= 2359 && 0 <= t2 && t2 <= 2359) {
                                            times = ss[2] + " " + ss[3];
                                        }
                                    } catch (NumberFormatException nfe) {
                                    }
                                }
                                if (times != null) {
                                    r = r + " " + times;
                                } else {
                                    r = r + " 0000 2359";
                                }
                                sb.append(" r ").append(r).append("\n");
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    } else if (s.startsWith("visit-time:")) {
                        s = s.substring("visit-time:".length()).trim();
                        if (s.length() > 0) {
                            try {
                                String r = null;
                                String[] ss = s.replaceAll(":", "").split("[ \\-]");
                                int t1 = Integer.parseInt(ss[0]);
                                int t2 = Integer.parseInt(ss[1]);
                                if (0 <= t1 && t1 <= 2359 && 0 <= t2 && t2 <= 2359) {
                                    r = ss[0] + " " + ss[1];
                                    sb.append(" t ").append(r).append("\n");
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }
            }
            synchronized (file) {
                if (isDisallowed(host, robotstxt) < 0) {
                    bw = new BufferedWriter(new FileWriter(file, true));
                    bw.write(sb.toString());
                    bw.close();
                }
            }
            myLogger.log(Logger.MessageType.DOWNLOADED_ROBOTS_TXT, "downloaded " + robotstxt);
        } catch (IOException ioe) {
            return;
        }
    }

    
}

