<%-- 
    Document   : login.jsp
    Created on : 2019年12月13日, 上午06:12:16
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <title>Log In | VTG Attendance System</title>
        <!-- Favicon-->
        <link rel="icon" href="favicon.ico" type="image/x-icon">

        <!-- Google Fonts -->
        <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&subset=latin,cyrillic-ext" rel="stylesheet" type="text/css">
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet" type="text/css">

        <!-- Bootstrap Core Css -->
        <link href="plugins/bootstrap/css/bootstrap.css" rel="stylesheet">

        <!-- Waves Effect Css -->
        <link href="plugins/node-waves/waves.css" rel="stylesheet" />

        <!-- Animation Css -->
        <link href="plugins/animate-css/animate.css" rel="stylesheet" />

        <!-- Custom Css -->
        <link href="css/style.css" rel="stylesheet">
    </head>

    <body class="login-page">
        <%
            String username = (String) session.getAttribute("username");
            if (username == null) {
                username = "";
            }
            String password = (String) session.getAttribute("password");
            if (password == null) {
                password = "";
            }

            String rememberme = (String) session.getAttribute("rememberme");

            String msg = "";
            if (request.getParameter("msg") != null) {
                msg = (String) request.getParameter("msg");
            }
        %>
        <div class="login-box">
            <div class="logo">
                <a>VTG Attendance System</a>
            </div>

            <%
                if (msg.equalsIgnoreCase("invalid")) {
                    out.println("<div class='alert alert-danger'>");
                    out.println("Invalid login, please try again");
                    out.println("</div>");
                }
                if (msg.equalsIgnoreCase("timeout")) {
                    out.println("<div class='alert alert-danger'>");
                    out.println("Session timeout. Please login again.");
                    out.println("</div>");
                }
            %>

            <div class="card">
                <div class="body">
                    <form id="sign_in" method="POST" action="handleUser">
                        <input type="hidden" name="action" value="login" />
                        <div class="msg">Sign in to start your session</div>
                        <div class="input-group">
                            <span class="input-group-addon">
                                <i class="material-icons">person</i>
                            </span>
                            <div class="form-line">
                                <input type="text" class="form-control" name="username" placeholder="Username" value="<%=username%>" required autofocus>
                            </div>
                        </div>
                        <div class="input-group">
                            <span class="input-group-addon">
                                <i class="material-icons">lock</i>
                            </span>
                            <div class="form-line">
                                <input type="password" class="form-control" name="password" placeholder="Password" value="<%=password%>" required>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-xs-8 p-t-5">
                                <input type="checkbox" name="rememberme" id="rememberme" class="filled-in chk-col-pink" 
                                       <%if (rememberme != null) {
                                               out.println("checked");
                                           }%>
                                       />
                                <label for="rememberme">Remember Me</label>
                            </div>
                            <div class="col-xs-4">
                                <button class="btn btn-block bg-pink waves-effect" type="submit">SIGN IN</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <!-- Jquery Core Js -->
        <script src="plugins/jquery/jquery.min.js"></script>

        <!-- Bootstrap Core Js -->
        <script src="plugins/bootstrap/js/bootstrap.js"></script>

        <!-- Waves Effect Plugin Js -->
        <script src="plugins/node-waves/waves.js"></script>

        <!-- Validation Plugin Js -->
        <script src="plugins/jquery-validation/jquery.validate.js"></script>

        <!-- Custom Js -->
        <script src="js/admin.js"></script>
        <script src="js/pages/examples/sign-in.js"></script>
    </body>

</html>