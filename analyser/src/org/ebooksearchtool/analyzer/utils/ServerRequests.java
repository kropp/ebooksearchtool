package org.ebooksearchtool.analyzer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
import javax.servlet.*;
import org.ebooksearchtool.analyzer.model.BookInfo;
import javax.servlet.http.*;

/**
 * @author Алексей
 */

public class ServerRequests {

    public static String formBookInfo(BookInfo info) {
        StringBuilder str = new StringBuilder();
        String message = encodeSpecialSymbols(info.getBookInfo());
        str.append("POST " + "/data/insert " + "HTTP/1.0");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Host: " + AnalyzerProperties.getPropertie("serverAddress") +
                ":" + AnalyzerProperties.getPropertie("serverPort"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Length: " + getContentLength(message));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("xml=" + message);
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        System.out.println(str.toString());
        return str.toString();
    }

    public static String formBookInfoRequest(BookInfo info) {
        StringBuilder str = new StringBuilder();
        //TODO: Сделать функцию для запроса данных у сервера
        String message = encodeSpecialSymbols(info.getBookInfo());
        str.append("POST " + "/data/insert " + "HTTP/1.0");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Host: " + AnalyzerProperties.getPropertie("serverAddress") +
                ":" + AnalyzerProperties.getPropertie("serverPort"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Length: " + getContentLength(message));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("xml=" + message);
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        System.out.println(str.toString());
        return str.toString();
    }

    //TODO: Доделать request для сервера
    public static String timeFoundRequest(BookInfo info) {
        StringBuilder str = new StringBuilder();
        String message = encodeSpecialSymbols(info.getBookInfoForRequest());
        str.append("POST " + "/data/insert " + "HTTP/1.0");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Host: " + AnalyzerProperties.getPropertie("serverAddress") +
                ":" + AnalyzerProperties.getPropertie("serverPort"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Type: application/x-www-form-urlencoded");
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("Content-Length: " + getContentLength(message));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append("xml=" + message);
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));
        str.append(AnalyzerProperties.getPropertie("systemSeparator"));

        System.out.println(str.toString());
        return str.toString();
    }

    private static long getContentLength(String str){
        return str.length() + 4;
    }

    private static String encodeSpecialSymbols(String message){
        StringBuilder sb = new StringBuilder(message);
        int length = sb.length();
        for (int i = 0; i < length; i++) {
            if(sb.charAt(i) == ';'){
                sb.deleteCharAt(i);
                sb.insert(i, "%3B");
            }
        }
        String test = AnalyzerProperties.getPropertie("systemSeparator") + 
                AnalyzerProperties.getPropertie("systemSeparator");
        while(sb.indexOf(test) != -1){
            sb.replace(sb.indexOf(test), sb.indexOf(test) + test.length(),
                    AnalyzerProperties.getPropertie("systemSeparator"));
        }

        return sb.toString();
    }

    public HttpServletRequest getRequest(String request){
        return new HttpServletRequest(){

            public String getAuthType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Cookie[] getCookies() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public long getDateHeader(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getHeader(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Enumeration getHeaders(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Enumeration getHeaderNames() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getIntHeader(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getMethod() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getPathInfo() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getPathTranslated() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getContextPath() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getQueryString() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getRemoteUser() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isUserInRole(String role) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Principal getUserPrincipal() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getRequestedSessionId() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getRequestURI() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public StringBuffer getRequestURL() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getServletPath() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public HttpSession getSession(boolean create) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public HttpSession getSession() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isRequestedSessionIdValid() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isRequestedSessionIdFromCookie() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isRequestedSessionIdFromURL() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isRequestedSessionIdFromUrl() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Object getAttribute(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Enumeration getAttributeNames() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getCharacterEncoding() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getContentLength() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getContentType() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public ServletInputStream getInputStream() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getParameter(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Enumeration getParameterNames() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String[] getParameterValues(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Map getParameterMap() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getProtocol() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getScheme() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getServerName() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getServerPort() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public BufferedReader getReader() throws IOException {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getRemoteAddr() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getRemoteHost() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void setAttribute(String name, Object o) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void removeAttribute(String name) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Locale getLocale() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Enumeration getLocales() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public boolean isSecure() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public RequestDispatcher getRequestDispatcher(String path) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getRealPath(String path) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getRemotePort() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getLocalName() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public String getLocalAddr() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public int getLocalPort() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
    };
    }
}
