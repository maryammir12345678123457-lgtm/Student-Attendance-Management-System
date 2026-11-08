package com.attendance.studentattendancesystem.model;

/** Flat attendance row used for lookups and legacy helpers. */
public class AttendanceRecord {
    private int attendanceId;
    private String studentId;
    private String studentName;
    private String courseName;
    private String courseCode;
    private int weekNumber;
    private int classNumber;
    private String classDate;
    private String status;
    private double attendancePercentage;

    public AttendanceRecord(int attendanceId, String studentId, String studentName, String courseName,
                            String courseCode, int weekNumber, int classNumber, String classDate, String status) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.weekNumber = weekNumber;
        this.classNumber = classNumber;
        this.classDate = classDate;
        this.status = status;
    }

    public int getAttendanceId() {
        return attendanceId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public String getClassDate() {
        return classDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAttendancePercentage() {
        return attendancePercentage;
    }

    public void setAttendancePercentage(double attendancePercentage) {
        this.attendancePercentage = attendancePercentage;
    }
}
