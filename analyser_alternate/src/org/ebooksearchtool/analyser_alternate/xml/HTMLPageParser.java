
package org.ebooksearchtool.analyser_alternate.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import net.htmlparser.jericho.*;
import java.util.logging.Logger;
import org.ebooksearchtool.analyser_alternate.dbconnection.DBConnector;

/**
 *
 * @author catherine_tuzova
 *
 * parse html page from crawler,
 * extracts information about author, title...
 */
public class HTMLPageParser implements Runnable {
    private DBConnector myDBConnector;
    private String myHTMLPage;
    private String myBookLink;
    private int mySleepTime = 1000;

    public HTMLPageParser(DBConnector dbConnector) {
        myDBConnector = dbConnector;
    }

    public void run() {
        Logger logger = Logger.getLogger("main.log");
        logger.log(Level.INFO, "Start html parser");
//        while(true) {
            try {
                // gets element from db
                ResultSet resultSet = myDBConnector.extractData();
                resultSet.next();
                myHTMLPage = resultSet.getString("page");
                myBookLink = resultSet.getString("book_link");
                myBookLink = myBookLink.substring(0,myBookLink.indexOf(".epub"));

                // process myHTMLPage
                Source source = new Source(myHTMLPage);
                source.getAllElements();    // for possibility to use getParent
                List<Element> links = source.getAllElements("a href");
                for (Element link : links) {
                    StringBuffer sb = new StringBuffer(link.toString());
                    if (sb.indexOf(myBookLink) != -1) {
                        System.out.println(link);

//                        System.out.println(link.getParentElement().getParentElement());
//                        System.out.println("");
                        Element parent = link.getParentElement().getParentElement();
                        TextExtractor t = parent.getTextExtractor();
                        System.out.println(t.toString());


                        //System.out.println(link.getParentElement().getParentElement().getParentElement());
                        //System.out.println(parent.getPreviousTag());
                    }
                }
                /*ParseText p  = source.getParseText();
                p.indexOf("qwe", 0);
                List<StartTag> links = source.getAllStartTags("a href=");
                for (StartTag link : links) {
                    StringBuffer sb = new StringBuffer(link.toString());
                    if (sb.indexOf(myBookLink) != -1) {
                        List<Element> childElements = link.getChildElements();
                        for (Element child : childElements) {
                            child.getParentElement();
                            System.out.println(child);
                        }
                    }
                }*/

            } catch (SQLException ex) {
                try {
                    Thread.sleep(mySleepTime);
                } catch (InterruptedException ex1) {
                    logger.log(Level.SEVERE, "Thread sleep has been interrupted."
                            + ex1.getMessage());
                }
                logger.log(Level.INFO, "Error extraction data operation. "
                                                            + ex.getMessage());
            }
//        }
        
    }

}
