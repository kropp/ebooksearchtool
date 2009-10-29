package org.ebooksearchtool.crawler;

import java.io.*;
import java.util.*;

public class Logger {
    
    public enum MessageType {
        DOWNLOADED_ROBOTS_TXT, CRAWLED_PAGES, FOUND_BOOKS, ERRORS, MISC;
    }
    
    private final boolean myLogToScreenEnabled;
    private final PrintWriter myOutput;
    private final Map<Logger.MessageType, Boolean> myLogOptions;
    
    public Logger(String logFile, boolean logToScreenEnabled, Map<Logger.MessageType, Boolean> logOptions) {
        PrintWriter output = null;
        try {
            output = new PrintWriter(logFile);
        } catch (Exception e) {
        }
        myOutput = output;
        if (myOutput != null) {
            myOutput.println("crawler started on " + Util.getDateString() + " at " + Util.getTimeString());
        }
        myLogToScreenEnabled = logToScreenEnabled;
        myLogOptions = logOptions;
    }
    
    public void finish() {
        if (myOutput != null) {
            myOutput.println("crawler finished on " + Util.getDateString() + " at " + Util.getTimeString());
            myOutput.close();
        }
    }
    
    public synchronized void log(MessageType type, String s) {
        if (!myLogOptions.get(type)) {
            return;
        }
        if (myOutput != null) {
            myOutput.println("[" + Util.getTimeString() + "]" + s);
            myOutput.flush();
        }
        if (myLogToScreenEnabled) {
            System.out.println(s);
        }
    }
    
}