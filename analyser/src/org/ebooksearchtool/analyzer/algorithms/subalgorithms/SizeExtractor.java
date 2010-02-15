package org.ebooksearchtool.analyzer.algorithms.subalgorithms;

import org.ebooksearchtool.analyzer.utils.NetUtils;

/**
 * @author Aleksey Podolskiy
 */

public class SizeExtractor {

    public static String extractSize(String link){
        return String.valueOf(NetUtils.getContentSize(link));
    }
}
