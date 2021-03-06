package org.ebooksearchtool.crawler;

import java.io.File;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Util {

    public static final File CACHE_DIR = new File("cache");
    
    private static Calendar ourCalendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
    private static DateFormat ourTimeFormat = new SimpleDateFormat("HH:mm:ss");
    private static DateFormat ourDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    
    public static boolean init() {
        if (!CACHE_DIR.exists()) {
            return CACHE_DIR.mkdir();
        }
        return true;
    }
    
    public static Link normalize(Link link) {
        String s = null;
        try {
            String scheme = link.getScheme().toLowerCase();
            if (!"http".equals(scheme) && !"https".equals(scheme) && !"ftp".equals(scheme)) {
                return null;
            }
            link = new Link(scheme, link.getHost().toLowerCase(), link.getPath(), link.getFragment());
            s = link + "";
            if (s.length() > 128) {
                ///TODO: invent something more clever
                return null;
            }
            int x = s.indexOf('#');
            if (x >= 0) {
                s = s.substring(0, x);
            }
            if (s.endsWith("/")) {
                s = s.substring(0, s.length() - 1);
            }
            x = s.indexOf("?PHPSESSID=");
            if (x >= 0) {
                s = s.substring(0, x);
            }
            x = s.indexOf(";jsessionid=");
            if (x >= 0) {
                s = s.substring(0, x);
            }
            ///TODO: /index.html, /index.htm, /index.php
            return new Link(s);
        } catch (Exception e) {
//            System.err.println(" error: normalizing " + link);
            return null;
        }
    }
    
    public static boolean isBook(Link link) {
        String s = link.getPath();
//        return s != null && (s.endsWith(".epub") || s.endsWith(".pdf") || s.endsWith(".txt") || s.endsWith(".doc"));
        return s != null && (s.endsWith(".epub")) || (s.endsWith(".fb2")) || (s.endsWith(".fb2.zip"));
    }
    
    public static Link createLink(String s) {
        try {
            s = s.replaceAll(" ", "%20");
            int lastDash = s.lastIndexOf('#');
            if (lastDash >= 0) {
                s = s.substring(0, lastDash);
            }
            return new Link(s);
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
    
    
    public static int getCurrentTime() {
        ourCalendar.setTime(new Date());
        int minute = ourCalendar.get(Calendar.MINUTE);
        int hour = ourCalendar.get(Calendar.HOUR);
        if (ourCalendar.get(Calendar.AM_PM) == 1) {
            hour += 12;
        }
        return hour * 100 + minute;
    }
    
    public static long getCurrentTimeInMillis() {
        return ourCalendar.getTimeInMillis();
    }
    
    
    public static String getTimeString() {
        return ourTimeFormat.format(new Date());
    }
    
    public static String getDateString() {
        return ourDateFormat.format(new Date());
    }
    
}
