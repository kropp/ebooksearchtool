package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.algorithms.AuthorsSimpleParser;
import org.ebooksearchtool.analyzer.model.Author;
import org.ebooksearchtool.analyzer.model.Lexema;

/**
 * @author Алексей
 */

public class AuthorsExtractor {

    public static List<Author> extractAuthors (ArrayList<Lexema> lexems){
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

        List<Author> arr = AuthorsSimpleParser.parse(sb.toString());

        if(arr.isEmpty()){
            arr.add(new Author());
        }

        return arr;
    }
}
