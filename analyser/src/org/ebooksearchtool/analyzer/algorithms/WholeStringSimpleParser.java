package org.ebooksearchtool.analyzer.algorithms;

import org.ebooksearchtool.analyzer.algorithms.subalgorithms.*;
import org.ebooksearchtool.analyzer.utils.Lexema;
import java.util.ArrayList;
import org.ebooksearchtool.analyzer.model.*;
import org.ebooksearchtool.analyzer.utils.SpecialWords.*;

/**
 * @author Алексей
 */

public class WholeStringSimpleParser implements IParser{

    private BookInfo myBookInfo;
    
    public WholeStringSimpleParser(){
        myBookInfo = new BookInfo();
    }

    public BookInfo parse(String input) {
        ArrayList<Lexema> temp = Lexema.convertToLexems(input);

        myBookInfo.addFile(new File(URLsExtractor.extractURL(temp)));
        myBookInfo.setLanguage(LanguageExtractor.extractLanguage(temp));
        myBookInfo.setAuthors(epubAuthorExtractor.extractAuthors(temp));
        myBookInfo.setTitle(epubTitleExtractor.extractTitle(temp));

        return myBookInfo;
    }
}
