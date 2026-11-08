package com.attendance.studentattendancesystem.dao;

import com.attendance.studentattendancesystem.DatabaseManager;
import com.attendance.studentattendancesystem.model.Attendance;
import com.attendance.studentattendancesystem.model.AttendanceRecord;
import com.attendance.studentattendancesystem.model.PeriodCell;
import com.attendance.studentattendancesystem.model.Student;
import com.attendance.studentattendancesystem.model.StudentAttendanceRow;
import com.attendance.studentattendancesystem.util.AttendanceCalculator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttendanceDAO {
    public void saveAttendance(Attendance attendance) throws SQLException {
        /*
         * Update an existing record first. Older installations may have an
         * attendance table without the expected UNIQUE(student_id, class_id)
         * constraint, so this method does not rely on an ON CONFLICT target.
         */
        String updateSql = """
                UPDATE attendance
                SET status = ?
                WHERE student_id = ? AND class_id = ?
                """;

        try (Connection connection = DatabaseManager.connect()) {
            try (PreparedStatement update = connection.prepareStatement(updateSql)) {
                update.setString(1, attendance.getStatus());
                update.setInt(2, attendance.getStudentId());
                update.setInt(3, attendance.getClassId());

                int updatedRows = update.executeUpdate();
                if (updatedRows > 0) {
                    return;
                }
            }

            String insertSql = """
                    INSERT INTO attendance (student_id, class_id, status)
                    VALUES (?, ?, ?)
                    """;
            try (PreparedStatement insert = connection.prepareStatement(insertSql)) {
                insert.setInt(1, attendance.getStudentId());
                insert.setInt(2, attendance.getClassId());
                insert.setString(3, attendance.getStatus());
                insert.executeUpdate();
            }
        }
    }

    public List<Attendance> getAttendanceByClass(int classId) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE class_id = ? ORDER BY student_id";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, classId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    attendanceList.add(new Attendance(
                            resultSet.getInt("id"),
                            resultSet.getInt("student_id"),
                            resultSet.getInt("class_id"),
                            resultSet.getString("status")));
                }
            }
        }
        return attendanceList;
    }

    public List<Attendance> getAttendanceByStudent(int studentId) throws SQLException {
        List<Attendance> attendanceList = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE student_id = ? ORDER BY class_id";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    attendanceList.add(new Attendance(
                            resultSet.getInt("id"),
                            resultSet.getInt("student_id"),
                            resultSet.getInt("class_id"),
                            resultSet.getString("status")));
                }
            }
        }
        return attendanceList;
    }

    public void deleteAttendance(int id) throws SQLException {
        String sql = "DELETE FROM attendance WHERE id = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void updateAttendanceStatus(int attendanceId, String status) throws SQLException {
        String sql = "UPDATE attendance SET status = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, status);
            statement.setInt(2, attendanceId);
            statement.executeUpdate();
        }
    }

    public double getAttendancePercentage() throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) AS total,
                    SUM(CASE WHEN status IN ('Present', 'Late') THEN 1 ELSE 0 END) AS present
                FROM attendance
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return AttendanceCalculator.percentage(
                        resultSet.getInt("present"),
                        resultSet.getInt("total"));
            }
        }
        return 0.0;
    }

    public double getAttendancePercentageByStudent(int studentId) throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) AS total,
                    SUM(CASE WHEN status IN ('Present', 'Late') THEN 1 ELSE 0 END) AS present
                FROM attendance
                WHERE student_id = ?
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return AttendanceCalculator.percentage(
                            resultSet.getInt("present"),
                            resultSet.getInt("total"));
                }
            }
        }
        return 0.0;
    }

    public double getAttendancePercentageByStudentAndCourse(int studentId, int courseId) throws SQLException {
        String sql = """
                SELECT
                    COUNT(*) AS total,
                    SUM(CASE WHEN a.status IN ('Present', 'Late') THEN 1 ELSE 0 END) AS present
                FROM attendance a
                JOIN classes cl ON a.class_id = cl.id
                WHERE a.student_id = ? AND cl.course_id = ?
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            statement.setInt(2, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return AttendanceCalculator.percentage(
                            resultSet.getInt("present"),
                            resultSet.getInt("total"));
                }
            }
        }
        return 0.0;
    }

    public List<AttendanceRecord> getAllAttendanceRecords() throws SQLException {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = """
                SELECT
                    a.id AS attendance_id,
                    s.id AS student_db_id,
                    s.student_id,
                    s.name,
                    c.id AS course_db_id,
                    c.course_name,
                    c.course_code,
                    cl.week_number,
                    cl.class_number,
                    cl.class_date,
                    a.status
                FROM attendance a
                JOIN students s ON a.student_id = s.id
                JOIN classes cl ON a.class_id = cl.id
                JOIN courses c ON cl.course_id = c.id
                ORDER BY s.name, cl.week_number, cl.class_number
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                AttendanceRecord record = new AttendanceRecord(
                        resultSet.getInt("attendance_id"),
                        resultSet.getString("student_id"),
                        resultSet.getString("name"),
                        resultSet.getString("course_name"),
                        resultSet.getString("course_code"),
                        resultSet.getInt("week_number"),
                        resultSet.getInt("class_number"),
                        resultSet.getString("class_date"),
                        resultSet.getString("status"));
                double percentage = getAttendancePercentageByStudentAndCourse(
                        resultSet.getInt("student_db_id"),
                        resultSet.getInt("course_db_id"));
                record.setAttendancePercentage(percentage);
                records.add(record);
            }
        }
        return records;
    }

    /**
     * Course attendance grid: one row per student, week columns with P1-P4, % at end.
     */
    public List<StudentAttendanceRow> getCourseAttendanceGrid(int courseId,
                                                              List<Student> students,
                                                              List<Integer> weekNumbers)
            throws SQLException {
        Map<Integer, StudentAttendanceRow> rows = new LinkedHashMap<>();
        for (Student student : students) {
            StudentAttendanceRow row = new StudentAttendanceRow(
                    student.getId(), student.getStudentId(), student.getName());
            for (Integer week : weekNumbers) {
                row.ensureWeek(week);
            }
            rows.put(student.getId(), row);
        }

        String sql = """
                SELECT
                    a.id AS attendance_id,
                    a.student_id AS student_db_id,
                    cl.id AS class_id,
                    cl.class_number,
                    cl.week_number,
                    a.status
                FROM attendance a
                JOIN classes cl ON a.class_id = cl.id
                WHERE cl.course_id = ?
                  AND cl.class_number BETWEEN 1 AND 4
                ORDER BY cl.week_number ASC, cl.class_number ASC, a.id ASC
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    StudentAttendanceRow row = rows.get(resultSet.getInt("student_db_id"));
                    if (row == null) {
                        continue;
                    }
                    row.setPeriod(
                            resultSet.getInt("week_number"),
                            resultSet.getInt("class_number"),
                            resultSet.getInt("attendance_id"),
                            resultSet.getInt("class_id"),
                            resultSet.getString("status"));
                }
            }
        }

        // Also attach class session ids for empty cells so clicks can save quickly.
        String sessionSql = """
                SELECT id, week_number, class_number
                FROM classes
                WHERE course_id = ?
                  AND class_number BETWEEN 1 AND 4
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sessionSql)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int week = resultSet.getInt("week_number");
                    int period = resultSet.getInt("class_number");
                    int classId = resultSet.getInt("id");
                    for (StudentAttendanceRow row : rows.values()) {
                        PeriodCell cell = row.getPeriodCell(week, period);
                        if (cell.getClassSessionId() == null) {
                            cell.setClassSessionId(classId);
                        }
                    }
                }
            }
        }

        List<StudentAttendanceRow> result = new ArrayList<>(rows.values());
        for (StudentAttendanceRow row : result) {
            row.recalculatePercentage(weekNumbers.size());
        }
        return result;
    }

    public void clearAttendance(int studentDbId, int classSessionId) throws SQLException {
        String sql = "DELETE FROM attendance WHERE student_id = ? AND class_id = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentDbId);
            statement.setInt(2, classSessionId);
            statement.executeUpdate();
        }
    }
}
