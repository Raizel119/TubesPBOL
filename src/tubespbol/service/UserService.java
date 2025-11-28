package tubespbol.service;

import tubespbol.model.Database;
import java.sql.*;
import java.util.*;

/**
 * User Service Class
 * Handles user authentication and user-related operations
 */
public class UserService {
    
    /**
     * Authenticate user login
     * @param id User ID (NIM for mahasiswa, ID for dosen)
     * @param password User password
     * @param role User role ("Mahasiswa" or "Dosen")
     * @return User data map if successful, null if failed
     */
    public Map<String, String> login(String id, String password, String role) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) {
                System.out.println("✗ Tidak dapat terhubung ke database!");
                return null;
            }
            
            String query = "";
            if (role.equals("Dosen")) {
                query = "SELECT id_dosen as id, nama, prodi FROM users_dosen WHERE id_dosen = ? AND password = ?";
            } else if (role.equals("Mahasiswa")) {
                query = "SELECT nim as id, nama, prodi FROM users_mahasiswa WHERE nim = ? AND password = ?";
            } else {
                return null;
            }
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, id);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Map<String, String> userData = new HashMap<>();
                userData.put("id", rs.getString("id"));
                userData.put("nama", rs.getString("nama"));
                userData.put("prodi", rs.getString("prodi"));
                userData.put("role", role);
                
                rs.close();
                stmt.close();
                return userData;
            }
            
            rs.close();
            stmt.close();
            return null;
            
        } catch (SQLException e) {
            System.out.println("✗ Error login: " + e.getMessage());
            return null;
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    /**
     * Get all dosen for selection
     * @return List of dosen data
     */
    public List<Map<String, String>> getAllDosen() {
        List<Map<String, String>> dosenList = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return dosenList;
            
            String query = "SELECT id_dosen, nama, prodi FROM users_dosen ORDER BY nama";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Map<String, String> dosen = new HashMap<>();
                dosen.put("id_dosen", rs.getString("id_dosen"));
                dosen.put("nama", rs.getString("nama"));
                dosen.put("prodi", rs.getString("prodi"));
                dosenList.add(dosen);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error getting dosen list: " + e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
        return dosenList;
    }
    
    /**
     * Get dosen details by ID
     * @param idDosen Dosen ID
     * @return Dosen data map
     */
    public Map<String, String> getDosenById(String idDosen) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return null;
            
            String query = "SELECT id_dosen, nama, prodi FROM users_dosen WHERE id_dosen = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, idDosen);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Map<String, String> dosen = new HashMap<>();
                dosen.put("id_dosen", rs.getString("id_dosen"));
                dosen.put("nama", rs.getString("nama"));
                dosen.put("prodi", rs.getString("prodi"));
                
                rs.close();
                stmt.close();
                return dosen;
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error getting dosen: " + e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
        return null;
    }
    
    /**
     * Get mahasiswa details by NIM
     * @param nim Mahasiswa NIM
     * @return Mahasiswa data map
     */
    public Map<String, String> getMahasiswaByNim(String nim) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return null;
            
            String query = "SELECT nim, nama, prodi FROM users_mahasiswa WHERE nim = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Map<String, String> mahasiswa = new HashMap<>();
                mahasiswa.put("nim", rs.getString("nim"));
                mahasiswa.put("nama", rs.getString("nama"));
                mahasiswa.put("prodi", rs.getString("prodi"));
                
                rs.close();
                stmt.close();
                return mahasiswa;
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error getting mahasiswa: " + e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
        return null;
    }
}