/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tag;

import com.bean.Course;
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
public class AttendanceList extends SimpleTagSupport {

    private int courseId;

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

        ArrayList<Student> students;
        Course course = db.queryCourseById(courseId + "");

        students = db.queryStudentByClass(course.getClassId());

        ArrayList<Student> passList = new ArrayList<Student>();
        ArrayList<Student> failList = new ArrayList<Student>();
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            double[] studentAttendance = db.getStudentAttendance(s.getStudentId(), courseId + "");
            s.setParticipatedRate(studentAttendance[0]);
            s.setAbscentRate(studentAttendance[1]);

            if (s.getAbscentRate() >= 0.4) {
                failList.add(s);
            } else {
                passList.add(s);
            }
        }

        try {
            // Student List that absence rate < 40%
            out.println("<div class=\"table-responsive p-t-10\">");
            out.println("<table class=\"table table-bordered table-striped table-hover js-basic-example dataTable\">");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Student Id</th>");
            out.println("<th>Name</th>");
            out.println("<th>Class</th>");
            out.println("<th>Attendance</th>");
            out.println("<th>Absence</th>");
            out.println("</tr>");
            out.println("</thead>");

            out.println("<tbody>");
            for (int i = 0; i < passList.size(); i++) {
                Student s = passList.get(i);
                String highlighted = (s.getAbscentRate() > 0.35) ? "class=\"font-bold\"" : "";
                out.println("<tr " + highlighted + " onclick=\"javascript:location.href='studentInfo?studentId=" + s.getStudentId() + "'\" style=\"cursor: pointer;\">");
                out.println("<td>" + s.getStudentId() + "</td>");
                out.println("<td>" + s.getName() + "</td>");
                out.println("<td>" + s.getClassId() + "</td>");
                out.println("<td>" + String.format("%.1f%%", s.getParticipatedRate() * 100) + "</td>");
                out.println("<td>" + String.format("%.1f%%", s.getAbscentRate() * 100) + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

            // Student rate that absence rate >= 40%
            out.println("<hr />");
            out.println("<h4>Attendance rate less than 60%</h4>");
            out.println("<div class=\"table-responsive p-t-10\">");
            out.println("<table class=\"table table-bordered table-striped table-hover js-exportable dataTable\">");

            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Student Id</th>");
            out.println("<th>Name</th>");
            out.println("<th>Class</th>");
            out.println("<th>Attendance</th>");
            out.println("<th>Absence</th>");
            out.println("</tr>");
            out.println("</thead>");

            out.println("<tbody>");
            for (int i = 0; i < failList.size(); i++) {
                Student s = failList.get(i);
                out.println("<tr onclick=\"javascript:location.href='studentInfo?studentId=" + s.getStudentId() + "'\" style=\"cursor: pointer;\">");
                out.println("<td>" + s.getStudentId() + "</td>");
                out.println("<td>" + s.getName() + "</td>");
                out.println("<td>" + s.getClassId() + "</td>");
                out.println("<td>" + String.format("%.1f%%", s.getParticipatedRate() * 100) + "</td>");
                out.println("<td>" + String.format("%.1f%%", s.getAbscentRate() * 100) + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>");

        } catch (IOException ex) {
        }
    }

}
