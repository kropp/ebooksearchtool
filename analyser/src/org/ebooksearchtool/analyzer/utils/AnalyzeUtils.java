package org.ebooksearchtool.analyzer.utils;

import java.util.HashSet;

/**
 * @author Алексей
 */

public class AnalyzeUtils {
    private static HashSet myLanguages = new HashSet();
    static{
        myLanguages.add("English");
        myLanguages.add("Russian");
        myLanguages.add("Русский");
        myLanguages.add("Английский");
    }

    public static boolean isLanguage(String value){
        if(myLanguages.contains(value)){
            return true;
        }else{
            return false;
        }
    }
}
