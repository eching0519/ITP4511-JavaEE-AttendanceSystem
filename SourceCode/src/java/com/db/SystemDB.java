/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.db;

import com.bean.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author EChing
 */
public class SystemDB {

    private String url;
    private String username;
    private String password;

    public SystemDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public java.sql.Connection getConnection() throws SQLException, IOException {
        System.setProperty("jdbc.drivers", "com.mysql.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

    public Course addCourse(String moduleCode, String teacher, String classId) {
        Course course = null;
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql = "INSERT INTO course (moduleCode, teacher, classId) VALUES ('" + moduleCode + "', '" + teacher + "', '" + classId + "')";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = statement.getGeneratedKeys();
            System.out.println(rs);
            if (rs.next()) {
                int courseId = rs.getInt(1);
                sql = "SELECT * FROM course WHERE courseId = " + courseId;
                rs = statement.executeQuery(sql);
                if (rs.next()) {
                    course = new Course();
                    course.setCourseId(rs.getInt("courseId"));
                    course.setModuleCode(rs.getString("moduleCode"));
                    course.setTeacherId(rs.getString("teacher"));
                    course.setClassId(rs.getString("classId"));
                }
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return course;
    }

    public boolean addLesson(String courseId, String date, String startTime, String endTime, String duration) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("INSERT INTO lesson (lessonId, courseId, date, startTime, endTime, duration) VALUES (NULL, ?, ?, ?, ?, ?)");
            pStatement.setString(1, courseId);
            pStatement.setString(2, date);
            pStatement.setString(3, startTime);
            pStatement.setString(4, endTime);
            pStatement.setString(5, duration);
            int affectedRow = pStatement.executeUpdate();
            if (affectedRow > 0) {
                isSuccess = true;
            } else {
                conn.rollback();
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public void addAttendance(String studentId, String lessonId, int participated, int late) {
        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("INSERT INTO attendance (studentId, lessonId, participated, late) VALUES (?, ?, ?, ?)");
            pStatement.setString(1, studentId);
            pStatement.setString(2, lessonId);
            pStatement.setInt(3, participated);
            pStatement.setInt(4, late);
            pStatement.executeUpdate();

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Event addSchoolDay(String title, String startDate, String endDate) {
        Event event = null;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql = "INSERT INTO schoolday (id, title, startDate, endDate) VALUES (NULL, '" + title + "', '" + startDate + "', '" + endDate + "')";
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                sql = "SELECT * FROM schoolday WHERE id = " + rs.getInt(1);
                rs = statement.executeQuery(sql);
                if (rs.next()) {
                    event = new Event();
                    event.setId(rs.getInt("id"));
                    event.setTitle(rs.getString("title"));
                    event.setStartDate(rs.getString("startDate"));
                    event.setEndDate(rs.getString("endDate"));
                }
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return event;
    }

    public void editSchoolDay(Event schoolDay) {
        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("UPDATE schoolday SET title = ?, startDate = ?, endDate = ? WHERE id = ?");
            pStatement.setString(1, schoolDay.getTitle());
            pStatement.setString(2, schoolDay.getStartDate());
            pStatement.setString(3, schoolDay.getEndDate());
            pStatement.setInt(4, schoolDay.getId());

            pStatement.executeUpdate();

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Module> queryModule() {
        ArrayList<Module> modules = new ArrayList<Module>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM module");
            while (rs.next()) {
                Module m = new Module();
                m.setModuleCode(rs.getString("moduleCode"));
                m.setModuleTitle(rs.getString("moduleTitle"));
                m.setContactHours(rs.getDouble("contactHours"));
                modules.add(m);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return modules;
    }

    public ArrayList<Course> queryCourse() {
        ArrayList<Course> courses = new ArrayList<Course>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM course");
            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("courseId"));
                c.setModuleCode(rs.getString("moduleCode"));
                c.setTeacherId(rs.getString("teacher"));
                c.setClassId(rs.getString("classId"));
                courses.add(c);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return courses;
    }

    public Course queryCourseById(String courseId) {
        Course course = null;

        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("SELECT * FROM course WHERE courseId = ?");
            pStatement.setString(1, courseId);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                course = new Course();
                course.setCourseId(rs.getInt("courseId"));
                course.setModuleCode(rs.getString("moduleCode"));
                course.setTeacherId(rs.getString("teacher"));
                course.setClassId(rs.getString("classId"));
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return course;
    }

    public ArrayList<Course> queryCourseByUser(String username) {
        ArrayList<Course> courses = new ArrayList<Course>();

        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("SELECT * FROM course WHERE teacher = ?");
            pStatement.setString(1, username);
            ResultSet rs = pStatement.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("courseId"));
                c.setModuleCode(rs.getString("moduleCode"));
                c.setTeacherId(rs.getString("teacher"));
                c.setClassId(rs.getString("classId"));
                courses.add(c);
            }

            pStatement = conn.prepareStatement("SELECT * FROM course, student WHERE course.classId = student.classId AND studentId = ?");
            pStatement.setString(1, username);
            rs = pStatement.executeQuery();
            while (rs.next()) {
                Course c = new Course();
                c.setCourseId(rs.getInt("courseId"));
                c.setModuleCode(rs.getString("moduleCode"));
                c.setTeacherId(rs.getString("teacher"));
                c.setClassId(rs.getString("classId"));
                courses.add(c);
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return courses;
    }

    public Module queryModule(String moduleCode) {
        Module m = null;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM module WHERE moduleCode = '" + moduleCode + "'");
            if (rs.next()) {
                m = new Module();
                m.setModuleCode(rs.getString("moduleCode"));
                m.setModuleTitle(rs.getString("moduleTitle"));
                m.setContactHours(rs.getDouble("contactHours"));
            }

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return m;
    }

    public boolean setModule(Module m) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql;
            sql = "UPDATE module SET moduleTitle = '" + m.getModuleTitle() + "', contactHours = '" + m.getContactHours() + "' WHERE moduleCode = '" + m.getModuleCode() + "'";
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                isSuccess = true;
            } else {
                conn.rollback();
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public boolean removeModule(String moduleCode) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM module WHERE moduleCode = '" + moduleCode + "'";
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                isSuccess = true;
            } else {
                conn.rollback();
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public boolean removeCourse(String courseId) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Savepoint savepoint = conn.setSavepoint();
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM lesson WHERE courseId = '" + courseId + "'";
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                sql = "DELETE FROM course WHERE courseId = '" + courseId + "'";
                affectedRow = statement.executeUpdate(sql);
                if (affectedRow > 0) {
                    isSuccess = true;
                }
            } else {
                conn.rollback();
            }

            if (!isSuccess) {
                conn.rollback(savepoint);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public void removeAttendance(String lessonId) {
        try {
            Connection conn = (Connection) getConnection();
            Savepoint savepoint = conn.setSavepoint();
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM attendance WHERE lessonId = '" + lessonId + "'";
            statement.executeUpdate(sql);

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void removeSchoolDay(String id) {
        try {
            Connection conn = (Connection) getConnection();
            Savepoint savepoint = conn.setSavepoint();
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM schoolday WHERE id = '" + id + "'";
            statement.executeUpdate(sql);

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public Attendance queryAttendance(String studentId, String lessonId) {
        Attendance a = null;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM attendance WHERE lessonId = ? AND studentId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, lessonId);
            ps.setString(2, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                a = new Attendance();
                a.setStudentId(rs.getString("studentId"));
                a.setLessonId(rs.getInt("lessonId"));
                a.setParticipated(rs.getInt("participated"));
                a.setLate(rs.getInt("late"));
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return a;
    }

    public ArrayList<Attendance> queryAttendanceByLesson(String lessonId) {
        ArrayList<Attendance> attendances = new ArrayList<Attendance>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql = "SELECT * FROM attendance, student WHERE lessonId = '" + lessonId + "' AND attendance.studentId = student.studentId ORDER BY student.studentId";
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                Attendance a = new Attendance();
                a.setStudentId(rs.getString("studentId"));
                a.setLessonId(rs.getInt("lessonId"));
                a.setParticipated(rs.getInt("participated"));
                a.setLate(rs.getInt("late"));

                Student s = new Student();
                s.setStudentId(rs.getString("studentId"));
                s.setClassId(rs.getString("classId"));
                s.setName(rs.getString("name"));
                a.setStudent(s);

                attendances.add(a);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return attendances;
    }

    public ArrayList<String> queryClasses() {
        ArrayList<String> classes = new ArrayList<String>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT DISTINCT classId FROM student");
            while (rs.next()) {
                classes.add(rs.getString("classId"));
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return classes;
    }

    public ArrayList<Teacher> queryTeacher() {
        ArrayList<Teacher> teachers = new ArrayList<Teacher>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM teacher");
            while (rs.next()) {
                Teacher t = new Teacher();
                t.setUsername(rs.getString("username"));
                t.setName(rs.getString("name"));
                teachers.add(t);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return teachers;
    }

    public boolean addModule(String moduleCode, String moduleTitle, String contactHours) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("INSERT INTO module (moduleCode, moduleTitle, contactHours) VALUES (?, ?, ?)");
            pStatement.setString(1, moduleCode);
            pStatement.setString(2, moduleTitle);
            pStatement.setString(3, contactHours);
            int affectedRow = pStatement.executeUpdate();
            if (affectedRow > 0) {
                isSuccess = true;
            } else {
                conn.rollback();
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public boolean addStudent(String studentId, String password, String name, String classId) {
        boolean isSuccess = false;

        Connection conn;
        PreparedStatement pStatement;
        try {
            conn = (Connection) getConnection();
            Savepoint savepoint = conn.setSavepoint();
            pStatement = conn.prepareStatement("INSERT INTO user (username, password, nickName, role) VALUES (?, ?, ?, ?)");
            pStatement.setString(1, studentId);
            pStatement.setString(2, password);
            pStatement.setString(3, name);
            pStatement.setString(4, "student");

            int affectedRow = pStatement.executeUpdate();
            if (affectedRow > 0) {
                pStatement = conn.prepareStatement("INSERT INTO student (studentId, name, classId) VALUES (?, ?, ?)");
                pStatement.setString(1, studentId);
                pStatement.setString(2, name);
                pStatement.setString(3, classId);

                affectedRow = pStatement.executeUpdate();
                if (affectedRow > 0) {
                    isSuccess = true;
                }
            }

            if (!isSuccess) {
                conn.rollback(savepoint);
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public ArrayList<Student> queryStudent() {
        ArrayList<Student> students = new ArrayList<Student>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM student ORDER BY studentId");
            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getString("studentId"));
                s.setName(rs.getString("name"));
                s.setClassId(rs.getString("classId"));
                students.add(s);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return students;
    }

    public ArrayList<Student> queryStudentByClass(String classId) {
        ArrayList<Student> students = new ArrayList<Student>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM student WHERE classId='" + classId + "' ORDER BY studentId");
            while (rs.next()) {
                Student s = new Student();
                s.setStudentId(rs.getString("studentId"));
                s.setName(rs.getString("name"));
                s.setClassId(rs.getString("classId"));
                students.add(s);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return students;
    }

    public Student queryStudentById(String studentId) {
        Student s = null;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM student WHERE studentId='" + studentId + "'");
            if (rs.next()) {
                s = new Student();
                s.setStudentId(rs.getString("studentId"));
                s.setName(rs.getString("name"));
                s.setClassId(rs.getString("classId"));
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return s;
    }

    public boolean setStudent(Student s) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Savepoint savepoint = conn.setSavepoint();
            Statement statement = conn.createStatement();
            String sql;
            sql = "UPDATE student SET name = '" + s.getName() + "', classId='" + s.getClassId() + "' WHERE studentId = '" + s.getStudentId() + "'";
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                sql = "UPDATE user SET nickName = '" + s.getName() + "' WHERE username = '" + s.getStudentId() + "'";
                affectedRow = statement.executeUpdate(sql);
                if (affectedRow > 0) {
                    isSuccess = true;
                }
            }

            if (!isSuccess) {
                conn.rollback(savepoint);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public boolean setPassword(String username, String password) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql;
            sql = "UPDATE user SET password = '" + password + "' WHERE username = '" + username + "'";
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                isSuccess = true;
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public boolean removeStudent(String studentId) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Savepoint savepoint = conn.setSavepoint();

            Statement statement = conn.createStatement();
            String sql = "DELETE FROM student WHERE studentId = '" + studentId + "'";
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                sql = "DELETE FROM user WHERE username = '" + studentId + "'";
                affectedRow = statement.executeUpdate(sql);
                if (affectedRow > 0) {
                    isSuccess = true;
                }
            }

            if (!isSuccess) {
                conn.rollback(savepoint);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public void removeLesson(String lessonId) {
        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM attendance WHERE lessonId = '" + lessonId + "'";
            statement.executeUpdate(sql);

            sql = "DELETE FROM lesson WHERE lessonId = '" + lessonId + "'";
            statement.executeUpdate(sql);

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean addTeacher(String username, String password, String name) {
        boolean isSuccess = false;

        Connection conn;
        PreparedStatement pStatement;
        try {
            conn = (Connection) getConnection();
            Savepoint savepoint = conn.setSavepoint();
            pStatement = conn.prepareStatement("INSERT INTO user (username, password, nickName, role) VALUES (?, ?, ?, ?)");
            pStatement.setString(1, username);
            pStatement.setString(2, password);
            pStatement.setString(3, name);
            pStatement.setString(4, "teacher");

            int affectedRow = pStatement.executeUpdate();
            if (affectedRow > 0) {
                pStatement = conn.prepareStatement("INSERT INTO teacher (username, name) VALUES (?, ?)");
                pStatement.setString(1, username);
                pStatement.setString(2, name);

                affectedRow = pStatement.executeUpdate();
                if (affectedRow > 0) {
                    isSuccess = true;
                }
            }

            if (!isSuccess) {
                conn.rollback(savepoint);
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public Teacher queryTeacher(String username) {
        Teacher t = null;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM teacher WHERE username='" + username + "'");
            if (rs.next()) {
                t = new Teacher();
                t.setUsername(rs.getString("username"));
                t.setName(rs.getString("name"));
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return t;
    }

    public boolean setTeacher(Teacher t) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Savepoint savepoint = conn.setSavepoint();
            Statement statement = conn.createStatement();
            String sql;
            sql = "UPDATE teacher SET name = '" + t.getName() + "' WHERE username = '" + t.getUsername() + "'";
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                sql = "UPDATE user SET nickName = '" + t.getName() + "' WHERE username = '" + t.getUsername() + "'";
                affectedRow = statement.executeUpdate(sql);
                if (affectedRow > 0) {
                    isSuccess = true;
                }
            }

            if (!isSuccess) {
                conn.rollback(savepoint);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public boolean removeTeacher(String username) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql = "DELETE FROM teacher WHERE username = '" + username + "'";
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                sql = "DELETE FROM user WHERE username = '" + username + "'";
                affectedRow = statement.executeUpdate(sql);
                if (affectedRow > 0) {
                    isSuccess = true;
                }
            }

            if (!isSuccess) {
                conn.rollback();
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public ArrayList<User> queryUser() {
        ArrayList<User> users = new ArrayList<User>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM user");
            while (rs.next()) {
                User u = new User();
                u.setUsername(rs.getString("username"));
                u.setRole(rs.getString("role"));
                users.add(u);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return users;
    }

    public User userVerify(String username, String password) {
        User user = null;

        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("SELECT * FROM user WHERE username = ? AND password = ?");
            pStatement.setString(1, username);
            pStatement.setString(2, password);

            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setNickName(rs.getString("nickName"));
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return user;
    }

    public boolean setUser(User u) {
        boolean isSuccess = false;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            String sql;
            if (u.getPassword() == null) {
                sql = "UPDATE user SET nickName = '" + u.getNickName() + "' WHERE username = '" + u.getUsername() + "'";
            } else {
                sql = "UPDATE user SET nickName = '" + u.getNickName() + "', password='" + u.getPassword() + "' WHERE username = '" + u.getUsername() + "'";
            }
            int affectedRow = statement.executeUpdate(sql);
            if (affectedRow > 0) {
                isSuccess = true;
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return isSuccess;
    }

    public Lesson queryLesson(String lessonId) {
        Lesson lesson = null;

        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("SELECT * FROM lesson WHERE lessonId = ?");
            pStatement.setString(1, lessonId);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                lesson = new Lesson();
                lesson.setLessonId(rs.getInt("lessonId"));
                lesson.setCourseId(rs.getInt("courseId"));
                lesson.setDate(rs.getString("date"));
                lesson.setStartTime(rs.getString("startTime"));
                lesson.setDuration(rs.getDouble("duration"));
                lesson.setEndTime(rs.getString("endTime"));
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lesson;
    }

    public ArrayList<Lesson> queryLessonByCourseId(int courseId) {
        ArrayList<Lesson> lessons = new ArrayList<Lesson>();

        try {
            Connection conn = (Connection) getConnection();
            PreparedStatement pStatement = conn.prepareStatement("SELECT * FROM lesson WHERE courseId = ?");
            pStatement.setInt(1, courseId);
            ResultSet rs = pStatement.executeQuery();
            while (rs.next()) {
                Lesson l = new Lesson();
                l.setLessonId(rs.getInt("lessonId"));
                l.setCourseId(rs.getInt("courseId"));
                l.setDate(rs.getString("date"));
                l.setStartTime(rs.getString("startTime"));
                l.setDuration(rs.getDouble("duration"));
                l.setEndTime(rs.getString("endTime"));
                lessons.add(l);
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lessons;
    }

    public ArrayList<Event> querySchoolDay() {
        ArrayList<Event> days = new ArrayList<Event>();

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM schoolday");
            while (rs.next()) {
                Event e = new Event();
                e.setId(rs.getInt("id"));
                e.setTitle(rs.getString("title"));
                e.setStartDate(rs.getString("startDate"));
                e.setEndDate(rs.getString("endDate"));
                days.add(e);
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return days;
    }

    public Event querySchoolDay(String id) {
        Event e = null;

        try {
            Connection conn = (Connection) getConnection();
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM schoolday WHERE id = '" + id + "'");
            if (rs.next()) {
                e = new Event();
                e.setId(rs.getInt("id"));
                e.setTitle(rs.getString("title"));
                e.setStartDate(rs.getString("startDate"));
                e.setEndDate(rs.getString("endDate"));
            }

            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return e;
    }

    public ArrayList<Event> queryLessonEventByTeacher(String username) {
        ArrayList<Event> events = new ArrayList<Event>();

        try {
            Connection conn = (Connection) getConnection();
            String url = "SELECT moduleCode, date, startTime, endTime FROM lesson, course WHERE lesson.courseId = course.courseId AND course.teacher = '" + username + "'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(url);
            while (rs.next()) {
                String moduleCode = rs.getString(1);
                String date = rs.getString(2);
                String startTime = rs.getString(3);
                String endTime = rs.getString(4);

                Event e = new Event();
                e.setTitle(moduleCode);
                e.setStartDate(date + "T" + startTime);
                e.setEndDate(date + "T" + endTime);
                events.add(e);
            }
            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return events;
    }

    public ArrayList<Event> queryLessonEventByClass(String classId) {
        ArrayList<Event> events = new ArrayList<Event>();

        try {
            Connection conn = (Connection) getConnection();
            String url = "SELECT moduleCode, date, startTime, endTime FROM lesson, course WHERE lesson.courseId = course.courseId AND course.classId = '" + classId + "'";
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(url);
            while (rs.next()) {
                String moduleCode = rs.getString(1);
                String date = rs.getString(2);
                String startTime = rs.getString(3);
                String endTime = rs.getString(4);

                Event e = new Event();
                e.setTitle(moduleCode);
                e.setStartDate(date + "T" + startTime);
                e.setEndDate(date + "T" + endTime);
                events.add(e);
            }
            statement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return events;
    }

    public double[] getStudentAttendance(String studentId, String courseId) {

        // studentAttendance[0] = attendance, studentAttendance[1] = absence
        double[] studentAttendance = new double[2];

        double contactHours = 0.1;
        int participatedMin = 0;
        int lateMin = 0;

        try {
            Connection conn = (Connection) getConnection();
            String sql = "SELECT SUM(participated), SUM(late) FROM attendance,lesson WHERE attendance.lessonId=lesson.lessonId AND courseId = ? AND studentId = ?";
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, courseId);
            pStatement.setString(2, studentId);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                participatedMin = rs.getInt(1);
                lateMin = rs.getInt(2);
            }

            sql = "SELECT contactHours FROM module, course WHERE course.moduleCode = module.moduleCode AND courseId = ?";
            pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, courseId);
            rs = pStatement.executeQuery();
            if (rs.next()) {
                contactHours = rs.getDouble(1);
            }

            // calculate attendance rate
            studentAttendance[0] = participatedMin / (contactHours * 60);
            studentAttendance[1] = lateMin / (contactHours * 60);

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return studentAttendance;
    }

    public int getCompletedHours(String courseId) {
        int completed = 0;

        try {
            Connection conn = (Connection) getConnection();
            String sql = "SELECT SUM(duration) FROM `lesson`,`course` WHERE lesson.courseId = course.courseId AND lesson.date < CURRENT_DATE AND course.courseId = ?";
            PreparedStatement pStatement = conn.prepareStatement(sql);
            pStatement.setString(1, courseId);
            ResultSet rs = pStatement.executeQuery();
            if (rs.next()) {
                completed = rs.getInt(1);
            }

            pStatement.close();
            conn.close();

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return completed;
    }

}
