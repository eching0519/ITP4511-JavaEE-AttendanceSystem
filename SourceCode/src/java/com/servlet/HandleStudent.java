/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet;

import com.bean.Student;
import com.db.SystemDB;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(urlPatterns = {"/handleStudent"})
public class HandleStudent extends HttpServlet {

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

        switch (action) {
            case "add":
                String studentId = request.getParameter("studentId");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
                String classId = request.getParameter("classId");

                boolean success = db.addStudent(studentId, password, name, classId);
                if (success) {
                    response.sendRedirect(getServletContext().getContextPath() + "/studentInfo?studentId=" + studentId);
                } else {
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/addStudent.jsp?msg=Student+ID+is+duplicate+with+the+current+record");
                    rd.forward(request, response);
                }
                break;

            case "edit":
                studentId = request.getParameter("studentId");
                password = request.getParameter("password");
                name = request.getParameter("name");
                classId = request.getParameter("classId");

                if (classId != null) {
                    Student s = new Student(studentId, name, classId);
                    db.setStudent(s);
                }

                if (password != null) {
                    db.setPassword(studentId, password);
                }

                response.sendRedirect("studentInfo?studentId=" + studentId + "&msg=Success!+Account+is+updated");

                break;

            case "check":
                studentId = request.getParameter("studentId");
                Student s = db.queryStudentById(studentId);
                request.setAttribute("student", s);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/studentRecord.jsp");
                rd.forward(request, response);
                break;

            case "remove":
                studentId = request.getParameter("studentId");
                boolean isSuccess = db.removeStudent(studentId);
                if (isSuccess) {
                    response.sendRedirect("students.jsp?msg=Success!+Student+" + studentId + "+is+removed");
                } else {
                    response.sendRedirect("studentInfo?studentId=" + studentId + "&msg=Error!+Fail+to+remove+this+student");
                }
                break;
            default:
                System.out.println("No such action");
        }
    }

}
