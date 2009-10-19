package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import java.util.ArrayList;
import org.ebooksearchtool.analyzer.utils.Lexema;
import org.ebooksearchtool.analyzer.utils.AnalyzeUtils;

/**
 * @author Алексей
 */

public class LanguageExtractor {

    //TODO: Исправить алгоритм(по ключевому слову?)
    public static String extractLanguage(ArrayList<Lexema> lexems){
        int length = lexems.size();
        for (int i = 0; i < length; i++) {
            if(AnalyzeUtils.isLanguage(lexems.get(i).getValue())){
                return lexems.get(i).getValue();
            }
        }

        return new String("Unknown");
    }
}
