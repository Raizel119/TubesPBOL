package tubespbol.model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Model class untuk Request Pertemuan (request_pertemuan table)
 * POJO - hanya berisi atribut data, constructor, getter, dan setter
 */
public class RequestPertemuan {
    private int id;
    private String nim;
    private String idDosen;
    private Date tanggal;
    private Time jamMulai;
    private Time jamSelesai;
    private String keperluan;
    private String status; // Menggunakan String untuk kompatibilitas dengan database enum
    private String alasanPenolakan;
    private String keteranganDosen;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructor default
    public RequestPertemuan() {
    }
    
    // Constructor dengan semua parameter
    public RequestPertemuan(int id, String nim, String idDosen, Date tanggal, 
                            Time jamMulai, Time jamSelesai, String keperluan, 
                            String status, String alasanPenolakan, String keteranganDosen,
                            Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.nim = nim;
        this.idDosen = idDosen;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.keperluan = keperluan;
        this.status = status;
        this.alasanPenolakan = alasanPenolakan;
        this.keteranganDosen = keteranganDosen;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Constructor tanpa id dan timestamps (untuk create)
    public RequestPertemuan(String nim, String idDosen, Date tanggal, 
                           Time jamMulai, Time jamSelesai, String keperluan) {
        this.nim = nim;
        this.idDosen = idDosen;
        this.tanggal = tanggal;
        this.jamMulai = jamMulai;
        this.jamSelesai = jamSelesai;
        this.keperluan = keperluan;
        this.status = "PENDING";
    }
    
    // Getter dan Setter
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNim() {
        return nim;
    }
    
    public void setNim(String nim) {
        this.nim = nim;
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
    
    public String getKeperluan() {
        return keperluan;
    }
    
    public void setKeperluan(String keperluan) {
        this.keperluan = keperluan;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getAlasanPenolakan() {
        return alasanPenolakan;
    }
    
    public void setAlasanPenolakan(String alasanPenolakan) {
        this.alasanPenolakan = alasanPenolakan;
    }
    
    public String getKeteranganDosen() {
        return keteranganDosen;
    }
    
    public void setKeteranganDosen(String keteranganDosen) {
        this.keteranganDosen = keteranganDosen;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}

