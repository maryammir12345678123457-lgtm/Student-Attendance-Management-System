package com.attendance.studentattendancesystem.dao;

import com.attendance.studentattendancesystem.DatabaseManager;
import com.attendance.studentattendancesystem.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    public void addCourse(Course course) throws SQLException {
        String checkSql = """
                SELECT COUNT(*)
                FROM courses
                WHERE LOWER(TRIM(course_code)) = LOWER(TRIM(?))
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setString(1, course.getCourseCode());
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new SQLException("Course code already exists: " + course.getCourseCode());
                }
            }
        }

        String sql = """
                INSERT INTO courses (course_name, course_code, teacher_name, total_classes)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getCourseCode().trim());
            statement.setString(3, course.getTeacherName());
            statement.setInt(4, course.getTotalClasses());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    course.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM courses ORDER BY course_name";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                courses.add(new Course(
                        resultSet.getInt("id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("course_code"),
                        resultSet.getString("teacher_name"),
                        resultSet.getInt("total_classes")));
            }
        }
        return courses;
    }

    public void deleteCourse(int courseId) throws SQLException {
        String deleteAttendanceSql = """
                DELETE FROM attendance
                WHERE class_id IN (SELECT id FROM classes WHERE course_id = ?)
                """;
        String deleteClassesSql = "DELETE FROM classes WHERE course_id = ?";
        String deleteCourseSql = "DELETE FROM courses WHERE id = ?";

        try (Connection connection = DatabaseManager.connect()) {
            connection.setAutoCommit(false);
            try {
                try (PreparedStatement statement = connection.prepareStatement(deleteAttendanceSql)) {
                    statement.setInt(1, courseId);
                    statement.executeUpdate();
                }
                try (PreparedStatement statement = connection.prepareStatement(deleteClassesSql)) {
                    statement.setInt(1, courseId);
                    statement.executeUpdate();
                }
                try (PreparedStatement statement = connection.prepareStatement(deleteCourseSql)) {
                    statement.setInt(1, courseId);
                    int deleted = statement.executeUpdate();
                    if (deleted == 0) {
                        throw new SQLException("Course not found.");
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public int getCourseCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM courses";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public void updateCourse(int id, String courseName, String courseCode, String teacherName, int totalClasses)
            throws SQLException {
        String checkSql = """
                SELECT COUNT(*)
                FROM courses
                WHERE LOWER(TRIM(course_code)) = LOWER(TRIM(?))
                AND id != ?
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setString(1, courseCode);
            checkStatement.setInt(2, id);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new SQLException("Another course already uses this course code.");
                }
            }
        }

        String sql = """
                UPDATE courses
                SET course_name = ?, course_code = ?, teacher_name = ?, total_classes = ?
                WHERE id = ?
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, courseName);
            statement.setString(2, courseCode.trim());
            statement.setString(3, teacherName);
            statement.setInt(4, totalClasses);
            statement.setInt(5, id);
            statement.executeUpdate();
        }
    }
}
