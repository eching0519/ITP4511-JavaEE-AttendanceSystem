/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tag;

import com.bean.*;
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
public class AttendanceList_Student extends SimpleTagSupport {

    private String studentId;

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public void doTag() {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();

        String dbUrl = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUrl");
        String dbUser = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUser");
        String dbPassword = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbPassword");
        SystemDB db = new SystemDB(dbUrl, dbUser, dbPassword);

        ArrayList<Course> courses = db.queryCourseByUser(studentId);

        try {
            out.println("<div class=\"table-responsive p-t-10\">");
            out.println("<table class=\"table table-bordered table-striped table-hover dataTable js-exportable\">");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Module Code</th>");
            out.println("<th>Module Title</th>");
            out.println("<th>Teacher</th>");
            out.println("<th>Attendance</th>");
            out.println("<th>Absence</th>");
            out.println("</tr>");
            out.println("</thead>");

            out.println("<tbody>");

            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                Module m = db.queryModule(c.getModuleCode());
                Teacher t = db.queryTeacher(c.getTeacherId());
                
                double[] studentAttendance = db.getStudentAttendance(studentId, c.getCourseId() + "");
                String highlighted = (studentAttendance[1] > 0.35) ? "class=\"font-bold\"" : "";
                highlighted = (studentAttendance[1] > 0.40) ? "class=\"font-bold col-pink\"" : highlighted;
                
                out.println("<tr " + highlighted + " onclick=\"javascript:location.href='handleCourse?action=display&courseId=" + c.getCourseId() + "'\" style=\"cursor: pointer;\">");
                out.println("<td>" + c.getModuleCode() + "</td>");
                out.println("<td>" + m.getModuleTitle() + "</td>");
                out.println("<td>" + t.getName() + "</td>");
                out.println("<td>" + String.format("%.1f%%", studentAttendance[0] * 100) + "</td>");
                out.println("<td>" + String.format("%.1f%%", studentAttendance[1] * 100) + "</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

        } catch (IOException ex) {
        }
    }

}
