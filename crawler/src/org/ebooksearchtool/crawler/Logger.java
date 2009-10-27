package org.ebooksearchtool.crawler;

import java.io.*;

public class Logger {
    
    private static boolean ourLogToScreenEnabled = true;
    private static PrintWriter ourOutput = null;
    
    public static boolean setOutput(String logFile) {
        if (ourOutput != null) {
            return false;
        }
        try {
            ourOutput = new PrintWriter(logFile);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    public static void setLogToScreenEnabled(boolean logToScreenEnabled) {
        ourLogToScreenEnabled = logToScreenEnabled;
    }
    
    public static void finish() {
        ourOutput.close();
    }
    
    public static synchronized void log(String s) {
        if (ourOutput != null) {
            ourOutput.println("[" + Util.getTimeString() + "]" + s);
        }
        if (ourLogToScreenEnabled) {
            System.out.println(s);
        }
    }
    
}
