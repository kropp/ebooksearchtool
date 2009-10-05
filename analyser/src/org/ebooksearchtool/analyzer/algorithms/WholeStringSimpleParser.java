package org.ebooksearchtool.analyzer.algorithms;

import org.ebooksearchtool.analyzer.algorithms.subalgorithms.*;
import org.ebooksearchtool.analyzer.utils.Lexema;
import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.SpecialWords.*;

/**
 * @author Алексей
 */

public class WholeStringSimpleParser implements IParser{

    private ArrayList<String> myAuthors;
    private String myBookName;
    private String myUrl;
    private String myLanguage;
    
    public WholeStringSimpleParser(){
        myAuthors = new ArrayList<String>();
        myBookName = "";
        myUrl = "";
        myLanguage = "";
    }


    //TODO:Сделать парсинг целой строки, пока выводит пустой массив
    public ArrayList<String> parse(String input) {
        ArrayList<String> out = new ArrayList<String>();

        ArrayList<Lexema> temp = Lexema.convertToLexems(input);

        myUrl = URLsExtractor.extractURL(temp);
        myLanguage = LanguageExtractor.extractLanguage(temp);
        myAuthors = AuthorsExtractor.extractAuthors(temp);
        myBookName = TitleExtractor.extractTitle(temp);

        int length = myAuthors.size();
        StringBuilder tempString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            tempString.append(myAuthors.get(i));
            tempString.append(", ");
        }
        tempString.delete(tempString.length() - 2, tempString.length());

        out.add("Title: " + myBookName);
        out.add("Authors: " + tempString);
        out.add("URL: " + myUrl);
        out.add("Language: " + myLanguage);

        return out;
    }

    public ArrayList<String> getAuthors(){
        return myAuthors;
    }

    public String getBookName(){
        return myBookName;
    }

    public String getURL(){
        return myUrl;
    }

    public String getLanguage(){
        return myLanguage;
    }

}
