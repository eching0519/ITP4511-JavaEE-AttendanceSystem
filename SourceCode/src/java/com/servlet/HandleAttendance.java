/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet;

import com.bean.*;
import com.db.SystemDB;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
@WebServlet(urlPatterns = {"/handleAttendance"})
public class HandleAttendance extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        String action = request.getParameter("action");
        RequestDispatcher rs;

        switch (action) {
            case "update":
                String lessonId = request.getParameter("lessonId");
                String classId = request.getParameter("classId");
                String startTime = request.getParameter("startTime");
                String endTime = request.getParameter("endTime");

                db.removeAttendance(lessonId);
                ArrayList<Student> students = db.queryStudentByClass(classId);
                try {
                    for (int i = 0; i < students.size(); i++) {
                        Student s = students.get(i);
                        String time = request.getParameter(s.getStudentId());

                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                        Date attendTime = timeFormat.parse(time);
                        Date endingTime = timeFormat.parse(endTime);
                        Date startingTime = timeFormat.parse(startTime);

                        long participated_ms = endingTime.getTime() - attendTime.getTime();
                        long late_ms = attendTime.getTime() - startingTime.getTime();

                        int participated = (int) (participated_ms / 1000) / 60;
                        int late = (int) (late_ms / 1000) / 60;

                        db.addAttendance(s.getStudentId(), lessonId, participated, late);
                    }
                } catch (ParseException ex) {
                }
                response.sendRedirect(getServletContext().getContextPath() + "/handleLesson?action=display&lessonId=" + lessonId + "&msg=Attendance+record+updated.");

                break;

            default:
                System.out.println("No such action");
        }
    }

}
