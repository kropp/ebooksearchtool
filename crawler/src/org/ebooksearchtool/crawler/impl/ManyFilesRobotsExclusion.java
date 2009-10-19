package org.ebooksearchtool.crawler.impl;

import java.io.*;
import java.net.*;
import java.util.*;
import org.ebooksearchtool.crawler.*;

public class ManyFilesRobotsExclusion extends AbstractRobotsExclusion {
    
    public static final File ROBOTS_DIR = new File("robotscache");
    public static final int FILES_NUMBER = 256;
        
    private File[] myCacheFile;
    private Map<String, Long> myLastAccess;
    private static Calendar myCalendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
    
    /*  stores all cached robots.txt in a number of files:
        0.txt, 1.txt, ..., {FILES_NUMBER - 1}.txt,
        where the number chosen for the given server is its name's hashcode modulo FILES_NUMBER */
    public ManyFilesRobotsExclusion() {
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
            System.err.println(ROBOTS_DIR + " cannot be initialized");
            System.exit(1);
        }
        //TODO: read myLastAccess from file
        myLastAccess = new HashMap<String, Long>();
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
            System.err.println(file + " cannot be read");
            return 1;
        }
        String s = "";
        boolean hostFound = false;
        String path = uri.getPath();
        try {
            while ((s = br.readLine()) != null) {
                if (s.length() == 0) continue;
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
                        myCalendar.setTime(new Date());
                        int minute = myCalendar.get(Calendar.MINUTE);
                        int hour = myCalendar.get(Calendar.HOUR);
                        if (myCalendar.get(Calendar.AM_PM) == 1) {
                            hour += 12;
                        }
                        int time = hour * 100 + minute;
                        if (time < time1 || time > time2) {
                            br.close();
                            return 1;
                        }
                    } else if (s.charAt(1) == 'r') {       // Request-rate
                        String[] ss = s.substring(3).split(" ");
                        int time1 = Integer.parseInt(ss[2]);
                        int time2 = Integer.parseInt(ss[3]);
                        myCalendar.setTime(new Date());
                        int minute = myCalendar.get(Calendar.MINUTE);
                        int hour = myCalendar.get(Calendar.HOUR);
                        if (myCalendar.get(Calendar.AM_PM) == 1) {
                            hour += 12;
                        }
                        int time = hour * 100 + minute;
                        if (time1 <= time && time <= time2) {
                            long docs = Long.parseLong(ss[0]);
                            long seconds = Long.parseLong(ss[1]);
                            long wait = (seconds + docs - 1) / docs;
                            long lastAccess = getLastAccessTime(host);
                            long now = myCalendar.getTimeInMillis() / 1000;
                            if (lastAccess + wait > now) {
                                br.close();
                                //TODO: return "to wait" instead of "disallowed"
                                return 1;
                            }
                            //TODO: setLastAccessTime
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
            System.err.println("error while reading " + file);
        }
        return hostFound ? 0 : -1;
    }
    
    protected void downloadRobotsTxt(String host) {
        File file = myCacheFile[Math.abs(host.hashCode()) % FILES_NUMBER];
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, true));
        } catch (IOException e) {
            System.err.println(file + " cannot be written");
            return;
        }
        BufferedReader br = null;
        try {
            URLConnection connection = new URL("http://" + host + "/robots.txt").openConnection(Crawler.getProxy());
            connection.setConnectTimeout(Crawler.getConnectionTimeout());
            connection.setRequestProperty("User-Agent", Crawler.getUserAgent());
            InputStream is = connection.getInputStream();
            String contentType = connection.getHeaderField("Content-Type");
            if (is == null || (contentType != null && !contentType.startsWith("text/plain"))) {
                throw new IOException();
            }
            br = new BufferedReader(new InputStreamReader(is));
        } catch (MalformedURLException mue) {
            System.err.println("malformed URL: " + host);
            try {
                bw.close();
            } catch (IOException ioe) { }
            return;
        } catch (IOException ioe) {
            try {
                bw.write(host);
                bw.newLine();
                bw.close();
            } catch (IOException ioe2) { }
            return;
        }
        String s = "";
        boolean me = false;
        try {
            bw.write(host);
            bw.newLine();
            while ((s = br.readLine()) != null) {
                s = s.replaceAll(" +", " ").toLowerCase();
                int end = s.indexOf('#');
                if (end < 0) end = s.length();
                s = s.substring(0, end).trim();
                if (s.startsWith("user-agent:")) {
                    s = s.substring(11).trim();
                    me = "*".equals(s) || Crawler.getUserAgent().toLowerCase().equals(s);
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
                                String[] ss = s.replaceAll(":", "").split("[ /\\-]");
                                if (ss[1].endsWith("h")) {
                                    long x = Long.parseLong(ss[1].substring(0, ss[1].length() - 1));
                                    ss[1] = (3600 * x) + "";
                                } else if (ss[1].endsWith("m")) {
                                    long x = Long.parseLong(ss[1].substring(0, ss[1].length() - 1));
                                    ss[1] = (60 * x) + "";
                                }
                                r = ss[0] + " " + ss[1];
                                if (ss.length == 4) {
                                    r = r + " " + ss[2] + " " + ss[3];
                                }
                            } catch (Exception e) {
                                continue;
                            }
                            bw.write(" r " + r);
                            bw.newLine();
                        }
                    } else if (s.startsWith("visit-time:")) {
                        s = s.substring(11).trim();
                        if (s.length() > 0) {
                            String r = null;
                            try {
                                String[] ss = s.replaceAll(":", "").split("[ -]");
                                r = ss[0] + " " + ss[1];
                            } catch (Exception e) {
                                continue;
                            }
                            bw.write(" t " + r);
                            bw.newLine();
                        }
                    }
                }
            }
            br.close();
            bw.close();
        } catch (IOException ioe) {
            return;
        }
    }
    
    
}

