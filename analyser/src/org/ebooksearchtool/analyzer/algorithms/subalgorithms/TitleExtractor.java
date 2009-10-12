package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.Lexema;

/**
 * @author Алексей
 */

public class TitleExtractor {

    public static String extractTitle(ArrayList<Lexema> lexems){
        int length = lexems.size();
        int index = 0;
        for (index = 0; index < length; index++) {
            if(lexems.get(index).getValue().equals("Title")){
                break;
            }
        }

        Lexema lex = new Lexema("");
        index++;

        StringBuilder sb = new StringBuilder();
        while(index < length && !lex.equalsInfo("!sep!")){
            sb.append(lex.getValue() + " ");
            lex = lexems.get(index);
            index++;
        }
        if(sb.length() == 0){
            return "Unknown title";
        }

        return sb.toString();
    }
}
