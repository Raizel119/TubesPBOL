package tubespbol.model;

import java.sql.Timestamp;

/**
 * Model class untuk Dosen (users_dosen table)
 * POJO - hanya berisi atribut data, constructor, getter, dan setter
 */
public class Dosen {
    private String idDosen;
    private String nama;
    private String password;
    private String prodi;
    private Timestamp createdAt;
    
    // Constructor default
    public Dosen() {
    }
    
    // Constructor dengan parameter
    public Dosen(String idDosen, String nama, String password, String prodi, Timestamp createdAt) {
        this.idDosen = idDosen;
        this.nama = nama;
        this.password = password;
        this.prodi = prodi;
        this.createdAt = createdAt;
    }
    
    // Constructor tanpa createdAt (untuk create)
    public Dosen(String idDosen, String nama, String password, String prodi) {
        this.idDosen = idDosen;
        this.nama = nama;
        this.password = password;
        this.prodi = prodi;
    }
    
    // Getter dan Setter
    public String getIdDosen() {
        return idDosen;
    }
    
    public void setIdDosen(String idDosen) {
        this.idDosen = idDosen;
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

