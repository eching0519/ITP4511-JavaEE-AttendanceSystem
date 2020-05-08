<%-- 
    Document   : studentRecord
    Created on : 2019年12月14日, 下午03:31:47
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.*"%>
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

            Student s;
            String studentId = "N/A";
            String name = "N/A";
            String classId = "N/A";

            if (request.getAttribute("student") != null) {
                s = (Student) request.getAttribute("student");
                studentId = s.getStudentId();
                name = s.getName();
                classId = s.getClassId();
            }

            String msg = (request.getParameter("msg") == null) ? "" : request.getParameter("msg");
        %>

        <!-- Top Bar -->
        <%@include file="component/topBar.jsp" %>
        <!-- #Top Bar -->
        <section>
            <!-- Left Sidebar -->
            <tag:nav role="<%=role%>" username="<%=username%>" nickname="<%=nickname%>" activeOn="student" />
            <!-- #END# Left Sidebar -->
        </section>

        <section class="content">
            <div class="container-fluid">
                <ol class="breadcrumb breadcrumb-col-pink">
                    <%
                        if("student".equalsIgnoreCase(role)) {
                    %>
                    <li><a href="index.jsp">Home</a></li>
                    <li class="active">Account Setting</li>
                        <%} else {%>
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="students.jsp">Student</a></li>
                    <li class="active"><%=name%></li>
                        <%
                            }
                        %>
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
                                    <p>Student ID: <%=studentId%></p>
                                    <p>Class: <a class="col-pink" href="classInfo?class=<%=classId%>"><%=classId%></a></p>
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
                                            <%
                                                if ("admin".equalsIgnoreCase(role) || studentId.equalsIgnoreCase(username)) {
                                                    out.println("<li role='presentation'><a href='#account_settings' aria-controls='settings' role='tab' data-toggle='tab'>Account Settings</a></li>");
                                                }

                                                if ("admin".equalsIgnoreCase(role)) {
                                                    out.println("<li role='presentation'><a href='#account_remove' aria-controls='settings' role='tab' data-toggle='tab'>Remove Account</a></li>");
                                                }
                                            %>
                                    </ul>

                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade in active" id="course">
                                            <tag:stuAttendList studentId="<%=studentId%>" />
                                        </div>
                                        <div role="tabpanel" class="tab-pane fade in" id="account_settings">

                                            <form class="form-horizontal" action="handleStudent">
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
                                                    <label for="form_studentID" class="col-sm-2 control-label">Student ID</label>
                                                    <div class="col-sm-10">
                                                        <div class="form-line">
                                                            <input type="text" class="form-control" id="form_studentID" placeholder="Student ID" value="<%=studentId%>" maxlength="25" disabled>
                                                            <input type="hidden" name="studentId" value="<%=studentId%>" />
                                                        </div>
                                                    </div>
                                                </div>

                                                <%if ("admin".equalsIgnoreCase(role)) {%>
                                                <div class="form-group">
                                                    <div class="col-sm-offset-2 col-sm-10">
                                                        <input type="checkbox" id="form_resetPw" class="chk-col-red filled-in" name="password" value="<%=studentId%>" />
                                                        <label for="form_resetPw">Reset password</label>
                                                    </div>
                                                </div>

                                                <%} else if (studentId.equalsIgnoreCase(username)) {%>
                                                <div class="form-group">
                                                    <label for="form_password" class="col-sm-2 control-label">Password</label>
                                                    <div class="col-sm-10">
                                                        <div class="form-line">
                                                            <input type="password" class="form-control" id="form_password" name="password" placeholder="Password" value="" maxlength="25" required="">
                                                        </div>
                                                    </div>
                                                </div>
                                                <%}%>

                                                <%if ("admin".equalsIgnoreCase(role)) {%>
                                                <div class="form-group">
                                                    <label for="form_class" class="col-sm-2 control-label">Class</label>
                                                    <div class="col-sm-10">
                                                        <div class="form-line">
                                                            <input type="text" class="form-control" id="form_class" name="classId" placeholder="Class" value="<%=classId%>" maxlength="20" required>
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

                                        <%
                                            if ("admin".equalsIgnoreCase(role)) {
                                                out.println("<div role='tabpanel' class='tab-pane fade in' id='account_remove'>");
                                                out.println("<p class='m-t-10 align-center'>Please click <a href='handleStudent?action=remove&studentId=" + studentId + "'>here</a> to remove the account.</p>");
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
