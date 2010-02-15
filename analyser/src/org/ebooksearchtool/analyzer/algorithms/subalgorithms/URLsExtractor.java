package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Lexema;

/**
 * @author Aleksey Podolskiy
 */

public class URLsExtractor {

    public static String extractURL(ArrayList<Lexema> lexems){
        int length = lexems.size();
        for (int i = 0; i < length; i++) {
            Lexema lex = lexems.get(i);
            if(lex.getType().equals(Lexema.LexemaType.tagOpen) && lex.getValue().indexOf("link") == 0){
                return trim(lex.getValue());
            }
        }
        return "";
    }

    private static String trim(String s){
        StringBuilder sb = new StringBuilder(s);
        while(sb.length() > 0 && sb.indexOf("\"") != 0){
            sb.deleteCharAt(0);
        }
        while(sb.length() > 0 && sb.lastIndexOf("\"") != sb.length() - 1){
            sb.deleteCharAt(sb.length() - 1);
        }
        if(sb.length() > 2){
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
