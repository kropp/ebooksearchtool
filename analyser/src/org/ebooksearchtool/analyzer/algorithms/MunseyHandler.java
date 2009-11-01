package org.ebooksearchtool.analyzer.algorithms;

/**
 * @author Алексей
 */

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.FormatExtractor;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.URLsExtractor;
import org.ebooksearchtool.analyzer.io.Logger;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.model.*;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.network.ClientSocketThread;
import org.ebooksearchtool.analyzer.utils.ServerRequests;

public class MunseyHandler extends DefaultHandler{

    private static boolean ourRightElementFlag = false;
    private static boolean ourTitleElementFlag = false;
    private static boolean ourAuthorElementFlag = false;
    private static boolean ourLinkElementFlag = false;
    private static boolean ourAnnotationElementFlag = false;
    private static boolean ourLanguageElementFlag = false;
    private static BookInfo ourBookInfo = new BookInfo();

    public MunseyHandler(){
        super();
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException{
        if(ourAuthorElementFlag == true){
            ourBookInfo.setAuthors(AuthorsSimpleParser.parse(new String(ch, start, length).trim()));
        }
        if(ourTitleElementFlag == true){
            ourBookInfo.setTitle(new String(ch, start, length).trim());
        }
        if(ourLinkElementFlag == true){
            ArrayList<Lexema> temp = Lexema.convertToLexems(new String(ch, start, length).trim());
            ourBookInfo.addFile(new File(URLsExtractor.extractURL(temp), "Unknown size",
                    FormatExtractor.extractFormat(temp), "Unknown info", "Unknown image link"));
        }
        if(ourAnnotationElementFlag == true){
            ourBookInfo.addAnnotation(new String(ch, start, length).trim());
        }
        //TODO:Вариант с уборкой неизвестных языков
        if(ourLanguageElementFlag == true){
            ourBookInfo.setLanguage(new String(ch, start, length).trim());
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
        if(qName.equals("row")){
            ourRightElementFlag = false;
        }
        if(qName.equals("field")){
            ourAuthorElementFlag = false;
            ourTitleElementFlag = false;
            ourAnnotationElementFlag = false;
            ourLinkElementFlag = false;
            ourLanguageElementFlag = false;
        }
//        if(!ourBookInfo.equals(new BookInfo())){
//
//        }
        String message = ClientSocketThread.sendRequest(ServerRequests.formBookInfo(ourBookInfo));
        Logger.setToLog(message);
        Logger.setToLog("Book Information succsesfully sent to server:" + AnalyzeUtils.bookInfoToString(ourBookInfo));
    }
    
    @Override
    public void startElement (String uri, String localName,
			      String qName, Attributes attributes)
	throws SAXException
    {
        if(qName.equals("row")){
            ourRightElementFlag = true;
        }
        if(qName.equals("field")){
            String str = attributes.getValue("name");
            if(str != null && ourRightElementFlag == true){
                if(str.equals("author")){
                    ourAuthorElementFlag = true;
                }
                if(str.equals("title")){
                    ourTitleElementFlag = true;
                }
                if(str.indexOf("format") != -1){
                    ourLinkElementFlag = true;
                }
                if(str.equals("descr")){
                    ourAnnotationElementFlag = true;
                }
                if(str.equals("language")){
                    ourLanguageElementFlag = true;
                }
            }
        }
    }

    @Override
    public void endDocument () throws SAXException {
    }
}
