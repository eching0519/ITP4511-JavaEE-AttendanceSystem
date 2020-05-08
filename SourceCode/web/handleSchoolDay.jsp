<%-- 
    Document   : index
    Created on : 2019年12月13日, 上午07:36:22
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.db.SystemDB"%>
<%@page import="com.bean.Event"%>
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

<jsp:useBean id="event" class="com.bean.Event" scope="page" />
<jsp:setProperty name="event" property="*" />
<%
    // System database
    String dbUrl = application.getInitParameter("dbUrl");
    String dbUser = application.getInitParameter("dbUser");
    String dbPassword = application.getInitParameter("dbPassword");
    SystemDB db = new SystemDB(dbUrl, dbUser, dbPassword);

    String id = request.getParameter("id");
    if (request.getParameter("id") != null) {
        event = db.querySchoolDay(id);
    } else {
        id = "-1";
    }

    String eventTitle = event.getTitle() == null ? "" : event.getTitle();
    String eventStartDate = event.getStartDate() == null ? "" : event.getStartDate();
    String eventEndDate = event.getEndDate() == null ? "" : event.getEndDate();

    // handle the submitted form
    String submitted = request.getParameter("submitted");
    if (submitted != null) {
        if (id.equals("-1")) {
            Event newEvent = db.addSchoolDay(event.getTitle(), event.getStartDate(), event.getEndDate());
        } else {
            event.setTitle(request.getParameter("title"));
            event.setStartDate(request.getParameter("startDate"));
            event.setEndDate(request.getParameter("endDate"));
            db.editSchoolDay(event);
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="component/head.jsp" %>
        <script>
            $(function () {
                $('#datepicker_range_container').datepicker({
                    autoclose: true,
                    container: '#datepicker_range_container',
                    format: 'yyyy-mm-dd'
                });
            });

            document.addEventListener('DOMContentLoaded', function () {
                var calendarEl = document.getElementById('calendar');

                var calendar = new FullCalendar.Calendar(calendarEl, {
                    plugins: ['interaction', 'dayGrid', 'timeGrid', 'list', 'momentTimezonePlugin'],
                    timeZone: 'Asia/Hong_Kong',
                    header: {
                        left: 'prev,next today',
                        center: 'title',
                        right: 'dayGridMonth,timeGridWeek,listDay'
                    },
                    navLinks: true, // can click day/week names to navigate views
                    businessHours: {
                        daysOfWeek: [1, 2, 3, 4, 5, 6], // Mon - Sat
                        startTime: '08:00',
                        endTime: '20:30',
                    },
                    editable: true,
                    eventSources: [
                        'schoolDayJson?editable=true'
                    ]
                });

                calendar.render();
            });
        </script>
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
                    <li><a href="calendar">Calendar</a></li>
                    <li class="active">Handle Event</li>
                </ol>
                <div class="row clearfix">
                    <div class="col-xs-12 col-sm-4">
                        <div class="card">
                            <div class="header">
                                <h2>Event</h2>
                            </div>
                            <div class="body">
                                <form class="form-horizontal" method="GET" autocomplete="off">
                                    <input type="hidden" name="id" value="<%=id%>" />
                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Title</label>
                                        <div class="col-sm-10">
                                            <div class="form-line">
                                                <input type="text" class="form-control" placeholder="Title" name="title" value="<%=eventTitle%>" required="" />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label for="form_name" class="col-sm-2 control-label">Date</label>
                                        <div class="col-sm-10">
                                            <div class="input-daterange input-group" id="datepicker_range_container">
                                                <div class="form-line">
                                                    <input type="text" class="form-control" placeholder="Start Date" name="startDate" value="<%=eventStartDate%>" required="">
                                                </div>
                                                <span class="input-group-addon">to</span>
                                                <div class="form-line">
                                                    <input type="text" class="form-control" placeholder="End Date" name="endDate" value="<%=eventEndDate%>" required="">
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <button class="btn btn-primary waves-effect" type="submit" name="submitted" value="true">SUBMIT</button>
                                </form>
                            </div>
                        </div>
                        <%
                            if (!id.equals("-1")) {
                        %>
                        <div class="alert bg-grey">
                            <form method="POST">
                                <input type="hidden" name="remove" value="true" />
                                <input type="hidden" name="id" value="<%=id%>" />
                                Please click <a href="javascript:void();" onclick="this.parentNode.submit();" class="alert-link">here</a> to remove this event.
                            </form>
                        </div>
                        <%
                            }
                            if (request.getParameter("remove") != null) {
                                db.removeSchoolDay(id);
                            }
                        %>

                    </div>

                    <div class="col-xs-12 col-sm-8">
                        <div class="card">
                            <div class="header">
                                <h2>Calendar</h2>
                            </div>
                            <div class="body">
                                <div id='calendar'></div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </section>

    </body>

</html>
