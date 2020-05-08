/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tag;

import com.bean.*;
import com.db.SystemDB;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author EChing
 */
public class LessonList_Student extends SimpleTagSupport {

    private String studentId;
    private int courseId;

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public void doTag() {
        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();

        String dbUrl = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUrl");
        String dbUser = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbUser");
        String dbPassword = ((PageContext) getJspContext()).getServletContext().getInitParameter("dbPassword");
        SystemDB db = new SystemDB(dbUrl, dbUser, dbPassword);

        ArrayList<Lesson> lessons;
        lessons = db.queryLessonByCourseId(courseId);

        try {
            out.println("<div class=\"table-responsive p-t-10\">");
            out.println("<table class=\"table table-bordered table-striped table-hover js-basic-example dataTable\">");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Date</th>");
            out.println("<th>Start Time</th>");
            out.println("<th>End Time</th>");
            out.println("<th>Duration(hours)</th>");
            out.println("<th>Attendance</th>");
            out.println("</tr>");
            out.println("</thead>");

            out.println("<tbody>");

            for (int i = 0; i < lessons.size(); i++) {
                Lesson l = lessons.get(i);
                Attendance attendance = db.queryAttendance(studentId, l.getLessonId() + "");
                String attend_desc;
                if (attendance == null) {
                    attend_desc = "";
                } else if (attendance.getLate() == 0) {
                    attend_desc = "Completed";
                } else if (attendance.getParticipated() == 0) {
                    attend_desc = "<span class=\"col-pink\">Absence</span>";
                } else {
                    attend_desc = "Late for " + attendance.getLate() + " minutes";
                }

                // for completed lesson, change the font style to bold
                String fontStyle = isPreviousLesson(l.getDate()) ? "class=\"font-bold\"" : "";

                out.println("<tr " + fontStyle + ">");
                out.println("<td>" + l.getDate() + "</td>");
                out.println("<td>" + l.getStartTime() + "</td>");
                out.println("<td>" + l.getEndTime() + "</td>");
                out.println("<td>" + l.getDuration() + "</td>");
                out.println("<td>" + attend_desc + "</td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

        } catch (IOException ex) {
        }
    }

    private boolean isPreviousLesson(String lessonDate) {
        boolean isPrevious = true;

        LocalDate today = LocalDate.now();
        LocalDate lDate = LocalDate.parse(lessonDate);
        if (lDate.compareTo(today) > 0) {
            isPrevious = false;
        }

        return isPrevious;
    }

}
