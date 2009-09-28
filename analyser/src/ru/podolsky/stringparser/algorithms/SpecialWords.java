package ru.podolsky.stringparser.algorithms;

import java.util.HashSet;

/**
 * @author Алексей
 */

public class SpecialWords {

    static HashSet separators = new HashSet();
    {   separators.add(",");
        separators.add("'");
    }

    static HashSet joiners = new HashSet();
    {   joiners.add("and");
        joiners.add("и");
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
