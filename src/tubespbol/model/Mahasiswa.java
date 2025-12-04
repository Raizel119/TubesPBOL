package tubespbol.model;

import java.sql.Timestamp;

/**
 * Model class untuk Mahasiswa (users_mahasiswa table)
 * POJO - hanya berisi atribut data, constructor, getter, dan setter
 */
public class Mahasiswa {
    private String nim;
    private String nama;
    private String password;
    private String prodi;
    private Timestamp createdAt;
    
    // Constructor default
    public Mahasiswa() {
    }
    
    // Constructor dengan parameter
    public Mahasiswa(String nim, String nama, String password, String prodi, Timestamp createdAt) {
        this.nim = nim;
        this.nama = nama;
        this.password = password;
        this.prodi = prodi;
        this.createdAt = createdAt;
    }
    
    // Constructor tanpa createdAt (untuk create)
    public Mahasiswa(String nim, String nama, String password, String prodi) {
        this.nim = nim;
        this.nama = nama;
        this.password = password;
        this.prodi = prodi;
    }
    
    // Getter dan Setter
    public String getNim() {
        return nim;
    }
    
    public void setNim(String nim) {
        this.nim = nim;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getProdi() {
        return prodi;
    }
    
    public void setProdi(String prodi) {
        this.prodi = prodi;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

