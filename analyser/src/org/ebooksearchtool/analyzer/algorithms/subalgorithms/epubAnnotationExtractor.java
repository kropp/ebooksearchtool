package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.utils.Lexema;

/**
 * @author Алексей
 */

public class epubAnnotationExtractor {

    public static List<String> extractAnnotation(ArrayList<Lexema> lexems){
        int length = lexems.size();
        int index = 0;
        for (index = 0; index < length; index++) {
            if(lexems.get(index).getValue().indexOf("Book") != -1 && lexems.get(index + 1).getValue().indexOf("Summary") != -1){
                break;
            }
        }

        StringBuilder sb = new StringBuilder();

        if(index < length){
            Lexema lex = new Lexema(lexems.get(index).getValue());
            index++;

            sb.append(lex.getValue() + " ");
            lex = lexems.get(index);

            while(index < length && (lex.getValue().indexOf("/div") == -1)){
                sb.append(lex.getValue() + " ");
                index++;
                lex = lexems.get(index);
            }
            sb.append(lex.getValue());
        }
        ArrayList<String> annotations = new ArrayList<String>();
        annotations.add(trim(sb));

        if(annotations.get(0).isEmpty()){
            annotations.clear();
            annotations.add(new String("Unknown annotation"));
        }

        return annotations;
    }

    private static String trim(StringBuilder sb){
        if(sb.lastIndexOf("<") != -1 && sb.lastIndexOf(">") != -1){
            while(sb.lastIndexOf("<") != (sb.length() - 1) && sb.lastIndexOf(">") != (sb.length() - 1)){
                    sb.delete(sb.length() - 1, sb.length());
            }
            while(true){
                if(sb.lastIndexOf("<") == (sb.length() - 1)){
                     sb.delete(sb.length() - 1, sb.length());
                }else{
                     if(sb.lastIndexOf(">") == (sb.length() - 1)){
                        while(sb.lastIndexOf("<") != (sb.length() - 1)){
                            sb.delete(sb.length() - 1, sb.length());
                        }
                        sb.delete(sb.length() - 1, sb.length());
                     }else{
                         break;
                     }
                }
            }
            while(sb.indexOf(">") != -1){
                sb.delete(0,1);
            }
        }

        return sb.toString();
    }

}

