package tubespbol.model;

import java.sql.*;

/**
 * Database Connection Class
 * Handles MySQL database connection for the system
 */
public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/sistemtemujanji";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Password kosong
    
    /**
     * Get database connection
     * @return Connection object or null if failed
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Register MySQL JDBC Driver
            try {
                DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            } catch (Exception e) {
                // Driver will auto-load in newer versions
            }
            
            // Create connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
            
        } catch (SQLException e) {
            System.err.println("✗ KONEKSI DATABASE GAGAL!");
            System.err.println("   Error: " + e.getMessage());
            System.err.println("   Pastikan MySQL sudah running dan database 'sistemtemujanji' sudah dibuat!");
            return null;
        }
    }
    
    /**
     * Close database connection safely
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Test database connection
     * @return true if connection successful
     */
    public static boolean testConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("✓ Koneksi database berhasil!");
            closeConnection(conn);
            return true;
        }
        return false;
    }
}