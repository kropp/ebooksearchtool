package ru.podolsky.stringparser.algorithms;

import java.util.ArrayList;

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
        ArrayList<String> temp = new ArrayList<String>();
        ArrayList<String> out = new ArrayList<String>();
        StringBuilder bd = new StringBuilder();
        int length = input.length();
        char ch = ' ';

        for (int i = 0; i < length; i++) {
            ch = input.charAt(i);
            if(ch != ' '){
                if(ch != ','){
                    bd.append(ch);
                }else{

                    temp.add(bd.toString());
                    temp.add("|separator|");
                    bd.delete(0, bd.length());
                }
            }else{
                if(bd.length() != 0){               //На случай прбела после запятой
                    temp.add(bd.toString());
                    bd.delete(0, bd.length());
                }
            }
        }
        temp.add(bd.toString());

        length = temp.size();
        while (length != 0){
            //Случай, когда остался только первый элимент
            if(1 == length){
                out.add(temp.get(0));
                temp.remove(0);
            }else{
                //Случай с сепаратором
                if(temp.get(1).equals("|separator|")){
                    //Если следующее слово - завние
                    if(length < 4 || temp.get(3).equals("|separator|")){
                        out.add(temp.get(0) + "," + temp.get(2));
                        temp.remove(0);//Удаление готового элимента
                        temp.remove(0);//Удаление сепаратора
                        temp.remove(0);//Удаление завния
                        if(length > 3){
                            temp.remove(0);//Уаление второго сепаратора
                        }
                    }else{
                        out.add(temp.get(0));
                        temp.remove(0);//Удаление готового элимента
                        temp.remove(0);//Удаление сепаратора
                    }
                }else{
                    //Случай с соединителем
                    if(temp.get(1).equals("and")){
                        //TODO: До конца продумать случай однофамильцев
                        if(temp.get(0).indexOf(" ") == -1){
                            int i = 2;
                            while(i < length && !temp.get(i).equals("|separator|")){
                                i++;
                            }
                            out.add(temp.get(0) + " " + temp.get(i - 1));
                        }else{
                            out.add(temp.get(0));
                        }
                        temp.remove(0);//Удаление готового элимента
                        temp.remove(0);//Удаление and
                    }else{
                        //Просто слово
                        temp.set(0, temp.get(0) + " " + temp.get(1));
                        temp.remove(1);//Удаление добавленного слова
                    }
                }
            }

            length = temp.size();
        }
        return out;
    }

}
