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
import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author EChing
 */
@WebServlet(urlPatterns = {"/handleCourse"})
public class HandleCourse extends HttpServlet {

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
        HttpSession session = request.getSession();

        boolean isSuccess = false;
        switch (action) {
            case "add":
                String moduleCode = request.getParameter("moduleCode");
                String classId = request.getParameter("class");
                String teacher = request.getParameter("teacher");
                Course course = db.addCourse(moduleCode, teacher, classId);
                
                if (course != null) {
                    response.sendRedirect(getServletContext().getContextPath() + "/handleCourse?action=lessonForm&courseId=" + course.getCourseId());
                } else {
                    response.sendRedirect(getServletContext().getContextPath() + "/courses.jsp?msg=Error!+Fail+to+add+new+course.");
                }
                break;

            case "addLesson":
                String courseId = request.getParameter("courseId");
                String startDate = request.getParameter("startDate");
                String startTime = request.getParameter("startTime");
                String endTime = request.getParameter("endTime");
                String duration = request.getParameter("duration");
                int repeat = Integer.parseInt(request.getParameter("repeat"));

                ArrayList<String> errorList = new ArrayList<String>();
                LocalDate date = LocalDate.parse(startDate);
                for (int i = 0; i < repeat; i++) {
                    LocalDate next = date.plusWeeks(i);
                    isSuccess = db.addLesson(courseId, next.toString(), startTime, endTime, duration);
                    if (!isSuccess) {
                        errorList.add(next.toString());
                    }
                }

                if (errorList.size() > 0) {
                    String dateString = errorList.get(0);
                    for (int i = 1; i < errorList.size(); i++) {
                        dateString += ", " + errorList.get(i);
                    }
                    response.sendRedirect(getServletContext().getContextPath() + "/handleCourse?action=display&courseId=" + courseId + "&msg=Error!+Fail+to+add+lesson+on+" + dateString + ".");
                } else {
                    response.sendRedirect(getServletContext().getContextPath() + "/handleCourse?action=display&courseId=" + courseId + "&msg=Success!+Lesson+added.");
                }

                break;

            case "display":
                courseId = request.getParameter("courseId");
                course = db.queryCourseById(courseId);
                course.setModule(db.queryModule(course.getModuleCode()));
                course.setTeacher(db.queryTeacher(course.getTeacherId()));
                int completedHours = db.getCompletedHours(courseId);
                course.setCompletedHours(completedHours);

                RequestDispatcher rd = getServletContext().getRequestDispatcher("/courseRecord.jsp");
                request.setAttribute("course", course);
                rd.forward(request, response);
                break;

            case "remove":
                courseId = request.getParameter("courseId");
                isSuccess = db.removeCourse(courseId);
                if (isSuccess) {
                    response.sendRedirect(getServletContext().getContextPath() + "/courses.jsp?msg=Success!+Course+is+removed.");
                } else {
                    response.sendRedirect(getServletContext().getContextPath()
                            + "/handleCourse?action=display&courseId=" + courseId + "&msg=Error!+Fail+to+remove+this+course");
                }
                break;

            case "addCourse":
                ArrayList<Module> modules = db.queryModule();
                ArrayList<String> classes = db.queryClasses();
                ArrayList<Teacher> teachers = db.queryTeacher();
                rd = this.getServletContext().getRequestDispatcher("/addCourse.jsp");
                request.setAttribute("modules", modules);
                request.setAttribute("classes", classes);
                request.setAttribute("teachers", teachers);
                rd.forward(request, response);
                break;

            case "lessonForm":
                courseId = request.getParameter("courseId");
                course = db.queryCourseById(courseId);
                course.setModule(db.queryModule(course.getModuleCode()));
                course.setTeacher(db.queryTeacher(course.getTeacherId()));

                rd = getServletContext().getRequestDispatcher("/addLesson.jsp?");
                request.setAttribute("course", course);
                request.setAttribute("teacher", course.getTeacherId());
                request.setAttribute("classId", course.getClassId());
                rd.forward(request, response);
                break;

            default:
                System.out.println("No such action");
        }

    }

}
