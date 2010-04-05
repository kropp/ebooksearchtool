package org.ebooksearchtool.analyzer.model;

/**
 * @author Алексей
 */

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.ebooksearchtool.analyzer.io.Logger;
import org.xml.sax.SAXException;
public class ServerAnswerParser {
    public static void parse(String input){
        try {
            SAXParserFactory factory1 = SAXParserFactory.newInstance();
            SAXParser pars1 = factory1.newSAXParser();
            ServerSearchRequestHandler dh = new ServerSearchRequestHandler();
            pars1.parse(input, dh);
        } catch (IOException ex) {
            Logger.setToLog(ex.getMessage() + ". Error in parsing server " +
                    "request. May be request format has been changed, please " +
                    "update the analyzer");
        } catch (ParserConfigurationException ex) {
            Logger.setToLog(ex.getMessage() + ". Error in parsing server " +
                    "request. May be request format has been changed, please " +
                    "update the analyzer");
        } catch (SAXException ex) {
            Logger.setToLog(ex.getMessage() + ". Error in parsing server " +
                    "request. May be request format has been changed, please " +
                    "update the analyzer");
        }
    }
}
