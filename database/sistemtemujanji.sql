-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Nov 17, 2025 at 04:24 AM
-- Server version: 10.11.10-MariaDB-log
-- PHP Version: 8.3.22

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sistemtemujanji`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `terima_request` (IN `p_request_id` INT)   BEGIN
    UPDATE request_pertemuan 
    SET status = 'DITERIMA', updated_at = NOW()
    WHERE id = p_request_id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `tolak_request` (IN `p_request_id` INT, IN `p_alasan` TEXT)   BEGIN
    UPDATE request_pertemuan 
    SET status = 'DITOLAK', alasan_penolakan = p_alasan, updated_at = NOW()
    WHERE id = p_request_id;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `agenda_dosen`
--

CREATE TABLE `agenda_dosen` (
  `id` int(11) NOT NULL,
  `id_dosen` varchar(8) NOT NULL,
  `tanggal` date NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL,
  `judul` varchar(200) NOT NULL,
  `keterangan` text DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `agenda_dosen`
--

INSERT INTO `agenda_dosen` (`id`, `id_dosen`, `tanggal`, `jam_mulai`, `jam_selesai`, `judul`, `keterangan`, `created_at`) VALUES
(1, '12345678', '2025-11-17', '12:00:00', '13:00:00', 'Istirahat & Sholat', 'Waktu istirahat siang', '2025-11-16 08:54:45'),
(2, '12345678', '2025-11-18', '15:00:00', '17:00:00', 'Rapat Jurusan', 'Rapat evaluasi kurikulum', '2025-11-16 08:54:45'),
(3, '12345678', '2025-11-19', '14:00:00', '15:00:00', 'Keperluan Pribadi', 'Urusan keluarga', '2025-11-16 08:54:45');

-- --------------------------------------------------------

--
-- Table structure for table `jadwal_mengajar`
--

CREATE TABLE `jadwal_mengajar` (
  `id` int(11) NOT NULL,
  `id_dosen` varchar(8) NOT NULL,
  `hari` enum('Senin','Selasa','Rabu','Kamis','Jumat','Sabtu','Minggu') NOT NULL,
  `mata_kuliah` varchar(100) NOT NULL,
  `ruangan` varchar(20) NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jadwal_mengajar`
--

INSERT INTO `jadwal_mengajar` (`id`, `id_dosen`, `hari`, `mata_kuliah`, `ruangan`, `jam_mulai`, `jam_selesai`, `created_at`) VALUES
(1, '12345678', 'Senin', 'Pemrograman Berbasis Objek', 'Lab 1', '08:00:00', '10:00:00', '2025-11-16 08:54:45'),
(2, '12345678', 'Senin', 'Basis Data', 'Lab 2', '13:00:00', '15:00:00', '2025-11-16 08:54:45'),
(3, '12345678', 'Rabu', 'Algoritma dan Struktur Data', 'Lab 1', '10:00:00', '12:00:00', '2025-11-16 08:54:45'),
(4, '12345678', 'Kamis', 'Pemrograman Web', 'Lab 3', '14:00:00', '16:00:00', '2025-11-16 08:54:45'),
(5, '12345678', 'Jumat', 'Rekayasa Perangkat Lunak', 'Lab 2', '08:00:00', '10:00:00', '2025-11-16 08:54:45'),
(6, '87654321', 'Senin', 'Kecerdasan Buatan', 'Lab 2', '10:00:00', '12:00:00', '2025-11-16 08:54:45'),
(7, '87654321', 'Selasa', 'Machine Learning', 'Lab 3', '08:00:00', '10:00:00', '2025-11-16 08:54:45'),
(8, '87654321', 'Rabu', 'Data Mining', 'Lab 2', '13:00:00', '15:00:00', '2025-11-16 08:54:45'),
(9, '87654321', 'Kamis', 'Deep Learning', 'Lab 3', '10:00:00', '12:00:00', '2025-11-16 08:54:45'),
(10, '11223344', 'Selasa', 'Manajemen Proyek', 'Kelas A', '10:00:00', '12:00:00', '2025-11-16 08:54:45'),
(11, '11223344', 'Rabu', 'Analisis Sistem', 'Kelas B', '08:00:00', '10:00:00', '2025-11-16 08:54:45'),
(12, '11223344', 'Kamis', 'Sistem Informasi Manajemen', 'Kelas A', '13:00:00', '15:00:00', '2025-11-16 08:54:45'),
(13, '11223344', 'Jumat', 'E-Business', 'Kelas B', '10:00:00', '12:00:00', '2025-11-16 08:54:45');

-- --------------------------------------------------------

--
-- Table structure for table `request_pertemuan`
--

CREATE TABLE `request_pertemuan` (
  `id` int(11) NOT NULL,
  `nim` varchar(10) NOT NULL,
  `id_dosen` varchar(8) NOT NULL,
  `tanggal` date NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL,
  `keperluan` text NOT NULL,
  `status` enum('PENDING','DITERIMA','DITOLAK') DEFAULT 'PENDING',
  `alasan_penolakan` text DEFAULT NULL,
  `keterangan_dosen` text DEFAULT NULL COMMENT 'Keterangan tambahan dari dosen saat menerima request',
  `created_at` timestamp NULL DEFAULT current_timestamp(),
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `request_pertemuan`
--

INSERT INTO `request_pertemuan` (`id`, `nim`, `id_dosen`, `tanggal`, `jam_mulai`, `jam_selesai`, `keperluan`, `status`, `alasan_penolakan`, `keterangan_dosen`, `created_at`, `updated_at`) VALUES
(1, '2021110001', '12345678', '2025-11-20', '10:00:00', '11:00:00', 'Konsultasi Tugas Akhir tentang metodologi penelitian', 'PENDING', NULL, NULL, '2025-11-16 08:54:45', '2025-11-16 08:54:45'),
(2, '2021110002', '12345678', '2025-11-21', '11:00:00', '12:00:00', 'Bimbingan skripsi bab 2', 'DITERIMA', NULL, 'SILAHKAN BAWA PROPOSAL NYA', '2025-11-16 08:54:45', '2025-11-16 10:06:57'),
(3, '2022110001', '87654321', '2025-11-22', '14:00:00', '15:00:00', 'Diskusi project Machine Learning', 'DITERIMA', NULL, NULL, '2025-11-16 08:54:45', '2025-11-16 08:54:45'),
(4, '2021110001', '11223344', '2025-11-19', '11:00:00', '12:00:00', 'Konsultasi proposal PKL', 'DITOLAK', 'Maaf, saya ada keperluan mendadak pada jam tersebut. Silakan ajukan waktu lain.', NULL, '2025-11-16 08:54:45', '2025-11-16 08:54:45');

--
-- Triggers `request_pertemuan`
--
DELIMITER $$
CREATE TRIGGER `before_insert_request` BEFORE INSERT ON `request_pertemuan` FOR EACH ROW BEGIN
    DECLARE bentrok INT;
    
    -- Cek bentrok dengan jadwal mengajar
    SELECT COUNT(*) INTO bentrok
    FROM jadwal_mengajar
    WHERE id_dosen = NEW.id_dosen 
    AND hari = DAYNAME(NEW.tanggal)
    AND NOT (NEW.jam_selesai <= jam_mulai OR NEW.jam_mulai >= jam_selesai);
    
    IF bentrok > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Bentrok dengan jadwal mengajar dosen!';
    END IF;
    
    -- Cek bentrok dengan agenda dosen
    SELECT COUNT(*) INTO bentrok
    FROM agenda_dosen
    WHERE id_dosen = NEW.id_dosen 
    AND tanggal = NEW.tanggal
    AND NOT (NEW.jam_selesai <= jam_mulai OR NEW.jam_mulai >= jam_selesai);
    
    IF bentrok > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Bentrok dengan agenda dosen!';
    END IF;
    
    -- Cek bentrok dengan request yang sudah diterima
    SELECT COUNT(*) INTO bentrok
    FROM request_pertemuan
    WHERE id_dosen = NEW.id_dosen 
    AND tanggal = NEW.tanggal
    AND status = 'DITERIMA'
    AND NOT (NEW.jam_selesai <= jam_mulai OR NEW.jam_mulai >= jam_selesai);
    
    IF bentrok > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Bentrok dengan request yang sudah diterima!';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `users_dosen`
--
-- Catatan: Tabel ini sekarang berfungsi sebagai tabel master dan user
--

CREATE TABLE `users_dosen` (
  `id_dosen` varchar(8) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `prodi` varchar(50) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users_dosen`
--

INSERT INTO `users_dosen` (`id_dosen`, `nama`, `password`, `prodi`, `created_at`) VALUES
('11223344', 'Dr. Ahmad Dahlan', 'password123', 'Sistem Informasi', '2025-11-16 08:54:45'),
('12345678', 'Dr. Budi Santoso', 'password123', 'Teknik Informatika', '2025-11-16 08:54:45'),
('87654321', 'Prof. Siti Nurhaliza', 'password123', 'Teknik Informatika', '2025-11-16 08:54:45');

-- --------------------------------------------------------

--
-- Table structure for table `users_mahasiswa`
--
-- Catatan: Tabel ini sekarang berfungsi sebagai tabel master dan user
--

CREATE TABLE `users_mahasiswa` (
  `nim` varchar(10) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `prodi` varchar(50) NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users_mahasiswa`
--

INSERT INTO `users_mahasiswa` (`nim`, `nama`, `password`, `prodi`, `created_at`) VALUES
('2021110001', 'Ahmad Rizki Pratama', 'password123', 'Teknik Informatika', '2025-11-16 08:54:45'),
('2021110002', 'Siti Aminah', 'password123', 'Teknik Informatika', '2025-11-16 08:54:45'),
('2022110001', 'Dewi Kusuma', 'password123', 'Teknik Informatika', '2025-11-16 08:54:45');

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_jadwal_dosen`
-- (See below for the actual view)
--
CREATE TABLE `view_jadwal_dosen` (
`id_dosen` varchar(8)
,`tipe` varchar(8)
,`hari` varchar(9)
,`tanggal` date
,`judul` varchar(200)
,`keterangan` mediumtext
,`jam_mulai` time /* mariadb-5.3 */
,`jam_selesai` time /* mariadb-5.3 */
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `view_request_detail`
-- (See below for the actual view)
--
CREATE TABLE `view_request_detail` (
`id` int(11)
,`nim` varchar(10)
,`nama_mahasiswa` varchar(100)
,`prodi_mahasiswa` varchar(50)
,`id_dosen` varchar(8)
,`nama_dosen` varchar(100)
,`prodi_dosen` varchar(50)
,`tanggal` date
,`jam_mulai` time
,`jam_selesai` time
,`keperluan` text
,`status` enum('PENDING','DITERIMA','DITOLAK','N/A')
,`alasan_penolakan` text
,`created_at` timestamp
,`updated_at` timestamp
);

-- --------------------------------------------------------

--
-- Structure for view `view_jadwal_dosen`
--
DROP TABLE IF EXISTS `view_jadwal_dosen`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_jadwal_dosen`  AS SELECT `jadwal_mengajar`.`id_dosen` AS `id_dosen`, 'Mengajar' AS `tipe`, `jadwal_mengajar`.`hari` AS `hari`, NULL AS `tanggal`, `jadwal_mengajar`.`mata_kuliah` AS `judul`, concat(`jadwal_mengajar`.`mata_kuliah`,' (',`jadwal_mengajar`.`ruangan`,')') AS `keterangan`, `jadwal_mengajar`.`jam_mulai` AS `jam_mulai`, `jadwal_mengajar`.`jam_selesai` AS `jam_selesai` FROM `jadwal_mengajar` union all select `agenda_dosen`.`id_dosen` AS `id_dosen`,'Agenda' AS `tipe`,dayname(`agenda_dosen`.`tanggal`) AS `hari`,`agenda_dosen`.`tanggal` AS `tanggal`,`agenda_dosen`.`judul` AS `judul`,`agenda_dosen`.`keterangan` AS `keterangan`,`agenda_dosen`.`jam_mulai` AS `jam_mulai`,`agenda_dosen`.`jam_selesai` AS `jam_selesai` from `agenda_dosen`  ;

-- --------------------------------------------------------

--
-- Structure for view `view_request_detail`
--
DROP TABLE IF EXISTS `view_request_detail`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `view_request_detail`  AS SELECT `r`.`id` AS `id`, `r`.`nim` AS `nim`, `m`.`nama` AS `nama_mahasiswa`, `m`.`prodi` AS `prodi_mahasiswa`, `r`.`id_dosen` AS `id_dosen`, `d`.`nama` AS `nama_dosen`, `d`.`prodi` AS `prodi_dosen`, `r`.`tanggal` AS `tanggal`, `r`.`jam_mulai` AS `jam_mulai`, `r`.`jam_selesai` AS `jam_selesai`, `r`.`keperluan` AS `keperluan`, `r`.`status` AS `status`, `r`.`alasan_penolakan` AS `alasan_penolakan`, `r`.`created_at` AS `created_at`, `r`.`updated_at` AS `updated_at` FROM ((`request_pertemuan` `r` join `users_mahasiswa` `m` on(`r`.`nim` = `m`.`nim`)) join `users_dosen` `d` on(`r`.`id_dosen` = `d`.`id_dosen`))  ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `agenda_dosen`
--
ALTER TABLE `agenda_dosen`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_dosen_tanggal` (`id_dosen`,`tanggal`);

--
-- Indexes for table `jadwal_mengajar`
--
ALTER TABLE `jadwal_mengajar`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_dosen_hari` (`id_dosen`,`hari`);

--
-- Indexes for table `request_pertemuan`
--
ALTER TABLE `request_pertemuan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `idx_dosen_status` (`id_dosen`,`status`),
  ADD KEY `idx_mahasiswa` (`nim`),
  ADD KEY `idx_tanggal` (`tanggal`);

--
-- Indexes for table `users_dosen`
--
ALTER TABLE `users_dosen`
  ADD PRIMARY KEY (`id_dosen`);

--
-- Indexes for table `users_mahasiswa`
--
ALTER TABLE `users_mahasiswa`
  ADD PRIMARY KEY (`nim`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `agenda_dosen`
--
ALTER TABLE `agenda_dosen`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `jadwal_mengajar`
--
ALTER TABLE `jadwal_mengajar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `request_pertemuan`
--
ALTER TABLE `request_pertemuan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `agenda_dosen`
--
ALTER TABLE `agenda_dosen`
  ADD CONSTRAINT `agenda_dosen_ibfk_1` FOREIGN KEY (`id_dosen`) REFERENCES `users_dosen` (`id_dosen`) ON DELETE CASCADE;

--
-- Constraints for table `jadwal_mengajar`
--
ALTER TABLE `jadwal_mengajar`
  ADD CONSTRAINT `jadwal_mengajar_ibfk_1` FOREIGN KEY (`id_dosen`) REFERENCES `users_dosen` (`id_dosen`) ON DELETE CASCADE;

--
-- Constraints for table `request_pertemuan`
--
ALTER TABLE `request_pertemuan`
  ADD CONSTRAINT `request_pertemuan_ibfk_1` FOREIGN KEY (`nim`) REFERENCES `users_mahasiswa` (`nim`) ON DELETE CASCADE,
  ADD CONSTRAINT `request_pertemuan_ibfk_2` FOREIGN KEY (`id_dosen`) REFERENCES `users_dosen` (`id_dosen`) ON DELETE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;