package com.attendance.studentattendancesystem;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        String studentsTable = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id TEXT NOT NULL UNIQUE,
                    name TEXT NOT NULL
                )
                """;
        String coursesTable = """
                CREATE TABLE IF NOT EXISTS courses (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    course_name TEXT NOT NULL,
                    course_code TEXT,
                    teacher_name TEXT,
                    total_classes INTEGER DEFAULT 0
                )
                """;
        String classesTable = """
                CREATE TABLE IF NOT EXISTS classes (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    course_id INTEGER NOT NULL,
                    week_number INTEGER NOT NULL,
                    class_number INTEGER NOT NULL,
                    class_date TEXT NOT NULL,
                    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
                )
                """;
        String attendanceTable = """
                CREATE TABLE IF NOT EXISTS attendance (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id INTEGER NOT NULL,
                    class_id INTEGER NOT NULL,
                    status TEXT NOT NULL,
                    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
                    FOREIGN KEY (class_id) REFERENCES classes(id) ON DELETE CASCADE,
                    UNIQUE(student_id, class_id)
                )
                """;

        try (Connection connection = DatabaseManager.connect();
             Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
            statement.execute(studentsTable);
            statement.execute(coursesTable);
            statement.execute(classesTable);
            statement.execute(attendanceTable);
            System.out.println("Database tables created successfully!");
        } catch (Exception e) {
            System.out.println("Failed to create database tables.");
            e.printStackTrace();
        }
    }
}
