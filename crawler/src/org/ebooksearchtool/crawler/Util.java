package org.ebooksearchtool.crawler;

import java.net.URI;
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
    
}
