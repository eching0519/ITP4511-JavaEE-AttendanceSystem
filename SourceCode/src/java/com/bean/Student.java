/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bean;

import java.io.Serializable;

/**
 *
 * @author EChing
 */
public class Student implements Serializable {
    private String studentId;
    private String name;
    private String classId;
    
    private double participatedRate;
    private double abscentRate;

    public Student() {
    }

    public Student(String studentId, String name, String classId) {
        this.studentId = studentId;
        this.name = name;
        this.classId = classId;
    }


    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public double getParticipatedRate() {
        return participatedRate;
    }

    public void setParticipatedRate(double participatedRate) {
        this.participatedRate = participatedRate;
    }

    public double getAbscentRate() {
        return abscentRate;
    }

    public void setAbscentRate(double abscentRate) {
        this.abscentRate = abscentRate;
    }
}
