package org.ebooksearchtool.analyzer.utils;

import java.util.HashSet;

/**
 * @author Алексей
 */

public class SpecialWords {

    private static HashSet ourSeparators = new HashSet();
    static{
        ourSeparators.add(",");
        ourSeparators.add("'");
        ourSeparators.add(',');
        ourSeparators.add('\'');
    }

    private static HashSet ourJoiners = new HashSet();
    static{
        ourJoiners.add("and");
        ourJoiners.add("и");
        ourJoiners.add('и');
    }

    private static HashSet ourNeedToTrim = new HashSet();
    static{
        ourNeedToTrim.add(')');
        ourNeedToTrim.add('(');
        ourNeedToTrim.add('[');
        ourNeedToTrim.add(']');
    }

    private static HashSet ourTypesSeparators = new HashSet();
    static{
        ourTypesSeparators.add("!sep!");
    }

    public static boolean isTypesSeparator(String value){
        if(ourTypesSeparators.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isNeedToTrim(char value){
        if(ourNeedToTrim.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isSepatator(String value){
        if(ourSeparators.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isSepatator(char value){
       if(ourSeparators.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isJoiner(String value){
        if(ourJoiners.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isJoiner(char value){
        if(ourJoiners.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public enum StringType{
        word,
        separator,
        joiner,
        typesSeparator;
    }
}
