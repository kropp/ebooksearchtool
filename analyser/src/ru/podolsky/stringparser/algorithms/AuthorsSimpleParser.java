package ru.podolsky.stringparser.algorithms;

import java.util.ArrayList;
import ru.podolsky.stringparser.algorithms.SpecialWords.*;

/**
 * @author Алексей
 */

public class AuthorsSimpleParser implements IParser{

    private static AuthorsSimpleParser instance;

    private AuthorsSimpleParser(){

    }

    public static AuthorsSimpleParser Instance(){
        if(instance == null){
            instance = new AuthorsSimpleParser();
            return instance;
        }else{
            return instance;
        }
    }

    public ArrayList<String> parse(String input) {
        ArrayList<Lexema> temp = new ArrayList<Lexema>();
        ArrayList<String> out = new ArrayList<String>();
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
        temp.add(new Lexema(bd.toString()));

        length = temp.size();
        while (length != 0){
            //Случай, когда остался только первый элимент
            if(1 == length){
                out.add(temp.get(0).toString());
                temp.remove(0);
            }else{
                //Случай с сепаратором
                if(temp.get(1).getType().equals(StringType.separator)){
                    //Если следующее слово - завние
                    if(length < 4 || temp.get(3).getType().equals(StringType.separator)){
                        out.add(temp.get(0).toString() + "," + temp.get(2).toString());
                        temp.remove(0);//Удаление готового элимента
                        temp.remove(0);//Удаление сепаратора
                        temp.remove(0);//Удаление завния
                        if(length > 3){
                            temp.remove(0);//Уаление второго сепаратора
                        }
                    }else{
                        out.add(temp.get(0).toString());
                        temp.remove(0);//Удаление готового элимента
                        temp.remove(0);//Удаление сепаратора
                    }
                }else{
                    //Случай с соединителем
                    if(temp.get(1).getType().equals(StringType.joiner)){
                        //TODO: До конца продумать случай однофамильцев
                        if(temp.get(0).getValue().indexOf(" ") == -1){
                            int i = 2;
                            while(i < length && !temp.get(i).getType().equals(StringType.joiner) && !temp.get(i).getType().equals(StringType.separator)){
                                i++;
                            }
                            out.add(temp.get(0).toString() + " " + temp.get(i - 1).toString());
                        }else{
                            out.add(temp.get(0).toString());
                        }
                        temp.remove(0);//Удаление готового элимента
                        temp.remove(0);//Удаление and
                    }else{
                        //Просто слово
                        temp.set(0, temp.get(0).join(temp.get(1)));
                        temp.remove(1);//Удаление добавленного слова
                    }
                }
            }

            length = temp.size();
        }
        return out;
    }

}
