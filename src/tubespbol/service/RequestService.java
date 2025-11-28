package tubespbol.service;

import tubespbol.model.Database;
import java.sql.*;
import java.util.*;

/**
 * Request Service Class
 * Handles meeting request operations between mahasiswa and dosen
 */
public class RequestService {
    
    /**
     * Submit meeting request from mahasiswa
     */
    public String ajukanRequest(String nim, String idDosen, String tanggal, 
                               String jamMulai, String jamSelesai, String keperluan) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return null;
            
            String query = "INSERT INTO request_pertemuan (nim, id_dosen, tanggal, jam_mulai, jam_selesai, keperluan, status) " +
                          "VALUES (?, ?, ?, ?, ?, ?, 'PENDING')";
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nim);
            stmt.setString(2, idDosen);
            stmt.setString(3, tanggal);
            stmt.setString(4, jamMulai);
            stmt.setString(5, jamSelesai);
            stmt.setString(6, keperluan);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                String requestId = "REQ" + String.format("%03d", rs.getInt(1));
                rs.close();
                stmt.close();
                return requestId;
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error ajukan request: " + e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
        return null;
    }
    
    public List<Map<String, String>> getRequestDosen(String dosenId) {
        List<Map<String, String>> requests = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return requests;
            
            // UPDATE: Join ke users_mahasiswa (bukan master_mahasiswa)
            String query = "SELECT r.*, m.nama as nama_mahasiswa, m.prodi as prodi_mahasiswa " +
                          "FROM request_pertemuan r " +
                          "JOIN users_mahasiswa m ON r.nim = m.nim " +
                          "WHERE r.id_dosen = ? " +
                          "ORDER BY r.tanggal, r.jam_mulai";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dosenId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, String> req = new HashMap<>();
                req.put("id", String.valueOf(rs.getInt("id")));
                req.put("nim", rs.getString("nim"));
                req.put("nama_mahasiswa", rs.getString("nama_mahasiswa"));
                req.put("prodi_mahasiswa", rs.getString("prodi_mahasiswa"));
                req.put("tanggal", rs.getString("tanggal"));
                req.put("jam_mulai", rs.getString("jam_mulai"));
                req.put("jam_selesai", rs.getString("jam_selesai"));
                req.put("keperluan", rs.getString("keperluan"));
                req.put("status", rs.getString("status")); // Tambahkan status biar bisa difilter
                requests.add(req);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error getting request dosen: " + e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
        return requests;
    }
    
    // UPDATE: Parameter requestId diubah jadi String agar cocok dengan Panel
    public boolean terimaRequest(String requestId, String keteranganDosen) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return false;
            
            String query = "UPDATE request_pertemuan SET status = 'DITERIMA', keterangan_dosen = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, keteranganDosen);
            stmt.setInt(2, Integer.parseInt(requestId)); // Parse String ke Int
            
            int affected = stmt.executeUpdate();
            stmt.close();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("✗ Error terima request: " + e.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    // UPDATE: Parameter requestId diubah jadi String
    public boolean tolakRequest(String requestId, String alasan) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return false;
            
            String query = "UPDATE request_pertemuan SET status = 'DITOLAK', alasan_penolakan = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, alasan);
            stmt.setInt(2, Integer.parseInt(requestId)); // Parse String ke Int
            
            int affected = stmt.executeUpdate();
            stmt.close();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("✗ Error tolak request: " + e.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn);
        }
    }

    // === METODE BARU YANG HILANG ===
    public boolean updateStatusToNA(String requestId) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return false;
            
            // Pastikan kolom ENUM di database sudah ada 'N/A'
            String query = "UPDATE request_pertemuan SET status = 'N/A' WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(requestId));
            
            int affected = stmt.executeUpdate();
            stmt.close();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("✗ Error update status NA: " + e.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    public List<Map<String, String>> getRequestMahasiswa(String nim) {
        List<Map<String, String>> requests = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return requests;
            
            // UPDATE: Join ke users_dosen
            String query = "SELECT r.*, d.nama as nama_dosen, d.prodi as prodi_dosen " +
                          "FROM request_pertemuan r " +
                          "JOIN users_dosen d ON r.id_dosen = d.id_dosen " +
                          "WHERE r.nim = ? " +
                          "ORDER BY r.tanggal DESC, r.jam_mulai DESC";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nim);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, String> req = new HashMap<>();
                req.put("id", String.valueOf(rs.getInt("id")));
                req.put("id_dosen", rs.getString("id_dosen"));
                req.put("nama_dosen", rs.getString("nama_dosen"));
                req.put("prodi_dosen", rs.getString("prodi_dosen"));
                req.put("tanggal", rs.getString("tanggal"));
                req.put("jam_mulai", rs.getString("jam_mulai"));
                req.put("jam_selesai", rs.getString("jam_selesai"));
                req.put("keperluan", rs.getString("keperluan"));
                req.put("status", rs.getString("status"));
                req.put("alasan_penolakan", rs.getString("alasan_penolakan") != null ? rs.getString("alasan_penolakan") : "");
                req.put("keterangan_dosen", rs.getString("keterangan_dosen") != null ? rs.getString("keterangan_dosen") : "");
                requests.add(req);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error getting request mahasiswa: " + e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
        return requests;
    }
    
    public boolean batalkanRequest(int requestId, String nim) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return false;
            
            String query = "DELETE FROM request_pertemuan WHERE id = ? AND nim = ? AND status = 'PENDING'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, requestId);
            stmt.setString(2, nim);
            
            int affected = stmt.executeUpdate();
            stmt.close();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("✗ Error batalkan request: " + e.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    public int countAcceptedRequests(String dosenId) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return 0;
            
            String query = "SELECT COUNT(*) as total FROM request_pertemuan WHERE id_dosen = ? AND status = 'DITERIMA'";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dosenId);
            ResultSet rs = stmt.executeQuery();
            
            int total = 0;
            if (rs.next()) {
                total = rs.getInt("total");
            }
            rs.close();
            stmt.close();
            return total;
        } catch (SQLException e) {
            System.out.println("✗ Error counting accepted requests: " + e.getMessage());
            return 0;
        } finally {
            Database.closeConnection(conn);
        }
    }
}