


























private String deskripsi;
    private final String idDivisi;
    private LocalDate deadline;
    private Status status;
    private String pengambil;

    /**
     * Membuat tugas baru berstatus TERSEDIA.
     *
     * @param id        kode unik tugas, misalnya "T01"
     * @param judul     judul tugas
     * @param deskripsi penjelasan tugas
     * @param idDivisi  kode divisi pemilik tugas
     * @param deadline  tenggat tugas
     */
    public Tugas(String id, String judul, String deskripsi, String idDivisi, LocalDate deadline) {
        this(id, judul, deskripsi, idDivisi, deadline, Status.TERSEDIA, BELUM_DIAMBIL);
    }

    /**
     * Membuat tugas lengkap (dipakai saat memuat dari file).
     *
     * @param id        kode unik tugas
     * @param judul     judul tugas
     * @param deskripsi penjelasan tugas
     * @param idDivisi  kode divisi pemilik tugas
     * @param deadline  tenggat tugas
     * @param status    status tugas
     * @param pengambil username member pengambil, atau {@link #BELUM_DIAMBIL}
     */
    public Tugas(String id, String judul, String deskripsi, String idDivisi, LocalDate deadline,
                 Status status, String pengambil) {
        this.id = id;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.idDivisi = idDivisi;
        this.deadline = deadline;
        this.status = status;
        this.pengambil = pengambil;
    }

    /**
     * Mengubah tugas menjadi satu baris teks untuk disimpan ke file.
     *
     * @return baris record tugas
     */
    public String toRecord() {
        return String.join(DataStorage.PEMISAH, id, DataStorage.bersih(judul), DataStorage.bersih(deskripsi),
                idDivisi, deadline.toString(), status.name(), pengambil);
    }

    /** Mengembalikan tugas ke status TERSEDIA tanpa pengambil. */
    public void lepaskan() {
        status = Status.TERSEDIA;
        pengambil = BELUM_DIAMBIL;
    }

    /**
     * Mengambil kode tugas.
     *
     * @return kode tugas
     */
    public String getId() {
        return id;
    }

    /**
     * Mengambil judul tugas.
     *
     * @return judul tugas
     */
    public String getJudul() {
        return judul;
    }

    /**
     * Mengubah judul.
     *
     * @param judul judul baru
     */
















































































     */
    public void setPengambil(String pengambil) {
        this.pengambil = pengambil;
    }
}