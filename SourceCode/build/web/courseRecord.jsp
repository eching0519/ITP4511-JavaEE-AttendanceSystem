<%-- 
    Document   : studentRecord
    Created on : 2019年12月14日, 下午03:31:47
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/taglib.tld" prefix="tag" %>

<jsp:useBean id="course" class="com.bean.Course" scope="request" />
<!DOCTYPE html>
<html>
    <head>
        <%@include file="component/head.jsp" %>
    </head>

    <body class="theme-red">

        <%
            User user = null;
            String role = "";
            String username = "";
            String nickname = "";

            if (session.getAttribute("user") == null) {
                response.sendRedirect(this.getServletContext().getContextPath() + "/login.jsp?msg=timeout");
            } else {
                user = (User) session.getAttribute("user");
                role = user.getRole();
                username = user.getUsername();
                nickname = user.getNickName();
            }

            int courseId = course.getCourseId();
            String classId = course.getClassId();

            String msg = (request.getParameter("msg") == null) ? "" : request.getParameter("msg");
        %>

        <!-- Top Bar -->
        <%@include file="component/topBar.jsp" %>
        <!-- #Top Bar -->
        <section>
            <!-- Left Sidebar -->
            <tag:nav role="<%=role%>" username="<%=username%>" nickname="<%=nickname%>" activeOn="course" />
            <!-- #END# Left Sidebar -->
        </section>

        <section class="content">
            <div class="container-fluid">
                <ol class="breadcrumb breadcrumb-col-pink">
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="courses.jsp">Course</a></li>
                    <li class="active"><%out.println(course.getModule().getModuleTitle());%></li>
                </ol>
                <%
                    if (!"".equals(msg)) {
                        out.println("<div class='alert bg-orange alert-dismissible'>" + msg);
                        out.println("<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>×</span></button>");
                        out.println("</div>");
                    }
                %>
                <div class="row clearfix">
                    <div class="col-xs-12 col-sm-3">
                        <div class="card profile-card">
                            <div class="profile-header">&nbsp;</div>
                            <div class="profile-body">

                                <div class="content-area">
                                    <h3 class="m-l-20 m-r-20"><%out.println(course.getModule().getModuleTitle());%></h3>
                                    <p>Teacher: <%out.println(course.getTeacher().getName());%></p>
                                    <p>Class: <%=classId%></p>

                                </div>

                            </div>
                            <div class="profile-footer">

                                <ul>

                                    <li>
                                        <span>Contact Hours</span>
                                        <span>
                                            <%
                                                double contactHours = course.getModule().getContactHours();
                                                out.println(contactHours);
                                            %>
                                        </span>
                                    </li>
                                    <li>
                                        <span>Completed Hours</span>
                                        <span>
                                            <%
                                                int completedHours = course.getCompletedHours();
                                                out.println(completedHours);
                                            %>
                                        </span>
                                    </li>
                                    <li>
                                        <span>Completion Rate</span>
                                        <span>
                                            <%
                                                out.println(String.format("%.1f%%", completedHours / contactHours * 100));
                                            %>
                                        </span>
                                    </li>
                                </ul>
                                <%
                                    if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
                                        out.println("<form action='handleCourse'>"
                                                + "<input type='hidden' name='action' value='lessonForm'>"
                                                + "<button class='btn btn-warning btn-lg waves-effect btn-block' name='courseId' value='" + courseId + "'>"
                                                + "ADD LESSON"
                                                + "</button>"
                                                + "</form>");
                                    }
                                %>
                            </div>

                        </div>

                    </div>
                    <div class="col-xs-12 col-sm-9">
                        <div class="card">
                            <div class="body">
                                <div>
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li role="presentation" class="active"><a href="#lessons" aria-controls="lessons" role="tab" data-toggle="tab">Lessons</a></li>
                                            <%
                                                if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
                                                    out.println("<li role='presentation'><a href='#student_list' aria-controls='settings' role='tab' data-toggle='tab'>Students</a></li>");
                                                }
                                                if ("admin".equalsIgnoreCase(role)) {
                                                    out.println("<li role='presentation'><a href='#course_remove' aria-controls='settings' role='tab' data-toggle='tab'>Remove Course</a></li>");
                                                }
                                            %>
                                    </ul>

                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade in active" id="lessons">
                                            <%
                                                if ("student".equalsIgnoreCase(role)) {
                                            %>
                                            <tag:stuLessonList courseId="<%=courseId%>" studentId="<%=username%>" />
                                            <%
                                            } else {
                                            %>
                                            <tag:lessonList courseId="<%=courseId%>" />
                                            <%
                                                }
                                            %>
                                        </div>

                                        <%
                                            if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
                                        %>
                                        <div role="tabpanel" class="tab-pane fade in" id="student_list">
                                            <tag:attendanceList courseId="<%=courseId%>" />
                                        </div>
                                        <%}%>
                                        <%
                                            if ("admin".equalsIgnoreCase(role)) {
                                                out.println("<div role='tabpanel' class='tab-pane fade in' id='course_remove'>");
                                                out.println("<p class='m-t-10 align-center'>Please click <a href='handleCourse?action=remove&courseId=" + courseId + "'>here</a> to remove the course.</p>");
                                                out.println("</div>");
                                            }
                                        %>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>

    </body>

</html>
