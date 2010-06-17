
package org.ebooksearchtool.analyser_alternate.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
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
    private DBConnector myRawDBConnector;
    private DBConnector myModelDBConnector;
    private String myHTMLPage;
    private String myBookLink;
    private int mySleepTime = 1000;

    public HTMLPageParser(DBConnector rawDBConnector, DBConnector modelDBConnector) {
        myRawDBConnector = rawDBConnector;
        myModelDBConnector = modelDBConnector;
    }

    public void run() {
        Logger logger = Logger.getLogger("main.log");
        logger.log(Level.INFO, "Start html parser");
        while(true) {
            try {
                // gets element from db
                ResultSet resultSet = myRawDBConnector.extractData();
                resultSet.next();
                myHTMLPage = resultSet.getString("page");
                myBookLink = resultSet.getString("book_link");
                if (myBookLink.indexOf("feedbooks") != -1) {
                    myBookLink = myBookLink.substring(0, myBookLink.indexOf(".epub"));

                    // process myHTMLPage
                    Source source = new Source(myHTMLPage);
                    source.getAllElements();    // for possibility to use getParent
                    List<Element> links = source.getAllElements("a href");
                    for (Element link : links) {
                        StringBuffer sb = new StringBuffer(link.toString());
                        if (sb.indexOf(myBookLink) != -1
                                && !link.getTextExtractor().toString().isEmpty()) {
                            System.out.println(link);

                            Element parent = link.getParentElement().getParentElement();
                            TextExtractor t = parent.getTextExtractor();
                            StringBuffer strBuff = new StringBuffer(t.toString());
                            int authorIndex = strBuff.indexOf("by");
                            if (authorIndex != -1) {
                                String title = strBuff.substring(0, authorIndex);
                                StringBuffer authorBuff = new StringBuffer();
                                StringTokenizer stringTokenizer = new StringTokenizer(strBuff.substring(authorIndex + 3));
                                for (int i = 0; i < 2; ++i) {
                                    String token = stringTokenizer.nextToken();
                                    authorBuff.append(token + " ");
                                }
                                ArrayList <String> params = new ArrayList<String>();
                                params.add(title);
                                params.add(authorBuff.toString());
                                myModelDBConnector.insertData(params);
                                /*System.out.println(title);
                                System.out.println(authorBuff);
                                System.out.println("");*/
                            }
                        }
                    }
                }
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
        }
        
    }

}
