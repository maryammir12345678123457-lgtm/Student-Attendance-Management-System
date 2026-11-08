package com.attendance.studentattendancesystem.dao;

import com.attendance.studentattendancesystem.DatabaseManager;
import com.attendance.studentattendancesystem.model.ClassSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClassSessionDAO {
    public void addClassSession(ClassSession session) throws SQLException {
        String sql = """
                INSERT INTO classes (course_id, week_number, class_number, class_date)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, session.getCourseId());
            statement.setInt(2, session.getWeekNumber());
            statement.setInt(3, session.getClassNumber());
            statement.setString(4, session.getClassDate());
            statement.executeUpdate();
        }
    }

    public List<ClassSession> getSessionsByCourse(int courseId) throws SQLException {
        List<ClassSession> sessions = new ArrayList<>();
        String sql = """
                SELECT *
                FROM classes
                WHERE course_id = ?
                ORDER BY week_number, class_number
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    sessions.add(new ClassSession(
                            resultSet.getInt("id"),
                            resultSet.getInt("course_id"),
                            resultSet.getInt("week_number"),
                            resultSet.getInt("class_number"),
                            resultSet.getString("class_date")));
                }
            }
        }
        return sessions;
    }

    public void deleteClassSession(int id) throws SQLException {
        String sql = "DELETE FROM classes WHERE id = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public int getClassCount() throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM classes cl
                INNER JOIN courses c ON cl.course_id = c.id
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }

    public boolean classExists(int courseId, int weekNumber, int classNumber) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM classes
                WHERE course_id = ? AND week_number = ? AND class_number = ?
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            statement.setInt(2, weekNumber);
            statement.setInt(3, classNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() && resultSet.getInt(1) > 0;
            }
        }
    }

    public List<Integer> getWeekNumbers(int courseId) throws SQLException {
        List<Integer> weeks = new ArrayList<>();
        String sql = """
                SELECT DISTINCT week_number
                FROM classes
                WHERE course_id = ?
                ORDER BY week_number
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    weeks.add(resultSet.getInt(1));
                }
            }
        }
        return weeks;
    }

    public ClassSession findSession(int courseId, int weekNumber, int classNumber) throws SQLException {
        String sql = """
                SELECT *
                FROM classes
                WHERE course_id = ? AND week_number = ? AND class_number = ?
                LIMIT 1
                """;
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            statement.setInt(2, weekNumber);
            statement.setInt(3, classNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new ClassSession(
                            resultSet.getInt("id"),
                            resultSet.getInt("course_id"),
                            resultSet.getInt("week_number"),
                            resultSet.getInt("class_number"),
                            resultSet.getString("class_date"));
                }
            }
        }
        return null;
    }

    /** Creates the week/period session if missing, then returns it. */
    public ClassSession getOrCreateSession(int courseId, int weekNumber, int classNumber, String classDate)
            throws SQLException {
        ClassSession existing = findSession(courseId, weekNumber, classNumber);
        if (existing != null) {
            return existing;
        }
        addClassSession(new ClassSession(courseId, weekNumber, classNumber, classDate));
        return findSession(courseId, weekNumber, classNumber);
    }
}
