package org.ebooksearchtool.analyzer.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
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
            Logger.setToErrorLog(ex.getMessage() + ". Try to encode into unsupported" +
                    "encoding in " + BookInfoFormer.class.getName() +" class.");
        }
        return null;
    }

    public static String initRequest(){
        try {
            StringBuilder message = new StringBuilder();
            message.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            message.append(AnalyzerProperties.getPropertie("system_separator"));
            message.append("<root>");
            message.append(AnalyzerProperties.getPropertie("system_separator"));
            message.append("<who_are_you>");
            message.append("</who_are_you>");
            message.append(AnalyzerProperties.getPropertie("system_separator"));
            message.append("</root>");
            message.append(AnalyzerProperties.getPropertie("system_separator"));
            return "xml=" + URLEncoder.encode(message.toString(), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.setToErrorLog(ex.getMessage() + ". Try to encode into unsupported" +
                    "encoding in " + BookInfoFormer.class.getName() +" class.");
        }
        return null;
    }
}
