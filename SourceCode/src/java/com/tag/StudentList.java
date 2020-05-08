/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tag;

import com.bean.Student;
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
public class StudentList extends SimpleTagSupport {

    private String classId = "";

    public void setClassId(String classId) {
        this.classId = classId;
    }

    @Override
    public void doTag() {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();

        String dbUrl = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUrl");
        String dbUser = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUser");
        String dbPassword = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbPassword");
        SystemDB db = new SystemDB(dbUrl, dbUser, dbPassword);
        
        ArrayList<Student> students;

        if ("".equals(classId)) {
            students = db.queryStudent();
        } else {
            students = db.queryStudentByClass(classId);
        }

        try {
            out.println("<div class=\"table-responsive p-t-10\">");
            out.println("<table class=\"table table-bordered table-striped table-hover js-basic-example dataTable\">");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Student Id</th>");
            out.println("<th>Name</th>");
            out.println("<th>Class</th>");
            out.println("</tr>");
            out.println("</thead>");

            out.println("<tbody>");

            for (int i = 0; i < students.size(); i++) {
                Student s = students.get(i);
                out.println("<tr onclick=\"javascript:location.href='studentInfo?studentId="+s.getStudentId()+"'\" style=\"cursor: pointer;\">");
                out.println("<td>" + s.getStudentId() + "</td>");
                out.println("<td>" + s.getName() + "</td>");
                out.println("<td>" + s.getClassId() + "</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

        } catch (IOException ex) {
        }
    }

}
