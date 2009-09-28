package org.ebooksearchtool.analyzer.tests;

import java.io.*;
import java.util.ArrayList;
import org.ebooksearchtool.analyzer.algorithms.WholeStringSimpleParser;
import org.ebooksearchtool.analyzer.io.Logger;

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
            ArrayList<String> temp = parser.parse(src);
            int length = temp.size();
            for (int i = 0; i < length; i++) {
                bw.write(temp.get(i));
                bw.newLine();
            }
        } catch (IOException ex) {
            Logger.setToLog(ex.getMessage());
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException ex) {
                Logger.setToLog(ex.getMessage());
            }
        }

    }
}
