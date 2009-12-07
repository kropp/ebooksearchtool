package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;

/**
 * @author Aleksey Podolskiy
 */

public class FormatExtractor {

    //TODO:Алгоритм дучше "2-х слэшевого"
    public static String extractFormat(ArrayList<Lexema> lexems){
        int length = lexems.size();
        String buff = "";
        for (int i = 0; i < length; i++) {
            buff = lexems.get(i).getValue();
            if(/*buff.indexOf("http") != -1 || */isManySlashes(buff)){
                buff = trim(buff);
                int position = buff.length() - 1;
                StringBuilder build = new StringBuilder();
                char ch = buff.charAt(position);
                while(ch != '.'){
                    build.append(ch);
                    position--;
                    ch = buff.charAt(position);
                }
                if(build.length() != 0){
                    (trimFormat(build)).reverse();
                    return build.toString();
                }
            }
        }
        return "Format not found";
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

    //TODO:Сделать вообще для всех символов, отличных от букв и цифр.
    private static StringBuilder trimFormat(StringBuilder sb){
        while(sb.length() > 0 && sb.indexOf("?") != -1){
            sb.deleteCharAt(0);
        }
        return sb;
    }
}
