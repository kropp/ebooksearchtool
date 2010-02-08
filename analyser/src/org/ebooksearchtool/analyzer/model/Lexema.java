package org.ebooksearchtool.analyzer.model;

/**
 * @author Алексей
 */

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.SpecialWords.*;

public class Lexema {

    private String myInfo;
    private LexemaType myType;

    public enum LexemaType{
        tagOpen,
        tagClose,
        sentence;
    }

    public Lexema(String input){
        myInfo = input.trim();
        if(SpecialWords.isTag(input)){
            if(myInfo.indexOf("</") == 0){
                myType = LexemaType.tagClose;
                myInfo = myInfo.substring(2, myInfo.length() - 1);
            }else{
                myType = LexemaType.tagOpen;
                myInfo = myInfo.substring(1, myInfo.length() - 1);
            }
        }else{
            myType = LexemaType.sentence;
        }
    }

    public Lexema(String input, LexemaType ty){
        myType = ty;
        myInfo = input;
    }

    public LexemaType getType(){
        return myType;
    }

    public String getValue(){
        return myInfo;
    }

    @Override
    public String toString(){
        return myInfo;
    }

//    public Lexema join(Lexema lex){
//        return new Lexema(myInfo + " " + lex.getValue(), StringType.word);
//    }

    //TODO: Переделать, сейчас нет соединителей.
    public static ArrayList<Lexema> convertToLexems(String input){
        ArrayList<Lexema> temp = new ArrayList<Lexema>();
        StringBuilder bd = new StringBuilder();
        int length = input.length();
        char ch = ' ';
        boolean isOpenTag = false;

        for (int i = 0; i < length; i++) {
            ch = input.charAt(i);
            switch(ch) {
                case '<' : {
                    //Предидущую часть записываем как информационную лексему
                    temp.add(new Lexema(bd.toString(), LexemaType.sentence));
                    //Разбор комментариев(нашили комментраий - пропускаем все до его конца)
                    //Сейчас - разбираем все теги и удаляем ненужные после разбора(см.далее)
//                    if(i + 3 < length && input.charAt(i + 1) == '!' &&
//                            input.charAt(i + 2) == '-' && input.charAt(i + 3) == '-'){
//                        while(i < length && input.charAt(i) == '-' &&
//                            input.charAt(i + 1) == '-' && input.charAt(i + 2) == '>'){
//                            i++;
//                        }
//                        if(i + 3 < length){
//                            i+=3;
//                        }
//                    }
                    //Проверяем, какой тип тега встретили(откр./закр.)
                    if(i + 1 < length && input.charAt(i + 1) == '/'){
                        isOpenTag = false;
                        i++;
                    }else{
                        isOpenTag = true;
                    }
                    bd.delete(0, bd.length());
                    break;
                }
                case '>' : {
                    //Добавлем лексему соответствующего типа тега.
                    temp.add(new Lexema(trimTag(bd), isOpenTag ? LexemaType.tagOpen :
                            LexemaType.tagClose));
                    isOpenTag = false;
                    bd.delete(0, bd.length());
                    break;
                }
                default : {
                    bd.append(ch);
                }
            }
        }
        //TODO:ПРОВЕРИТЬ!!
        temp.add(new Lexema(bd.toString(), LexemaType.sentence));
        //Удаление ненужных тегов.
        length = temp.size();
        for (int i = 0; i < length; i++) {
            if(temp.get(i).getValue().indexOf("!--") == 0 ||
                    temp.get(i).getValue().indexOf("script") == 0) {
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

    private static String trimTag(StringBuilder sb){
        sb = new StringBuilder(sb.toString().trim());
        if(sb.length() > 2){
            if(sb.indexOf("</") == 0 && sb.length() > 3){
                sb.delete(0, 2);
            }else{
                sb.deleteCharAt(0);
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString().trim();
    }
}
