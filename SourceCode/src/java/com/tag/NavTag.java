/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tag;

import java.io.IOException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 *
 * @author EChing
 */
public class NavTag extends SimpleTagSupport {

    private String role;
    private String username;
    private String nickname;
    private String activeOn = "home";

    public void setRole(String role) {
        this.role = role;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setActiveOn(String activeOn) {
        this.activeOn = activeOn;
    }

    @Override
    public void doTag() {

        if (!("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role) || "student".equalsIgnoreCase(role))) {
            return;
        }

        PageContext pageContext = (PageContext) getJspContext();
        JspWriter out = pageContext.getOut();

        try {
            out.println("<aside id=\"leftsidebar\" class=\"sidebar\">");
            out.println("<div class=\"user-info\">");
            out.println("<div class=\"info-container\">");
            out.println("<div class=\"name\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"false\">" + nickname + "</div>");
            out.println("<div class=\"email\">" + username + "</div>");
            out.println("</div>");
            out.println("</div>");
            out.println("<div class=\"menu\">");
            out.println("<ul class=\"list\">");

            // Menu button - Home
            out.println("<li" + (("home".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                    + "<a href=\"index.jsp\"><i class=\"material-icons\">home</i><span>Home</span></a>"
                    + "</li>");

            // Menu - User Maintenance (only admin can access)
            if ("admin".equalsIgnoreCase(role)) {
                out.println("<li class=\"header\">USER MAINTENANCE</li>");
                out.println("<li" + (("teacher".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                        + "<a href=\"teachers.jsp\"><i class=\"material-icons\">insert_emoticon</i><span>Teacher</span></a>"
                        + "</li>");

                out.println("<li" + (("student".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                        + "<a href=\"students.jsp\"><i class=\"material-icons\">face</i><span>Student</span></a>"
                        + "</li>");
            }
            
            // Menu - Student Record (for teacher to access)
            if ("teacher".equalsIgnoreCase(role)) {
                out.println("<li class=\"header\">STUDENT RECORD</li>");

                out.println("<li" + (("student".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                        + "<a href=\"students.jsp\"><i class=\"material-icons\">face</i><span>Student</span></a>"
                        + "</li>");
            }

            // Menu - Module & Course
            out.println("<li class=\"header\">MODULE & COURSE</li>");

            // Menu button - Module
            out.println("<li" + (("module".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                    + "<a href=\"modules.jsp\"><i class=\"material-icons\">storage</i><span>Module</span></a>"
                    + "</li>");

            // Menu button - Course
            out.println("<li" + (("course".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                    + "<a href=\"courses.jsp\"><i class=\"material-icons\">school</i><span>Course</span></a>"
                    + "</li>");

            // Menu - Others
            out.println("<li class=\"header\">OTHERS</li>");

            // Menu button - Schedule school day
            out.println("<li" + (("schedule".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                    + "<a href=\"calendar\"><i class=\"material-icons\">date_range</i><span>Calendar</span></a>"
                    + "</li>");

            // Menu button - Account setting
            if ("admin".equalsIgnoreCase(role)) {
                out.println("<li" + (("setting".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                        + "<a href=\"adminSetting.jsp\"><i class=\"material-icons\">person</i><span>Account Setting</span></a>"
                        + "</li>");
            }
            if ("teacher".equalsIgnoreCase(role)) {
                out.println("<li" + (("setting".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                        + "<a href=\"teacherInfo?id=" + username + "\"><i class=\"material-icons\">person</i><span>Account Setting</span></a>"
                        + "</li>");
            }
            if ("student".equalsIgnoreCase(role)) {
                out.println("<li" + (("setting".equalsIgnoreCase(activeOn)) ? " class=\"active\">" : ">")
                        + "<a href=\"studentInfo?studentId=" + username + "\"><i class=\"material-icons\">person</i><span>Account Setting</span></a>"
                        + "</li>");
            }

            out.println("</ul>");
            out.println("</div>");
            out.println("<div class=\"legal\">");
            out.println("<div class=\"copyright\">");
            out.println("&copy; 2016 - 2017 <a href=\"javascript:void(0);\">AdminBSB - Material Design</a>.");
            out.println("</div>");
            out.println("<div class=\"version\">");
            out.println("<b>Version: </b> 1.0.5");
            out.println("</div>");
            out.println("</div>");
            out.println("</aside>");

        } catch (IOException ex) {
        }

    }

}
