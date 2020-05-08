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
import java.util.ArrayList;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author EChing
 */
@WebServlet(urlPatterns = {"/lessonEventJson"})
public class LessonEventJSON extends HttpServlet {

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
        processRequest(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp); //To change body of generated methods, choose Tools | Templates.
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ArrayList<Event> events = null;
        String bgColor = "";
        if (request.getParameter("teacher") != null) {
            String teacher = request.getParameter("teacher");
            events = db.queryLessonEventByTeacher(teacher);
            bgColor = "#00BCD4";

        } else if (request.getParameter("classId") != null) {
            String classId = request.getParameter("classId");
            events = db.queryLessonEventByClass(classId);
            bgColor = "#FF9800";
        } else {
            return;
        }

        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

        JsonObjectBuilder jsonBuilder = null;
        for (Event event : events) {
            jsonBuilder = Json.createObjectBuilder()
                    .add("title", event.getTitle())
                    .add("start", event.getStartDate())
                    .add("end", event.getEndDate())
                    .add("color", bgColor);

            arrayBuilder.add(jsonBuilder);
        }

        JsonArray events_arr = arrayBuilder.build();

        out.println(events_arr);

    }

}
