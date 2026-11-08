package com.attendance.studentattendancesystem;

import com.attendance.studentattendancesystem.model.StudentAttendanceRow;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentAttendanceRowTest {

    @Test
    void buildsWeekPeriodsAndCalculatesPercentageOverAllSlots() {
        StudentAttendanceRow row = new StudentAttendanceRow(1, "S1", "Ali");
        row.setPeriod(1, 1, 101, 11, "Present");
        row.setPeriod(1, 2, 102, 12, "Absent");
        row.setPeriod(1, 3, 103, 13, "Late");
        row.setPeriod(1, 4, 104, 14, "Present");
        row.ensureWeek(2);
        row.recalculatePercentage(2);

        assertEquals("Present", row.getPeriodStatus(1, 1));
        assertEquals("Absent", row.getPeriodStatus(1, 2));
        // 3 attended out of 8 slots (2 weeks * 4 periods)
        assertEquals(37.5, row.getOverallPercentage(), 0.0001);
        assertEquals("37.5%", row.getOverallPercentageText());
        assertTrue(row.hasStatus("Late"));
        assertTrue(row.getStudentLabel().contains("Ali"));
    }

    @Test
    void ignoresInvalidPeriodNumbers() {
        StudentAttendanceRow row = new StudentAttendanceRow(1, "S1", "Ali");
        row.setPeriod(1, 9, 1, 1, "Present");
        row.recalculatePercentage(1);
        assertEquals(0.0, row.getOverallPercentage(), 0.0001);
        assertEquals("", row.getPeriodStatus(1, 9));
    }
}
