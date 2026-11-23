public class TokoBukuSolid {
    public static void main(String[] args) {
        BukuCetak cetak1 = new BukuCetak("Pemrograman Java", "John Doe", 120000, 750);
        BukuCetak cetak2 = new BukuCetak("Algoritma Dasar", "Jane Smith", 95000, 400);
        BukuDigital digital = new BukuDigital("Belajar OOP", "Ani Wijaya", 80000, 12.5);
        BukuAudio audio = new BukuAudio("Kisah Programmer", "Budi Santoso", 60000, 240);

        Keranjang keranjang = new Keranjang();
        keranjang.tambah(cetak1);
        keranjang.tambah(cetak2);
        keranjang.tambah(digital);
        keranjang.tambah(audio);

        BisaDikirim[] dikirim = { cetak1, cetak2 };
        BisaDiunduh[] unduhan = { digital, audio };

        Checkout checkout = new Checkout(new StrukPrinter(), new FileRiwayat(), new EmailNotifikasi());
        checkout.proses("Sari", "sari@mail.com", keranjang, new EWallet(), dikirim, unduhan);
    }
}