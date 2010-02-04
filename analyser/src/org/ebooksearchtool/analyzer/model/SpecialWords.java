package org.ebooksearchtool.analyzer.model;

import java.util.ArrayList;
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
        if(ourHTMLSymbols.contains(value)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isHTMLSymbol(char value){
        if(ourHTMLSymbols.contains(value + "")){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isTag(String value){
        if(value.indexOf("<") == 0 && value.lastIndexOf(">") == value.length() - 1){
            return true;
        }
        return false;
    }
    
    public enum StringType{
        word,
        separator,
        joiner,
        typesSeparator,
        HTMLSymbol;
    }

    public static ArrayList<Sentence> devide(String input){
        int length = input.length();
        StringBuilder sb = new StringBuilder();
        ArrayList<Sentence> out = new ArrayList<Sentence>();
        Sentence current = new Sentence();

        for (int i = 0; i < length; i++) {
            if(input.charAt(i) == ' '){
                if(!current.getInfo().equals("")){
                    out.add(current);
                }
                current.clear();
                continue;
            }
            if(SpecialWords.isSepatator(input.charAt(i))){
                if(!current.getInfo().equals("")){
                    out.add(current);
                    out.add(new Sentence (input.charAt(i), StringType.separator));
                }
                current.clear();
                continue;
            }
            if(SpecialWords.isJoiner(input.charAt(i))){
                if(!current.getInfo().equals("")){
                    out.add(current);
                    out.add(new Sentence (input.charAt(i), StringType.joiner));
                }
                current.clear();
                continue;
            }
            current.setInfo(current.getInfo() + input.charAt(i));
        }

        length = out.size();
        for (int i = 0; i < length; i++) {
            if(SpecialWords.isJoiner(out.get(i).getInfo())){
               out.set(i, new Sentence(out.get(i).getInfo(), StringType.joiner));
            }
        }
        return out;
    }
}
