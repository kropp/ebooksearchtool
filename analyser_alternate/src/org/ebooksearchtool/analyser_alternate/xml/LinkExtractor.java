
package org.ebooksearchtool.analyser_alternate.xml;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.analyserAlternate.LogDocument;
import org.analyserAlternate.Record;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;

/**
 *
 * @author catherine_tuzova
 */
public class LinkExtractor {

    /**
     * parse file from crawler
     * @param fileName The file name to parse
     * @return ArrayList of links to books
     */
    public ArrayList extract(String fileName) {
        File foundBooksFile = new File(fileName);
        ArrayList<String> links = new ArrayList<String>();
        try {
            // set xml option for using namespace
            XmlOptions xmlOptions = new XmlOptions();
            xmlOptions.setLoadSubstituteNamespaces(Collections.singletonMap("", "http://analyser_alternate.org"));
            
            //parse file from crawler
            LogDocument logDoc = LogDocument.Factory.parse(foundBooksFile, xmlOptions);
            LogDocument.Log log = logDoc.getLog();
            Record[] records = log.getRecordArray();
            
            for (Record record : records) {
                links.add(record.getMessage());
            }
        } catch (XmlException ex) {
            Logger logger = Logger.getLogger("main.log");
            logger.log(Level.SEVERE,"Cannot parse " + foundBooksFile + ", invalid Xml.");
        } catch (IOException ex) {
            Logger logger = Logger.getLogger("main.log");
            logger.log(Level.SEVERE,"Cannot parse " + foundBooksFile + ", invalid Xml.");
        }

        return links;
    }

}
