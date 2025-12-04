package tubespbol.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Model class untuk Agenda Dosen (agenda_dosen table)
 * POJO - hanya berisi atribut data, constructor, getter, dan setter
 */
public class AgendaDosen {
    private int id;
    private String idDosen;
    private Date tanggal;
    private Time jamMulai;
    private Time jamSelesai;
    private String judul;
    private String keterangan;
    private Timestamp createdAt;
    
    // Constructor default
    public AgendaDosen() {
    }
    
    // Constructor dengan parameter
    public AgendaDosen(int id, String idDosen, Date tanggal, Time jamMulai, 
                      Time jamSelesai, String judul, String keterangan, Timestamp createdAt) {
        this.id = id;
        this.idDosen = idDosen;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.judul = judul;
        this.keterangan = keterangan;
        this.createdAt = createdAt;
    }
    
    // Constructor tanpa id dan createdAt (untuk create)
    public AgendaDosen(String idDosen, Date tanggal, Time jamMulai, 
                      Time jamSelesai, String judul, String keterangan) {
        this.idDosen = idDosen;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.judul = judul;
        this.keterangan = keterangan;
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
    
    public Date getTanggal() {
        return tanggal;
    }
    
    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
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
    
    public String getJudul() {
        return judul;
    }
    
    public void setJudul(String judul) {
        this.judul = judul;
    }
    
    public String getKeterangan() {
        return keterangan;
    }
    
    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

