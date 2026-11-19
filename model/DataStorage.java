


































private final List<Divisi> divisiList = new ArrayList<>();
    private final List<Anggota> anggotaList = new ArrayList<>();
    private final List<Tugas> tugasList = new ArrayList<>();
    private final List<RiwayatAdmin> riwayatAdminList = new ArrayList<>();
    private final List<RiwayatMember> riwayatMemberList = new ArrayList<>();

    /**
     * Membuat penyimpanan data pada folder tertentu.
     *
     * @param namaFolder nama folder file data (dibuat otomatis saat menyimpan)
     */
    public DataStorage(String namaFolder) {
        this.folder = Paths.get(namaFolder);
    }

    /**
     * Membersihkan teks dari karakter yang merusak format file (pemisah dan baris baru).
     *
     * @param teks teks mentah
     * @return teks yang aman disimpan
     */
    public static String bersih(String teks) {
        if (teks == null) {
            return "";
        }
        return teks.replace(PEMISAH, "/").replace("\n", " ").replace("\r", " ");
    }

    // ======================================================================
    // AKUN
    // ======================================================================

    /**
     * Memverifikasi username dan password.
     *
     * @param username username yang diinput
     * @param password password yang diinput
     * @return akun yang berhasil login (bisa {@link Admin} atau {@link Member})
     * @throws IllegalArgumentException jika username atau password salah
     */
    public Account login(String username, String password) {
        Account akun = accounts.get(username.trim().toLowerCase());
        if (akun == null || !akun.cekPassword(password)) {
            throw new IllegalArgumentException("Username atau password salah.");
        }
        return akun;
    }

    /**
     * Mendaftarkan akun member baru.
     *
     * @param username username baru
     * @param password password baru
     * @param nama     nama lengkap
     * @param nim      nomor induk mahasiswa
     * @param jurusan  jurusan/program studi
     * @return akun member yang dibuat
     * @throws IllegalArgumentException jika data tidak valid atau username sudah dipakai
     */
    public Member registrasiMember(String username, String password, String nama, String nim, String jurusan) {
        if (username.isEmpty() || username.contains(" ") || username.contains(PEMISAH) || username.length() < 3) {
            throw new IllegalArgumentException("Username minimal 3 karakter, tanpa spasi dan tanpa '|'.");
        }
        if (password.length() < 4 || password.contains(PEMISAH)) {
            throw new IllegalArgumentException("Password minimal 4 karakter dan tidak boleh mengandung '|'.");
        }
        wajib(nama, "Nama lengkap");
        wajib(nim, "NIM");
        wajib(jurusan, "Jurusan");
        if (accounts.containsKey(username.toLowerCase())) {
            throw new IllegalArgumentException("Username '" + username + "' sudah digunakan.");
        }
        Member member = new Member(username, password, nama.trim(), nim.trim(), jurusan.trim());
        accounts.put(username.toLowerCase(), member);
        return member;
    }

    /**
     * Mengambil semua akun yang berperan sebagai member.
     *
































































































































































    *
     * @return salinan daftar anggota aktif (yang sudah diverifikasi)
     */
    public List<Anggota> getAnggotaList() {
        return anggotaList.stream()
                .filter(a -> a.getStatus() == Anggota.Status.AKTIF)
                .collect(Collectors.toList());
    }

    /**
     * Mengambil anggota aktif pada satu divisi.
     *
     * @param idDivisi kode divisi
     * @return daftar anggota aktif divisi tersebut
     */
    public List<Anggota> getAnggotaByDivisi(String idDivisi) {
        return anggotaList.stream()
                .filter(a -> a.getStatus() == Anggota.Status.AKTIF && a.getIdDivisi().equalsIgnoreCase(idDivisi))
                .collect(Collectors.toList());
    }

    /**
     * Mencari anggota aktif berdasarkan kode.
     *
     * @param id kode anggota
     * @return anggota yang ditemukan
     * @throws NoSuchElementException jika kode tidak ada atau anggota belum aktif
     */
    public Anggota cariAnggota(String id) {
        for (Anggota a : anggotaList) {
            if (a.getStatus() == Anggota.Status.AKTIF && a.getId().equalsIgnoreCase(id.trim())) {
                return a;
            }
        }
        throw new NoSuchElementException("Anggota dengan kode '" + id + "' tidak ditemukan.");
    }

    /**
     * Mencari data keanggotaan/pendaftaran milik seorang member, apa pun statusnya.
     *
     * @param username username member
     * @return data anggota terkait, atau {@code null} jika member belum pernah mendaftar
     */
    public Anggota cariAnggotaByUsername(String username) {
        for (Anggota a : anggotaList) {
            if (a.punyaAkun() && a.getUsername().equalsIgnoreCase(username)) {
                return a;
            }
        }
        return null;
    }

    /**
     * Mencari data anggota aktif milik seorang member.
     *
     * @param username username member
     * @return anggota aktif terkait, atau {@code null} jika member belum resmi menjadi panitia
     */
    public Anggota cariAnggotaAktifByUsername(String username) {
        Anggota a = cariAnggotaByUsername(username);
        return (a != null && a.getStatus() == Anggota.Status.AKTIF) ? a : null;
    }

    /**
     * Menambah anggota secara manual oleh admin.
     *
     * @param admin    username admin pelaku
     * @param nama     nama anggota
     * @param jabatan  jabatan anggota
     * @param idDivisi kode divisi tujuan
     * @return anggota yang dibuat
     * @throws NoSuchElementException   jika divisi tidak ada
     * @throws IllegalArgumentException jika input kosong
     * @throws IllegalStateException    jika kuota divisi penuh
     */
    public Anggota tambahAnggota(String admin, String nama, String jabatan, String idDivisi) {
        wajib(nama, "Nama anggota");
        wajib(jabatan, "Jabatan");
        Divisi divisi = cariDivisi(idDivisi);
        cekKuota(divisi);
















































































     anggotaList.add(pendaftaran);
        }
        catatMember(username, "DAFTAR PANITIA", "Mendaftar ke divisi " + divisi.getNama() + " (menunggu verifikasi admin)");
        return pendaftaran;
    }

    /**
     * Mengambil daftar pendaftaran panitia yang menunggu verifikasi admin.
     *
     * @return daftar pendaftaran panitia yang menunggu verifikasi admin
     */
    public List<Anggota> getPendaftaranMenunggu() {
        return anggotaList.stream()
                .filter(a -> a.getStatus() == Anggota.Status.MENUNGGU)
                .collect(Collectors.toList());
    }

    /**
     * Mencari pendaftaran panitia yang sedang menunggu verifikasi.
     *
     * @param id kode pendaftaran (kode anggota)
     * @return pendaftaran yang ditemukan
     * @throws NoSuchElementException jika kode tidak ada atau pendaftaran tidak sedang menunggu
     */
    public Anggota cariPendaftaran(String id) {
        for (Anggota a : anggotaList) {
            if (a.getStatus() == Anggota.Status.MENUNGGU && a.getId().equalsIgnoreCase(id.trim())) {
                return a;
            }
        }
        throw new NoSuchElementException("Pendaftaran dengan kode '" + id + "' tidak ditemukan atau sudah diverifikasi.");
    }

    /**
     * Admin memverifikasi pendaftaran panitia. Jika diterima, member resmi menjadi anggota divisi (AKTIF);
     * jika ditolak, statusnya menjadi DITOLAK dan member boleh mendaftar ulang.
     *
     * @param admin  username admin pelaku
     * @param id     kode pendaftaran (kode anggota)
     * @param terima true untuk menerima, false untuk menolak
     * @throws NoSuchElementException jika pendaftaran tidak ditemukan
     * @throws IllegalStateException  jika diterima tetapi kuota divisi sudah penuh
     */
    public void verifikasiPendaftaran(String admin, String id, boolean terima) {
        Anggota pendaftaran = cariPendaftaran(id);
        Divisi divisi = cariDivisi(pendaftaran.getIdDivisi());
        String hasil;
        if (terima) {
            cekKuota(divisi);
            pendaftaran.setStatus(Anggota.Status.AKTIF);
            hasil = "DITERIMA";
        } else {
            pendaftaran.setStatus(Anggota.Status.DITOLAK);
            hasil = "DITOLAK";
        }
        catatAdmin(admin, "VERIFIKASI PANITIA", pendaftaran.getNama() + " -> divisi " + divisi.getNama() + " " + hasil);
        catatMember(pendaftaran.getUsername(), "VERIFIKASI PANITIA",
                "Pendaftaran ke divisi " + divisi.getNama() + " " + hasil);
    }

    // ======================================================================
    // TUGAS
    // ======================================================================

    /**
     * Mengambil salinan daftar seluruh tugas.
     *
     * @return salinan daftar seluruh tugas
     */
    public List<Tugas> getTugasList() {
        return new ArrayList<>(tugasList);
    }

    /**
     * Mengambil tugas pada satu divisi.
     *
     * @param idDivisi kode divisi
     * @return daftar tugas divisi tersebut
     */
    public List<Tugas> getTugasByDivisi(String idDivisi) {
















































































































































































































































        // ======================================================================
    // RIWAYAT
    // ======================================================================

    /**
     * Mengambil salinan seluruh riwayat aktivitas admin.
     *
     * @return salinan seluruh riwayat aktivitas admin
     */
    public List<RiwayatAdmin> getRiwayatAdmin() {
        return new ArrayList<>(riwayatAdminList);
    }

    /**
     * Mengambil salinan seluruh riwayat member (semua pengguna).
     *
     * @return salinan seluruh riwayat member (semua pengguna)
     */
    public List<RiwayatMember> getRiwayatMemberSemua() {
        return new ArrayList<>(riwayatMemberList);
    }

    /**
     * Mengambil riwayat milik satu member.
     *
     * @param username username member
     * @return daftar riwayat member tersebut
     */
    public List<RiwayatMember> getRiwayatMember(String username) {
        return riwayatMemberList.stream()
                .filter(r -> r.getUsername().equalsIgnoreCase(username))
                .collect(Collectors.toList());
    }

    /**
     * Mencatat aktivitas admin ke riwayat admin.
     *
     * @param admin  username admin pelaku
     * @param aksi   jenis aksi
     * @param detail keterangan aksi
     */
    private void catatAdmin(String admin, String aksi, String detail) {
        int id = riwayatAdminList.stream().mapToInt(RiwayatAdmin::getId).max().orElse(0) + 1;
        riwayatAdminList.add(new RiwayatAdmin(id, admin, aksi, detail));
    }

    /**
     * Mencatat transaksi member ke riwayat member.
     *
     * @param username username member terkait
     * @param aksi     jenis aksi
     * @param detail   keterangan aksi
     */
    private void catatMember(String username, String aksi, String detail) {
        int id = riwayatMemberList.stream().mapToInt(RiwayatMember::getId).max().orElse(0) + 1;
        riwayatMemberList.add(new RiwayatMember(id, username, aksi, detail));
    }

    // ======================================================================
    // PENYIMPANAN FILE
    // ======================================================================

    /**
     * Membaca seluruh data dari file di folder penyimpanan, lalu menyiapkan data awal bila masih kosong
     * (akun admin bawaan dan beberapa divisi contoh).
     *
     * @return true jika semua file berhasil dibaca, false jika terjadi kesalahan baca
     */
    public boolean muat() {
        boolean berhasil = true;
        try {
            accounts.clear();
            divisiList.clear();
            anggotaList.clear();
            tugasList.clear();
            riwayatAdminList.clear();
            riwayatMemberList.clear();

            for (String[] p : bacaBaris("akun.txt")) {

             *
     * @param baris isi laporan, satu elemen per baris
     * @return lokasi file laporan, atau {@code null} jika gagal menyimpan
     */
    public Path simpanLaporan(List<String> baris) {
        try {
            Files.createDirectories(folder);
            Path file = folder.resolve("laporan.txt");
            Files.write(file, baris, StandardCharsets.UTF_8);
            return file;
        } catch (IOException e) {
            System.err.println("Gagal menyimpan laporan: " + e.getMessage());
            return null;
        }
    }

    /**
     * Membaca file teks menjadi daftar baris yang sudah dipecah per kolom.
     *
     * @param namaFile nama file di folder penyimpanan
     * @return daftar baris (kosong jika file belum ada)
     * @throws IOException jika file gagal dibaca
     */
    private List<String[]> bacaBaris(String namaFile) throws IOException {
        List<String[]> hasil = new ArrayList<>();
        Path file = folder.resolve(namaFile);
        if (!Files.exists(file)) {
            return hasil;
        }
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String baris;
            while ((baris = reader.readLine()) != null) {
                if (!baris.isBlank()) {
                    hasil.add(baris.split("\\|", -1));
                }
            }
        }
        return hasil;
    }

    /**
     * Menulis daftar baris ke file teks (menimpa isi lama).
     *
     * @param namaFile nama file di folder penyimpanan
     * @param baris    isi file, satu elemen per baris
     * @throws IOException jika file gagal ditulis
     */
    private void tulis(String namaFile, List<String> baris) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(folder.resolve(namaFile), StandardCharsets.UTF_8)) {
            for (String b : baris) {
                writer.write(b);
                writer.newLine();
            }
        }
    }

    /** Menyiapkan akun admin bawaan dan beberapa divisi contoh jika data masih kosong. */
    private void siapkanDataAwal() {
        boolean adaAdmin = accounts.values().stream().anyMatch(a -> a instanceof Admin);
        if (!adaAdmin) {
            accounts.put("admin", new Admin("admin", "admin123", "Administrator"));
        }
        if (divisiList.isEmpty()) {
            divisiList.add(new Divisi("D01", "Acara", "Menyusun rundown dan menjalankan acara", 5));
            divisiList.add(new Divisi("D02", "Humas", "Relasi sponsor dan media partner", 4));
            divisiList.add(new Divisi("D03", "Konsumsi", "Penyediaan makanan dan minuman", 3));
        }
    }

    // ======================================================================
    // HELPER PRIVAT
    // ======================================================================

    /**
     * Memastikan sebuah input tidak kosong.
     *
     * @param teks      input yang diperiksa
     * @param namaField nama kolom untuk pesan kesalahan
     * @throws IllegalArgumentException jika input kosong
     */
