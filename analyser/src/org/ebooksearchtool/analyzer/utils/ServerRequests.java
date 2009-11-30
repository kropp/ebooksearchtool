package org.ebooksearchtool.analyzer.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Алексей
 */

public class ServerRequests {

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

    public static long getContentLength(String str){
        return str.length() + 4;
    }

    private static String encodeSpecialSymbols(String message){
        try {
            return "xml=" + URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.setToErrorLog(ex.getMessage());
        }
        return null;
    }

//    private static String formatPost(String message){
//            connect.setRequestMethod("POST");
//            connect.setRequestProperty( "Content-type", "application/x-www-form-urlencoded" );
//            connect.setRequestProperty( "Content-length", String.valueOf(getContentLength(message1)));
////        StringBuilder str = new StringBuilder();
////        str.append("POST " + "/data/insert " + "HTTP/1.0");
////        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
////        str.append("Host: " + AnalyzerProperties.getPropertie("serverAddress") +
////                ":" + AnalyzerProperties.getPropertie("serverPort"));
////        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
////        str.append("Content-Type: application/x-www-form-urlencoded");
////        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
////        str.append("Content-Length: " + getContentLength(message));
////        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
////        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
////        str.append("xml=" + message);
////        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
////        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
////
////        System.out.println(str.toString());
////        return str.toString();
//    }

//    public static String formatGet(String message){
//        StringBuilder str = new StringBuilder();
//        str.append("GET http://www.alexkosh.ucoz.ru HTTP/1.0\r\n" +
//                "Host: www.alexkosh.ucoz.ru \r\n" +
//                "Referer: http://www.alexkosh.ucoz.ru \r\n" +
//                "Cookie: income=1\r\n" +
//                "\r\n");
//        return str.toString();
//    }
}
