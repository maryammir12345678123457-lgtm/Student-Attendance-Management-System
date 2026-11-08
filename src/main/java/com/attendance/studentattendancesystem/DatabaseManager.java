package com.attendance.studentattendancesystem;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static final Path DEFAULT_APP_DATA_DIR =
            Paths.get(System.getProperty("user.home"), "StudentAttendanceSystem");
    private static final Path DEFAULT_DATABASE_PATH = DEFAULT_APP_DATA_DIR.resolve("attendance.db");

    private static volatile String overrideUrl;

    public static void setDatabaseUrl(String jdbcUrl) {
        overrideUrl = jdbcUrl;
    }

    public static void clearDatabaseUrlOverride() {
        overrideUrl = null;
    }

    public static Path getDefaultDatabasePath() {
        String custom = System.getProperty("attendance.db.path");
        if (custom != null && !custom.isBlank()) {
            return Paths.get(custom);
        }
        return DEFAULT_DATABASE_PATH;
    }

    public static Connection connect() throws SQLException {
        String url = overrideUrl;
        if (url == null) {
            Path databasePath = getDefaultDatabasePath();
            try {
                Path parent = databasePath.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
            } catch (Exception e) {
                throw new SQLException("Unable to create application data folder.", e);
            }
            url = "jdbc:sqlite:" + databasePath;
        }

        Connection connection = DriverManager.getConnection(url);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }
}
