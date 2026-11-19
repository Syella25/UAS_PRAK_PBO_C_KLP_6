


































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
<<<<<<< HEAD
 * @return daftar member
     */
    public List<Member> getSemuaMember() {
        List<Member> hasil = new ArrayList<>();
        for (Account akun : accounts.values()) {
            if (akun instanceof Member) {
                hasil.add((Member) akun);
            }
        }
        return hasil;
    }

    /**
     * Mencari nama lengkap dari username (dipakai untuk tampilan).
     *
     * @param username username akun
     * @return nama lengkap, atau username itu sendiri bila akun tidak ditemukan
     */
    public String namaDari(String username) {
        Account akun = accounts.get(username.toLowerCase());
        return akun == null ? username : akun.getNamaLengkap();
    }

    // ======================================================================
    // DIVISI
    // ======================================================================

    /**
     * Mengambil salinan daftar divisi.
     *
     * @return salinan daftar divisi
     */
    public List<Divisi> getDivisiList() {
        return new ArrayList<>(divisiList);
    }

    /**
     * Mencari divisi berdasarkan kode.
     *
     * @param id kode divisi (tidak membedakan huruf besar/kecil)
     * @return divisi yang ditemukan
     * @throws NoSuchElementException jika kode tidak ada
     */
    public Divisi cariDivisi(String id) {
        for (Divisi d : divisiList) {
            if (d.getId().equalsIgnoreCase(id.trim())) {
                return d;
            }
        }
        throw new NoSuchElementException("Divisi dengan kode '" + id + "' tidak ditemukan.");
    }

    /**
     * Menambah divisi baru dengan kode otomatis.
     *
     * @param admin     username admin pelaku
     * @param nama      nama divisi
     * @param deskripsi deskripsi divisi
     * @param kuota     kuota anggota
     * @return divisi yang dibuat
     * @throws IllegalArgumentException jika input tidak valid atau nama sudah ada
     */
    public Divisi tambahDivisi(String admin, String nama, String deskripsi, int kuota) {
        wajib(nama, "Nama divisi");
        if (kuota < 1) {
            throw new IllegalArgumentException("Kuota minimal 1.");
        }
        cekNamaDivisiUnik(nama, null);
        Divisi divisi = new Divisi(nextKode("D", idDivisi()), nama.trim(), deskripsi.trim(), kuota);
        divisiList.add(divisi);
        catatAdmin(admin, "TAMBAH DIVISI", divisi.getId() + " - " + divisi.getNama());
        return divisi;
    }

    /**
     * Mengubah data divisi.
     *
     * @param admin     username admin pelaku
     * @param id        kode divisi
     * @param nama      nama baru
=======
































































































































































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
>>>>>>> 17076f93e2e01bb7ba328604a7fef5d46da7a361
