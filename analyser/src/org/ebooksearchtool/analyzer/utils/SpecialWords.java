package org.ebooksearchtool.analyzer.utils;

import java.util.HashSet;

/**
 * @author Алексей
 */

public class SpecialWords {

    static HashSet separators = new HashSet();
    static{
        separators.add(",");
        separators.add("'");
        separators.add(',');
        separators.add('\'');
    }

    static HashSet joiners = new HashSet();
    static{
        joiners.add("and");
        joiners.add("и");
        joiners.add('и');
    }

    static HashSet needToTrim = new HashSet();
    static{
        needToTrim.add(')');
        needToTrim.add('(');
        needToTrim.add('[');
        needToTrim.add(']');
    }

    public static boolean isNeedToTrim(char value){
        if(needToTrim.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isSepatator(String value){
        if(separators.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isSepatator(char value){
       if(separators.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isJoiner(String value){
        if(joiners.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isJoiner(char value){
        if(joiners.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public enum StringType{
        word,
        separator,
        joiner;
    }
}
