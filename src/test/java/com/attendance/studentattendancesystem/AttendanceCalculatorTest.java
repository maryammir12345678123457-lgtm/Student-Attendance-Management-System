package com.attendance.studentattendancesystem;

import com.attendance.studentattendancesystem.util.AttendanceCalculator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AttendanceCalculatorTest {

    @Test
    void presentAndLateCountAsAttended() {
        assertTrue(AttendanceCalculator.countsAsPresent("Present"));
        assertTrue(AttendanceCalculator.countsAsPresent("Late"));
        assertFalse(AttendanceCalculator.countsAsPresent("Absent"));
        assertFalse(AttendanceCalculator.countsAsPresent(null));
    }

    @Test
    void percentageHandlesEmptyAndNormalCases() {
        assertEquals(0.0, AttendanceCalculator.percentage(0, 0), 0.0001);
        assertEquals(50.0, AttendanceCalculator.percentage(1, 2), 0.0001);
        assertEquals(100.0, AttendanceCalculator.percentage(3, 3), 0.0001);
    }

    @Test
    void percentageFromStatusesIgnoresBlanks() {
        ArrayList<String> statuses = new ArrayList<>();
        statuses.add("Present");
        statuses.add("Absent");
        statuses.add("Late");
        statuses.add("");
        statuses.add(null);
        double value = AttendanceCalculator.percentageFromStatuses(statuses);
        assertEquals(66.666, value, 0.01);
    }

    @Test
    void periodValidationAllowsOneToFour() {
        assertTrue(AttendanceCalculator.isValidPeriod(1));
        assertTrue(AttendanceCalculator.isValidPeriod(4));
        assertFalse(AttendanceCalculator.isValidPeriod(0));
        assertFalse(AttendanceCalculator.isValidPeriod(5));
    }

    @Test
    void formatPercentageUsesOneDecimal() {
        assertEquals("87.5%", AttendanceCalculator.formatPercentage(87.5));
    }

    @Test
    void toggleCyclesBlankPresentAbsent() {
        assertEquals("Present", AttendanceCalculator.nextToggleStatus(null));
        assertEquals("Absent", AttendanceCalculator.nextToggleStatus("Present"));
        assertNull(AttendanceCalculator.nextToggleStatus("Absent"));
        assertEquals("Absent", AttendanceCalculator.nextToggleStatus("Late"));
    }

    @Test
    void statusSymbolsMatchGridIcons() {
        assertEquals("\u2713", AttendanceCalculator.statusSymbol("Present"));
        assertEquals("\u2717", AttendanceCalculator.statusSymbol("Absent"));
        assertEquals("", AttendanceCalculator.statusSymbol(null));
    }
}
