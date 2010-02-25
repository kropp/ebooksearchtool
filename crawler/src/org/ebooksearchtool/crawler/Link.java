package org.ebooksearchtool.crawler;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;

public class Link implements Comparable<Link> {
    
    private final URI myURI;
    
    private Link(URI uri) {
        myURI = uri;
    }
    
    public Link(String s) throws URISyntaxException {
        myURI = new URI(s);
    }
    
    public Link(String scheme, String host, String path, String fragment) throws URISyntaxException {
        myURI = new URI(scheme, host, path, fragment);
    }
    
    public String getHost() {
        return myURI.getHost();
    }
    
    public String getPath() {
        return myURI.getPath();
    }
    
    public String getScheme() {
        return myURI.getScheme();
    }
    
    public String getFragment() {
        return myURI.getFragment();
    }
    
    public Link resolve(Link link) {
        return new Link(myURI.resolve(link.myURI));
    }
    
    public URL toURL() throws MalformedURLException {
        return myURI.toURL();
    }
    
    public String toString() {
        return myURI.toString();
    }
    
    public boolean equals(Object o) {
        if (!(o instanceof Link)) {
            return false;
        }
        Link link = (Link)o;
        return myURI.equals(link.myURI);
    }
    
    public int hashCode() {
        return myURI.hashCode();
    }
    
    public int compareTo(Link link) {
        return myURI.compareTo(link.myURI);
    }
    
    
    public String getSecondLevelDomain() {
        String host = myURI.getHost();
        int x = host.lastIndexOf('.');
        if (x == -1 || x == 0) return host;
        int y = host.lastIndexOf('.', x - 1) + 1;
        return host.substring(y);
    }
    
}
