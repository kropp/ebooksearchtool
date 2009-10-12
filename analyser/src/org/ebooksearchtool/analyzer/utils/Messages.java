package org.ebooksearchtool.analyzer.utils;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Алексей
 */

public class Messages {

    public static final String SEPARATOR = System.getProperty("line.separator");

    public static String formBookInfo(ArrayList<String> list) {
        StringBuilder str = new StringBuilder();
        str.append("POST");
        str.append(Messages.SEPARATOR);
        str.append("<html>");
        str.append(Messages.SEPARATOR);
        str.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
        str.append(Messages.SEPARATOR);
        str.append("<field name=\"messgetype\">" + "BookInfo" + "</field>");
        str.append(Messages.SEPARATOR);
        str.append("<field name=\"title\">" + list.get(0) + "</field>");
        str.append(Messages.SEPARATOR);
        str.append("<field name=\"author\">" + list.get(1) + "</field>");
        str.append(Messages.SEPARATOR);
        str.append("<field name=\"link\">" + list.get(2) + "</field>");
        str.append(Messages.SEPARATOR);
        str.append("<field name=\"language\">" + list.get(3) + "</field>");
        str.append(Messages.SEPARATOR);
        str.append("</html>");
        str.append(Messages.SEPARATOR);
        str.append("HTTP/1.0");
        str.append(Messages.SEPARATOR);
        str.append(Messages.SEPARATOR);
        return str.toString();
    }
}
