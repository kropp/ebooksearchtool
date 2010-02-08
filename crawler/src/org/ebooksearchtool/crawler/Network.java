package org.ebooksearchtool.crawler;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import javax.net.ssl.*;

public class Network {
	
    private static final File LAST_ACCESS_FILE = new File(Util.CACHE_DIR + "/lastaccess");
	private static final int BUFFER_SIZE = 16384;
	
    private final Crawler myCrawler;
	private final Proxy myProxy;
	private final int myConnectionTimeout;
    private final int myReadTimeout;
    private final int myWaitingForAccessTimeout;
	private final String myUserAgent;
    private final Logger myLogger;
    private final Map<String, Long> myLastAccess = new HashMap<String, Long>();
    private final Map<String, Long> myNextAccess = new HashMap<String, Long>();
    private final ConcurrentMap<String, URI> myLastAccessBlockingMap = new ConcurrentHashMap<String, URI>();
	
	public Network(Crawler crawler, Proxy proxy, int connectionTimeout, int readTimeout, int waitingForAccessTimeout, String userAgent, Logger logger) {
        myCrawler = crawler;
		myProxy = proxy;
		myConnectionTimeout = connectionTimeout;
        myReadTimeout = readTimeout;
        myWaitingForAccessTimeout = waitingForAccessTimeout;
		myUserAgent = userAgent;
        myLogger = logger;
        try {
            if (!LAST_ACCESS_FILE.exists()) {
                new PrintWriter(LAST_ACCESS_FILE).close();
            }
            BufferedReader br = new BufferedReader(new FileReader(LAST_ACCESS_FILE));
            String s1 = null, s2 = null, s3 = null;
            while ((s1 = br.readLine()) != null) {
                s2 = br.readLine();
                s3 = br.readLine();
                long last = Long.parseLong(s2);
                long next = Long.parseLong(s3);
                myLastAccess.put(s1, last);
                myNextAccess.put(s1, next);
            }
            br.close();
        } catch (Exception e) {
            myLogger.log(Logger.MessageType.ERRORS, LAST_ACCESS_FILE + " could not be initialized, some data may be missing");
        }
        
        // configure HttpsURLConnection so that it trusts all certificates
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
            }
        };
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        } catch (Exception e) { }
            
	}
    
    public long getLastAccessTime(String host) {
        Long answer = myLastAccess.get(host);
        return answer == null ? Long.MIN_VALUE : answer;
    }
    
    public long getNextAccessTime(String host) {
        Long answer = myNextAccess.get(host);
        return answer == null ? Long.MIN_VALUE : answer;
    }
    
    public void setLastAccessTime(String host, long time) {
        myLastAccess.put(host, time);
    }
    
    public void setNextAccessTime(String host, long time) {
        myNextAccess.put(host, time);
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
	
    public String download(URI uri, String wantedContentType, boolean logErrors, int threadID) {
        try {
            String host = uri.getHost();
            long nextAccess = getNextAccessTime(host);
            if (nextAccess > Long.MIN_VALUE && threadID != -1) {
                myCrawler.getCrawlerThread(threadID).setDoNotInterruptInAnyCase(true);
                while (true) {
                    URI u = myLastAccessBlockingMap.putIfAbsent(host, uri);
                    if (u == null) break;
                    try {
                        synchronized (this) {
                            wait();
                        }
                    } catch (InterruptedException ie) {
                        return null;
                    }
                }
                nextAccess = getNextAccessTime(host);
                long now = Util.getCurrentTimeInMillis();
                if (nextAccess > now) {
                    if (nextAccess < now + myWaitingForAccessTimeout) {
                        try {
                            Thread.sleep(nextAccess - now);
                        } catch (InterruptedException e) {
                            myLastAccessBlockingMap.remove(host, uri);
                            synchronized (this) {
                                notify();
                            }
                            myCrawler.getCrawlerThread(threadID).setDoNotInterruptInAnyCase(false);
                            return null;
                        }
                    } else {
                        myLastAccessBlockingMap.remove(host, uri);
                        synchronized (this) {
                            notify();
                        }
                        myCrawler.getCrawlerThread(threadID).setDoNotInterruptInAnyCase(false);
                        return null;
                    }
                }
                now = Util.getCurrentTimeInMillis();
                if (nextAccess > Long.MIN_VALUE) {
                    long diff = nextAccess - getLastAccessTime(host);
                    setLastAccessTime(host, now);
                    setNextAccessTime(host, now + diff);
                }
                myLastAccessBlockingMap.remove(host, uri);
                synchronized (this) {
                    notify();
                }
                myCrawler.getCrawlerThread(threadID).setDoNotInterruptInAnyCase(false);
            }
            
            URLConnection connection = uri.toURL().openConnection(myProxy);
            connection.setConnectTimeout(myConnectionTimeout);
            connection.setReadTimeout(myReadTimeout);
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
        } catch (IOException e) {
            if (logErrors) {
                myLogger.log(Logger.MessageType.ERRORS, " network error on " + uri);
                String message = e.getMessage();
                if (message == null ? uri != null : !message.equals(uri.toString())) {
                    myLogger.log(Logger.MessageType.ERRORS, " " + message);
                }
            }
            return null;
        }
    }
    
	public void finish() {
        try {
            PrintWriter pw = new PrintWriter(LAST_ACCESS_FILE);
            for (String host : myLastAccess.keySet()) {
                pw.println(host);
                pw.println(myLastAccess.get(host));
                pw.println(myNextAccess.get(host));
            }
            pw.close();
        } catch (Exception e) {
            myLogger.log(Logger.MessageType.ERRORS, "error while writing to " + LAST_ACCESS_FILE);
        }
    }
    
}
