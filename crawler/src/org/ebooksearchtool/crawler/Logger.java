package org.ebooksearchtool.crawler;

import java.io.*;

public class Logger {
    
    private final boolean myLogToScreenEnabled;
    private final PrintWriter myOutput;
    
    public Logger(String logFile, boolean logToScreenEnabled) {
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
    }
    
    public void finish() {
        if (myOutput != null) {
            myOutput.println("crawler finished on " + Util.getDateString() + " at " + Util.getTimeString());
            myOutput.close();
        }
    }
    
    public synchronized void log(String s) {
        if (myOutput != null) {
            myOutput.println("[" + Util.getTimeString() + "]" + s);
        }
        if (myLogToScreenEnabled) {
            System.out.println(s);
        }
    }
    
}
