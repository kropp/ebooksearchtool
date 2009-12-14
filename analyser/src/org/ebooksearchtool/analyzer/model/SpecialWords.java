package org.ebooksearchtool.analyzer.model;

import java.util.HashSet;

/**
 * @author Алексей
 */

public class SpecialWords {

    private static HashSet<String> ourSeparators = new HashSet<String>();
    static{
        ourSeparators.add(",");
        //ourSeparators.add("'");
        //ourSeparators.add('\'');
    }

    private static HashSet<String> ourJoiners = new HashSet<String>();
    static{
        ourJoiners.add("and");
        ourJoiners.add("и");
    }

    private static HashSet<String> ourNeedToTrim = new HashSet<String>();
    static{
        ourNeedToTrim.add(")");
        ourNeedToTrim.add("(");
        ourNeedToTrim.add("[");
        ourNeedToTrim.add("]");
    }

    private static HashSet<String> ourHTMLSymbols = new HashSet<String>();
    static{
        ourNeedToTrim.add("<");
        ourNeedToTrim.add(">");
    }

    public static boolean isNeedToTrim(char value){
        if(ourNeedToTrim.contains(value + "")){
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
       if(ourSeparators.contains(value + "")){
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
        if(ourJoiners.contains(value + "")){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isHTMLSymbol(String value){
        if(ourHTMLSymbols.contains(value + "")){
            return true;
        }else{
            return false;
        }
    }
    public enum StringType{
        word,
        separator,
        joiner,
        typesSeparator,
        HTMLSymbol;
    }
}
