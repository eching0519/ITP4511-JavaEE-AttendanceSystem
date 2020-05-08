/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tag;

import com.bean.Teacher;
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
public class TeacherList extends SimpleTagSupport {

    @Override
    public void doTag() {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();

        String dbUrl = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUrl");
        String dbUser = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUser");
        String dbPassword = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbPassword");
        SystemDB db = new SystemDB(dbUrl, dbUser, dbPassword);

        ArrayList<Teacher> teachers;
        teachers = db.queryTeacher();

        try {
            out.println("<div class=\"table-responsive p-t-10\">");
            out.println("<table class=\"table table-bordered table-striped table-hover js-basic-example dataTable\">");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Username</th>");
            out.println("<th>Name</th>");
            out.println("</tr>");
            out.println("</thead>");

            out.println("<tbody>");

            for (int i = 0; i < teachers.size(); i++) {
                Teacher t = teachers.get(i);
                out.println("<tr onclick=\"javascript:location.href='teacherInfo?id=" + t.getUsername() + "'\" style=\"cursor: pointer;\">");
                out.println("<td>" + t.getUsername() + "</td>");
                out.println("<td>" + t.getName() + "</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

        } catch (IOException ex) {
        }
    }

}
