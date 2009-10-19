package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.Lexema;

/**
 * @author Алексей
 */

public class FormatExtractor {

    public static String extractFormat(ArrayList<Lexema> lexems){
        int length = lexems.size();
        String buff = "";
        for (int i = 0; i < length; i++) {
            buff = lexems.get(i).getValue();
            if(buff.indexOf("http") != -1 || isManySlashes(buff)){
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
                    build.reverse();
                    return build.toString();
                }
            }
        }
        return "URL not found";
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
        while(sb.indexOf("\"") != (sb.length() - 1)){
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.delete(sb.length() - 1, sb.length());

        return sb.toString();
    }
}
