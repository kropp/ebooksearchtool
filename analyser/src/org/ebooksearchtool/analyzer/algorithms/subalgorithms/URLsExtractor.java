package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Lexema;

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
            if(buff.indexOf("http") != -1 || isManySlashes(buff)){
                return trim(buff);
            }

        }
        return "";
    }


    private static boolean isManySlashes(String input){
        int i = 0;
        int position = input.indexOf("\\");
        while(position != -1){
            i++;
            input.replaceFirst("\\", "a");
            position = input.indexOf("\\");
        }

        if(i > 2){
            return true;
        }else{
            return false;
        }
    }

    private static String trim(String s){
        StringBuilder sb = new StringBuilder(s);
        while(sb.indexOf("http") != 0){
            sb.delete(0, 1);
        }
        while(sb.length() != 0 && sb.indexOf("\"") != -1){
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }
}
