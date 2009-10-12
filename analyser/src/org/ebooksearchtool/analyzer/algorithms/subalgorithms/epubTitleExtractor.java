package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.Lexema;

/**
 * @author Алексей
 */

public class epubTitleExtractor {

    public static String extractTitle(ArrayList<Lexema> lexems){
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
            lex = lexems.get(index);

            while(index < length && (lex.getValue().indexOf("<") == -1)){
                sb.append(lex.getValue() + " ");
                index++;
                lex = lexems.get(index);
            }
            sb.append(lex.getValue());

            return trim(sb);
        }else{
            return "Unknown author";
        }
    }

    private static String trim(StringBuilder sb){
        while(sb.indexOf(">") != 0){
            sb.delete(0, 1);
        }
        sb.delete(0, 1);
        while(sb.indexOf("<") != (sb.length() - 1)){
            sb.delete(sb.length() - 1, sb.length());
        }
        sb.delete(sb.length() - 1, sb.length());

        return sb.toString();
    }
}
