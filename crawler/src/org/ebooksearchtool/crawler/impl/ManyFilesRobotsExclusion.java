package org.ebooksearchtool.crawler.impl;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.AbstractRobotsExclusion;
import org.ebooksearchtool.crawler.Network;
import org.ebooksearchtool.crawler.Logger;
import org.ebooksearchtool.crawler.Util;

public class ManyFilesRobotsExclusion extends AbstractRobotsExclusion {
    
    public static final File ROBOTS_DIR = new File("robotscache");
    public static final File LAST_ACCESS_FILE = new File(ROBOTS_DIR + "/lastaccess");
    public static final int FILES_NUMBER = 256;
    public static final int MAX_WAIT_FOR_ACCESS = 5000;
        
    private final Network myNetwork;
    private final Logger myLogger;
    
    private File[] myCacheFile;
    private Map<String, Long> myLastAccess;
    
    /*  stores all cached robots.txt in a number of files:
        0.txt, 1.txt, ..., {FILES_NUMBER - 1}.txt,
        where the number chosen for the given server is its name's hashcode modulo FILES_NUMBER */
    public ManyFilesRobotsExclusion(Network network, Logger logger) {
        myNetwork = network;
        myLogger = logger;
        try {
            if (!ROBOTS_DIR.exists()) {
                boolean success = ROBOTS_DIR.mkdir();
                if (!success) throw new Exception();
            }
            myCacheFile = new File[FILES_NUMBER];
            int digits = (FILES_NUMBER + "").length();
            for (int i = 0; i < FILES_NUMBER; i++) {
                myCacheFile[i] = new File(ROBOTS_DIR + "/" + String.format("%0" + digits + "d", i) + ".txt");
                if (!myCacheFile[i].exists()) {
                    new PrintWriter(myCacheFile[i]).close();
                }
            }
        } catch (Exception e) {
            myLogger.log(Logger.MessageType.ERRORS, ROBOTS_DIR + " cannot be initialized");
            System.exit(1);
        }
        myLastAccess = new HashMap<String, Long>();
        try {
            if (!LAST_ACCESS_FILE.exists()) {
                new PrintWriter(LAST_ACCESS_FILE).close();
            }
            BufferedReader br = new BufferedReader(new FileReader(LAST_ACCESS_FILE));
            String s1 = null, s2 = null;
            while ((s1 = br.readLine()) != null) {
                s2 = br.readLine();
                myLastAccess.put(s1, Long.parseLong(s2));
            }
            br.close();
        } catch (Exception e) {
            myLogger.log(Logger.MessageType.ERRORS, LAST_ACCESS_FILE + " cannot be initialized");
            System.exit(1);
        }
    }
    
    protected long getLastAccessTime(String host) {
        Long answer = myLastAccess.get(host);
        if (answer == null) {
            answer = Long.MIN_VALUE;
            myLastAccess.put(host, answer);
        }
        return answer;
    }
    
    protected void setLastAccessTime(String host, long value) {
        myLastAccess.put(host, value);
    }
    
    protected synchronized int isDisallowed(String host, URI uri) {
        File file = myCacheFile[Math.abs(host.hashCode()) % FILES_NUMBER];
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
                            long wait = 1000L * (seconds + docs - 1) / docs;
                            long lastAccess = getLastAccessTime(host);
                            long now = Util.getCurrentTimeInMillis();
                            if (lastAccess + wait > now) {
                                //TODO: invent something more clever
                                if (lastAccess + wait < now + MAX_WAIT_FOR_ACCESS) {
                                    try {
                                        Thread.sleep(wait);
                                    } catch (InterruptedException e) {
                                        br.close();
                                        return 1;
                                    }
                                } else {
                                    br.close();
                                    return 1;
                                }
   	                        } else {
                                setLastAccessTime(host, now);
                            }
                        }
                    }
                } else {
                    if (s.equals(host)) {
                        hostFound = true;
                    } else {
                        if (hostFound) {
                            // assuming there are no more records about this user agent further in the cached file
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
        File file = myCacheFile[Math.abs(host.hashCode()) % FILES_NUMBER];
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            myLogger.log(Logger.MessageType.ERRORS, file + " cannot be written");
            return;
        }
        URI robotstxt = null;
        try {
            robotstxt = new URI("http://" + host + "/robots.txt");
        } catch (URISyntaxException e) {
            myLogger.log(Logger.MessageType.ERRORS, "URI syntax exception: " + e.getMessage());
            return;
        }
        String content = myNetwork.download(robotstxt, "text/plain", false);
        if (content == null) {
            try {
                bw.write(host);
                bw.newLine();
                bw.close();
            } catch (IOException ioe) { }
            return;
        }
        String[] lines = content.split("\n");
        boolean me = false;
        try {
            bw.write(host);
            bw.newLine();
            for (String s : lines) {
                s = s.replaceAll(" +", " ").toLowerCase();
                int end = s.indexOf('#');
                if (end < 0) end = s.length();
                s = s.substring(0, end).trim();
                if (s.startsWith("user-agent:")) {
                    s = s.substring(11).trim();
                    me = "*".equals(s) || myNetwork.getUserAgent().toLowerCase().equals(s);
                } else if (me) {
                    if (s.startsWith("disallow:")) {
                        s = s.substring(9).trim();
                        if (s.length() > 0) {
                            bw.write(" - " + s);
                            bw.newLine();
                        }
                    } else if (s.startsWith("allow:")) {
                        s = s.substring(6).trim();
                        if (s.length() > 0) {
                            bw.write(" + " + s);
                            bw.newLine();
                        }
                    } else if (s.startsWith("request-rate:")) {
                        s = s.substring(13).trim();
                        if (s.length() > 0) {
                            String r = null;
                            try {
                                String[] ss = s.replaceAll(":", "").split("[ \\/\\-]");
                                if (ss[1].endsWith("h")) {
                                    long x = Long.parseLong(ss[1].substring(0, ss[1].length() - 1));
                                    ss[1] = (3600 * x) + "";
                                } else if (ss[1].endsWith("m")) {
                                    long x = Long.parseLong(ss[1].substring(0, ss[1].length() - 1));
                                    ss[1] = (60 * x) + "";
                                }
                                r = ss[0] + " " + ss[1];
                                String times = null;
                                if (ss.length == 4) {
                                    ss[2] = ss[2].replaceAll(":", "");
                                    ss[3] = ss[3].replaceAll(":", "");
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
                            } catch (Exception e) {
                                continue;
                            }
                            if (r != null) {
                                bw.write(" r " + r);
                                bw.newLine();
                            }
                        }
                    } else if (s.startsWith("visit-time:")) {
                        s = s.substring(11).trim();
                        if (s.length() > 0) {
                            String r = null;
                            try {
                                String[] ss = s.replaceAll(":", "").split("[ \\-]");
                                r = ss[0] + " " + ss[1];
                            } catch (Exception e) {
                                continue;
                            }
                            if (r != null) {
                                bw.write(" t " + r);
                                bw.newLine();
                            }
                        }
                    }
                }
            }
            bw.close();
            myLogger.log(Logger.MessageType.DOWNLOADED_ROBOTS_TXT, "downloaded " + robotstxt);
        } catch (IOException ioe) {
            return;
        }
    }

    
    public void finish() {
        try {
            PrintWriter pw = new PrintWriter(LAST_ACCESS_FILE);
            for (String host : myLastAccess.keySet()) {
                pw.println(host);
                pw.println(myLastAccess.get(host));
            }
            pw.close();
        } catch (Exception e) {
            myLogger.log(Logger.MessageType.ERRORS, "error while writing to " + LAST_ACCESS_FILE);
        }
    }
    

}

