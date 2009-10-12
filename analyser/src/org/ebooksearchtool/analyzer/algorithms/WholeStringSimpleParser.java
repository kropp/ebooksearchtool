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

    public ArrayList<String> parse(String input) {
        ArrayList<String> out = new ArrayList<String>();

        ArrayList<Lexema> temp = Lexema.convertToLexems(input);

        myUrl = URLsExtractor.extractURL(temp);
        myLanguage = LanguageExtractor.extractLanguage(temp);
        myAuthors = epubAuthorExtractor.extractAuthors(temp);
        myBookName = epubTitleExtractor.extractTitle(temp);

        int length = myAuthors.size();
        StringBuilder tempString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            tempString.append(myAuthors.get(i));
            tempString.append(", ");
        }
        tempString.delete(tempString.length() - 2, tempString.length());

        out.add(myBookName);
        out.add(tempString.toString());
        out.add(myUrl);
        out.add(myLanguage);

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
