package org.ebooksearchtool.analyzer.algorithms;

import org.ebooksearchtool.analyzer.utils.Lexema;
import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.SpecialWords.*;

/**
 * @author Алексей
 */

public class WholeStringSimpleParser implements IParser{

    private static WholeStringSimpleParser instance;

    private ArrayList<String> authors;
    private String bookName;
    private String url;
    private String language;
    
    public WholeStringSimpleParser(){
        authors = new ArrayList<String>();
        bookName = "";
        url = "";
        language = "";
    }


    //TODO:Сделать парсинг целой строки, пока выводит пустой массив
    public ArrayList<String> parse(String input) {
        ArrayList<String> out = new ArrayList<String>();

        ArrayList<Lexema> temp = Lexema.convertToLexems(input);

        url = URLsExtractor.extractURL(temp);

        out.add("URL: " + url);

        return out;
    }

    public ArrayList<String> getAuthors(){
        return authors;
    }

    public String getBookName(){
        return bookName;
    }

    public String getURL(){
        return url;
    }

    public String getLanguage(){
        return language;
    }

}
