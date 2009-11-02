package org.ebooksearchtool.analyzer.network;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import org.ebooksearchtool.analyzer.model.BookInfo;
import org.ebooksearchtool.analyzer.utils.ServerRequests;

/**
 * @author Aleksey Podoplsky
 */

public class Servlet implements javax.servlet.Servlet {

    private ServletConfig myConfig;
    private String myRequest;

    public void init(ServletConfig config) throws ServletException {
        this.myConfig = config;
        this.myRequest = "";
    }

    public void init(ServletConfig config, String info)
            throws ServletException {
        this.myConfig = config;
        this.myRequest = info;
    }

    public void destroy() {
    } // do nothing

    public ServletConfig getServletConfig() {
        return myConfig;
    }

    public String getServletInfo() {
        return "Analyzer Servlet";
    }

    public void service(ServletRequest req,
            ServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        out.println(myRequest);
        out.close();
    }
}
