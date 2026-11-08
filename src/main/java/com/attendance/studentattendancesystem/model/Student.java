package com.attendance.studentattendancesystem.model;

public class Student {
    private int id;
    private String studentId;
    private String name;

    public Student() {
    }

    public Student(int id, String studentId, String name) {
        this.id = id;
        this.studentId = studentId;
        this.name = name;
    }

    public Student(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public String toString() {
        return studentId + " - " + name;
    }
}
