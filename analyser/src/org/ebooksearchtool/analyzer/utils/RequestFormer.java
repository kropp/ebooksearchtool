package org.ebooksearchtool.analyzer.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.BookInfo;

/**
 * @author Алексей
 */

public class RequestFormer {

    //TODO:Изменить формат(2 варианта)
    public static String formBookInfo(BookInfo info) {
        return encodeSpecialSymbols(info.getBookInfo());
    }

    /**
     * Forms request in string from <b>BookInfo</b>. 
     * @param info <b>BookInfo</b> to be formed
     * @param reqType type of request (now only Authors - 0 and Title - 1). See 
     * <b>BookInfo</b> for this types.
     * @return request in String form.
     */
    public static String formSearchRequest(BookInfo info, int reqType) {
        return encodeSpecialSymbols(info.getBookInfoForSearch(reqType));
    }

    private static String encodeSpecialSymbols(String message){
        try {
            return "xml=" + URLEncoder.encode(message, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.setToErrorLog(ex.getMessage() + 
                    ". Analyzer try to encode into unsupported encoding in " +
                    RequestFormer.class.getName() +" class.");
        }
        return null;
    }

    public static String getInitRequest(){
        //TODO:Add initial request in normal form
        return "";
    }

}
