<%-- 
    Document   : ErrorPage
    Created on : 2019年12月21日, 上午12:15:52
    Author     : EChing
--%>

<%@page import="java.io.PrintWriter"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page isErrorPage="true" %>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="component/head.jsp" %>
    </head>
    <body>
        <div class="five-zero-zero-container">
            <div class="error-code"><%=exception%></div>
            <div class="error-message">
                <%
                    exception.printStackTrace(new PrintWriter(out));
                %>
            </div>
            <div class="button-place">
                <a href="index.jsp" class="btn btn-default btn-lg waves-effect">GO TO HOMEPAGE</a>
            </div>
        </div>
    </body>
</html>
