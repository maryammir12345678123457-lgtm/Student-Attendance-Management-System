package com.attendance.studentattendancesystem.model;

import com.attendance.studentattendancesystem.util.AttendanceCalculator;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * One student row for a course attendance grid:
 * student | W1(P1-P4) | W2(P1-P4) | ... | percentage
 */
public class StudentAttendanceRow {
    private final int dbId;
    private final String studentId;
    private final String studentName;
    private final Map<Integer, PeriodCell[]> periodsByWeek = new LinkedHashMap<>();
    private double overallPercentage;

    public StudentAttendanceRow(int dbId, String studentId, String studentName) {
        this.dbId = dbId;
        this.studentId = studentId;
        this.studentName = studentName;
    }

    public int getDbId() {
        return dbId;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentLabel() {
        return studentId + " (" + getOverallPercentageText() + ")\n" + studentName;
    }

    public void ensureWeek(int weekNumber) {
        periodsByWeek.computeIfAbsent(weekNumber, week -> {
            PeriodCell[] cells = new PeriodCell[AttendanceCalculator.PERIODS_PER_CLASS];
            for (int i = 0; i < cells.length; i++) {
                cells[i] = new PeriodCell();
            }
            return cells;
        });
    }

    public void setPeriod(int weekNumber, int period, Integer attendanceId, Integer classSessionId, String status) {
        if (!AttendanceCalculator.isValidPeriod(period)) {
            return;
        }
        ensureWeek(weekNumber);
        PeriodCell cell = periodsByWeek.get(weekNumber)[period - 1];
        cell.setAttendanceId(attendanceId);
        cell.setClassSessionId(classSessionId);
        cell.setStatus(status);
    }

    public PeriodCell getPeriodCell(int weekNumber, int period) {
        PeriodCell[] cells = periodsByWeek.get(weekNumber);
        if (cells == null || !AttendanceCalculator.isValidPeriod(period)) {
            return new PeriodCell();
        }
        return cells[period - 1];
    }

    public String getPeriodStatus(int weekNumber, int period) {
        return getPeriodCell(weekNumber, period).displayStatus();
    }

    /**
     * Percentage over all visible week/period slots (empty counts as not present),
     * matching the screenshot-style grid rate.
     */
    public void recalculatePercentage(int weekCount) {
        int totalSlots = Math.max(0, weekCount) * AttendanceCalculator.PERIODS_PER_CLASS;
        int present = 0;
        for (PeriodCell[] cells : periodsByWeek.values()) {
            for (PeriodCell cell : cells) {
                if (AttendanceCalculator.countsAsPresent(cell.getStatus())) {
                    present++;
                }
            }
        }
        overallPercentage = AttendanceCalculator.percentage(present, totalSlots);
    }

    public double getOverallPercentage() {
        return overallPercentage;
    }

    public String getOverallPercentageText() {
        return AttendanceCalculator.formatPercentage(overallPercentage);
    }

    public boolean hasStatus(String status) {
        if (status == null) {
            return false;
        }
        for (PeriodCell[] cells : periodsByWeek.values()) {
            for (PeriodCell cell : cells) {
                if (status.equalsIgnoreCase(cell.getStatus())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Integer> getWeekNumbers() {
        return new ArrayList<>(periodsByWeek.keySet());
    }

    public Map<Integer, PeriodCell[]> getPeriodsByWeek() {
        return periodsByWeek;
    }
}
