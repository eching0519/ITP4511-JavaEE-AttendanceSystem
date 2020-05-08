<%-- 
    Document   : index
    Created on : 2019年12月13日, 上午07:36:22
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.Module"%>
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

            String moduleCode = (request.getParameter("moduleCode") == null) ? "" : request.getParameter("moduleCode");
            String moduleTitle = (request.getParameter("moduleTitle") == null) ? "" : request.getParameter("moduleTitle");
            String contactHours = (request.getParameter("contactHours") == null) ? "" : request.getParameter("contactHours");
            String action = "Add";

            if (request.getAttribute("module") != null) {
                Module m = (Module) request.getAttribute("module");
                if (m != null) {
                    moduleCode = m.getModuleCode();
                    moduleTitle = m.getModuleTitle();
                    contactHours = m.getContactHours() + "";
                    action = "Edit";
                }
            }
            String msg = (request.getParameter("msg") == null) ? "" : request.getParameter("msg");

        %>

        <!-- Top Bar -->
        <%@include file="component/topBar.jsp" %>
        <!-- #Top Bar -->
        <section>
            <!-- Left Sidebar -->
            <tag:nav role="<%=role%>" username="<%=username%>" nickname="<%=nickname%>" activeOn="module" />
            <!-- #END# Left Sidebar -->
        </section>

        <section class="content">
            <div class="container-fluid">
                <ol class="breadcrumb breadcrumb-col-pink">
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="modules.jsp">Module</a></li>
                    <li class="active"><%=action%></li>
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
                        <h2>MODULE INFORMATION</h2>
                    </div>
                    <div class="body">
                        <form class="form-horizontal" action="handleModule" method="POST">
                            <input type="hidden" name="action" value="<%=action%>" id="action">
                            <div class="form-group">
                                <label for="form_code" class="col-sm-1 control-label">Module Code</label>
                                <div class="col-sm-6">
                                    <div class="form-line focused">
                                        <input type="text" class="form-control" id="form_code" name="moduleCode" placeholder="Module Code" value="<%=moduleCode%>" maxlength="10" required=""
                                               <% if ("edit".equalsIgnoreCase(action)) {
                                                       out.println("readonly");
                                                   }%>
                                               />
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="form_moduleTitle" class="col-sm-1 control-label">Module Title</label>
                                <div class="col-sm-6">
                                    <div class="form-line focused">
                                        <input type="text" class="form-control" id="form_moduleTitle" name="moduleTitle" placeholder="Module Title" value="<%=moduleTitle%>" maxlength="50" required="">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="form_contactHours" class="col-sm-1 control-label">Contact Hours</label>
                                <div class="col-sm-6">
                                    <div class="form-line focused">
                                        <input type="text" class="form-control" id="form_contactHours" name="contactHours" placeholder="Contact Hours" pattern="[\d]{1,3}([.][\d])?" title="The format should be 999.9" value="<%=contactHours%>" required="">
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-sm-offset-1 col-sm-6">
                                    <button type="submit" class="btn btn-danger m-r-10">SUBMIT</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <% if ("edit".equalsIgnoreCase(action)) {
                %>
                <div class="alert bg-grey">
                    Please click <a href="handleModule?action=remove&moduleCode=<%=moduleCode%>" class="alert-link">here</a> to remove this module.
                </div>
                <%}%>
            </div>
        </section>

    </body>

</html>
