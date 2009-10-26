package org.ebooksearchtool.analyzer.algorithms;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.ebooksearchtool.analyzer.io.Logger;
import org.xml.sax.SAXException;

/**
 * @author Алексей
 */

public class MunseyParser{

    public void parse(String input){
        try {
            SAXParserFactory factory1 = SAXParserFactory.newInstance();
            SAXParser pars1 = factory1.newSAXParser();
            MunseyHandler dh = new MunseyHandler();
            pars1.parse(input, dh);
        } catch (IOException ex) {
            Logger.setToLog(ex.getMessage());
        } catch (ParserConfigurationException ex) {
            Logger.setToLog(ex.getMessage());
        } catch (SAXException ex) {
            Logger.setToLog(ex.getMessage());
        }
    }

}