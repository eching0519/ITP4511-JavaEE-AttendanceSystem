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

            String studentId = (request.getParameter("studentId") == null) ? "" : request.getParameter("studentId");
            String password = (request.getParameter("password") == null) ? "" : request.getParameter("password");
            String name = (request.getParameter("name") == null) ? "" : request.getParameter("name");
            String classId = (request.getParameter("classId") == null) ? "" : request.getParameter("classId");
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
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="students.jsp">Student</a></li>
                    <li class="active">Add</li>
                </ol>
                <%
                    if (!msg.equals("")) {
                        out.println("<div class='alert alert-danger'>");
                        out.println(msg);
                        out.println("</div>");
                    }
                %>
                <div class="card">
                    <div class="header">
                        <h2>STUDENT INFORMATION</h2>
                    </div>
                    <div class="body">
                        <form action="handleStudent" method="POST">
                            <input type="hidden" name="action" value="add" />

                            <div class="form-group form-float">
                                <div class="form-line">
                                    <input type="text" class="form-control" name="name" value="<%=name%>" maxlength="25" required>
                                    <label class="form-label">Name</label>
                                </div>
                            </div>

                            <div class="form-group form-float">
                                <div class="form-line">
                                    <input type="text" class="form-control" name="studentId" value="<%=studentId%>" maxlength="25" required>
                                    <label class="form-label">Student ID</label>
                                </div>
                            </div>

                            <div class="form-group form-float">
                                <div class="form-line">
                                    <input type="password" class="form-control" name="password" value="<%=password%>" maxlength="25" required>
                                    <label class="form-label">Password</label>
                                </div>
                            </div>

                            <div class="form-group form-float">
                                <div class="form-line">
                                    <input type="text" class="form-control" name="classId" value="<%=classId%>" maxlength="20" required>
                                    <label class="form-label">Class</label>
                                </div>
                            </div>

                            <button class="btn btn-primary waves-effect" type="submit">SUBMIT</button>
                        </form>
                    </div>
                </div>
            </div>
        </section>

    </body>

</html>
