package org.ebooksearchtool.crawler;

import java.net.*;
import java.util.*;

class Util {

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
            URI tmp = new URI(s.replaceAll(" ", "%20"));
            return new URI(tmp.getScheme(), tmp.getHost(), tmp.getPath(), null);
        } catch (URISyntaxException e) {
            return null;
        }
    }
    
}
