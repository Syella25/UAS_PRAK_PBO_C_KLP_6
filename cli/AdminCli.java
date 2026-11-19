














































LoginCli.menu("8", "Riwayat Transaksi Member");
            LoginCli.menu("9", "Laporan Rekap");
            LoginCli.menu("0", "Logout");
            int pilihan = LoginCli.bacaInt("Pilih menu: ");
            try {
                switch (pilihan) {
                    case 1:
                        menuDivisi();
                        break;
                    case 2:
                        menuAnggota();
                        break;
                    case 3:
                        menuTugas();
                        break;
                    case 4:
                        verifikasiPendaftaran();
                        break;
                    case 5:
                        verifikasiTugas();
                        break;
                    case 6:
                        daftarMember();
                        break;
                    case 7:
                        riwayatAdmin();
                        break;
                    case 8:
                        riwayatMember();
                        break;
                    case 9:
                        laporanRekap();
                        break;
                    case 0:
                        return;
                    default:
                        LoginCli.error("Pilihan tidak tersedia.");
                }
            } catch (IllegalArgumentException | IllegalStateException | NoSuchElementException e) {
                LoginCli.error(e.getMessage());
            }
        }
    }

    // ---------------------------------------------------------------- DIVISI

    /** Menampilkan submenu kelola divisi (lihat, tambah, edit, hapus, urutkan). */
    private void menuDivisi() {
        while (true) {
            LoginCli.judul("KELOLA DIVISI");
            LoginCli.menu("1", "Lihat semua divisi");
            LoginCli.menu("2", "Tambah divisi");
            LoginCli.menu("3", "Edit divisi");
            LoginCli.menu("4", "Hapus divisi");
            LoginCli.menu("5", "Urutkan divisi (sisa kuota terbanyak)");
            LoginCli.menu("0", "Kembali");
            int pilihan = LoginCli.bacaInt("Pilih menu: ");
            try {
                switch (pilihan) {
                    case 1:
                        LoginCli.tabelDivisi(storage, storage.getDivisiList());
                        break;
                    case 2:
                        tambahDivisi();
                        break;
                    case 3:
                        editDivisi();
                        break;
                    case 4:
                        hapusDivisi();
                        break;
                    case 5:
                        LoginCli.info("Diurutkan dari sisa kuota terbanyak.");
                        LoginCli.tabelDivisi(storage, storage.urutkanDivisiBySisaKuota());
                        break;
                    case 0:
                        return;
                    default:
                        LoginCli.error("Pilihan tidak tersedia.");
                }