package com.attendance.studentattendancesystem.util;

import java.util.Collection;

/**
 * Pure attendance math used by the UI and unit tests.
 * Present and Late count as attended.
 */
public final class AttendanceCalculator {
    public static final int PERIODS_PER_CLASS = 4;

    private AttendanceCalculator() {
    }

    public static boolean countsAsPresent(String status) {
        if (status == null) {
            return false;
        }
        return "Present".equalsIgnoreCase(status) || "Late".equalsIgnoreCase(status);
    }

    public static double percentage(int presentCount, int totalCount) {
        if (totalCount <= 0) {
            return 0.0;
        }
        return presentCount * 100.0 / totalCount;
    }

    public static double percentageFromStatuses(Collection<String> statuses) {
        int total = 0;
        int present = 0;
        for (String status : statuses) {
            if (status == null || status.isBlank()) {
                continue;
            }
            total++;
            if (countsAsPresent(status)) {
                present++;
            }
        }
        return percentage(present, total);
    }

    public static String formatPercentage(double value) {
        return String.format("%.1f%%", value);
    }

    public static boolean isValidPeriod(int period) {
        return period >= 1 && period <= PERIODS_PER_CLASS;
    }

    /** Click cycle: blank → Present → Absent → blank */
    public static String nextToggleStatus(String current) {
        if (current == null || current.isBlank()) {
            return "Present";
        }
        if ("Present".equalsIgnoreCase(current) || "Late".equalsIgnoreCase(current)) {
            return "Absent";
        }
        if ("Absent".equalsIgnoreCase(current)) {
            return null;
        }
        return "Present";
    }

    /** Compact symbols for grid cells (like the reference screenshot). */
    public static String statusSymbol(String status) {
        if (status == null || status.isBlank()) {
            return "";
        }
        if ("Present".equalsIgnoreCase(status) || "Late".equalsIgnoreCase(status)) {
            return "\u2713";
        }
        if ("Absent".equalsIgnoreCase(status)) {
            return "\u2717";
        }
        return status.substring(0, 1).toUpperCase();
    }
}
