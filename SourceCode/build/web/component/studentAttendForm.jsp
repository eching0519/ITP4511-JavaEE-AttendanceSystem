<%-- 
    Document   : studentAttendForm
    Created on : 2019年12月16日, 上午10:23:08
    Author     : EChing
--%>


<%@page import="com.bean.*, java.util.ArrayList, java.text.SimpleDateFormat, java.util.Date"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="students" class="java.util.ArrayList" scope="request" />
<jsp:useBean id="attendances" class="java.util.ArrayList" scope="request" />
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            $(document).ready(function () {
                $(":radio").change(function () {
                    studentId = $(this).attr("name");

                    time = $(this)[0].value.toString();

                    if (time == "") {
                        $("#lateTime-" + studentId).removeAttr("disabled");
                        $("#lateTime-" + studentId).attr("required", "");
                        date = new Date();
                        hour = date.getHours();
                        min = date.getMinutes();
                        $("#lateTime-" + studentId).val(hour + ":" + min);
                        $("#late-" + studentId).val(hour + ":" + min);
                    } else {
                        $("#lateTime-" + studentId).attr("disabled", "");
                        $("#lateTime-" + studentId).removeAttr("required");
                        $("#lateTime-" + studentId).val("");
                        $("#late-" + studentId).val("");
                    }
                });

                $(".timeTxtBox").change(function () {
                    studentId = $(this).attr("id").split('-')[1];
                    $("#late-" + studentId).val($(this)[0].value);
                    console.log($("#late-" + studentId)[0].value);
                });
            });
        </script>
    </head>
    <body>
        <%
            int lessonId = lesson.getLessonId();
            String startTime = lesson.getStartTime();
            String endTime = lesson.getEndTime();
            String classId = lesson.getCourse().getClassId();
        %>
        <div class="body table-responsive">
            <form action="handleAttendance" method="GET">
                <input type="hidden" name="action" value="update" />
                <input type="hidden" name="lessonId" value="<%=lessonId%>" />
                <input type="hidden" name="classId" value="<%=classId%>" />
                <input type="hidden" name="startTime" value="<%=startTime%>" />
                <input type="hidden" name="endTime" value="<%=endTime%>" />
                <table class="table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>STUDENT ID</th>
                            <th>NAME</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            ArrayList dataList;
                            Attendance a = null;
                            if (attendances.size() > 0) {
                                dataList = attendances;
                            } else {
                                dataList = students;
                            }
                            for (int i = 0; i < dataList.size(); i++) {
                                Student s;
                                if (attendances.size() > 0) {
                                    a = (Attendance) attendances.get(i);
                                    s = a.getStudent();
                                } else {
                                    s = (Student) students.get(i);
                                }
                                String sId = s.getStudentId();
                                String sName = s.getName();
                        %>
                        <tr>
                            <th scope="row"><%=i + 1%></th>
                            <td><%=sId%></td>
                            <td><%=sName%></td>
                            <td style="position:relative;">
                                <input name="<%=sId%>" type="radio" id="attend-<%=sId%>" class="with-gap radio-col-red" value="<%=startTime%>"
                                       <%
                                           if (attendances.size() > 0) {
                                               if (a.getLate() == 0) {
                                                   out.print("checked=''");
                                               }
                                           }
                                       %>
                                       />
                                <label for="attend-<%=sId%>">Attend</label>

                                <input name="<%=sId%>" type="radio" id="absence-<%=sId%>" class="with-gap radio-col-red" 
                                       <%
                                           if (attendances.size() > 0) {
                                               if (a.getParticipated() == 0) {
                                                   out.print("checked=''");
                                               }
                                           } else {
                                               out.print("checked=''");
                                           }
                                       %>
                                       value="<%=endTime%>" />
                                <label for="absence-<%=sId%>">Absence</label>

                                <input name="<%=sId%>" type="radio" id="late-<%=sId%>" class="with-gap radio-col-red radio-late"
                                       <%
                                           SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                                           String attendingTime_str = "";

                                           if (attendances.size() > 0 && a.getParticipated() != 0) {
                                               if (a.getLate() > 0) {
                                                   out.print("checked=''");

                                                   Date startingTime = format.parse(startTime);
                                                   Date lateTime = new Date(a.getLate() * 1000 * 60);
                                                   Date attendingTime = new Date(startingTime.getTime() + lateTime.getTime());
                                                   attendingTime_str = attendingTime.toString().split(" ")[3].substring(0, 5);
                                                   out.print("value='" + attendingTime_str + "'");
                                               }
                                           }
                                       %>
                                       />
                                <label for="late-<%=sId%>">Late</label>
                                <input type="text" class="form-control timeTxtBox" placeholder="12:59" pattern="(([0]\d)|(1\d)|(2[0-3])):[0-5]\d" 
                                       style="width:65px;position: absolute; top:7px; left:215px;" id="lateTime-<%=sId%>" 
                                       <%
                                           if (attendances.size() > 0 && a.getParticipated() != 0) {
                                               if (a.getLate() > 0) {
                                                   out.print("value='" + attendingTime_str + "'");
                                               } else {
                                                   out.print("disabled=''");
                                               }
                                           } else {
                                               out.print("disabled=''");
                                           }
                                       %>
                                       />
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
                <div class="align-right">
                    <button type="submit" class="btn btn-danger waves-effect">SUBMIT</button>
                </div>
            </form>
        </div>
    </body>
</html>
