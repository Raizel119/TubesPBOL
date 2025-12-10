package tubespbol.controller;

/*
 * PermintaanMasukController
 * 
 * Controller yang menangani panel permintaan pertemuan masuk
 * untuk dosen pada aplikasi penjadwalan dosen–mahasiswa.
 * 
 * Tanggung jawab utama:
 * - Mengambil data permintaan dari service
 * - Menyaring permintaan dengan status PENDING
 * - Menangani aksi setujui, tolak, refresh, dan detail
 * - Menghubungkan View dengan RequestService
 */

import java.util.ArrayList;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import tubespbol.service.RequestService;
import tubespbol.view.panels.PanelPermintaanMasuk;

public class PermintaanMasukController {
    
    // Reference ke panel tampilan permintaan masuk
    private PanelPermintaanMasuk view;
    
    // Service untuk proses bisnis permintaan pertemuan
    private RequestService requestService;
    
    // ID dosen yang sedang login
    private String idDosen;

    /*
     * Constructor PermintaanMasukController
     * 
     * @param view     PanelPermintaanMasuk yang dikontrol
     * @param service  RequestService untuk akses data
     * @param idDosen  ID dosen yang aktif
     */
    public PermintaanMasukController(PanelPermintaanMasuk view, RequestService service, String idDosen) {
        this.view = view;
        this.requestService = service;
        this.idDosen = idDosen;
        
        initController();
    }
    
    /*
     * Inisialisasi controller
     * Memasang listener pada View dan memuat data awal
     */
    private void initController() {
        // Listener tombol Refresh
        view.addRefreshListener(e -> loadData());
        
        // Listener tombol Setujui
        view.addSetujuiListener(e -> actionSetujui());
        
        // Listener tombol Tolak
        view.addTolakListener(e -> actionTolak());
        
        // Listener tombol Detail (ditangani langsung oleh View)
        view.addDetailListener(
            e -> view.showDetailDialog(view.getSelectedData())
        );
        
        // Load data pertama kali saat panel dibuka
        loadData();
    }
    
    /*
     * Memuat ulang data permintaan
     * Sekaligus menjalankan auto update untuk request yang kedaluwarsa
     */
    private void loadData() {
        new Thread(() -> {
            // Auto update status request yang sudah expired
            requestService.autoUpdateExpired(idDosen);
            
            // Setelah proses selesai, ambil data terbaru di EDT
            SwingUtilities.invokeLater(this::fetchDataFromDB);
        }).start();
    }
    
    /*
     * Mengambil data permintaan dari database menggunakan SwingWorker
     * hanya menampilkan request dengan status PENDING
     */
    private void fetchDataFromDB() {
        new SwingWorker<ArrayList<Map<String, String>>, Void>() {
            
            @Override
            protected ArrayList<Map<String, String>> doInBackground() {
                try {
                    // Ambil semua data request milik dosen
                    Object result = requestService.getRequestDosen(idDosen);
                    
                    if (result instanceof ArrayList) {
                        ArrayList<Map<String, String>> all =
                            (ArrayList<Map<String, String>>) result;
                        
                        ArrayList<Map<String, String>> pendingOnly = new ArrayList<>();
                        
                        // Filter manual hanya status PENDING
                        for (Map<String, String> req : all) {
                            if ("PENDING".equalsIgnoreCase(req.get("status"))) {
                                pendingOnly.add(req);
                            }
                        }
                        return pendingOnly;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ArrayList<>();
            }

            @Override
            protected void done() {
                try {
                    // Update tabel pada View dengan data terbaru
                    view.updateTableData(get());
                } catch (Exception e) {
                    view.showMessage(
                        "Gagal memuat data: " + e.getMessage(),
                        true
                    );
                }
            }
        }.execute();
    }
    
    /*
     * Aksi ketika dosen menyetujui permintaan pertemuan
     */
    private void actionSetujui() {
        Map<String, String> data = view.getSelectedData();
        
        // Validasi data terpilih
        if (data == null) {
            view.showMessage("Pilih permintaan terlebih dahulu!", true);
            return;
        }
        
        // Dialog input catatan dosen
        String keterangan = view.showInputDialog(
            "Keterangan Dosen",
            "Masukkan catatan tambahan (opsional):"
        );
        
        if (keterangan == null) return; // Cancel
        if (keterangan.trim().isEmpty()) {
            keterangan = "Pertemuan disetujui";
        }
        
        String finalKet = keterangan;
        String idReq = data.get("id");
        
        // Eksekusi persetujuan di background
        new SwingWorker<Boolean, Void>() {
            
            @Override
            protected Boolean doInBackground() {
                return requestService.terimaRequest(idReq, finalKet);
            }
            
            @Override
            protected void done() {
                try {
                    if (get()) {
                        view.showMessage("Permintaan disetujui!", false);
                        loadData(); // Refresh tabel
                    } else {
                        view.showMessage("Gagal menyetujui permintaan.", true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
    
    /*
     * Aksi ketika dosen menolak permintaan pertemuan
     */
    private void actionTolak() {
        Map<String, String> data = view.getSelectedData();
        
        // Validasi data terpilih
        if (data == null) {
            view.showMessage("Pilih permintaan terlebih dahulu!", true);
            return;
        }
        
        // Dialog input alasan penolakan
        String alasan = view.showInputDialog(
            "Alasan Penolakan",
            "Masukkan alasan penolakan:"
        );
        
        if (alasan == null) return;
        if (alasan.trim().isEmpty()) {
            view.showMessage("Alasan penolakan wajib diisi!", true);
            return;
        }
        
        String idReq = data.get("id");
        
        // Konfirmasi sebelum menolak
        if (!view.showConfirmDialog(
            "Yakin ingin menolak permintaan ini?"
        )) return;
        
        // Eksekusi penolakan di background
        new SwingWorker<Boolean, Void>() {
            
            @Override
            protected Boolean doInBackground() {
                return requestService.tolakRequest(idReq, alasan);
            }
            
            @Override
            protected void done() {
                try {
                    if (get()) {
                        view.showMessage("Permintaan ditolak.", false);
                        loadData(); // Refresh tabel
                    } else {
                        view.showMessage("Gagal menolak permintaan.", true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }
}
