package org.ebooksearchtool.analyzer.utils;

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

public class ServerAnswerHandler extends DefaultHandler{

    private static StringBuilder ourServerInfo = new StringBuilder();
    private static boolean ourNameFlag = false;
    private static boolean ourVersionFlag = false;
    private static boolean ourBuildFlag = false;

    public ServerAnswerHandler(){
        super();
    }

    @Override
    public void characters (char ch[], int start, int length) throws SAXException{
        if(ourNameFlag == true){
            ourServerInfo.append("Server name: " + new String(ch, start, length).trim() + ";");
        }
        if(ourVersionFlag == true){
            ourServerInfo.append("Server version: " + new String(ch, start, length).trim() + ";");
        }
        if(ourBuildFlag == true){
            ourServerInfo.append("Server build: " + new String(ch, start, length).trim() + ";");
        }
    }

    @Override
    public void endElement (String uri, String localName, String qName)
	throws SAXException
    {
        if(qName.equals("build")){
            System.out.println(ourServerInfo);
            Logger.setToLog("Server information: " + ourServerInfo.toString());
        }
        ourNameFlag = false;
        ourVersionFlag = false;
        ourBuildFlag = false;
    }
    
    @Override
    public void startElement (String uri, String localName,
			      String qName, Attributes attributes)
	throws SAXException
    {
        if(qName.equals("name")){
            ourNameFlag = true;
        }
        if(qName.equals("version")){
            ourVersionFlag = true;
        }
        if(qName.equals("build")){
            ourBuildFlag = true;
        }
    }

    @Override
    public void endDocument () throws SAXException {
    }
}
