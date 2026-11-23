public class StrukPrinter {
    public void cetakKeranjang(String pelanggan, Keranjang keranjang) {
        System.out.println("================ STRUK =================");
        System.out.println("Pelanggan : " + pelanggan);
        for (Buku b : keranjang.getIsi()) {
            System.out.println(b.getJudul() + " (" + b.getPenulis() + ")");
            System.out.println("  Harga       : " + Rupiah.format(b.getHarga()));
            System.out.println("  Keterangan  : " + b.keterangan());
            System.out.println("  Harga akhir : " + Rupiah.format(b.hargaAkhir()));
        }
    }

    public void cetakPembayaran(int totalBelanja, MetodeBayar metode, int totalBayar) {
        System.out.println("-----------------------------------------");
        System.out.println("Total belanja : " + Rupiah.format(totalBelanja));
        System.out.println("Metode bayar  : " + metode.nama());
        System.out.println("Total bayar   : " + Rupiah.format(totalBayar));
    }

    public void cetakPengiriman(int totalBerat, BisaDiunduh[] unduhan) {
        System.out.println("Berat kiriman : " + totalBerat + " gram");

        System.out.println("-------------- LINK UNDUH ---------------");
        for (BisaDiunduh u : unduhan) {
            System.out.println(u.linkUnduh());
        }
        System.out.println("-----------------------------------------");
    }
}