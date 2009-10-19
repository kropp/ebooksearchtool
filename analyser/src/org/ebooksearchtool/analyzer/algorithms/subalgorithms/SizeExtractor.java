package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.Lexema;

/**
 * @author Алексей
 */

public class SizeExtractor {

    public static String extractSize(ArrayList<Lexema> lexems){
        int length = lexems.size();
        int index = 0;
        for (int i = 0; i < length; i++) {
            if(hasSize(lexems.get(i).getValue())){
                String lex = lexems.get(i).getValue();
                index = lex.indexOf("KB");
                if(index < 0){
                    index = lex.indexOf("MB");
                }
                if(index == 0){
                    break;
                }else{
                    lex = lex.substring(0, index + 2);
                    index--;
                }
                char ch = lex.charAt(index);
                while(index >= 0 && isNumber(ch)){
                    ch = lex.charAt(index);
                    index--;
                }
                
                lex = lex.substring(index + 1, lex.length());
                if(!isNumber(lex.charAt(0))){
                    lex = lex.substring(1, lex.length());
                }
                return lex;
            }
        }
        return new String("Unknown size");
    }

    private static boolean hasSize(String str){
        if(str.indexOf("KB") != -1 || str.indexOf("MB") != -1){
            return true;
        }
        return false;
    }

    private static boolean isNumber(char ch){
        switch(ch){
            case '0':{return true;}
            case '1':{return true;}
            case '2':{return true;}
            case '3':{return true;}
            case '4':{return true;}
            case '5':{return true;}
            case '6':{return true;}
            case '7':{return true;}
            case '8':{return true;}
            case '9':{return true;}
            default :{return false;}
        }
    }
}
