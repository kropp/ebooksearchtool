package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;

/**
 * @author Алексей
 */

public class URLsExtractor {

    //TODO:Алгоритм дучше "2-х слэшевого"
    public static String extractURL(ArrayList<Lexema> lexems){
        int length = lexems.size();
        String buff = "";
        for (int i = 0; i < length; i++) {
            buff = lexems.get(i).getValue();
            if(AnalyzeUtils.isManySlashes(buff)){
                return trim(buff);
            }

        }
        return "";
    }

    private static String trim(String s){
        StringBuilder sb = new StringBuilder(s);
        int index = 0;
        while(sb.indexOf("://") != index){
            index++;
        }

        if(index < 1){
            return "";
        }
        index--;
        while(index >= 0 && AnalyzeUtils.isLetter(sb.charAt(index))){
            index--;
        }

        if(index >= 0){
            sb.delete(0, index + 1);
        }

        while(sb.length() != 0 && sb.indexOf("\"") != -1){
            sb.delete(sb.length() - 1, sb.length());
        }

        return sb.toString();
    }
}
