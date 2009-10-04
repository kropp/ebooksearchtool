package org.ebooksearchtool.analyzer.utils;

/**
 * @author Алексей
 */

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.SpecialWords.*;

public class Lexema {

    private String info;
    private StringType type;

    public Lexema(String input){
        info = input.trim();
        if(SpecialWords.isSepatator(input)){
            type = StringType.separator;
        }else{
            if(SpecialWords.isJoiner(input)){
                type = StringType.joiner;
            }else{
                type = StringType.word;
            }
        }
    }

    public Lexema(String input, StringType ty){
        type = ty;
        info = input;
    }

    public StringType getType(){
        return type;
    }

    public String getValue(){
        return info;
    }

    @Override
    public String toString(){
        return info;
    }

    public Lexema join(Lexema lex){
        return new Lexema(info + " " + lex.getValue(), StringType.word);
    }

    public static ArrayList<Lexema>convertToLexems(String input){
        ArrayList<Lexema> temp = new ArrayList<Lexema>();
        StringBuilder bd = new StringBuilder();
        int length = input.length();
        char ch = ' ';

        for (int i = 0; i < length; i++) {
            ch = input.charAt(i);
            if(ch != ' '){
                if(!SpecialWords.isSepatator(ch)){
                    bd.append(ch);
                }else{
                    temp.add(new Lexema(bd.toString()));
                    temp.add(new Lexema(ch + "",StringType.separator));
                    bd.delete(0, bd.length());
                }
            }else{
                if(bd.length() != 0){               //На случай прбела после запятой
                    temp.add(new Lexema(bd.toString()));
                    bd.delete(0, bd.length());
                }
            }
        }
        //Случай пробела в конце строки
        if(bd.length() != 0){
            temp.add(new Lexema(bd.toString()));
        }

        length = temp.size();
        for (int i = 0; i < length; i++) {
            //Здесь проверяются и удаляются пустые лексемы
            if(!temp.get(i).trim()){
                temp.remove(i);
                length--;
            }
        }

        return temp;
    }

    private boolean trim(){
        if(info != null){
            StringBuilder temp = new StringBuilder(info.trim());

            while(temp.length() != 0 && SpecialWords.isNeedToTrim(temp.charAt(0))){
                temp.deleteCharAt(0);
            }

            while(temp.length() != 0 && SpecialWords.isNeedToTrim(temp.charAt(temp.length() - 1))){
                temp.deleteCharAt(temp.length() - 1);
            }
            if(temp.length() != 0){
                info = temp.toString();
                return true;
            }
            return false;
        }
        return false;
    }
}
