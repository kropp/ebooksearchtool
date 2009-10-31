package org.ebooksearchtool.analyzer.utils;

import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Алексей
 */

public class Messages {

    public static final String SEPARATOR = System.getProperty("line.separator");

    public static String formBookInfo(BookInfo info) {
        StringBuilder str = new StringBuilder();
        String message = info.getBookInfo();
        str.append("POST " + "/data/insert " + "HTTP/1.0");
        str.append(Messages.SEPARATOR);
        str.append("Host: " + "192.168.2.104:8000");
        str.append(Messages.SEPARATOR);
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(Messages.SEPARATOR);
        str.append("Content-Length: " + getContentLength(message));
        str.append(Messages.SEPARATOR);
        str.append(Messages.SEPARATOR);
        str.append("xml=" + message);
        str.append(Messages.SEPARATOR);
        str.append(Messages.SEPARATOR);

        System.out.println(str.toString());
        return str.toString();
    }

    public static String formBookInfoRequest(BookInfo info) {
        StringBuilder str = new StringBuilder();
        String message = info.getBookInfo();//TODO: Сделать функцию для запроса данных у сервера
        str.append("POST " + "/data/insert " + "HTTP/1.0");
        str.append(Messages.SEPARATOR);
        str.append("Host: " + "192.168.2.104:8000");
        str.append(Messages.SEPARATOR);
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(Messages.SEPARATOR);
        str.append("Content-Length: " + getContentLength(message));
        str.append(Messages.SEPARATOR);
        str.append(Messages.SEPARATOR);
        str.append("xml=" + message);
        str.append(Messages.SEPARATOR);
        str.append(Messages.SEPARATOR);

        System.out.println(str.toString());
        return str.toString();
    }

    private static long getContentLength(String str){
        return str.length() + 4;
    }
}
