/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet;

import com.bean.User;
import com.db.SystemDB;
import java.io.IOException;
import java.io.PrintWriter;
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
@WebServlet(urlPatterns = {"/handleUser"})
public class HandleUser extends HttpServlet {

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

        switch (action) {
            case "login":
                PrintWriter out = response.getWriter();
                String login_username = request.getParameter("username");
                String login_password = request.getParameter("password");
                String rememberme = request.getParameter("rememberme");

                session.setAttribute("username", login_username);
                session.setAttribute("password", login_password);
                session.setAttribute("rememberme", rememberme);

                User user = db.userVerify(login_username, login_password);
                if (user == null) {
                    response.sendRedirect(this.getServletContext().getContextPath() + "/login.jsp?msg=invalid");
                } else {
                    if (rememberme == null) {
                        session.setAttribute("username", null);
                        session.setAttribute("password", null);
                        session.setAttribute("rememberme", null);
                    }

                    session.setAttribute("user", user);
                    response.sendRedirect(this.getServletContext().getContextPath() + "/index.jsp");
                }

                break;

            case "logout":
                session.setAttribute("user", null);
                response.sendRedirect(this.getServletContext().getContextPath() + "/login.jsp");
                break;

            default:
                System.out.println("No such action");
        }
    }

}
