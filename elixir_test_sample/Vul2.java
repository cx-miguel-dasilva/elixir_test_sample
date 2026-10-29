package com.example.vulnerable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class VulnerableApp {

    // ==============================
    // VulnerABILITY #1:
    // Hardcoded Credentials (OWASP A02: Cryptographic Failures)
    // ==============================
    private static final String DB_URL = "jdbc:mysql://localhost:3306/appdb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "admin123"; // ❌ Hardcoded secret

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java VulnerableApp <username>");
            return;
        }

        String username = args[0];
        fetchUserData(username);
    }

    // ==============================
    // VulnerABILITY #2:
    // SQL Injection (OWASP A03: Injection)
    // ==============================
    public static void fetchUserData(String username) {
        Connection connection = null;
        Statement statement = null;

        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            statement = connection.createStatement();

            // ❌ User input is directly concatenated into SQL query
            String sql = "SELECT id, username, email FROM users WHERE username = '"
                       + username + "'";

            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                System.out.println(
                    "ID: " + rs.getInt("id") +
                    ", Username: " + rs.getString("username") +
                    ", Email: " + rs.getString("email")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (statement != null) statement.close(); } catch (Exception ignored) {}
            try { if (connection != null) connection.close(); } catch (Exception ignored) {}
        }
    }
}