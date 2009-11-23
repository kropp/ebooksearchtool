package org.ebooksearchtool.crawler;

import java.net.URI;

public abstract class AbstractRobotsExclusion {
    
    protected static boolean matches(String url, String pattern) {
        // regular expressions?...
        return url.startsWith(pattern);
    }
    
    /*  returns -1 if no robots.txt is stored for this server,
                 0 if url is not disallowed,
                 1 if url is disallowed                                */
    protected abstract int isDisallowed(String host, URI uri);
    
    /*  creates information about robots.txt located on the server.
        if there is no robots.txt, information about total allowance
        must be generated anyway.                                      */
    protected abstract void downloadRobotsTxt(String host);
    
    boolean canGo(URI uri) {
        if (uri == null) return false;
        String host = uri.getHost();
        if (host == null) return false;
        int disallowed = isDisallowed(host, uri);
        if (disallowed == -1) {
            downloadRobotsTxt(host);
            disallowed = isDisallowed(host, uri);
        }
        assert disallowed >= 0;
        return disallowed == 0;
    }
    
}
