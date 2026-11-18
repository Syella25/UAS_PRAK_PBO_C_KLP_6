


 */
    @Override
    public String getInfo() {
        return "Admin - dapat mengelola divisi, anggota, tugas, serta memverifikasi pendaftaran dan tugas.";
    }

    /**
     * Mengubah akun admin menjadi satu baris teks untuk disimpan ke file.
     *
     * @return baris record akun
     */
    @Override
    public String toRecord() {
        return recordDasar();
    }
}
