package org.ebooksearchtool.analyzer.utils;

import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Алексей
 */

public class Messages {

    public static final String SEPARATOR = System.getProperty("line.separator");

    public static String formBookInfo(BookInfo info) {
        StringBuilder str = new StringBuilder();
        str.append("POST");
        str.append(Messages.SEPARATOR);
        str.append("<html>");
        str.append(Messages.SEPARATOR);
        str.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        str.append(Messages.SEPARATOR);
        str.append("<center>");
        str.append(Messages.SEPARATOR);
        str.append("<form action='http://127.0.0.1:8000/data/get/' method=POST>");
        str.append(Messages.SEPARATOR);
        str.append("<textarea name='xml' cols='90' rows='20'>");
        str.append(Messages.SEPARATOR);
        str.append(info.getBookInfo());
        str.append(Messages.SEPARATOR);
        str.append("</textarea>");
        str.append(Messages.SEPARATOR);
        str.append("<input type='submit' value='Lets Go!'>");
        str.append(Messages.SEPARATOR);
        str.append("</form>");
        str.append(Messages.SEPARATOR);
        str.append("</center>");
        str.append(Messages.SEPARATOR);
        str.append("</html>");
        str.append(Messages.SEPARATOR);
        str.append("HTTP/1.0");
        str.append(Messages.SEPARATOR);
        str.append(Messages.SEPARATOR);
        return str.toString();
    }
}
