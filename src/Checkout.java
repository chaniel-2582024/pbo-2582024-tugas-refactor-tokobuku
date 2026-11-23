public class Checkout {
    private StrukPrinter printer = new StrukPrinter();
    private EmailSender email = new EmailSender();
    private FileRiwayat riwayat = new FileRiwayat();

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
        email.kirim(alamatEmail, "Terima kasih " + pelanggan + ", total bayar " + Rupiah.format(bayar));
    }
}