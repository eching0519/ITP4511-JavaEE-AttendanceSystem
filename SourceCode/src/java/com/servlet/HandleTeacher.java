/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet;

import com.bean.Teacher;
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
@WebServlet(urlPatterns = {"/handleTeacher"})
public class HandleTeacher extends HttpServlet {
    
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
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String name = request.getParameter("name");
                
                boolean success = db.addTeacher(username, password, name);
                if (success) {
                    response.sendRedirect(getServletContext().getContextPath() + "/teacherInfo?id=" + username);
                } else {
                    RequestDispatcher rd = getServletContext().getRequestDispatcher("/addTeacher.jsp?msg=Username+is+duplicate+with+the+current+record");
                    rd.forward(request, response);
                }
                break;
            
            case "edit":
                username = request.getParameter("username");
                password = request.getParameter("password");
                name = request.getParameter("name");
                
                Teacher t = new Teacher(username,name);
                db.setTeacher(t);
                
                if (password != null) {
                    db.setPassword(username, password);
                }
                
                response.sendRedirect("teacherInfo?id=" + username + "&msg=Success!+Account+is+updated");
                
                break;
            
            case "check":
                username = request.getParameter("username");
                t = db.queryTeacher(username);
                request.setAttribute("teacher", t);
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/teacherRecord.jsp");
                rd.forward(request, response);
                break;
            
            case "remove":
                username = request.getParameter("id");
                boolean isSuccess = db.removeTeacher(username);
                if (isSuccess) {
                    response.sendRedirect("teachers.jsp?msg=Success!+Teacher+" + username + "+is+removed");
                } else {
                    response.sendRedirect("teacherInfo?id=" + username + "&msg=Error!+Fail+to+remove+this+teacher");
                }
                break;
            default:
                System.out.println("No such action");
        }
    }
    
}
