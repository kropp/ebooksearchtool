package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;

/**
 * @author Алексей
 */

public class LanguageExtractor {

   public static String extractLanguage(ArrayList<Lexema> lexems){
        int length = lexems.size();
        for (int i = 0; i < length; i++) {
            if(lexems.get(i).getValue().indexOf("Language") != -1){
                if(i < length -1 && AnalyzeUtils.isLanguage(trim(lexems.get(i+1).getValue()))){
                    return AnalyzeUtils.normalizeLanguage(trim(lexems.get(i+1).getValue()));
                }
            }
        }
        return new String("Unknown language");
    }

    private static String trim(String s){
        StringBuilder sb = new StringBuilder(s);
        while(sb.length() != 0 && sb.indexOf(" ") == 0){
            sb.delete(0, 1);
        }
        while(sb.length() != 0 && sb.indexOf("<") != (sb.length() - 1)){
            sb.delete(sb.length() - 1, sb.length());
        }
        if(sb.length() != 0){
            sb.delete(sb.length() - 1, sb.length());
        }
        
        return sb.toString();
    }
}
