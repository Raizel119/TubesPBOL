# Sistem Penjadwalan Pertemuan Dosen dan Mahasiswa

Proyek ini merupakan tugas mata kuliah **PBOL (Pemrograman Berorientasi Objek Lanjutan)** yang bertujuan untuk membangun sebuah sistem penjadwalan pertemuan antara **dosen dan mahasiswa** secara terstruktur, transparan, dan efisien. Sistem ini dirancang untuk mengatasi permasalahan penjadwalan manual yang sering menimbulkan ketidakpastian waktu, keterlambatan bimbingan, serta bentrok jadwal.

Sistem memungkinkan dosen untuk mengelola ketersediaan waktu, sementara mahasiswa dapat melihat jadwal dosen dan mengajukan permintaan pertemuan sesuai slot yang tersedia.

---

## 📌 Overview Proyek

**Nama Proyek**
Sistem Penjadwalan Pertemuan Dosen dan Mahasiswa

**Studi Kasus**
Program Studi Teknologi Informasi, Universitas Sumatera Utara

**Tujuan Utama**

* Mempermudah proses penjadwalan pertemuan antara dosen dan mahasiswa
* Mengurangi bentrok jadwal melalui sistem validasi otomatis
* Meningkatkan efisiensi dan transparansi komunikasi akademik

Sistem ini berfokus pada dua peran utama:

* **Dosen**: Mengatur jadwal ketersediaan dan memvalidasi permintaan pertemuan
* **Mahasiswa**: Melihat jadwal dosen dan mengajukan permintaan pertemuan

---

## ✨ Fitur Utama

### 🔐 Autentikasi Pengguna

* Login dengan role **Dosen** atau **Mahasiswa**
* Validasi kredensial berdasarkan peran pengguna

### 👨‍🏫 Fitur Dosen

* Mengatur jadwal ketersediaan (agenda pribadi)
* Melihat daftar permintaan pertemuan dari mahasiswa
* Menyetujui atau menolak permintaan pertemuan
* Memberikan alasan penolakan
* Melihat riwayat pertemuan

### 🎓 Fitur Mahasiswa

* Melihat jadwal ketersediaan dosen secara real-time
* Mengajukan permintaan pertemuan
* Melihat status permintaan (menunggu, disetujui, ditolak)
* Melihat riwayat pertemuan dan alasan penolakan

### ⛔ Pencegahan Bentrok Jadwal

* Sistem secara otomatis memblokir slot waktu yang tidak tersedia
* Mahasiswa hanya dapat memilih waktu kosong

---

## 🛠️ Teknologi yang Digunakan

* **Bahasa Pemrograman**: Java
* **Paradigma**: Object-Oriented Programming (OOP)
* **Database**: MySQL
* **Arsitektur**: Client–Server (berbasis aplikasi)
* **Tools Pendukung**:

  * JDBC untuk koneksi database
  * IDE Java (NetBeans / IntelliJ IDEA)

---

## 👥 Tim Pengembang (Kelompok 5)

| NIM       | Nama                                  | Peran   | Tugas                                                         |
| --------- | ------------------------------------- | ------- | ------------------------------------------------------------- |
| 241402060 | Daniele Christian Hasiholland Siahaan | Backend | Membuat alur bisnis dan alur pemrograman untuk user dosen     |
| 241402039 | Vincent Simbolon                      | UI/UX   | Membuat tampilan dan halaman untuk role dosen dan mahasiswa   |
| 241402070 | Joy Christian Barus                   | UI/UX   | Membuat halaman login dan membantu tampilan dosen & mahasiswa |
| 241402043 | Rahma Sarita Nasution                 | Laporan | Membuat PowerPoint dan membantu penyusunan laporan            |
| 241402046 | Enjely Margaret Sianturi              | Laporan | Menyusun laporan dalam bentuk dokumen Word                    |
| 241402063 | Medeleine Rovita Anggraini Aritonang  | Backend | Merancang dan membuat database sistem                         |
| 241402073 | Richard Lim                           | Backend | Membuat alur bisnis dan alur pemrograman untuk user mahasiswa |

---

## 📂 Ruang Lingkup Proyek

* Sistem hanya mencakup penjadwalan pertemuan dosen dan mahasiswa
* Tidak mencakup fitur video call atau chat
* Data pengguna dimasukkan secara manual dan belum terintegrasi dengan sistem akademik kampus
