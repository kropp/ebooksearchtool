package org.ebooksearchtool.analyzer.utils;

import java.util.HashSet;

/**
 * @author Алексей
 */

public class AnalyzeUtils {
    static HashSet languages = new HashSet();
    static{
        languages.add("English");
        languages.add("Russian");
        languages.add("Русский");
        languages.add("Английский");
    }

    public static boolean isLanguage(String value){
        if(languages.contains(value)){
            return true;
        }else{
            return false;
        }
    }
}
