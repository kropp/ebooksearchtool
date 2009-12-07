package org.ebooksearchtool.analyzer.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.*;

/**
 * @author Алексей
 */

public class AnalyzeUtils {

    // <editor-fold defaultstate="collapsed" desc="Language utils">
    private static HashSet<Language> myLanguages = new HashSet<Language>();
    static{
       myLanguages.addAll(createLanguageList());
    }

    public static boolean isLanguage(String value){
        for(Language lang : myLanguages){
            if(lang.getFullDescription().equals(value) || lang.getShortDescriptions().contains(value)){
                return true;
            }
        }
        return false;
    }

    public static String normalizeLanguage(String value){
        for(Language lang : myLanguages){
            if(lang.getFullDescription().equals(value) || lang.getShortDescriptions().contains(value)){
                return lang.getShortDescriptions().get(0);
            }
        }
        return "";
    }

    private static ArrayList<Language> createLanguageList(){
        BufferedReader br = null;
        ArrayList<Language> languages = new ArrayList<Language>();
        try{
            try{
                br = new BufferedReader(new FileReader(AnalyzerProperties.getPropertie("languagesList")));
                String str = br.readLine();
                while(str.charAt(0) != 'a'){
                    str = str.substring(1);
                }
                while(str != null){
                    languages.add(parseLanguage(str));
                    str = br.readLine();
                }
            }catch(IOException ex){
                Logger.setToErrorLog(ex.getMessage());
            }finally{
                br.close();
            }
        }catch(IOException ex){
            Logger.setToErrorLog(ex.getMessage());
        }
        return languages;
    }

    private static Language parseLanguage(String str){
        Language lang = new Language();
        int length = str.length();
        int index = 0;
        StringBuilder buff = new StringBuilder();
        String pattern = "-";
        for (int i = 0; i < 3; i++) {
            while(index < length && str.charAt(index) != ' ' && str.charAt(index) != '\t' ){
                buff.append(str.charAt(index));
                index++;
            }
            if(buff.indexOf(pattern) == -1){
                lang.addShortDescription(buff.toString());
            }
            buff.delete(0, length);
            index++;
        }
        while(index < length - 1 && str.charAt(index) != ' ' && str.charAt(index) != '\t'){
                index++;
        }
        while(index < length - 1 && (str.charAt(index) == ' ' || str.charAt(index) == '\t' ||
                str.charAt(index) == '+' || isNumber(str.charAt(index)))){
                index++;
        }
        while(str.charAt(index) != ' ' && str.charAt(index) != '\t'){
            buff.append(str.charAt(index));
            index++;
        }
        if(buff.toString().equals("Modern") || buff.toString().equals("Church")
                || buff.toString().equals("Finnish") || buff.toString().equals("Hiri")
                || buff.toString().equals("Sichuan") || buff.toString().equals("Central")
                || buff.toString().equals("South")){
            buff.append(str.charAt(index));
            index++;
            while(str.charAt(index) != ' ' && str.charAt(index) != ',' && str.charAt(index) != '\t'){
                buff.append(str.charAt(index));
                index++;
            }
        }
        if(buff.charAt(buff.length() - 1) == ','){
            buff.delete(buff.length() - 1, buff.length());
        }
        lang.setFullDescription(buff.toString());

        return lang;
    }

    //</editor-fold>
//    public static boolean isNumber(char ch){
//        switch(ch){
//            case '0':{return true;}
//            case '1':{return true;}
//            case '2':{return true;}
//            case '3':{return true;}
//            case '4':{return true;}
//            case '5':{return true;}
//            case '6':{return true;}
//            case '7':{return true;}
//            case '8':{return true;}
//            case '9':{return true;}
//            default :{return false;}
//        }
//    }

    public static String bookInfoToString(BookInfo info){
       StringBuilder sb = new StringBuilder();

       sb.append("Title: " + info.getTitle() + AnalyzerProperties.getPropertie("systemSeparator"));
       List<Author> authors = info.getAuthors();
       int length = authors.size();
       sb.append("Authors: " + AnalyzerProperties.getPropertie("systemSeparator"));
       for (int i = 0; i < length; i++) {
           sb.append("    " + authors.get(i).getName() + AnalyzerProperties.getPropertie("systemSeparator"));
       }
       List<File> files = info.getFiles();
       length = files.size();
       sb.append("Files: " + AnalyzerProperties.getPropertie("systemSeparator"));
       for (int i = 0; i < length; i++) {
           sb.append("    Link: " + files.get(i).getLink() +"; " );
           sb.append("Size: " + files.get(i).getSize() +"; " );
           sb.append("Type: " + files.get(i).getType() + AnalyzerProperties.getPropertie("systemSeparator"));
       }
       sb.append("Language: " + info.getLanguage() + AnalyzerProperties.getPropertie("systemSeparator"));

       return sb.toString();
   }

    //    // <editor-fold defaultstate="collapsed" desc="Author utils">
//    private static HashSet<String> myAuthors;
//    static{
//        myAuthors.add("by");
//        myAuthors.add("author/");
//    }
//
//    public static boolean isAuthor(Lexema lex){
//        String str = lex.getValue();
//        for(String elem : myAuthors){
//            return str.indexOf(elem) != -1 ? true : false;
//        }
//        return false;
//    }
//    // </editor-fold>

    public static boolean isLetter(char let){
        if(let >= 'a' && let <= 'z'){
            return true;
        }
        return false;
    }

    public static boolean isNumber(char num){
        if(num >= '0' && num <= '9'){
            return true;
        }
        return false;
    }
}
