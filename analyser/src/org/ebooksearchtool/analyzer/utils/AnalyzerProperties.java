package org.ebooksearchtool.analyzer.utils;

import java.util.HashMap;

/**
 * @author Алексей
 */

public class AnalyzerProperties{

    private static HashMap<String,String> ourProperties;
    
    static{
        ourProperties = new HashMap<String, String>();
        ourProperties.put("numberOfAnalyzerThreads", "10");
        ourProperties.put("logDirectoryName", "log");
        ourProperties.put("systemSeparator", System.getProperty("line.separator"));
    }

    public static void setPropertie(String key, String value){
        if(ourProperties.containsKey(key)){
            ourProperties.put(key, value);
        }
    }

    public static String getPropertie(String key){
       return ourProperties.get(key);
    }

    public static HashMap<String,String> getProperties(){
       return ourProperties;
    }
}
