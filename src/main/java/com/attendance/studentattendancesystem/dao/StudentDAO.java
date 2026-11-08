package com.attendance.studentattendancesystem.dao;

import com.attendance.studentattendancesystem.DatabaseManager;
import com.attendance.studentattendancesystem.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    public void addStudent(Student student) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM students WHERE student_id = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setString(1, student.getStudentId());
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new SQLException("A student with this ID already exists.");
                }
            }
        }

        String sql = "INSERT INTO students (student_id, name) VALUES (?, ?)";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getStudentId());
            statement.setString(2, student.getName());
            statement.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students ORDER BY name";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                students.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("student_id"),
                        resultSet.getString("name")));
            }
        }
        return students;
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public void updateStudent(int id, String studentId, String name) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM students WHERE student_id = ? AND id != ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement checkStatement = connection.prepareStatement(checkSql)) {
            checkStatement.setString(1, studentId);
            checkStatement.setInt(2, id);
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    throw new SQLException("Another student already uses this ID.");
                }
            }
        }

        String sql = "UPDATE students SET student_id = ?, name = ? WHERE id = ?";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentId);
            statement.setString(2, name);
            statement.setInt(3, id);
            statement.executeUpdate();
        }
    }

    public List<Student> searchStudents(String keyword) throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students WHERE student_id LIKE ? OR name LIKE ? ORDER BY name";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String searchValue = "%" + keyword + "%";
            statement.setString(1, searchValue);
            statement.setString(2, searchValue);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("student_id"),
                            resultSet.getString("name")));
                }
            }
        }
        return students;
    }

    public int getStudentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection connection = DatabaseManager.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        return 0;
    }
}
