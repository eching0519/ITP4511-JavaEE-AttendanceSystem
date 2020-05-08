/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet;

import com.bean.*;
import com.db.SystemDB;
import java.io.IOException;
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
@WebServlet(urlPatterns = {"/calendar"})
public class HandleCalendar extends HttpServlet {

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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String role = user.getRole();

        RequestDispatcher rd = request.getRequestDispatcher("/showCalendar.jsp");
        if ("searchCalendar".equalsIgnoreCase(action)) {
            String classId = request.getParameter("classId");
            request.setAttribute("classId", classId);
            
        } else {
            if ("student".equalsIgnoreCase(role)) {
                String classId = db.queryStudentById(user.getUsername()).getClassId();
                request.setAttribute("classId", classId);
            }

            if ("teacher".equalsIgnoreCase(role)) {
                request.setAttribute("teacher", user.getUsername());
            }
        }

        rd.forward(request, response);
    }

}
