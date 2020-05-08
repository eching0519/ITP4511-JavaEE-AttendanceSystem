<%-- 
    Document   : index
    Created on : 2019年12月13日, 上午07:36:22
    Author     : EChing
--%>

<%@page errorPage="ErrorPage.jsp" %>
<%@page import="com.bean.Module"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/taglib.tld" prefix="tag" %>
<jsp:useBean id="course" class="com.bean.Course" scope="request" />
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
        <script>
            $(document).ready(function () {
                //Bootstrap datepicker plugin
                $('#bs_datepicker_container input').datepicker({
                    format: 'yyyy-mm-dd',
                    autoclose: true,
                    container: '#bs_datepicker_container'
                });
            });

            function validate() {
                if (document.getElementById("duration").value <= 0) {
                    alert("Invalid Time");
                    return false;
                }
                return true;
            }

            function getDuration() {
                startTime = $("#startTime").val();
                endTime = $("#endTime").val();
                if (startTime == "" || endTime == "")
                    return;

                time_arr = startTime.split(":");
                hour = time_arr[0];
                min = (time_arr[1] == 0) ? 0 : 5;
                start = hour + "." + min

                time_arr = endTime.split(":");
                hour = time_arr[0];
                min = (time_arr[1] == 0) ? 0 : 5;
                end = hour + "." + min

                document.getElementById("duration").value = end - start;
            }

            $(document).ready(function () {

                $("#date").change(function () {
                    week = (new Date($(this).val())).toDateString().substr(0, 3)
                    $("#week")[0].innerHTML = "(" + week + ")";
                });

                $("#repeat, #date").change(function () {
                    repeat = $("#repeat").val();
                    if (repeat <= 0) {
                        document.getElementById("endDate").value = "";
                        return;
                    }
                    date = new Date($("#date").val());
                    date.setDate(date.getDate() + (repeat - 1) * 7);
                    endDate = date.toISOString().substr(0, 10);
                    document.getElementById("endDate").value = endDate;
                });
            });
        </script>
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
                <ol class="breadcrumb breadcrumb-col-pink">
                    <li><a href="index.jsp">Home</a></li>
                    <li><a href="courses.jsp">Course</a></li>
                    <li>
                        <a href="handleCourse?action=display&courseId=<%out.println(course.getCourseId());%>">
                            <%out.println(course.getModule().getModuleTitle());%>
                        </a>
                    </li>
                    <li class="active">Add Lesson</li>
                </ol>
                <div class="row clearfix">
                    <div class="col-xs-12 col-sm-4">
                        <div class="card">
                            <div class="header">
                                <h2>LESSON INFORMATION</h2>
                            </div>
                            <div class="body">
                                <form class="form-horizontal" onsubmit="return validate();">
                                    <input type="hidden" name="action" value="addLesson" />
                                    <input type="hidden" name="courseId" value=<%out.println(course.getCourseId());%> />
                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Course</label>
                                        <div class="col-sm-10">
                                            <div>
                                                <input type="text" class="form-control" value="<%out.println(course.getModule().getModuleTitle());%>" readonly="" />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Teacher</label>
                                        <div class="col-sm-10">
                                            <div>
                                                <input type="text" class="form-control" value="<%out.println(course.getTeacher().getName());%>" readonly="" />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Class</label>
                                        <div class="col-sm-10">
                                            <div>
                                                <input type="text" class="form-control" value="<%out.println(course.getClassId());%>" readonly="" />
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label for="form_name" class="col-sm-2 control-label">Date</label>
                                        <div class="col-sm-10">
                                            <div class="form-line" id="bs_datepicker_container">
                                                <input type="text" class="form-control" placeholder="Please choose a date..." name="startDate" id="date" required="" readonly="">
                                                <span style="position: absolute;bottom: 7px; right: 15px;" id="week"></span>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">From</label>
                                        <div class="col-sm-4">
                                            <select class="form-control show-tick" name="startTime" required="" id="startTime" onchange="getDuration()">
                                                <option value=""> - Time - </option>
                                                <option value="08:00">08:00</option>
                                                <option value="08:30">08:30</option>
                                                <option value="09:00">09:00</option>
                                                <option value="09:30">09:30</option>
                                                <option value="10:00">10:00</option>
                                                <option value="10:30">10:30</option>
                                                <option value="11:00">11:00</option>
                                                <option value="11:30">11:30</option>
                                                <option value="12:00">12:00</option>
                                                <option value="12:30">12:30</option>
                                                <option value="13:00">13:00</option>
                                                <option value="13:30">13:30</option>
                                                <option value="14:00">14:00</option>
                                                <option value="14:30">14:30</option>
                                                <option value="15:00">15:00</option>
                                                <option value="15:30">15:30</option>
                                                <option value="16:00">16:00</option>
                                                <option value="16:30">16:30</option>
                                                <option value="17:00">17:00</option>
                                                <option value="17:30">17:30</option>
                                                <option value="18:00">18:00</option>
                                                <option value="18:30">18:30</option>
                                                <option value="19:00">19:00</option>
                                                <option value="19:30">19:30</option>
                                                <option value="20:00">20:00</option>
                                                <option value="20:30">20:30</option>
                                            </select>
                                        </div>
                                        <label class="col-sm-2 control-label">To</label>
                                        <div class="col-sm-4">
                                            <select class="form-control show-tick" name="endTime" required="" id="endTime" onchange="getDuration()">
                                                <option value=""> - Time - </option>
                                                <option value="08:00">08:00</option>
                                                <option value="08:30">08:30</option>
                                                <option value="09:00">09:00</option>
                                                <option value="09:30">09:30</option>
                                                <option value="10:00">10:00</option>
                                                <option value="10:30">10:30</option>
                                                <option value="11:00">11:00</option>
                                                <option value="11:30">11:30</option>
                                                <option value="12:00">12:00</option>
                                                <option value="12:30">12:30</option>
                                                <option value="13:00">13:00</option>
                                                <option value="13:30">13:30</option>
                                                <option value="14:00">14:00</option>
                                                <option value="14:30">14:30</option>
                                                <option value="15:00">15:00</option>
                                                <option value="15:30">15:30</option>
                                                <option value="16:00">16:00</option>
                                                <option value="16:30">16:30</option>
                                                <option value="17:00">17:00</option>
                                                <option value="17:30">17:30</option>
                                                <option value="18:00">18:00</option>
                                                <option value="18:30">18:30</option>
                                                <option value="19:00">19:00</option>
                                                <option value="19:30">19:30</option>
                                                <option value="20:00">20:00</option>
                                                <option value="20:30">20:30</option>
                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Duration</label>
                                        <div class="col-sm-10">
                                            <div>
                                                <input class="form-control" name="duration" id="duration" value="0" readonly="" required="" />
                                            </div>
                                        </div>
                                    </div>

                                    <hr />

                                    <div class="form-group form-float">
                                        <label for="form_name" class="col-sm-2 control-label">Repeat</label>
                                        <div class="col-sm-10">
                                            <div class="form-line">
                                                <input type="number" class="form-control" name="repeat" id="repeat" min="1" max="30" value="1">
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group form-float">
                                        <label class="col-sm-2 control-label">Until</label>
                                        <div class="col-sm-10">
                                            <div>
                                                <input type="text" class="form-control" id="endDate" value="" readonly="" />
                                            </div>
                                        </div>
                                    </div>
                                    <button class="btn btn-primary waves-effect" type="submit">SUBMIT</button>
                                </form>
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-12 col-sm-8">
                        <div class="card">
                            <div class="header">
                                <h2>Current Schedule</h2>
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
