package org.ebooksearchtool.analyzer.algorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.*;
import org.ebooksearchtool.analyzer.model.*;

/**
 * @author Aleksey Podoplsky
 */

public class WholeParser implements IParser{

    private BookInfo myBookInfo;

    public WholeParser(){
        myBookInfo = new BookInfo();
    }

    public BookInfo parse(String input) {
        ArrayList<Lexema> temp = Lexema.convertToLexems(input);

        //В этой части мы должны гарантировать правильность найденной информации
        myBookInfo.addFile(new File(URLsExtractor.extractURL(temp)));
        myBookInfo.getFiles().get(0).setType(FormatExtractor.extractFormat(temp));
        myBookInfo.getFiles().get(0).setSize(SizeExtractor.extractSize(temp));
        myBookInfo.getFiles().get(0).setImgLink(BookCoverExtractor.extractBookCover(temp));
        myBookInfo.setAnnotations(epubAnnotationExtractor.extractAnnotation(temp));
        myBookInfo.setLanguage(LanguageExtractor.extractLanguage(temp));
        myBookInfo.setAuthors(epubAuthorExtractor.extractAuthors(temp));
        myBookInfo.setTitle(epubTitleExtractor.extractTitle(temp));

        return myBookInfo;
    }

}
