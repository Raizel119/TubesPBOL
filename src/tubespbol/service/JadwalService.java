package tubespbol.service;

import tubespbol.util.Database;
import java.sql.*;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Jadwal Service Class
 * Handles schedule-related operations for dosen
 */
public class JadwalService {
    
    /**
     * Get complete schedule for a dosen on a specific date
     * Includes: jadwal mengajar, agenda dosen, and accepted requests
     * @param dosenId Dosen ID
     * @param tanggal Date in YYYY-MM-DD format
     * @return List of schedule items
     */
    public List<Map<String, String>> getJadwalLengkap(String dosenId, String tanggal) {
        List<Map<String, String>> jadwal = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return jadwal;
            
            String hari = getHariFromTanggal(tanggal);
            
            // Query combines: jadwal mengajar, agenda dosen, and accepted requests
            String query = "SELECT 'Mengajar' as tipe, mata_kuliah as judul, " +
                          "CONCAT(mata_kuliah, ' (', ruangan, ')') as keterangan, " +
                          "jam_mulai, jam_selesai, id as jadwal_id " +
                          "FROM jadwal_mengajar " +
                          "WHERE id_dosen = ? AND hari = ? " +
                          "UNION ALL " +
                          "SELECT 'Agenda' as tipe, judul, keterangan, " +
                          "jam_mulai, jam_selesai, id as jadwal_id " +
                          "FROM agenda_dosen " +
                          "WHERE id_dosen = ? AND tanggal = ? " +
                          "UNION ALL " +
                          "SELECT 'Pertemuan' as tipe, " +
                          "CONCAT('Pertemuan dengan ', m.nama) as judul, " +
                          "r.keperluan as keterangan, " +
                          "r.jam_mulai, r.jam_selesai, r.id as jadwal_id " +
                          "FROM request_pertemuan r " +
                          "JOIN users_mahasiswa m ON r.nim = m.nim " +
                          "WHERE r.id_dosen = ? AND r.tanggal = ? AND r.status = 'DITERIMA' " +
                          "ORDER BY jam_mulai";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dosenId);
            stmt.setString(2, hari);
            stmt.setString(3, dosenId);
            stmt.setString(4, tanggal);
            stmt.setString(5, dosenId);
            stmt.setString(6, tanggal);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, String> item = new HashMap<>();
                item.put("tipe", rs.getString("tipe"));
                item.put("judul", rs.getString("judul"));
                item.put("keterangan", rs.getString("keterangan"));
                item.put("jam_mulai", rs.getString("jam_mulai"));
                item.put("jam_selesai", rs.getString("jam_selesai"));
                item.put("jadwal_id", rs.getString("jadwal_id"));
                jadwal.add(item);
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error getting jadwal: " + e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
        return jadwal;
    }
    
    /**
     * Get available time slots for a dosen on a specific date
     * @param dosenId Dosen ID
     * @param tanggal Date in YYYY-MM-DD format
     * @return List of available time slots
     */
    public List<String> getJamTersedia(String dosenId, String tanggal) {
        List<String> tersedia = new ArrayList<>();
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return tersedia;
            
            String hari = getHariFromTanggal(tanggal);
            
            // Get all blocked time slots
            String query = "SELECT jam_mulai, jam_selesai FROM jadwal_mengajar " +
                          "WHERE id_dosen = ? AND hari = ? " +
                          "UNION " +
                          "SELECT jam_mulai, jam_selesai FROM agenda_dosen " +
                          "WHERE id_dosen = ? AND tanggal = ? " +
                          "UNION " +
                          "SELECT jam_mulai, jam_selesai FROM request_pertemuan " +
                          "WHERE id_dosen = ? AND tanggal = ? AND status = 'DITERIMA'";
            
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dosenId);
            stmt.setString(2, hari);
            stmt.setString(3, dosenId);
            stmt.setString(4, tanggal);
            stmt.setString(5, dosenId);
            stmt.setString(6, tanggal);
            ResultSet rs = stmt.executeQuery();
            
            // Collect blocked time slots
            List<String[]> jamOff = new ArrayList<>();
            while (rs.next()) {
                jamOff.add(new String[]{
                    rs.getString("jam_mulai"),
                    rs.getString("jam_selesai")
                });
            }
            
            // Check each hour slot (08:00 - 16:00)
            for (int hour = 8; hour < 17; hour++) {
                String jam = String.format("%02d:00 - %02d:00", hour, hour + 1);
                String jamMulai = String.format("%02d:00:00", hour);
                String jamSelesai = String.format("%02d:00:00", hour + 1);
                
                boolean bentrok = false;
                for (String[] off : jamOff) {
                    // Check if time slot overlaps with blocked time
                    if (!(jamSelesai.compareTo(off[0]) <= 0 || jamMulai.compareTo(off[1]) >= 0)) {
                        bentrok = true;
                        break;
                    }
                }
                
                if (!bentrok) {
                    tersedia.add(jam);
                }
            }
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println("✗ Error getting jam tersedia: " + e.getMessage());
        } finally {
            Database.closeConnection(conn);
        }
        return tersedia;
    }
    
    /**
     * Add custom agenda for dosen (block time)
     * @param dosenId Dosen ID
     * @param tanggal Date
     * @param jamMulai Start time
     * @param jamSelesai End time
     * @param judul Title
     * @param keterangan Description
     * @return true if successful
     */
    public boolean tambahAgenda(String dosenId, String tanggal, String jamMulai, 
                                String jamSelesai, String judul, String keterangan) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return false;
            
            String query = "INSERT INTO agenda_dosen (id_dosen, tanggal, jam_mulai, jam_selesai, judul, keterangan) " +
                          "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, dosenId);
            stmt.setString(2, tanggal);
            stmt.setString(3, jamMulai);
            stmt.setString(4, jamSelesai);
            stmt.setString(5, judul);
            stmt.setString(6, keterangan);
            
            int affected = stmt.executeUpdate();
            stmt.close();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("✗ Error tambah agenda: " + e.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    /**
     * Delete agenda by ID
     * @param agendaId Agenda ID
     * @return true if successful
     */
    public boolean hapusAgenda(int agendaId) {
        Connection conn = null;
        try {
            conn = Database.getConnection();
            if (conn == null) return false;
            
            String query = "DELETE FROM agenda_dosen WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, agendaId);
            
            int affected = stmt.executeUpdate();
            stmt.close();
            return affected > 0;
        } catch (SQLException e) {
            System.out.println("✗ Error hapus agenda: " + e.getMessage());
            return false;
        } finally {
            Database.closeConnection(conn);
        }
    }
    
    /**
     * Get day name from date
     * @param tanggal Date in YYYY-MM-DD format
     * @return Indonesian day name
     */
    private String getHariFromTanggal(String tanggal) {
        try {
            Connection conn = Database.getConnection();
            if (conn == null) return "";
            
            String query = "SELECT DAYNAME(?) as hari";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, tanggal);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String hari = rs.getString("hari");
                switch(hari) {
                    case "Monday": return "Senin";
                    case "Tuesday": return "Selasa";
                    case "Wednesday": return "Rabu";
                    case "Thursday": return "Kamis";
                    case "Friday": return "Jumat";
                    case "Saturday": return "Sabtu";
                    case "Sunday": return "Minggu";
                }
            }
            
            rs.close();
            stmt.close();
            Database.closeConnection(conn);
        } catch (SQLException e) {
            System.out.println("✗ Error getting hari: " + e.getMessage());
        }
        return "";
    }
}