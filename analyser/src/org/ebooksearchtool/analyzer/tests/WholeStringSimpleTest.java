package org.ebooksearchtool.analyzer.tests;

import java.io.*;
import java.util.List;
import org.ebooksearchtool.analyzer.algorithms.WholeStringSimpleParser;
import org.ebooksearchtool.analyzer.io.Logger;
import org.ebooksearchtool.analyzer.model.Author;
import org.ebooksearchtool.analyzer.model.BookInfo;

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
            WholeStringSimpleParser parser = new WholeStringSimpleParser();
            BookInfo temp = parser.parse(src);
            List<Author> authors = temp.getAuthors();
            int length = authors.size();
            for (int i = 0; i < length; i++) {
                bw.write(authors.get(i).getName());
                bw.newLine();
            }
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
