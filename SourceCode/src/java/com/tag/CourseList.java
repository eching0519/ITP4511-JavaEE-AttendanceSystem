/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tag;

import com.bean.Course;
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
public class CourseList extends SimpleTagSupport {
    
    String user = "";
    
    public void setUser(String user) {
        this.user = user;
    }
    
    @Override
    public void doTag() {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();
        
        String dbUrl = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUrl");
        String dbUser = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUser");
        String dbPassword = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbPassword");
        SystemDB db = new SystemDB(dbUrl, dbUser, dbPassword);
        
        ArrayList<Course> courses;
        if ("admin".equals(user)) {
            courses = db.queryCourse();
        } else {
            courses = db.queryCourseByUser(user);
        }
        
        try {
            out.println("<div class=\"table-responsive p-t-10\">");
            out.println("<table class=\"table table-bordered table-striped table-hover js-basic-example dataTable\">");
            
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Module Code</th>");
            out.println("<th>Module Title</th>");
            out.println("<th>Teacher</th>");
            out.println("<th>Class</th>");
            out.println("</tr>");
            out.println("</thead>");
            
            out.println("<tbody>");
            
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                c.setModule(db.queryModule(c.getModuleCode()));
                c.setTeacher(db.queryTeacher(c.getTeacherId()));
                out.print("<tr onclick=\"javascript:location.href='handleCourse?action=display&courseId=" + c.getCourseId() + "';\" style=\"cursor: pointer;\">");
                out.println("<td>" + c.getModuleCode() + "</td>");
                out.println("<td>" + c.getModule().getModuleTitle() + "</td>");
                out.println("<td>" + c.getTeacher().getName() + "</td>");
                out.println("<td>" + c.getClassId() + "</td>");
                out.println("</tr>");
            }
            
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");
            
        } catch (IOException ex) {
        }
    }
    
}
