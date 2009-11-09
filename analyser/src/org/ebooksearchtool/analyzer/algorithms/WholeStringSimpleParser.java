package org.ebooksearchtool.analyzer.algorithms;

import org.ebooksearchtool.analyzer.algorithms.subalgorithms.*;
import org.ebooksearchtool.analyzer.model.Lexema;
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
        myBookInfo.getFiles().get(0).setType(FormatExtractor.extractFormat(temp));
        myBookInfo.getFiles().get(0).setSize(SizeExtractor.extractSize(temp));
        myBookInfo.getFiles().get(0).setImgLink(BookCoverExtractor.extractBookCover(temp));
        myBookInfo.setAnnotations(AnnotationExtractor.extractAnnotation(temp));
        myBookInfo.setLanguage(LanguageExtractor.extractLanguage(temp));
        myBookInfo.setAuthors(AuthorExtractor.extractAuthors(temp));
        myBookInfo.setTitle(TitleExtractor.extractTitle(temp));

        return myBookInfo;
    }
}
