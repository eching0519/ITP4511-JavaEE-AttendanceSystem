/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tag;

import com.bean.Module;
import com.db.SystemDB;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author EChing
 */
public class ModuleList extends SimpleTagSupport {

    private boolean editable = false;

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    @Override
    public void doTag() {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();

        String dbUrl = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUrl");
        String dbUser = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUser");
        String dbPassword = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbPassword");
        SystemDB db = new SystemDB(dbUrl, dbUser, dbPassword);

        ArrayList<Module> modules;
        modules = db.queryModule();

        try {
            out.println("<div class=\"table-responsive p-t-10\">");
            out.println("<table class=\"table table-bordered table-striped table-hover js-basic-example dataTable\">");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Module Code</th>");
            out.println("<th>Module Title</th>");
            out.println("<th>Contact Hours</th>");
            out.println("</tr>");
            out.println("</thead>");

            out.println("<tbody>");

            for (int i = 0; i < modules.size(); i++) {
                Module m = modules.get(i);
                out.print("<tr");
                if (editable) {
                    out.print(" onclick=\"javascript:location.href='handleModule?moduleCode=" + m.getModuleCode() + "';\"");
                }
                out.println(" style=\"cursor: pointer;\">");
                out.println("<td>" + m.getModuleCode() + "</td>");
                out.println("<td>" + m.getModuleTitle() + "</td>");
                out.println("<td>" + m.getContactHours() + "</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

        } catch (IOException ex) {
        }
    }

}
