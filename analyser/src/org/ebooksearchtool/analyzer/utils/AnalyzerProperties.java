package org.ebooksearchtool.analyzer.utils;

import java.util.HashMap;

/**
 * @author Алексей
 */

public class AnalyzerProperties{

    private static HashMap<String,String> ourProperties;
    
    //Default Parametrs
    static{
        ourProperties = new HashMap<String, String>();
        ourProperties.put("numberOfAnalyzerThreads", "10");
        ourProperties.put("logDirectoryName", "log");
        ourProperties.put("system_separator", System.getProperty("line.separator"));
        ourProperties.put("file_separator", System.getProperty("file.separator"));
        ourProperties.put("demonRepeatConditionsChekTime", "3600000");
        ourProperties.put("demonHourWhenRefresh", "15");
        ourProperties.put("serverAddress", "localhost");
        ourProperties.put("serverPort", "8000");
        ourProperties.put("crawlerPort", "9999");
        ourProperties.put("serverConnectionTimeout", "1000");
        ourProperties.put("languagesList", "languages.lang");
        ourProperties.put("demon_is_enable", "false");
        ourProperties.put("server_insert_distanation", "/data/insert");
        ourProperties.put("server_get_distanation", "/data/get");
        ourProperties.put("default_protocol", "http");
        ourProperties.put("proxy_enabled", "false");
        ourProperties.put("proxy_type", "http");
        ourProperties.put("proxy_host", "192.168.0.2");
        ourProperties.put("proxy_port", "3128");
        ourProperties.put("server_proxy_enabled", "false");
        ourProperties.put("server_proxy_type", "http");
        ourProperties.put("server_proxy_host", "192.168.0.2");
        ourProperties.put("server_proxy_port", "3128");
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

    public static boolean getPropertieAsBoolean(String key) throws NullPointerException{
        if(ourProperties.get(key).equals("true") || ourProperties.get(key).equals("True")){
            return true;
        }else{
            if(ourProperties.get(key).equals("false") || ourProperties.get(key).equals("False")){
                return false;
            }
        }
        throw new NullPointerException("No such property");
    }

    public static HashMap<String,String> getProperties(){
       return ourProperties;
    }
}
