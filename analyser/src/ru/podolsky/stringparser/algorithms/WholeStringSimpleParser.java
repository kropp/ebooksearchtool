package ru.podolsky.stringparser.algorithms;

import ru.podolsky.stringparser.utils.Lexema;
import java.util.ArrayList;
import ru.podolsky.stringparser.utils.SpecialWords.*;

/**
 * @author Алексей
 */

public class WholeStringSimpleParser implements IParser{

    private static WholeStringSimpleParser instance;

    private WholeStringSimpleParser(){

    }

    public static WholeStringSimpleParser Instance(){
        if(instance == null){
            instance = new WholeStringSimpleParser();
            return instance;
        }else{
            return instance;
        }
    }

    //TODO:Сделать парсинг целой строки, пока выводит пустой массив
    public ArrayList<String> parse(String input) {
        ArrayList<String> out = new ArrayList<String>();

        ArrayList<Lexema> temp = Lexema.convertToLexems(input);

        

        return out;
    }

}
