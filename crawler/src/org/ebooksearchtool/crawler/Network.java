package org.ebooksearchtool.crawler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.security.cert.X509Certificate;
import java.security.SecureRandom;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.HttpsURLConnection;

public class Network {
	
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
    private final ConcurrentMap<String, Link> myLastAccessBlockingMap = new ConcurrentHashMap<String, Link>();
	
	public Network(Crawler crawler, Proxy proxy, int connectionTimeout, int readTimeout, int waitingForAccessTimeout, String userAgent, Logger logger) {
        myCrawler = crawler;
		myProxy = proxy;
		myConnectionTimeout = connectionTimeout;
        myReadTimeout = readTimeout;
        myWaitingForAccessTimeout = waitingForAccessTimeout;
		myUserAgent = userAgent;
        myLogger = logger;
        
        // configure HttpsURLConnection so that it trusts all certificates
        TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                public void checkServerTrusted(X509Certificate[] certs, String authType) { }
            }
        };
        try {
            SSLContext context = SSLContext.getInstance("SSL");
            context.init(null, trustAllCerts, new SecureRandom());
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
	
    public String download(Link link, String wantedContentType, boolean logErrors, int threadID) {
        InputStream is = null;
        try {
            String host = link.getHost();
            long nextAccess = getNextAccessTime(host);
            if (nextAccess > Long.MIN_VALUE && threadID != -1) {
                myCrawler.getCrawlerThread(threadID).setDoNotInterruptInAnyCase(true);
                while (true) {
                    Link u = myLastAccessBlockingMap.putIfAbsent(host, link);
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
                            myLastAccessBlockingMap.remove(host, link);
                            synchronized (this) {
                                notify();
                            }
                            myCrawler.getCrawlerThread(threadID).setDoNotInterruptInAnyCase(false);
                            return null;
                        }
                    } else {
                        myLastAccessBlockingMap.remove(host, link);
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
                myLastAccessBlockingMap.remove(host, link);
                synchronized (this) {
                    notify();
                }
                myCrawler.getCrawlerThread(threadID).setDoNotInterruptInAnyCase(false);
            }
            
            URLConnection connection = link.toURL().openConnection(myProxy);
            connection.setConnectTimeout(myConnectionTimeout);
            connection.setReadTimeout(myReadTimeout);
            connection.setRequestProperty("User-Agent", myUserAgent);
            connection.setRequestProperty("Connection", "Close");
            try {
                connection.connect();
            } catch (Throwable e) {
                return null;
            }
            is = connection.getInputStream();
            if (is == null) return null;
            String contentType = connection.getHeaderField("Content-Type");
            if (contentType != null && !contentType.startsWith(wantedContentType)) {
                is.close();
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder page = new StringBuilder();
            char[] buf = new char[BUFFER_SIZE];
            int r;
            while ((r = br.read(buf, 0, BUFFER_SIZE)) != -1) {
                page.append(buf, 0, r);
            }
            is.close();
            br.close();
//if (connection instanceof HttpURLConnection) ((HttpURLConnection)connection).disconnect();
            return page.toString();
        } catch (IOException e) {
            if (logErrors) {
                myLogger.log(Logger.MessageType.ERRORS, " network error on " + link);
                String message = e.getMessage();
                if (message == null ? link != null : !message.equals(link + "")) {
                    myLogger.log(Logger.MessageType.ERRORS, " " + message);
                }
            }
            return null;
        } finally {
            if (is != null) try {
                is.close();
            } catch (IOException e) {
                System.err.println(" error closing input stream on " + link);
                e.printStackTrace();
            }
        }
    }
    
    public String sendPOST(Link link, String request) {
        InputStream is = null;
        OutputStream os = null;
        try {
            HttpURLConnection connection = (HttpURLConnection)link.toURL().openConnection(myProxy);
            connection.setConnectTimeout(myConnectionTimeout);
            connection.setReadTimeout(myReadTimeout);
            connection.setRequestProperty("User-Agent", myUserAgent);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setDoOutput(true);
            try {
                connection.connect();
            } catch (Throwable e) {
                return null;
            }
            os = connection.getOutputStream();
            if (os == null) return null;
            os.write(request.getBytes());
            os.close();
            is = connection.getInputStream();
            if (is == null) return null;
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder page = new StringBuilder();
            char[] buf = new char[BUFFER_SIZE];
            int r;
            while ((r = br.read(buf, 0, BUFFER_SIZE)) != -1) {
                page.append(buf, 0, r);
            }
            is.close();
            br.close();
            return page.toString();
        } catch (IOException e) {
            myLogger.log(Logger.MessageType.ERRORS, " network error (POST) on " + link);
            String message = e.getMessage();
            if (message == null ? link != null : !message.equals(link + "")) {
                myLogger.log(Logger.MessageType.ERRORS, " " + message);
            }
            return null;
        } finally {
            try {
                if (is != null) is.close();
                if (os != null) os.close();
            } catch (IOException e) {
                System.err.println(" error closing streams (POST) on " + link);
                e.printStackTrace();
            }
        }
    }
    
	public void finish() {
    }
    
}
