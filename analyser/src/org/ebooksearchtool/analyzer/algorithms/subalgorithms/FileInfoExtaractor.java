package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.List;
import org.ebooksearchtool.analyzer.model.File;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.utils.NetUtils;

/**
 * @author Aleksey Podolskiy
 */

public class FileInfoExtaractor {

    public static File extractFileInfo(List<Lexema> lexems){
        File file = new File();

        //Link extraction
        int length = lexems.size();
        for (int i = 0; i < length; i++) {
            Lexema lex = lexems.get(i);
            if(lex.getType().equals(Lexema.LexemaType.tagOpen) && lex.getValue().indexOf("link") == 0){
                file.setLink(trim(lex.getValue()));
                break;
            }
        }

        String link = file.getLink();
        //Size extraction
        file.setSize(String.valueOf(NetUtils.getContentSize(link)));

        //Format extraction
        StringBuilder str = new StringBuilder(link);
        int index = str.length() - 1;
        while(index > 0 && str.lastIndexOf(".") != index){
            str.deleteCharAt(index);
            index--;
        }
        if(index > 0 && index < link.length()){
            file.setType(link.substring(index + 1));
        }
        return file;
    }

    private static String trim(String s){
        StringBuilder sb = new StringBuilder(s);
        while(sb.length() > 0 && sb.indexOf("\"") != 0){
            sb.deleteCharAt(0);
        }
        while(sb.length() > 0 && sb.lastIndexOf("\"") != sb.length() - 1){
            sb.deleteCharAt(sb.length() - 1);
        }
        if(sb.length() > 2){
            sb.deleteCharAt(0);
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
