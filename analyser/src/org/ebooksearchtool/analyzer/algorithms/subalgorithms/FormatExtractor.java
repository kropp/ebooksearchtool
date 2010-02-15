package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

/**
 * @author Aleksey Podolskiy
 */

public class FormatExtractor {

    public static String extractFormat(String link){
        StringBuilder str = new StringBuilder(link);
        int index = str.length() - 1;
        while(index > 0 && str.lastIndexOf(".") != index){
            str.deleteCharAt(index);
            index--;
        }
        if(index > 0 && index < link.length()){
            return link.substring(index + 1);
        }
        return "";
    }
}
