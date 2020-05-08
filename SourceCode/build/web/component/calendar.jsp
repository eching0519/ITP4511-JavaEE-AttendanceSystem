<%-- 
    Document   : index
    Created on : 2019年12月13日, 上午07:36:22
    Author     : EChing
--%>

<%@page import="com.bean.User"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="/WEB-INF/tlds/taglib.tld" prefix="tag" %>
<!DOCTYPE html>
<html>
    <head>
        <script>
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
            <%
                if (request.getAttribute("classId") != null) {
                    String calendar_classId = (String) request.getAttribute("classId");
                    out.println("'lessonEventJson?classId=" + calendar_classId + "',");
                }

                if (request.getAttribute("teacher") != null) {
                    String teacher = (String) request.getAttribute("teacher");
                    out.println("'lessonEventJson?teacher=" + teacher + "',");
                }
            %>
                        'schoolDayJson'
                    ]
                });

                calendar.render();
            });


        </script>
    </head>

    <body>
        <div class="body">
            <div id='calendar'></div>
        </div>
    </body>

</html>
