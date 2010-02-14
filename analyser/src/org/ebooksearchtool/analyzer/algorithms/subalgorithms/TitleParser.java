package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import org.ebooksearchtool.analyzer.model.Title;

/**
 * @author Aleksey Podolskiy
 */

public class TitleParser {
    public static Title parse(String input){
        //Приведение названия в единый вид(большие и маленькие буквы)
        Title tit = new Title();
        int length = input.length();
        StringBuilder temp = new StringBuilder(input.toLowerCase());
        char t = temp.charAt(0);
        temp.deleteCharAt(0);
        temp.insert(0, Character.toUpperCase(t));
        for (int i = 1; i < length; i++) {
            if(temp.charAt(i) != ' ' && temp.charAt(i - 1) == ' '){
                t = temp.charAt(i);
                temp.deleteCharAt(i);
                temp.insert(i, Character.toUpperCase(t));
            }
        }


        tit.setName(temp.toString());
        return tit;
    }
}
