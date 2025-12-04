package tubespbol.model;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * Model class untuk Jadwal Mengajar (jadwal_mengajar table)
 * POJO - hanya berisi atribut data, constructor, getter, dan setter
 */
public class JadwalMengajar {
    private int id;
    private String idDosen;
    private String hari; // Menggunakan String untuk kompatibilitas dengan database enum
    private String mataKuliah;
    private String ruangan;
    private Time jamMulai;
    private Time jamSelesai;
    private Timestamp createdAt;
    
    // Constructor default
    public JadwalMengajar() {
    }
    
    // Constructor dengan parameter
    public JadwalMengajar(int id, String idDosen, String hari, String mataKuliah, 
                         String ruangan, Time jamMulai, Time jamSelesai, Timestamp createdAt) {
        this.id = id;
        this.idDosen = idDosen;
        this.hari = hari;
        this.mataKuliah = mataKuliah;
        this.ruangan = ruangan;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.createdAt = createdAt;
    }
    
    // Constructor tanpa id dan createdAt (untuk create)
    public JadwalMengajar(String idDosen, String hari, String mataKuliah, 
                         String ruangan, Time jamMulai, Time jamSelesai) {
        this.idDosen = idDosen;
        this.hari = hari;
        this.mataKuliah = mataKuliah;
        this.ruangan = ruangan;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
    }
    
    // Getter dan Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getIdDosen() {
        return idDosen;
    }
    
    public void setIdDosen(String idDosen) {
        this.idDosen = idDosen;
    }
    
    public String getHari() {
        return hari;
    }
    
    public void setHari(String hari) {
        this.hari = hari;
    }
    
    public String getMataKuliah() {
        return mataKuliah;
    }
    
    public void setMataKuliah(String mataKuliah) {
        this.mataKuliah = mataKuliah;
    }
    
    public String getRuangan() {
        return ruangan;
    }
    
    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
    }
    
    public Time getJamMulai() {
        return jamMulai;
    }
    
    public void setJamMulai(Time jamMulai) {
        this.jamMulai = jamMulai;
    }
    
    public Time getJamSelesai() {
        return jamSelesai;
    }
    
    public void setJamSelesai(Time jamSelesai) {
        this.jamSelesai = jamSelesai;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

