package org.ebooksearchtool.analyzer.model;

/**
 * @author Алексей
 */

import org.ebooksearchtool.analyzer.utils.*;
import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.SpecialWords.*;

public class Lexema {

    private String myInfo;
    private StringType myType;

    public Lexema(String input){
        myInfo = input.trim();
        if(SpecialWords.isSepatator(input)){
            myType = StringType.separator;
        }else{
            if(SpecialWords.isJoiner(input)){
                myType = StringType.joiner;
            }else{
                myType = StringType.word;
            }
        }
    }

    public Lexema(String input, StringType ty){
        myType = ty;
        myInfo = input;
    }

    public StringType getType(){
        return myType;
    }

    public String getValue(){
        return myInfo;
    }

    @Override
    public String toString(){
        return myInfo;
    }

    public Lexema join(Lexema lex){
        return new Lexema(myInfo + " " + lex.getValue(), StringType.word);
    }

    public static ArrayList<Lexema> convertToLexems(String input){
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
        if(myInfo != null){
            StringBuilder temp = new StringBuilder(myInfo.trim());

            while(temp.length() != 0 && SpecialWords.isNeedToTrim(temp.charAt(0))){
                temp.deleteCharAt(0);
            }

            while(temp.length() != 0 && SpecialWords.isNeedToTrim(temp.charAt(temp.length() - 1))){
                temp.deleteCharAt(temp.length() - 1);
            }
            if(temp.length() != 0){
                myInfo = temp.toString();
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean equals(Lexema lex){
        return myInfo.equals(lex.myInfo) && myType.equals(lex.myType);
    }

    public boolean equalsInfo(String str){
        return myInfo.equals(str);
    }
}
