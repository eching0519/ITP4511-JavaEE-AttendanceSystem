/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet;

import com.bean.*;
import com.db.SystemDB;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author EChing
 */
@WebServlet(urlPatterns = {"/handleLesson"})
public class HandleLesson extends HttpServlet {

    private SystemDB db;

    @Override
    public void init() throws ServletException {
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        db = new SystemDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        RequestDispatcher rs;

        switch (action) {
            case "display":
                String lessonId = request.getParameter("lessonId");
                Lesson lesson = db.queryLesson(lessonId);
                Course c = db.queryCourseById(lesson.getCourseId() + "");
                c.setModule(db.queryModule(c.getModuleCode()));
                c.setTeacher(db.queryTeacher(c.getTeacherId()));
                lesson.setCourse(c);

                ArrayList<Student> students = db.queryStudentByClass(c.getClassId());
                ArrayList<Attendance> attendances = db.queryAttendanceByLesson(lessonId);

                rs = request.getRequestDispatcher("lessonRecord.jsp");
                request.setAttribute("lesson", lesson);
                request.setAttribute("students", students);
                request.setAttribute("attendances", attendances);
                rs.forward(request, response);
                break;

            case "remove":
                lessonId = request.getParameter("lessonId");
                lesson = db.queryLesson(lessonId);
                db.removeLesson(lessonId);
                response.sendRedirect(getServletContext().getContextPath() + "/handleCourse?action=display&courseId=" + lesson.getCourseId() + "&msg=Lesson+on+date+" + lesson.getDate() + "+is+removed.");
                break;

            default:
                System.out.println("No such action");
        }
    }

}
