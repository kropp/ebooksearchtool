package org.ebooksearchtool.analyzer.network.demon;

/**
 * @author Алексей
 */

import java.io.IOException;
import java.io.RandomAccessFile;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.AuthorsParser;
import org.ebooksearchtool.analyzer.io.Logger;
import java.util.ArrayList;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.LanguageExtractor;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.TitleParser;
import org.ebooksearchtool.analyzer.algorithms.subalgorithms.FileInfoExtaractor;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.ebooksearchtool.analyzer.model.*;
import org.ebooksearchtool.analyzer.model.Lexema;
import org.ebooksearchtool.analyzer.utils.AnalyzerProperties;
import org.ebooksearchtool.analyzer.utils.NetUtils;

public class MunseyHandler extends DefaultHandler{

    private static boolean ourRightElementFlag = false;
    private static boolean ourTitleElementFlag = false;
    private static boolean ourAuthorElementFlag = false;
    private static boolean ourLinkElementFlag = false;
    private static boolean ourAnnotationElementFlag = false;
    private static boolean ourLanguageElementFlag = false;
    private static BookInfo ourBookInfo = new BookInfo();
    private static boolean ourTestStatus = false;

    public MunseyHandler(){
        super();
    }

    public MunseyHandler(boolean testStatus){
        super();
        ourTestStatus = testStatus;
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException{
        if(ourAuthorElementFlag == true){
            ourBookInfo.setAuthors(AuthorsParser.parse(
                   new String(ch, start, length).trim()));
        }
        if(ourTitleElementFlag == true){
            ourBookInfo.setTitle(TitleParser.parse(new String(ch, start, length).trim()));
        }
        if(ourLinkElementFlag == true){
            String temp = new String(ch, start, length).trim();
            File link = FileInfoExtaractor.extractFileInfo(Lexema.convertToLexems("<link src=\"" + temp + "\">"));
            if(link.getLink().length() != 0){
            ourBookInfo.addFile(link);
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
                if(ourTestStatus == false){
                    NetUtils.sendBookInfo(ourBookInfo);
                    ourBookInfo = new BookInfo();
                }else{
                    RandomAccessFile file = null;
                    try {
                        file = new RandomAccessFile("munseytest.tst", "rws");
                        long length = file.length();
                        while(length > 0){
                            if(1000000 > length){
                                file.read(new byte[1000000]);
                                length-=1000000;
                            }else{
                                file.read(new byte[(int)length]);                                
                            }
                        }
                        file.writeBytes(ourBookInfo.getTitle().getName());
                        file.writeBytes(AnalyzerProperties.getPropertie("system_separator"));
                        length = ourBookInfo.getAuthors().size();
                        for (int i = 0; i < length; i++) {
                            file.writeBytes(ourBookInfo.getAuthors().get(i).getName());
                            file.writeBytes(AnalyzerProperties.getPropertie("system_separator"));
                        }
                        file.writeBytes(AnalyzerProperties.getPropertie("system_separator"));
                        file.writeBytes(AnalyzerProperties.getPropertie("system_separator"));
                        file.close();
                    } catch (IOException ex) {
                        Logger.setToLog(ex.getMessage() + ". Occurs in authors test");
                    } finally {
                        if(!file.equals(null)){
                            try {
                                file.close();
                            } catch (IOException ex) {
                                //Exception never thrown
                            }
                        }
                    }
                }
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
