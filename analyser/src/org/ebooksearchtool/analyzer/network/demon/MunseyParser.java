package org.ebooksearchtool.analyzer.network.demon;

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

    private static boolean ourTestStatus = false;

    public MunseyParser(){
        super();
    }

    public MunseyParser(boolean testStatus){
        super();
        ourTestStatus = testStatus;
    }
    /**
     * Parse munsey.xml from file described by input string
     * @param input file contains munsey.xml
     */
    public void parse(String input){
        try {
            SAXParserFactory factory1 = SAXParserFactory.newInstance();
            SAXParser pars1 = factory1.newSAXParser();
            MunseyHandler dh = new MunseyHandler(ourTestStatus);
            pars1.parse(input, dh);
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage() + ". Parser can't parse file. " +
                    "IOException occurs in " + MunseyParser.class.getName() + " class.");
        } catch (ParserConfigurationException ex) {
            Logger.setToErrorLog(ex.getMessage() + ". Parser can't parse file. " +
                    "ParserConfigurationException occurs in " + MunseyParser.class.getName() + " class.");
        } catch (SAXException ex) {
            Logger.setToErrorLog(ex.getMessage() + ". Parser can't parse file. " +
                    "SAXException occurs in " + MunseyParser.class.getName() + " class.");
        }
    }

}
