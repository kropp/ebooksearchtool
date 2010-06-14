
package org.ebooksearchtool.analyser_alternate.xml;

/**
 *
 * @author catherine_tuzova
 *
 * parse messages from crawler,
 * extracts book_link and html_page
 */

public class CrawlerMessageParser {
    private StringBuffer myMessage = null;
    private static final int ourLinkShift = 11;
    private static final int ourPageShift = 9;

    public void setMessage(StringBuffer message) {
        myMessage = message;
    }

    public String getLink() {
        int linkBeginIndex = myMessage.indexOf("<link src=");
        int linkEndIndex = myMessage.indexOf("\" />");
        String link = myMessage.substring(linkBeginIndex + ourLinkShift, linkEndIndex);
        return link;
    }

    /**
     *
     * @return html page from crawler as String 
     */
    public String getPage() {
        int pageBeginIndex = myMessage.indexOf("<![CDATA[");
        String page = myMessage.substring(pageBeginIndex + ourPageShift);
        return page;
    }
}
