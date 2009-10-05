package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.Lexema;

/**
 * @author Алексей
 */

public class URLsExtractor {

    public static String extractURL(ArrayList<Lexema> lexems){
        int length = lexems.size();
        String buff = "";
        for (int i = 0; i < length; i++) {
            buff = lexems.get(i).getValue();
            if(buff.indexOf("http") != -1 || isManySlashes(buff)){
                return buff;
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
}
