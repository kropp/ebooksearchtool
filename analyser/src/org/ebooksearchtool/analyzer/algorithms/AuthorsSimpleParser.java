package org.ebooksearchtool.analyzer.algorithms;

import org.ebooksearchtool.analyzer.utils.Lexema;
import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.SpecialWords.*;

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
        ArrayList<String> out = new ArrayList<String>();
        
        ArrayList<Lexema> temp = Lexema.convertToLexems(input);
        
        int length = temp.size();
        while (length != 0){
            //Случай, когда остался только первый элимент
            if(1 == length){
                out.add(temp.get(0).toString());
                temp.remove(0);
            }else{
                //Случай с сепаратором
                if(temp.get(1).getType().equals(StringType.separator)){
                    //Случай, когда сепаратор - последний в строке
                    if(length == 2){
                        out.add(temp.get(0).toString());
                        temp.remove(0);//Удаление готового элимента
                        temp.remove(0);//Удаление сепаратора
                    }else{
                        //Если следующее слово - завние
                        if(length < 4 || temp.get(3).getType().equals(StringType.separator)){
                            out.add(temp.get(0).toString() + ", " + temp.get(2).toString());
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
