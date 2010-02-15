package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Lexema;

/**
 * @author Aleksey Podolskiy
 */

public class BookCoverExtractor {

    public static String extractBookCover(ArrayList<Lexema> lexems){
        int length = lexems.size();
        int index = 0;
        for (index = 0; index < length - 2; index++) {
            if(lexems.get(index).getValue().indexOf("Book") != -1 && lexems.get(index + 1).getValue().indexOf("Cover") != -1
                    && lexems.get(index + 2).getValue().indexOf("Image") != -1){
                break;
            }
        }

        if (index < length){
            StringBuilder sb = new StringBuilder();

            Lexema lex = new Lexema(lexems.get(index).getValue());
            index++;

            sb.append(lex.getValue() + " ");

            while(index < length) {
                lex = lexems.get(index);
                if(lex.getValue().indexOf("<") == -1) {
                    sb.append(lex.getValue() + " ");
                }else{
                    break;
                }
                index++;
            }
            sb.append(lex.getValue());

            return trim(sb, extractURL(lexems));
        }else{
            return "";
        }
    }

    private static String trim(StringBuilder s, String str){
        if (str.equals("")){
            if(s.indexOf("http") == -1){
                return "";
            }else{
                return trimURL(s.toString());
            }
        }
        while(s.length() != 0 && s.indexOf("/") != 0){
            s.delete(0, 1);
        }
        while(s.length() != 0 && s.lastIndexOf("\"") != (s.length() - 1)){
            s.delete(s.length() - 1, s.length());
        }

        StringBuilder sb = new StringBuilder();
        
        if(s.length() != 0){
            s.delete(s.length() - 1, s.length());
            
            int index = str.indexOf(".");
            while(str.charAt(index) != '/'){
                index++;
            }
            sb.append(str.subSequence(0, index));
            sb.append(s);
        }

        return sb.toString();
    }

    private static String extractURL(ArrayList<Lexema> lexems){
        int length = lexems.size();
        String buff = "";
        for (int i = 0; i < length; i++) {
            buff = lexems.get(i).getValue();
            if(buff.indexOf("http") != -1 || isManySlashes(buff)){
                return trimURL(buff);
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

    private static String trimURL(String s){
        StringBuilder sb = new StringBuilder(s);
        while(sb.indexOf("http") != 0){
            sb.delete(0, 1);
        }
        while(sb.indexOf("\"") != (sb.length() - 1)){
            sb.delete(sb.length() - 1, sb.length());
        }
        if(sb.length() > 0){
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }
}
