package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.Lexema;

/**
 * @author Алексей
 */

public class BookCoverExtractor {

    //TODO: Подумать, как брать ее с сайта, ПОКА ЧТО НЕ СПОЛЬЗОВАТЬ!!!
    public static String extractBookCover(ArrayList<Lexema> lexems){
        int length = lexems.size();
        for (int i = 0; i < length; i++) {
            if(lexems.get(i).getValue().indexOf("bookcover") != -1){
                return trim(lexems.get(i).getValue());
            }
        }
        return new String("Unknown cover");
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
