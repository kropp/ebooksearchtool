package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;

/**
 * @author Алексей
 */

public class SizeExtractor {

    public static String extractSize(ArrayList<Lexema> lexems){
        int length = lexems.size();
        int index = 0;
        for (int i = 0; i < length; i++) {
            if(i > 0 && hasSize(lexems.get(i).getValue())){
                String lex = lexems.get(i - 1).getValue() + lexems.get(i).getValue();
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

                
                if(lex.indexOf("KB") != -1){
                    lex = lex.substring(0, lex.length() - 2);
                    long size = Integer.parseInt(lex);
                    size*=1024;
                    lex = String.valueOf(size);
                }else{
                    lex = lex.substring(0, lex.length() - 2);
                    long size = Integer.parseInt(lex);
                    size*=1024;
                    size*=1024;
                    lex = String.valueOf(size);
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
        if(AnalyzeUtils.isNumber(ch)){
            return true;
        }
        switch(ch){
            case '.':{return true;}
            case ',':{return true;}
            default :{return false;}
        }
    }
}
