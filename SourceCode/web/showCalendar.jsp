<%-- 
    Document   : index
    Created on : 2019年12月13日, 上午07:36:22
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.*"%>
<%@page import="com.db.SystemDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/taglib.tld" prefix="tag" %>
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
%>

<!DOCTYPE html>
<html>
    <head>
        <%@include file="component/head.jsp" %>
    </head>

    <body class="theme-red">
        <!-- Top Bar -->
        <%@include file="component/topBar.jsp" %>
        <!-- #Top Bar -->
        <section>
            <!-- Left Sidebar -->
            <tag:nav role="<%=role%>" username="<%=username%>" nickname="<%=nickname%>" activeOn="schedule" />
            <!-- #END# Left Sidebar -->
        </section>

        <section class="content">
            <div class="container-fluid">
                <ol class="breadcrumb breadcrumb-col-pink">
                    <li><a href="index.jsp">Home</a></li>
                    <li class="active">Calendar</li>
                </ol>
                <div class="row clearfix">
                    <%
                        if ("admin".equalsIgnoreCase(role) || "teacher".equalsIgnoreCase(role)) {
                    %>
                    <div class="col-xs-12 col-sm-4">
                        <%
                            if ("admin".equalsIgnoreCase(role)) {
                        %>
                        <div class="card">
                            <div class="header">
                                <h2>EDIT SCHOOL DAY</h2>
                            </div>
                            <div class="body">
                                <form action="handleSchoolDay.jsp">
                                    <button class="btn btn-primary waves-effect" type="submit">EDIT</button>
                                </form>
                            </div>
                        </div>
                        <%}%>

                        <div class="card">
                            <div class="header">
                                <h2>SEARCH CALENDAR</h2>
                            </div>
                            <div class="body">
                                <form class="form-horizontal" method="GET" action="calendar" autocomplete="off">
                                    <input type="hidden" name="action" value="searchCalendar" />
                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Class</label>
                                        <div class="col-sm-10">
                                            <div class="form-line">
                                                <input type="text" class="form-control" placeholder="Class" name="classId" value="" required="" />
                                            </div>
                                        </div>
                                    </div>

                                    <button class="btn btn-primary waves-effect" type="submit">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                    </div>
                    <%}%>

                    <div class="col-xs-12 col-sm-8">
                        <div class="card">
                            <div class="header">
                                <h2>Calendar</h2>
                            </div>
                            <div class="body">
                                <%@include file="component/calendar.jsp" %>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </section>

    </body>

</html>
