package org.ebooksearchtool.analyzer.utils;

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
        StringBuilder sb = new StringBuilder(message);
        int length = sb.length();
        for (int i = 0; i < length; i++) {
            if(sb.charAt(i) == ';'){
                sb.deleteCharAt(i);
                sb.insert(i, "%3B");
            }
        }
        String test = AnalyzerProperties.getPropertie("systemSeparator") + 
                AnalyzerProperties.getPropertie("systemSeparator");
        while(sb.indexOf(test) != -1){
            sb.replace(sb.indexOf(test), sb.indexOf(test) + test.length(),
                    AnalyzerProperties.getPropertie("systemSeparator"));
        }

        return sb.toString();
    }
}
