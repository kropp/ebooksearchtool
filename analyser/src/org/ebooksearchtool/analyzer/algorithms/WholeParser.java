package org.ebooksearchtool.analyzer.algorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.*;
import org.ebooksearchtool.analyzer.model.*;
import org.ebooksearchtool.analyzer.network.ServerConnector;
import org.ebooksearchtool.analyzer.utils.RequestFormer;

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
        int currentBook = 0;

        //TODO:Часть 1, ищем инфу на странице
        myBookInfo.set(currentBook, extractInfo(temp));

        //TODO:Часть 2 Проверяем ее на сервере. Add matching for different
        //variants of BookInfo
        ArrayList<Author> myAuthors = new ArrayList<Author>();
        myAuthors.add(new Author("James Joyce"));
        Title myTitle = new Title("Title Dubliners");
        ArrayList<File> myFiles = new ArrayList<File>();
        myFiles.add(new File());
        String myLanguage = "";
        ArrayList<String> myAnnotations = new ArrayList<String>();
        return parseInfo(new BookInfo(myAuthors, myTitle, myFiles, myLanguage, myAnnotations));//myBookInfo.get(currentBook));
    }

    private BookInfo extractInfo(ArrayList<Lexema> input){
        BookInfo out = new BookInfo();
        out.addFile(new File(URLsExtractor.extractURL(input)));
        out.getFiles().get(0).setType(FormatExtractor.extractFormat(input));
        out.getFiles().get(0).setSize(SizeExtractor.extractSize(out.getFiles().get(0).getLink()));
        out.getFiles().get(0).setImgLink(BookCoverExtractor.extractBookCover(input));
        out.setAnnotations(AnnotationExtractor.extractAnnotation(input));
        out.setLanguage(LanguageExtractor.extractLanguage(input));
        out.setAuthors(AuthorExtractor.extractAuthors(input));
        out.setTitle(TitleExtractor.extractTitle(input));

        return out;
    }

    private BookInfo parseInfo(BookInfo info){
        ArrayList<BookInfo> reqBooks = new ArrayList<BookInfo>();
        reqBooks = BookInfo.getBooksInfoFromRequest(
                ServerConnector.sendRequest(RequestFormer.formBookInfoRequest(info), ServerConnector.GET_REQUEST));

        if(reqBooks.size() == 0){
            return myBookInfo.get(0);
        }

        return myBookInfo.get(0);
    }

}
