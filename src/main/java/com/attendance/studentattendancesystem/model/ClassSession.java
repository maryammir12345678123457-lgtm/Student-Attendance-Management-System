package com.attendance.studentattendancesystem.model;

public class ClassSession {
    private int id;
    private int courseId;
    private int weekNumber;
    private int classNumber;
    private String classDate;

    public ClassSession() {
    }

    public ClassSession(int id, int courseId, int weekNumber, int classNumber, String classDate) {
        this.id = id;
        this.courseId = courseId;
        this.weekNumber = weekNumber;
        this.classNumber = classNumber;
        this.classDate = classDate;
    }

    public ClassSession(int courseId, int weekNumber, int classNumber, String classDate) {
        this.courseId = courseId;
        this.weekNumber = weekNumber;
        this.classNumber = classNumber;
        this.classDate = classDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    @Override
    public String toString() {
        return "Week " + weekNumber + " - Period " + classNumber + " (" + classDate + ")";
    }
}
