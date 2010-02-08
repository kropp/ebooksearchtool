package org.ebooksearchtool.analyzer.algorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.*;
import org.ebooksearchtool.analyzer.model.*;
import org.ebooksearchtool.analyzer.network.ServerConnector;
import org.ebooksearchtool.analyzer.utils.BookInfoFormer;

/**
 * @author Aleksey Podolskiy
 */

public class WholeParser implements IParser{

    private ArrayList<BookInfo> myBookInfo;

    public WholeParser(){
        myBookInfo = new ArrayList<BookInfo>();
        myBookInfo.add(new BookInfo());
    }

    public BookInfo parse(String input) {
        ArrayList<Lexema> temp = Lexema.convertToLexems(input);
        ArrayList<BookInfo> reqBooks = new ArrayList<BookInfo>();
        int currentBook = 0;

        //TODO:Часть 1, ищем инфу на странице
        myBookInfo.get(currentBook).addFile(new File(URLsExtractor.extractURL(temp)));
        myBookInfo.get(currentBook).getFiles().get(0).setType(FormatExtractor.extractFormat(temp));
        myBookInfo.get(currentBook).getFiles().get(0).setSize(SizeExtractor.extractSize(temp));//TODO:или доставать из head
        myBookInfo.get(currentBook).getFiles().get(0).setImgLink(BookCoverExtractor.extractBookCover(temp));
        myBookInfo.get(currentBook).setAnnotations(AnnotationExtractor.extractAnnotation(temp));
        myBookInfo.get(currentBook).setLanguage(LanguageExtractor.extractLanguage(temp));
        myBookInfo.get(currentBook).setAuthors(AuthorExtractor.extractAuthors(temp));
        myBookInfo.get(currentBook).setTitle(TitleExtractor.extractTitle(temp));

        //TODO:Часть 2 Проверяем ее на сервере.

        reqBooks = BookInfo.getBooksInfoFromRequest(
                ServerConnector.sendRequest(BookInfoFormer.formBookInfoRequest(myBookInfo.get(currentBook)), 0));

        if(reqBooks.size() == 0){
            return myBookInfo.get(0);
        }

        return myBookInfo.get(0);
    }

}
