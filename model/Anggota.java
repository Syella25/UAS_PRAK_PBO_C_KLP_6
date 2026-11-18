





















































        this.nama = nama;
        this.jabatan = jabatan;
        this.idDivisi = idDivisi;
        this.username = username;
        this.status = status;
    }

    /**
     * Mengubah anggota menjadi satu baris teks untuk disimpan ke file.
     *
     * @return baris record anggota
     */
    public String toRecord() {
        return String.join(DataStorage.PEMISAH, id, DataStorage.bersih(nama),
                DataStorage.bersih(jabatan), idDivisi, username, status.name());
    }

    /**
     * Memeriksa apakah anggota terhubung ke akun member.
     *
     * @return true jika anggota terhubung ke akun member
     */
    public boolean punyaAkun() {
        return !TANPA_AKUN.equals(username);
    }

    /**
     * Mengambil kode anggota.
     *
     * @return kode anggota
     */
    public String getId() {
        return id;
    }

    /**
     * Mengambil nama anggota.
     *
     * @return nama anggota
     */
    public String getNama() {
        return nama;
    }

    /**
     * Mengubah nama.
     *
     * @param nama nama baru
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * Mengambil jabatan anggota.
     *
     * @return jabatan anggota
     */
    public String getJabatan() {
        return jabatan;
    }

    /**
     * Mengubah jabatan.
     *
     * @param jabatan jabatan baru
     */
    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    /**
     * Mengambil kode divisi anggota.
     *
     * @return kode divisi anggota
     */
    public String getIdDivisi() {
        return idDivisi;
    }

 /**
     * Mengubah kode divisi.
     *
     * @param idDivisi kode divisi baru
     */
    public void setIdDivisi(String idDivisi) {
        this.idDivisi = idDivisi;
    }

    /**
     * Mengambil username akun yang terhubung, atau {@link #TANPA_AKUN}.
     *
     * @return username akun yang terhubung, atau {@link #TANPA_AKUN}
     */
    public String getUsername() {
        return username;
    }

    /**
     * Mengambil status keanggotaan saat ini.
     *
     * @return status keanggotaan saat ini
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Mengubah status keanggotaan.
     *
     * @param status status keanggotaan baru
     */
    public void setStatus(Status status) {
        this.status = status;
    }
}
