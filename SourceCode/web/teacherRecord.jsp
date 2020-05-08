<%-- 
    Document   : studentRecord
    Created on : 2019年12月14日, 下午03:31:47
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.User, com.bean.Teacher"%>
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

            Teacher t;
            String tId = "N/A";
            String name = "N/A";

            if (request.getAttribute("teacher") != null) {
                t = (Teacher) request.getAttribute("teacher");
                tId = t.getUsername();
                name = t.getName();
            }

            String msg = (request.getParameter("msg") == null) ? "" : request.getParameter("msg");
        %>

        <!-- Top Bar -->
        <%@include file="component/topBar.jsp" %>
        <!-- #Top Bar -->
        <section>
            <!-- Left Sidebar -->
            <tag:nav role="<%=role%>" username="<%=username%>" nickname="<%=nickname%>" activeOn="teacher" />
            <!-- #END# Left Sidebar -->
        </section>

        <section class="content">
            <div class="container-fluid">
                <ol class="breadcrumb breadcrumb-col-pink">
                    <%
                        if ("teacher".equalsIgnoreCase(role)) {
                    %>
                    <li><a href="index.jsp">Home</a></li>
                    <li class="active">Account Setting</li>
                        <%} else {%>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="teachers.jsp">Teacher</a></li>
                    <li class="active"><%=name%></li>
                        <%}%>
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
                                    <h3><%=name%></h3>
                                    <p>Username: <%=tId%></p>
                                </div>
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-12 col-sm-9">
                        <div class="card">
                            <div class="body">
                                <div>
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li role="presentation" class="active"><a href="#course" aria-controls="course" role="tab" data-toggle="tab">Course</a></li>
                                        <li role="presentation"><a href="#account_settings" aria-controls="settings" role="tab" data-toggle="tab">Account Settings</a></li>
                                            <%
                                                if ("admin".equalsIgnoreCase(role)) {
                                            %>
                                        <li role="presentation"><a href="#account_remove" aria-controls="settings" role="tab" data-toggle="tab">Remove Account</a></li>
                                            <%}%>
                                    </ul>

                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade in active" id="course">
                                            <tag:courseList user="<%=tId%>" />
                                        </div>
                                        <div role="tabpanel" class="tab-pane fade in" id="account_settings">
                                            <form class="form-horizontal" action="handleTeacher">
                                                <input type="hidden" name="action" value="edit" />
                                                <div class="form-group">
                                                    <label for="form_name" class="col-sm-2 control-label">Name</label>
                                                    <div class="col-sm-10">
                                                        <div class="form-line">
                                                            <input type="text" class="form-control" id="form_name" name="name" placeholder="Name" value="<%=name%>" maxlength="25" required>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label for="form_username" class="col-sm-2 control-label">Username</label>
                                                    <div class="col-sm-10">
                                                        <div class="form-line">
                                                            <input type="text" class="form-control" id="form_username" placeholder="Username" value="<%=tId%>" maxlength="25" disabled>
                                                            <input type="hidden" name="username" value="<%=tId%>" />
                                                        </div>
                                                    </div>
                                                </div>

                                                <%if ("admin".equalsIgnoreCase(role)) {%>
                                                <div class="form-group">
                                                    <div class="col-sm-offset-2 col-sm-10">
                                                        <input type="checkbox" id="form_resetPw" class="chk-col-red filled-in" name="password" value="<%=username%>" />
                                                        <label for="form_resetPw">Reset password</label>
                                                    </div>
                                                </div>

                                                <%} else if (username.equalsIgnoreCase(username)) {%>
                                                <div class="form-group">
                                                    <label for="form_password" class="col-sm-2 control-label">Password</label>
                                                    <div class="col-sm-10">
                                                        <div class="form-line">
                                                            <input type="password" class="form-control" id="form_password" name="password" placeholder="Password" value="" maxlength="25" required="">
                                                        </div>
                                                    </div>
                                                </div>
                                                <%}%>


                                                <div class="form-group">
                                                    <div class="col-sm-offset-2 col-sm-10">
                                                        <button type="submit" class="btn btn-danger">SUBMIT</button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                        <%if ("admin".equalsIgnoreCase(role)) {%>
                                        <div role="tabpanel" class="tab-pane fade in" id="account_remove">
                                            <p class="m-t-10 align-center">Please click <a href="handleTeacher?action=remove&id=<%=tId%>">here</a> to remove the account.</p>
                                        </div>
                                        <%}%>
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
