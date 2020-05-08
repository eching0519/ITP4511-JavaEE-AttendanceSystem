<%-- 
    Document   : index
    Created on : 2019年12月13日, 上午07:36:22
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/taglib.tld" prefix="tag" %>
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

            String classId = (request.getParameter("class") == null) ? "" : request.getParameter("class");
            String msg = (request.getParameter("msg") == null) ? "" : request.getParameter("msg");
        %>

        <!-- Top Bar -->
        <%@include file="component/topBar.jsp" %>
        <!-- #Top Bar -->
        <section>
            <!-- Left Sidebar -->
            <tag:nav role="<%=role%>" username="<%=username%>" nickname="<%=nickname%>" activeOn="class" />
            <!-- #END# Left Sidebar -->
        </section>

        <section class="content">
            <div class="container-fluid">
                <ol class="breadcrumb breadcrumb-col-pink">
                    <li><a href="index.jsp">Home</a></li>
                    <li class="active">Class</li>
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
                                    <h3>Class: <%=classId%></h3>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12 col-sm-9">
                        <div class="card">
                            <div class="body">
                                <div>
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li role="presentation" class="active"><a href="#calendar" aria-controls="home" role="tab" data-toggle="tab">Calendar</a></li>
                                        <li role="presentation"><a href="#students" aria-controls="settings" role="tab" data-toggle="tab">Students</a></li>
                                    </ul>

                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade in active" id="calendar">
                                            <%@include file="component/calendar.jsp" %>
                                        </div>
                                        <div role="tabpanel" class="tab-pane fade in" id="students">
                                            <tag:sList classId="<%=classId%>" />
                                        </div>
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
