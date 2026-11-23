import java.util.ArrayList;

// =====================================================================
// TokoBukuKotor.java (tahap 2) - Buku sudah dipecah per jenis (LSP + ISP).
// =====================================================================

class EmailSender {
    void kirim(String alamat, String isi) {
        System.out.println("[EMAIL " + alamat + "] " + isi);
    }
}

class FileRiwayat {
    void simpan(String baris) {
        System.out.println("[FILE riwayat.txt] " + baris);
    }
}

class Toko {
    ArrayList<Buku> keranjang = new ArrayList<>();
    EmailSender email = new EmailSender();
    FileRiwayat riwayat = new FileRiwayat();

    void tambah(Buku buku) {
        keranjang.add(buku);
    }

    String namaMetode(String metode) {
        if (metode.equals("TUNAI")) {
            return "Tunai";
        } else if (metode.equals("TRANSFER")) {
            return "Transfer (+Rp2.500)";
        } else if (metode.equals("EWALLET")) {
            return "E-Wallet (diskon 2%)";
        }
        return "?";
    }

    int totalBayar(String metode, int totalBelanja) {
        if (metode.equals("TUNAI")) {
            return totalBelanja;
        } else if (metode.equals("TRANSFER")) {
            return totalBelanja + 2500;
        } else if (metode.equals("EWALLET")) {
            return totalBelanja * 98 / 100;
        }
        return totalBelanja;
    }

    void checkout(String pelanggan, String alamatEmail, String metode,
                  BisaDikirim[] dikirim, BisaDiunduh[] unduhan) {
        System.out.println("================ STRUK =================");
        System.out.println("Pelanggan : " + pelanggan);
        int total = 0;
        for (Buku b : keranjang) {
            System.out.println(b.getJudul() + " (" + b.getPenulis() + ")");
            System.out.println("  Harga       : " + formatRupiah(b.getHarga()));
            System.out.println("  Keterangan  : " + b.keterangan());
            System.out.println("  Harga akhir : " + formatRupiah(b.hargaAkhir()));
            total += b.hargaAkhir();
        }
        int bayar = totalBayar(metode, total);
        System.out.println("-----------------------------------------");
        System.out.println("Total belanja : " + formatRupiah(total));
        System.out.println("Metode bayar  : " + namaMetode(metode));
        System.out.println("Total bayar   : " + formatRupiah(bayar));

        int totalBerat = 0;
        for (BisaDikirim d : dikirim) {
            totalBerat += d.getBeratGram();
        }
        System.out.println("Berat kiriman : " + totalBerat + " gram");

        System.out.println("-------------- LINK UNDUH ---------------");
        for (BisaDiunduh u : unduhan) {
            System.out.println(u.linkUnduh());
        }
        System.out.println("-----------------------------------------");

        riwayat.simpan(pelanggan + ";" + keranjang.size() + " buku;" + formatRupiah(bayar));
        email.kirim(alamatEmail, "Terima kasih " + pelanggan + ", total bayar " + formatRupiah(bayar));
    }

    String formatRupiah(int angka) {
        return String.format("Rp%,d", angka).replace(',', '.');
    }
}

public class TokoBukuKotor {
    public static void main(String[] args) {
        Toko toko = new Toko();
        BukuCetak cetak1 = new BukuCetak("Pemrograman Java", "John Doe", 120000, 750);
        BukuCetak cetak2 = new BukuCetak("Algoritma Dasar", "Jane Smith", 95000, 400);
        BukuDigital digital = new BukuDigital("Belajar OOP", "Ani Wijaya", 80000, 12.5);
        BukuAudio audio = new BukuAudio("Kisah Programmer", "Budi Santoso", 60000, 240);

        toko.tambah(cetak1);
        toko.tambah(cetak2);
        toko.tambah(digital);
        toko.tambah(audio);

        BisaDikirim[] dikirim = { cetak1, cetak2 };
        BisaDiunduh[] unduhan = { digital, audio };

        toko.checkout("Sari", "sari@mail.com", "EWALLET", dikirim, unduhan);
    }
}