package org.ebooksearchtool.analyzer.tests;

import java.io.*;
import java.util.List;
import org.ebooksearchtool.analyzer.algorithms.WholeStringSimpleParser;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.Author;
import org.ebooksearchtool.analyzer.model.BookInfo;
import org.ebooksearchtool.analyzer.network.demon.MunseyParser;

/**
 * @author Алексей
 */

public class WholeStringSimpleTest {

    public static void createTest(File input){
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            bw = new BufferedWriter(new FileWriter("wholetest.tst"));
            br = new BufferedReader(new FileReader(input));
            String src = br.readLine();
            MunseyParser parser = new MunseyParser(true);
            parser.parse("munsey.xml");
        } catch (IOException ex) {
            Logger.setToErrorLog(ex.getMessage() + ". Can't operate with test or" +
                    "tested file in " + WholeStringSimpleTest.class.getName() + " class.");
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException ex) {
                Logger.setToErrorLog(ex.getMessage());
            }
        }

    }
}
