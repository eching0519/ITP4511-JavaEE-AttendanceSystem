/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.servlet;

import com.bean.Module;
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
@WebServlet(urlPatterns = {"/handleModule"})
public class HandleModule extends HttpServlet {

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

        if (action != null) {
            switch (action.toLowerCase()) {
                case "add":
                    String moduleCode = request.getParameter("moduleCode");
                    String moduleTitle = request.getParameter("moduleTitle");
                    String contactHours = request.getParameter("contactHours");
                    boolean isSuccess = db.addModule(moduleCode, moduleTitle, contactHours);
                    if (!isSuccess) {
                        RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/addModule.jsp?msg=Error!+Module+code+is+duplicated.");
                        rd.forward(request, response);
                    } else {
                        response.sendRedirect(getServletContext().getContextPath() + "/modules.jsp?msg=Success!+Module+" + moduleCode + "+is+added");
                    }
                    break;

                case "edit":
                    moduleCode = request.getParameter("moduleCode");
                    moduleTitle = request.getParameter("moduleTitle");
                    contactHours = request.getParameter("contactHours");
                    double hours = Double.parseDouble(contactHours);
                    Module m = new Module(moduleCode, moduleTitle, hours);
                    isSuccess = db.setModule(m);
                    if (!isSuccess) {
                        RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/addModule.jsp?msg=Error!+Module+update+fail.");
                        rd.forward(request, response);
                    } else {
                        response.sendRedirect(getServletContext().getContextPath() + "/modules.jsp?msg=Success!+Module+" + moduleCode + "+is+updated.");
                    }
                    break;

                case "remove":
                    moduleCode = request.getParameter("moduleCode");
                    isSuccess = db.removeModule(moduleCode);
                    if (!isSuccess) {
                        RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/addModule.jsp?msg=Error!+Module+remove+fail.");
                        rd.forward(request, response);
                    } else {
                        response.sendRedirect(getServletContext().getContextPath() + "/modules.jsp?msg=Success!+Module+" + moduleCode + "+is+removed.");
                    }
                    break;
                
                default:
                    System.out.println("No such action");
            }
        } else {
            String moduleCode = request.getParameter("moduleCode");
            if (moduleCode == null) {
                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/addModule.jsp");
                rd.forward(request, response);
            } else {
                Module m = db.queryModule(moduleCode);
                request.setAttribute("module", m);
                RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/addModule.jsp");
                rd.forward(request, response);
            }
        }
    }

}
