package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.model.Title;

/**
 * @author Aleksey Podolskiy
 */

public class TitleExtractor {

    public static Title extractTitle(ArrayList<Lexema> lexems){
        int length = lexems.size();
        int index = 0;
        for (index = 0; index < length; index++) {
            if(lexems.get(index).getValue().indexOf("book-page-title") != -1){
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

            return TitleParser.parse(trim(sb));
        }else{
            return new Title();
        }
    }

    private static String trim(StringBuilder sb){
        while(sb.length() != 0 && sb.indexOf(">") != 0){
            sb.delete(0, 1);
        }
        if(sb.length() != 0){
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
