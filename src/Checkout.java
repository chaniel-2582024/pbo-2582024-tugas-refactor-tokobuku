public class Checkout {
    private final StrukPrinter printer;
    private final PenyimpanRiwayat riwayat;
    private final Notifikasi notifikasi;

    public Checkout(StrukPrinter printer, PenyimpanRiwayat riwayat, Notifikasi notifikasi) {
        this.printer = printer;
        this.riwayat = riwayat;
        this.notifikasi = notifikasi;
    }

    public void proses(String pelanggan, String alamatEmail, Keranjang keranjang,
                       MetodeBayar metode, BisaDikirim[] dikirim, BisaDiunduh[] unduhan) {
        int total = keranjang.hitungTotal();
        int bayar = metode.totalBayar(total);

        int totalBerat = 0;
        for (BisaDikirim d : dikirim) {
            totalBerat += d.getBeratGram();
        }

        printer.cetakKeranjang(pelanggan, keranjang);
        printer.cetakPembayaran(total, metode, bayar);
        printer.cetakPengiriman(totalBerat, unduhan);

        riwayat.simpan(pelanggan + ";" + keranjang.getIsi().size() + " buku;" + Rupiah.format(bayar));
        notifikasi.kirim(alamatEmail, "Terima kasih " + pelanggan + ", total bayar " + Rupiah.format(bayar));
    }
}