





































































































































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