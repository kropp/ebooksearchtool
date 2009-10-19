package org.ebooksearchtool.analyzer.utils;

/**
 * @author Алексей
 */

public class Properties {

    public static final String SYSTEM_SEPARATOR = System.getProperty("line.separator");

    private static String ourNumberOfAnalyzerThreads = "10";
    public static String ourLogDirectoryName = "log";

    public static void setPropertie(String key, String value){
        if(key.equals("numberOfAnalyzerThreads")){
            ourNumberOfAnalyzerThreads = value;
            return;
        }
        if(key.equals("logDirectoryName")){
            ourLogDirectoryName = value;
            return;
        }
    }

    public static String getPropertie(String key){
        if(key.equals("numberOfAnalyzerThreads")){
            return ourNumberOfAnalyzerThreads;
        }
        if(key.equals("logDirectoryName")){
            return ourLogDirectoryName;
        }
        return null;
    }
}
