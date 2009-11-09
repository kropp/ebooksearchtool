package org.ebooksearchtool.analyzer.utils;

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
    //TODO:Сепаратор переделать или убрать совсем, это TYPE сепаратор!!!
//    private static HashSet<String> ourTypesSeparators = new HashSet<String>();
//    static{
//        ourTypesSeparators.add("!sep!");
//    }

//    public static boolean isTypesSeparator(String value){
//        if(ourTypesSeparators.contains(value)){
//            return true;
//        }else{
//            return false;
//        }
//    }

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

    public enum StringType{
        word,
        separator,
        joiner,
        typesSeparator;
    }
}
