package org.ebooksearchtool.analyzer.network.demon;

/**
 * @author Алексей
 */

import org.ebooksearchtool.analyzer.algorithms.*;
import java.util.ArrayList;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.FormatExtractor;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.LanguageExtractor;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.URLsExtractor;
import org.ebooksearchtool.analyzer.io.Logger;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.model.*;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.network.ServerConnector;
import org.ebooksearchtool.analyzer.utils.BookInfoFormer;
import org.ebooksearchtool.analyzer.utils.NetUtils;

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
            ourBookInfo.setAuthors(AuthorsSimpleParser.parse(
                    Lexema.convertToLexems(new String(ch, start, length).trim())));
        }
        if(ourTitleElementFlag == true){
            ourBookInfo.setTitle(new String(ch, start, length).trim());
        }
        if(ourLinkElementFlag == true){
            ArrayList<Lexema> temp = Lexema.convertToLexems(new String(ch, start, length).trim());
            String link = URLsExtractor.extractURL(temp);
            if(link.length() != 0){
            ourBookInfo.addFile(new File(link, "",
                    FormatExtractor.extractFormat(temp), "", ""));
            }
        }
        if(ourAnnotationElementFlag == true){
            ourBookInfo.addAnnotation(new String(ch, start, length).trim());
        }
        if(ourLanguageElementFlag == true){
            ArrayList<Lexema> temp = new ArrayList<Lexema>();
            temp.add(new Lexema("Language"));
            temp.addAll(Lexema.convertToLexems(new String(ch, start, length).trim()));
            String lang = LanguageExtractor.extractLanguage(temp);
            if(lang.length() != 0){
            ourBookInfo.setLanguage(lang);
            }
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
        if(qName.equals("row")){
            ourRightElementFlag = false;
            if(!ourBookInfo.getFiles().isEmpty()){
                NetUtils.sendBookInfo(ourBookInfo);
                ourBookInfo = new BookInfo();
            }
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
