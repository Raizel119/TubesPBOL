package tubespbol.controller;

import java.util.ArrayList;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import tubespbol.service.RequestService;
import tubespbol.view.panels.PanelPermintaanMasuk;

public class PermintaanMasukController {
    
    private PanelPermintaanMasuk view;
    private RequestService requestService;
    private String idDosen;

    public PermintaanMasukController(PanelPermintaanMasuk view, RequestService service, String idDosen) {
        this.view = view;
        this.requestService = service;
        this.idDosen = idDosen;
        
        initController();
    }
    
    private void initController() {
        // Pasang Action Listener ke View
        view.addRefreshListener(e -> loadData());
        view.addSetujuiListener(e -> actionSetujui());
        view.addTolakListener(e -> actionTolak());
        view.addDetailListener(e -> view.showDetailDialog(view.getSelectedData())); // Detail cukup di handle view/helper
        
        // Load data awal
        loadData();
    }
    
    private void loadData() {
        // Logika Auto-Clean (Opsional, dari pembahasan sebelumnya)
        new Thread(() -> {
            requestService.autoUpdateExpired(idDosen);
            SwingUtilities.invokeLater(this::fetchDataFromDB);
        }).start();
    }
    
    private void fetchDataFromDB() {
        new SwingWorker<ArrayList<Map<String, String>>, Void>() {
            @Override
            protected ArrayList<Map<String, String>> doInBackground() {
                try {
                    // Ambil semua data request dosen
                    Object result = requestService.getRequestDosen(idDosen);
                    if (result instanceof ArrayList) {
                        ArrayList<Map<String, String>> all = (ArrayList<Map<String, String>>) result;
                        ArrayList<Map<String, String>> pendingOnly = new ArrayList<>();
                        
                        // Filter manual: Hanya PENDING
                        for(Map<String, String> req : all) {
                            if("PENDING".equalsIgnoreCase(req.get("status"))) {
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
                    // Update View dengan data baru
                    view.updateTableData(get());
                } catch (Exception e) {
                    view.showMessage("Gagal memuat data: " + e.getMessage(), true);
                }
            }
        }.execute();
    }
    
    private void actionSetujui() {
        Map<String, String> data = view.getSelectedData();
        if (data == null) {
            view.showMessage("Pilih permintaan terlebih dahulu!", true);
            return;
        }
        
        String keterangan = view.showInputDialog("Keterangan Dosen", "Masukkan catatan tambahan (opsional):");
        if (keterangan == null) return; // Cancel
        if (keterangan.trim().isEmpty()) keterangan = "Pertemuan disetujui";
        
        String finalKet = keterangan;
        String idReq = data.get("id");
        
        // Eksekusi Update
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
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }
    
    private void actionTolak() {
        Map<String, String> data = view.getSelectedData();
        if (data == null) {
            view.showMessage("Pilih permintaan terlebih dahulu!", true);
            return;
        }
        
        String alasan = view.showInputDialog("Alasan Penolakan", "Masukkan alasan penolakan:");
        if (alasan == null) return;
        if (alasan.trim().isEmpty()) {
            view.showMessage("Alasan penolakan wajib diisi!", true);
            return;
        }
        
        String idReq = data.get("id");
        
        // Confirm dulu
        if (!view.showConfirmDialog("Yakin ingin menolak permintaan ini?")) return;
        
        // Eksekusi Update
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
                } catch (Exception e) { e.printStackTrace(); }
            }
        }.execute();
    }
}