<%-- 
    Document   : index
    Created on : 2019年12月13日, 上午07:36:22
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.Module,com.bean.Teacher"%>
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
<jsp:useBean id="modules" class="java.util.ArrayList" scope="request" />
<jsp:useBean id="classes" class="java.util.ArrayList" scope="request" />
<jsp:useBean id="teachers" class="java.util.ArrayList" scope="request" />
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
            <tag:nav role="<%=role%>" username="<%=username%>" nickname="<%=nickname%>" activeOn="course" />
            <!-- #END# Left Sidebar -->
        </section>

        <section class="content">
            <div class="container-fluid">
                <div class="row clearfix">
                    <div class="col-xs-12 col-sm-6">
                        <div class="card">
                            <div class="header">
                                <h2>NEW COURSE</h2>
                            </div>
                            <div class="body">
                                <form class="form-horizontal" action="handleCourse">
                                    <input type="hidden" name="action" value="add" />
                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Module</label>
                                        <div class="col-sm-10">
                                            <select class="form-control show-tick" name="moduleCode" required="">
                                                <option value="">-- Please select --</option>
                                                <%
                                                    for (int i = 0; i < modules.size(); i++) {
                                                        Module m = (Module) modules.get(i);
                                                        out.print("<option value='" + m.getModuleCode() + "'>" + m.getModuleCode() + " " + m.getModuleTitle() + "</option>");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Class</label>
                                        <div class="col-sm-10">
                                            <select class="form-control show-tick" name="class" required="">
                                                <option value="">-- Please select --</option>
                                                <%
                                                    for (int i = 0; i < classes.size(); i++) {
                                                        String c = (String) classes.get(i);
                                                        out.print("<option value='" + c + "'>" + c + "</option>");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Teacher</label>
                                        <div class="col-sm-10">
                                            <select class="form-control show-tick" name="teacher" required=""
                                                    <%
                                                        if ("teacher".equalsIgnoreCase(role)) {
                                                            out.println("disabled");
                                                        }
                                                    %>
                                                    >
                                                <option value="">-- Please select --</option>
                                                <%
                                                    for (int i = 0; i < teachers.size(); i++) {
                                                        Teacher t = (Teacher) teachers.get(i);
                                                        out.print("<option value='" + t.getUsername() + "'");
                                                        if (username.equalsIgnoreCase(t.getUsername())) {
                                                            out.print(" selected");
                                                        }
                                                        out.println(">" + t.getName() + "</option>");
                                                    }
                                                %>
                                            </select>
                                        </div>
                                    </div>
                                    <%
                                        if ("teacher".equalsIgnoreCase(role)) {
                                            out.println("<input type='hidden' name='teacher' value='" + username + "' />");
                                        }
                                    %>

                                    <div class="form-group">
                                        <div class="col-sm-offset-2 col-sm-10">
                                            <button type="submit" class="btn btn-danger">SUBMIT</button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>
                </div>

            </div>
        </section>

    </body>

</html>
