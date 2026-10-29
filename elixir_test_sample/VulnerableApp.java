import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;

public class VulnerableApp {

    /**
     * A03: Injection (SQL Injection)
     *
     * User input is concatenated directly into the SQL query.
     * An attacker can manipulate `username` to alter query logic.
     */
    public static void findUser(String username) {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/appdb", "app", "app123");
            Statement stmt = conn.createStatement();

            // ❌ Vulnerable: SQL Injection
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                System.out.println("User found: " + rs.getString("username"));
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * A02: Cryptographic Failures
     *
     * Uses MD5, which is cryptographically broken and unsuitable
     * for password hashing.
     */
    public static String hashPassword(String password) {
        try {
            // ❌ Vulnerable: Weak cryptographic algorithm
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(password.getBytes());

            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // Simulated user input
        findUser("admin' OR '1'='1");

        String hashed = hashPassword("P@ssw0rd!");
        System.out.println("Hashed password: " + hashed);
    }
}