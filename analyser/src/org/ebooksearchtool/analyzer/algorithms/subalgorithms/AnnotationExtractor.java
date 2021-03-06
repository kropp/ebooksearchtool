package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.model.Lexema;

/**
 * @author Aleksey Podolskiy
 */

public class AnnotationExtractor {

    public static List<String> extractAnnotation(ArrayList<Lexema> lexems){
        int length = lexems.size();
        int index = 0;
        for (index = 0; index < length; index++) {
            if(lexems.get(index).getValue().indexOf("Book") != -1 && lexems.get(index).getValue().indexOf("Summary") != -1){
                break;
            }
        }

        StringBuilder sb = new StringBuilder();

        if(index < length){
            Lexema lex = new Lexema(lexems.get(index).getValue());
            index++;

            sb.append(lex.getValue() + " ");

            while(index < length && (lex.getValue().indexOf("/div") == -1)){
                lex = lexems.get(index);
                sb.append(lex.getValue() + " ");
                index++;
            }
            sb.append(lex.getValue());
        }
        ArrayList<String> annotations = new ArrayList<String>();
        annotations.add(trim(sb));

        if(annotations.get(0).isEmpty()){
            annotations.clear();
            annotations.add(new String(""));
        }

        return annotations;
    }

    private static String trim(StringBuilder sb){
        if(sb.lastIndexOf("<") != -1 && sb.lastIndexOf(">") != -1){
            while(sb.length() != 0 && sb.lastIndexOf("<") != (sb.length() - 1) && sb.lastIndexOf(">") != (sb.length() - 1)){
                    sb.delete(sb.length() - 1, sb.length());
            }
            while(true){
                if(sb.length() != 0 && sb.lastIndexOf("<") == (sb.length() - 1)){
                     sb.delete(sb.length() - 1, sb.length());
                }else{
                     if(sb.length() != 0 && sb.lastIndexOf(">") == (sb.length() - 1)){
                        while(sb.lastIndexOf("<") != (sb.length() - 1)){
                            sb.delete(sb.length() - 1, sb.length());
                        }
                        sb.delete(sb.length() - 1, sb.length());
                     }else{
                         break;
                     }
                }
            }
            while(sb.length() != 0 && sb.indexOf(">") != -1){
                sb.delete(0,1);
            }
        }

        return sb.toString();
    }

}

