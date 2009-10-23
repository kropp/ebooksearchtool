package org.ebooksearchtool.crawler;

import java.net.*;
import java.util.*;

public class Util {

    private static Calendar ourCalendar = new GregorianCalendar(TimeZone.getTimeZone("America/New_York"));
    
    public static List<URI> createSimilarLinks(URI uri) {
        List<URI> answer = new ArrayList<URI>();
        answer.add(uri);
        try {
            String s = uri.toString();
            if (s.endsWith("/")) {
                answer.add(new URI(s.substring(0, s.length() - 1)));
            } else {
                answer.add(new URI(s + "/"));
            }
            //TODO: /index.html, /index.htm, /index.php, #...
        } catch (Exception e) {
            System.err.println(" error: creating similar links to " + uri);
        }
        return answer;
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
    
}
