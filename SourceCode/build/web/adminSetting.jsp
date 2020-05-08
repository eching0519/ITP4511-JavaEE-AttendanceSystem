<%-- 
    Document   : studentRecord
    Created on : 2019年12月14日, 下午03:31:47
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.*, com.db.SystemDB"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/taglib.tld" prefix="tag" %>
<%
    String submitted = request.getParameter("submitted");
    if ("true".equalsIgnoreCase(submitted)) {
        String dbUrl = application.getInitParameter("dbUrl");
        String dbUser = application.getInitParameter("dbUser");
        String dbPassword = application.getInitParameter("dbPassword");
        SystemDB db = new SystemDB(dbUrl, dbUser, dbPassword);

        String form_username = request.getParameter("username");
        String form_password = request.getParameter("password");
        boolean isSuccess = db.setPassword(form_username, form_password);
        if (isSuccess) {
            response.sendRedirect(application.getContextPath()+"/adminSetting.jsp?msg=Success!+Account+is+updated");
        } else {
            response.sendRedirect(application.getContextPath()+"/adminSetting.jsp?msg=Error!+Account+updated+fail");
        }
    }
%>
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

            String msg = (request.getParameter("msg") == null) ? "" : request.getParameter("msg");
        %>

        <!-- Top Bar -->
        <%@include file="component/topBar.jsp" %>
        <!-- #Top Bar -->
        <section>
            <!-- Left Sidebar -->
            <tag:nav role="<%=role%>" username="<%=username%>" nickname="<%=nickname%>" activeOn="setting" />
            <!-- #END# Left Sidebar -->
        </section>

        <section class="content">
            <div class="container-fluid">
                <ol class="breadcrumb breadcrumb-col-pink">
                    <li><a href="index.jsp">Home</a></li>
                    <li class="active">Account Setting</li>
                </ol>
                <%
                    if (!"".equals(msg)) {
                        out.println("<div class='alert bg-orange alert-dismissible'>" + msg);
                        out.println("<button type='button' class='close' data-dismiss='alert' aria-label='Close'><span aria-hidden='true'>×</span></button>");
                        out.println("</div>");
                    }
                %>
                <div class="row clearfix">
                    <div class="col-xs-12 col-sm-9">
                        <div class="card">
                            <div class="body">
                                <div>
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li role="presentation" class="active"><a href="#account_settings" aria-controls="settings" role="tab" data-toggle="tab">Account Settings</a></li>
                                    </ul>

                                    <div class="tab-content">
                                        <div role="tabpanel" class="tab-pane fade in active" id="account_settings">
                                            <form class="form-horizontal">
                                                <div class="form-group">
                                                    <label for="form_username" class="col-sm-2 control-label">Username</label>
                                                    <div class="col-sm-10">
                                                        <div class="form-line">
                                                            <input type="text" class="form-control" id="form_username" placeholder="Username" value="<%=username%>" maxlength="25" disabled>
                                                            <input type="hidden" name="username" value="<%=username%>" />
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="form-group">
                                                    <label for="form_password" class="col-sm-2 control-label">Password</label>
                                                    <div class="col-sm-10">
                                                        <div class="form-line">
                                                            <input type="password" class="form-control" id="form_password" name="password" placeholder="Password" value="" maxlength="25" required="">
                                                        </div>
                                                    </div>
                                                </div>


                                                <div class="form-group">
                                                    <div class="col-sm-offset-2 col-sm-10">
                                                        <button type="submit" class="btn btn-danger" name="submitted" value="true">SUBMIT</button>
                                                    </div>
                                                </div>
                                            </form>
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
