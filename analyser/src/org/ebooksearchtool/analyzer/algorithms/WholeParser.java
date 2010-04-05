package org.ebooksearchtool.analyzer.algorithms;

import java.util.ArrayList;
import java.util.List;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.*;
import org.ebooksearchtool.analyzer.model.*;
import org.ebooksearchtool.analyzer.network.ServerConnector;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;
import org.ebooksearchtool.analyzer.utils.RequestFormer;

/**
 * @author Aleksey Podolskiy
 */

public class WholeParser implements IParser{

    //TODO:Add using of Req fields
    private ArrayList<BookInfo> myBookInfo;
    private List<Author> myReqAuthors;
    private List<Title> myReqTitles;

    public WholeParser(){
        myBookInfo = new ArrayList<BookInfo>();
        myBookInfo.add(new BookInfo());
    }

    public BookInfo parse(String input) {
        ArrayList<Lexema> temp = Lexema.convertToLexems(input);
        int currentBook = 0;

        //TODO:Часть 1, ищем инфу на странице
        //myBookInfo.set(currentBook, extractInfo(temp));

        //TODO:Часть 2 Проверяем ее на сервере. Add matching for different
        //variants of BookInfo
        //For test
        ArrayList<Author> myAuthors = new ArrayList<Author>();
        myAuthors.add(new Author("James Joyce"));
        Title myTitle = new Title("Title Dubliners");
        ArrayList<File> myFiles = new ArrayList<File>();
        myFiles.add(new File());
        String myLanguage = "";
        ArrayList<String> myAnnotations = new ArrayList<String>();
        myBookInfo.set(currentBook, new BookInfo(myAuthors, myTitle, myFiles, myLanguage, myAnnotations));
        return parseInfo(myBookInfo.get(currentBook));
    }

    private BookInfo extractInfo(ArrayList<Lexema> input){
        BookInfo out = new BookInfo();
        out.addFile(FileInfoExtaractor.extractFileInfo(input));
        out.getFiles().get(0).setImgLink(BookCoverExtractor.extractBookCover(input));
        out.getFiles().get(0).setTimeFound(AnalyzeUtils.getCurrentDate());
        out.setAnnotations(AnnotationExtractor.extractAnnotation(input));
        out.setLanguage(LanguageExtractor.extractLanguage(input));
        out.setAuthors(AuthorExtractor.extractAuthors(input));
        out.setTitle(TitleExtractor.extractTitle(input));

        return out;
    }

    //TODO:now it used only authors request.
    private BookInfo parseInfo(BookInfo info){
        ArrayList<BookInfo> reqBooks = new ArrayList<BookInfo>();
        ServerAnswerParser.parse(ServerConnector.sendRequest(
                RequestFormer.formSearchRequest(info, BookInfo.AUTHORS),
                ServerConnector.GET_REQUEST));
//        reqBooks = BookInfo.getBooksInfoFromRequest(
//                ServerConnector.sendRequest(RequestFormer.formBookInfoRequest(info), ServerConnector.GET_REQUEST));

        if(reqBooks.size() == 0){
            return myBookInfo.get(0);
        }

        return myBookInfo.get(0);
    }

    public void setRequestedInfo(List<Author> authors, List<Title> titles){
        myReqAuthors = authors;
        myReqTitles = titles;
    }
}
