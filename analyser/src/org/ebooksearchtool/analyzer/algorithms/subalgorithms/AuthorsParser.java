package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.Author;
import org.ebooksearchtool.analyzer.model.Sentence;
import org.ebooksearchtool.analyzer.model.SpecialWords;
import org.ebooksearchtool.analyzer.model.SpecialWords.*;

/**
 * @author Aleksey Podolskiy
 */

public class AuthorsParser{
    public static ArrayList<Author> parse(String input) {
        //Часть, производящая разбор авторов
        ArrayList<String> out = new ArrayList<String>();
        ArrayList<Sentence> temp = SpecialWords.devide(input);

        int length = temp.size();
        while (length != 0){
            //Случай, когда остался только первый элимент
            if(1 == length){
                out.add(temp.get(0).getInfo());
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
                            out.add(temp.get(0).getInfo());
                            temp.remove(0);//Удаление готового элимента
                            temp.remove(0);//Удаление сепаратора
                        }
                    }
                }else{
                    //Случай с соединителем
                    if(temp.get(1).getType().equals(StringType.joiner)){
                        //TODO: До конца продумать случай однофамильцев, падеж и колличество слов в имени.
                        if(temp.get(0).getInfo().indexOf(" ") == -1){
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
                        temp.get(0).join(temp.get(1));
                        temp.remove(1);//Удаление добавленного слова
                    }
                }
            }
            length = temp.size();
        }

        //Часть, отвечающая за обработку ошибок в написании имени авторов
        length = out.size();
        StringBuilder tmp;
        //Деление авторов без пробелов
        for (int i = 0; i < length; i++) {
            tmp = new StringBuilder(out.get(i));
            int len = tmp.length();
            for (int j = 1; j < len; j++) {
                if(Character.isUpperCase(tmp.charAt(j)) &&
                        !Character.isUpperCase(tmp.charAt(j-1)) &&
                        tmp.charAt(j-1) != ' '){
                    tmp = tmp.insert(j," ");
                }
                if(tmp.charAt(j) == '.'){
                    tmp = tmp.insert(j + 1," ");
                }
            }
            out.set(i, tmp.toString().trim());
        }

        //Удаление символов, не являющихся буквами, из имен авторов.
        for (int i = 0; i < length; i++) {
            tmp = new StringBuilder(out.get(i));
            int len = tmp.length();
            for (int j = 0; j < len; j++) {
                if(!Character.isLetter(tmp.charAt(j)) &&
                        !SpecialWords.isJoiner(tmp.charAt(j)) &&
                        !SpecialWords.isSepatator(tmp.charAt(j)) &&
                        tmp.charAt(j) != '.'){
                    while(j < tmp.length() && tmp.charAt(j) != ' '){
                        tmp.deleteCharAt(j);
                    }
                    len = tmp.length();
                }
            }
            if(len > 0){
                out.set(i, tmp.toString());
            }else{
                out.remove(i);
                length--;
            }
        }

        //Приведение авторов в единый вид(большие и маленькие буквы)
        for (int i = 0; i < length; i++) {
            tmp = new StringBuilder(out.get(i).toLowerCase());
            int len = tmp.length();
            char t = tmp.charAt(0);
            tmp.deleteCharAt(0);
            tmp.insert(0, Character.toUpperCase(t));
            for (int j = 1; j < len; j++) {
                if (tmp.charAt(j) != ' ' && tmp.charAt(j - 1) == ' '){
                    t = tmp.charAt(j);
                    tmp.deleteCharAt(j);
                    tmp.insert(j, Character.toUpperCase(t));
                }
            }
            out.set(i, tmp.toString());
        }

        //Расстановка точек после инициалов авторов
        for (int i = 0; i < length; i++) {
            tmp = new StringBuilder(out.get(i));
            int len = tmp.length();
            //For first symbol
            if(tmp.charAt(1) == ' ' && tmp.charAt(0) != ' ') {
                    tmp.insert(1, '.');
            }
            //For all other symbols
            for (int j = 1; j < len - 1; j++) {
                if(tmp.charAt(j - 1) == ' ' && tmp.charAt(j) != ' ' &&
                        tmp.charAt(j + 1) == ' ') {
                    tmp.insert(j + 1, '.');
                }
            }
            if(tmp.charAt(len - 2) == ' ' && Character.isLetter(tmp.charAt(len - 1))){
                tmp.append('.');
            }
            out.set(i, tmp.toString());
        }

        ArrayList<Author> authors = new ArrayList<Author>();
        length = out.size();
        for (int i = 0; i < length; i++) {
            authors.add(new Author(out.get(i)));
        }

        return authors;
    }
}
