package org.ebooksearchtool.crawler;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.*;
import java.util.*;

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
    
    public static URI normalize(URI uri) {
        String s = null;
        try {
            String scheme = uri.getScheme().toLowerCase();
            if (!"http".equals(scheme) && !"https".equals(scheme) && !"ftp".equals(scheme)) {
                return null;
            }
            uri = new URI(scheme, uri.getHost().toLowerCase(), uri.getPath(), uri.getFragment());
            s = uri.toString();
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
            return new URI(s);
        } catch (Exception e) {
//            System.err.println(" error: normalizing " + uri);
            return null;
        }
    }
    
    public static boolean isBook(URI uri) {
        String s = uri.getPath();
        return s != null && (s.endsWith(".epub") || s.endsWith(".pdf") || s.endsWith(".txt") || s.endsWith(".doc"));
    }
    
    public static URI createURI(String s) {
        try {
            s = s.replaceAll(" ", "%20");
            int lastDash = s.lastIndexOf('#');
            if (lastDash >= 0) {
                s = s.substring(0, lastDash);
            }
            return new URI(s);
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
