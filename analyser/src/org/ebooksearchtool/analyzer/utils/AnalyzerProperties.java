package org.ebooksearchtool.analyzer.utils;

import java.util.HashMap;

/**
 * @author Алексей
 */

public class AnalyzerProperties{

    private static HashMap<String,String> ourProperties;
    
    //TODO: Потом убрать, пока ради теста
    static{
        ourProperties = new HashMap<String, String>();
        ourProperties.put("numberOfAnalyzerThreads", "10");
        ourProperties.put("logDirectoryName", "log");
        ourProperties.put("systemSeparator", System.getProperty("line.separator"));
        ourProperties.put("demonRepeatConditionsChekTime", "3600000");
        ourProperties.put("demonHourWhenRefresh", "15");
        ourProperties.put("serverAddress", "192.168.2.104");
        ourProperties.put("serverPort", "8000");
        ourProperties.put("crawlerPort", "9999");
        ourProperties.put("serverConnectionTimeout", "1000");
        ourProperties.put("languagesList", "languages.lang");
    }

    public static void setPropertie(String key, String value){
        if(ourProperties.containsKey(key)){
            ourProperties.put(key, value);
        }
    }

    public static String getPropertie(String key){
       return ourProperties.get(key);
    }

    public static Integer getPropertieAsNumber(String key) throws NullPointerException{
        Integer in = Integer.parseInt(ourProperties.get(key));
        if(in != null){
            return in;
        }
        throw new NullPointerException("No such property");
    }

    public static HashMap<String,String> getProperties(){
       return ourProperties;
    }
}
