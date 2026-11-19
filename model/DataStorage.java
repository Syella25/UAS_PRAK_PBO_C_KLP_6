


































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