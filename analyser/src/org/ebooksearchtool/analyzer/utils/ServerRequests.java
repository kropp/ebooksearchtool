package org.ebooksearchtool.analyzer.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Алексей
 */

public class ServerRequests {

    public static String formBookInfo(BookInfo info) {
        StringBuilder str = new StringBuilder();
        String message = encodeSpecialSymbols(info.getBookInfo());
        str.append("POST " + "/data/insert " + "HTTP/1.0");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Host: " + AnalyzerProperties.getPropertie("serverAddress") +
                ":" + AnalyzerProperties.getPropertie("serverPort"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Length: " + getContentLength(message));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("xml=" + message);
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        System.out.println(str.toString());
        return str.toString();
    }

    public static String formBookInfoRequest(BookInfo info) {
        StringBuilder str = new StringBuilder();
        //TODO: Сделать функцию для запроса данных у сервера
        String message = encodeSpecialSymbols(info.getBookInfo());
        str.append("POST " + "/data/get " + "HTTP/1.0");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Host: " + AnalyzerProperties.getPropertie("serverAddress") +
                ":" + AnalyzerProperties.getPropertie("serverPort"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Length: " + getContentLength(message));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("xml=" + message);
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        System.out.println(str.toString());
        return str.toString();
    }

    //TODO: Доделать request для сервера
    public static String timeFoundRequest(BookInfo info) {
        StringBuilder str = new StringBuilder();
        String message = encodeSpecialSymbols(info.getBookInfoForRequest());
        str.append("POST " + "/data/insert " + "HTTP/1.0");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Host: " + AnalyzerProperties.getPropertie("serverAddress") +
                ":" + AnalyzerProperties.getPropertie("serverPort"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Length: " + getContentLength(message));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("xml=" + message);
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        System.out.println(str.toString());
        return str.toString();
    }

    private static long getContentLength(String str){
        return str.length() + 4;
    }

    private static String encodeSpecialSymbols(String message){
        try {
            return URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.setToErrorLog(ex.getMessage());
        }
        return null;
    }
}
