package com.attendance.studentattendancesystem;

import com.attendance.studentattendancesystem.dao.AttendanceDAO;
import com.attendance.studentattendancesystem.dao.ClassSessionDAO;
import com.attendance.studentattendancesystem.dao.CourseDAO;
import com.attendance.studentattendancesystem.dao.StudentDAO;
import com.attendance.studentattendancesystem.model.Attendance;
import com.attendance.studentattendancesystem.model.ClassSession;
import com.attendance.studentattendancesystem.model.Course;
import com.attendance.studentattendancesystem.model.Student;
import com.attendance.studentattendancesystem.model.StudentAttendanceRow;
import com.attendance.studentattendancesystem.util.AttendanceCalculator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DaoIntegrationTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        Path db = tempDir.resolve("test-attendance.db");
        DatabaseManager.setDatabaseUrl("jdbc:sqlite:" + db);
        DatabaseInitializer.initialize();
    }

    @AfterEach
    void tearDown() {
        DatabaseManager.clearDatabaseUrlOverride();
    }

    @Test
    void clickableGridFlowSavesAndTogglesAttendance() throws Exception {
        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        ClassSessionDAO classDAO = new ClassSessionDAO();
        AttendanceDAO attendanceDAO = new AttendanceDAO();

        studentDAO.addStudent(new Student("S001", "Ayesha"));
        Course math = new Course("Mathematics", "MATH101", "Mr. Khan", 16);
        courseDAO.addCourse(math);

        ClassSession p1 = classDAO.getOrCreateSession(math.getId(), 1, 1, "2026-01-01");
        ClassSession p2 = classDAO.getOrCreateSession(math.getId(), 1, 2, "2026-01-01");
        assertNotNull(p1);
        assertNotNull(p2);
        assertEquals(List.of(1), classDAO.getWeekNumbers(math.getId()));

        Student ayesha = studentDAO.getAllStudents().get(0);
        String first = AttendanceCalculator.nextToggleStatus(null);
        attendanceDAO.saveAttendance(new Attendance(ayesha.getId(), p1.getId(), first));
        String second = AttendanceCalculator.nextToggleStatus(first);
        attendanceDAO.saveAttendance(new Attendance(ayesha.getId(), p1.getId(), second));
        assertEquals("Absent", second);

        attendanceDAO.saveAttendance(new Attendance(ayesha.getId(), p2.getId(), "Present"));

        List<StudentAttendanceRow> rows = attendanceDAO.getCourseAttendanceGrid(
                math.getId(), studentDAO.getAllStudents(), List.of(1));
        StudentAttendanceRow row = rows.get(0);
        assertEquals("Absent", row.getPeriodStatus(1, 1));
        assertEquals("Present", row.getPeriodStatus(1, 2));
        assertEquals(25.0, row.getOverallPercentage(), 0.0001);

        attendanceDAO.clearAttendance(ayesha.getId(), p1.getId());
        rows = attendanceDAO.getCourseAttendanceGrid(
                math.getId(), studentDAO.getAllStudents(), List.of(1));
        assertEquals("", rows.get(0).getPeriodStatus(1, 1));
    }

    @Test
    void addWeekCreatesFourPeriods() throws Exception {
        CourseDAO courseDAO = new CourseDAO();
        ClassSessionDAO classDAO = new ClassSessionDAO();
        Course course = new Course("Physics", "PHY101", "Dr. Ali", 8);
        courseDAO.addCourse(course);

        for (int period = 1; period <= 4; period++) {
            classDAO.getOrCreateSession(course.getId(), 1, period, "2026-01-01");
        }
        for (int period = 1; period <= 4; period++) {
            classDAO.getOrCreateSession(course.getId(), 2, period, "2026-01-08");
        }

        assertEquals(List.of(1, 2), classDAO.getWeekNumbers(course.getId()));
        assertEquals(8, classDAO.getClassCount());
        assertTrue(classDAO.classExists(course.getId(), 2, 4));
    }

    @Test
    void duplicateStudentIdIsRejected() throws Exception {
        StudentDAO studentDAO = new StudentDAO();
        studentDAO.addStudent(new Student("S100", "One"));
        assertThrows(Exception.class, () -> studentDAO.addStudent(new Student("S100", "Two")));
    }

    @Test
    void deleteCourseRemovesRelatedClassesAndAttendance() throws Exception {
        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        ClassSessionDAO classDAO = new ClassSessionDAO();
        AttendanceDAO attendanceDAO = new AttendanceDAO();

        studentDAO.addStudent(new Student("S777", "Omar"));
        Course course = new Course("Chemistry", "CHM101", "Ms. Noor", 4);
        courseDAO.addCourse(course);
        ClassSession session = classDAO.getOrCreateSession(course.getId(), 1, 1, "2026-02-01");
        Student student = studentDAO.getAllStudents().get(0);
        attendanceDAO.saveAttendance(new Attendance(student.getId(), session.getId(), "Present"));

        courseDAO.deleteCourse(course.getId());
        assertEquals(0, courseDAO.getCourseCount());
        assertEquals(0, classDAO.getClassCount());
        assertEquals(0, attendanceDAO.getAttendanceByStudent(student.getId()).size());
    }
}
