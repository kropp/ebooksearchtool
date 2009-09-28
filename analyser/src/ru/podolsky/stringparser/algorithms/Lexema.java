package ru.podolsky.stringparser.algorithms;

/**
 * @author Алексей
 */

import ru.podolsky.stringparser.algorithms.SpecialWords.*;

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
}
