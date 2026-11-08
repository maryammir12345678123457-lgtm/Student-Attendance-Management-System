package com.attendance.studentattendancesystem.model;

/** One week/period cell in the attendance grid. */
public class PeriodCell {
    private Integer attendanceId;
    private Integer classSessionId;
    private String status;

    public PeriodCell() {
    }

    public PeriodCell(Integer attendanceId, Integer classSessionId, String status) {
        this.attendanceId = attendanceId;
        this.classSessionId = classSessionId;
        this.status = status;
    }

    public Integer getAttendanceId() {
        return attendanceId;
    }

    public void setAttendanceId(Integer attendanceId) {
        this.attendanceId = attendanceId;
    }

    public Integer getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(Integer classSessionId) {
        this.classSessionId = classSessionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String displayStatus() {
        return status == null ? "" : status;
    }
}
