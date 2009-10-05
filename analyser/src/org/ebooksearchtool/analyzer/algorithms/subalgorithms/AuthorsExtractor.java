package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.algorithms.AuthorsSimpleParser;
import org.ebooksearchtool.analyzer.utils.Lexema;

/**
 * @author Алексей
 */

public class AuthorsExtractor {

    public static ArrayList<String> extractAuthors (ArrayList<Lexema> lexems){
        int length = lexems.size();
        int index = 0;
        for (index = 0; index < length; index++) {
            if(lexems.get(index).getValue().equals("Author")){
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

        ArrayList<String> arr = AuthorsSimpleParser.Instance().parse(sb.toString());

        if(arr.isEmpty()){
            arr.add("Unknown author");
        }

        return arr;
    }
}
