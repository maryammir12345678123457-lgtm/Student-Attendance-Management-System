package com.attendance.studentattendancesystem.model;

public class Course {
    private int id;
    private String courseName;
    private String courseCode;
    private String teacherName;
    private int totalClasses;

    public Course() {
    }

    public Course(int id, String courseName, String courseCode, String teacherName, int totalClasses) {
        this.id = id;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.teacherName = teacherName;
        this.totalClasses = totalClasses;
    }

    public Course(String courseName, String courseCode, String teacherName, int totalClasses) {
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.teacherName = teacherName;
        this.totalClasses = totalClasses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public void setTotalClasses(int totalClasses) {
        this.totalClasses = totalClasses;
    }

    @Override
    public String toString() {
        return courseCode + " - " + courseName;
    }
}
