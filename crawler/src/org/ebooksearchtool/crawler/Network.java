package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;

public class Network {
	
	private static final int BUFFER_SIZE = 16384;
	
	private final Proxy myProxy;
	private final int myConnectionTimeout;
	private final String myUserAgent;
	
	public Network(Proxy proxy, int connectionTimeout, String userAgent) {
		myProxy = proxy;
		myConnectionTimeout = connectionTimeout;
		myUserAgent = userAgent;
	}
    
    public Proxy getProxy() {
        return myProxy;
    }
    
    public int getConnectionTimeout() {
        return myConnectionTimeout;
    }
    
    public String getUserAgent() {
        return myUserAgent;
    }
	
    public String download(URI uri, String wantedContentType) {
        try {
            URLConnection connection = uri.toURL().openConnection(myProxy);
            connection.setConnectTimeout(myConnectionTimeout);
            connection.setRequestProperty("User-Agent", myUserAgent);
            InputStream is = connection.getInputStream();
            String contentType = connection.getHeaderField("Content-Type");
            if (is == null || (contentType != null && !contentType.startsWith(wantedContentType))) {
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder page = new StringBuilder();
            char[] buf = new char[BUFFER_SIZE];
            int r;
            while ((r = br.read(buf, 0, BUFFER_SIZE)) >= 0) {
                for (int i = 0; i < r; i++) {
                    page.append(buf[i]);
                }
            }
            br.close();
            return page.toString();
        } catch (Exception e) {
            System.err.println(" error on " + uri);
            System.err.println(" " + e.getMessage());
            //e.printStackTrace();
            return null;
        }
    }
    
	
}
