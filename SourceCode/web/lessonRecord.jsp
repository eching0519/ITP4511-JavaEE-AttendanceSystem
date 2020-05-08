<%-- 
    Document   : studentRecord
    Created on : 2019年12月14日, 下午03:31:47
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.*, java.time.LocalDate"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/taglib.tld" prefix="tag" %>

<jsp:useBean id="lesson" class="com.bean.Lesson" scope="request" />
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

            Course course = lesson.getCourse();
            String msg = (request.getParameter("msg") == null) ? "" : request.getParameter("msg");

            LocalDate lessonDate = LocalDate.parse(lesson.getDate());
            LocalDate currentDate = LocalDate.now();
            boolean previousLesson = true;
            if (lessonDate.compareTo(currentDate) > 0) {
                previousLesson = false;
            }
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
                    <li>
                        <a href="handleCourse?action=display&courseId=<%out.print(lesson.getCourseId());%>">
                            <%out.println(course.getModule().getModuleTitle());%>
                        </a>
                    </li>
                    <li class="active">Lesson</li>
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
                                    <p>Class: <%out.println(course.getClassId());%></p>
                                </div>
                            </div>
                            <div class="profile-footer">
                                <ul>
                                    <li>
                                        <span>Date</span>
                                        <span><%out.print(lesson.getDate());%></span>
                                    </li>
                                    <li>
                                        <span>Time</span>
                                        <span><%out.print(lesson.getStartTime() + " - " + lesson.getEndTime());%></span>
                                    </li>
                                </ul>
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-12 col-sm-9">
                        <div class="card">
                            <div class="body">
                                <div>
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li role="presentation" class="active"><a href="#attendance" aria-controls="settings" role="tab" data-toggle="tab">Attendance</a></li>
                                            <%
                                                if ("admin".equalsIgnoreCase(role) && !previousLesson) {
                                                    out.println("<li role='presentation'><a href='#lesson_remove' aria-controls='settings' role='tab' data-toggle='tab'>Remove Lesson</a></li>");
                                                }
                                            %>
                                    </ul>

                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade in active" id="attendance">
                                            <%@include file="component/studentAttendForm.jsp" %>
                                        </div>
                                        <%                                            if (!previousLesson) {
                                                if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
                                                    out.println("<div role='tabpanel' class='tab-pane fade in' id='lesson_remove'>");
                                                    out.println("<p class='m-t-10 align-center'>Please click <a href='handleLesson?action=remove&lessonId=" + lesson.getLessonId() + "'>here</a> to remove the lesson.</p>");
                                                    out.println("</div>");
                                                }
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
