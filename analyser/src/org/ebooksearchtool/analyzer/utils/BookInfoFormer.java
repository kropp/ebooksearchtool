package org.ebooksearchtool.analyzer.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Алексей
 */

public class BookInfoFormer {

    //TODO:Изменить формат(2 варианта)
    public static String formBookInfo(BookInfo info) {
        return encodeSpecialSymbols(info.getBookInfo());
    }

    public static String formBookInfoRequest(BookInfo info) {
        return encodeSpecialSymbols(info.getBookInfoForRequest());
    }

    public static String formBookByIDRequest(BookInfo info, String id){
        return encodeSpecialSymbols(info.getBookInfoForBookIDRequest(id));
    }

    public static String formBookByFileIDRequest(BookInfo info, String id){
        return encodeSpecialSymbols(info.getBookInfoForFileIDRequest(id));
    }

    public static String formBookByAuthorIDRequest(BookInfo info, String id){
        return encodeSpecialSymbols(info.getBookInfoForAuthorIDRequest(id));
    }

    public static String formBookByBookIDReplace(BookInfo info, String id){
        return encodeSpecialSymbols(info.getBookInfoForBookIDReplace(id));
    }

    private static String encodeSpecialSymbols(String message){
        try {
            return "xml=" + URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.setToErrorLog(ex.getMessage());
        }
        return null;
    }
}
