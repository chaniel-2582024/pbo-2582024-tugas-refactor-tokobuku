# Laporan Refactor Toko Buku

**Nama:** Chaniel Siregar  **NIM:** 2582024

Program awal `TokoBukuKotor.java` berjalan benar, tetapi melanggar kelima prinsip SOLID. Saya me-refactor-nya tanpa mengubah keluaran. Setelah setiap commit, keluaran program saya bandingkan dengan keluaran awal dan hasilnya selalu sama persis. Hasil akhirnya: satu berkas `.java` per class/interface, dengan `main` di class `TokoBukuSolid`.

---

## S — Single Responsibility Principle

**(a) Kode awal yang melanggar**

```java
class Toko {
    ArrayList<Buku> keranjang = new ArrayList<>();
    EmailSender email = new EmailSender();
    FileRiwayat riwayat = new FileRiwayat();
    ...
    void checkout(String pelanggan, String alamatEmail, String metode) {
        System.out.println("================ STRUK =================");
        ...
        riwayat.simpan(pelanggan + ";" + keranjang.size() + " buku;" + formatRupiah(bayar));
        email.kirim(alamatEmail, "Terima kasih " + pelanggan + ", total bayar " + formatRupiah(bayar));
    }
```

**(b) Masalahnya**

Class `Toko` mengerjakan terlalu banyak hal sekaligus: menyimpan keranjang, menghitung harga dan ongkir, mencetak struk, menyimpan riwayat, mengirim email, dan menentukan nama metode bayar. Padahal yang meminta perubahan bisa berbeda-beda: bagian keuangan (aturan harga), bagian desain (tampilan struk), dan tim IT (cara menyimpan riwayat atau mengirim email). Kalau semua mengedit class yang sama, satu perubahan bisa merusak bagian lain.

**(c) Hasil refactor**

`Keranjang` (menyimpan buku dan menjumlah), `StrukPrinter` (hanya tampilan), `Checkout` (hanya mengatur alur checkout), `Rupiah` (format uang). Aturan harga dan ongkir pindah ke `Buku` dan turunannya (`BukuCetak`, `BukuDigital`, `BukuAudio`).

---

## O — Open/Closed Principle

**(a) Kode awal yang melanggar**

```java
int hargaAkhir(Buku b) {
    switch (b.jenis) {
        case "CETAK":   return b.getHarga() + hitungOngkir(b);
        case "DIGITAL": return b.getHarga() * 80 / 100;
        case "AUDIO":   return b.getHarga() * 90 / 100;
        default:        return b.getHarga();
    }
}
...
int totalBayar(String metode, int totalBelanja) {
    if (metode.equals("TUNAI")) { ... }
    else if (metode.equals("TRANSFER")) { ... }
    else if (metode.equals("EWALLET")) { ... }
```

**(b) Masalahnya**

Setiap ada jenis buku baru atau metode bayar baru (misalnya QRIS), saya harus membuka dan mengubah `switch` dan `if/else` yang sama. `switch` itu juga muncul di beberapa tempat (`hargaAkhir`, `keterangan`, `namaMetode`, `totalBayar`), jadi kalau ada satu yang terlewat, programnya diam-diam salah.

**(c) Hasil refactor**

`Buku` menjadi abstract class dengan method abstract `hargaAkhir()` dan `keterangan()`, yang diisi oleh `BukuCetak`, `BukuDigital`, dan `BukuAudio`. Metode bayar menjadi interface `MetodeBayar` dengan implementasi `Tunai`, `Transfer`, dan `EWallet`. Sekarang jenis baru cukup berupa class baru, tanpa mengubah `Checkout` atau `StrukPrinter`.

---

## L — Liskov Substitution Principle

**(a) Kode awal yang melanggar**

```java
public int getBeratGram() {
    if (!jenis.equals("CETAK")) {
        throw new UnsupportedOperationException(judul + " tidak punya berat");
    }
    return beratGram;
}
```
(`linkUnduh()` dan `getDurasiMenit()` juga melempar `UnsupportedOperationException` dengan pola yang sama.)

**(b) Masalahnya**

`Buku` berjanji bahwa semua buku punya berat, link unduh, dan durasi. Padahal buku digital tidak punya berat dan buku cetak tidak bisa diunduh. Jadi pemanggil harus tahu jenis bukunya (itu sebabnya ada `if (b.jenis.equals("CETAK"))` di `checkout`). Kalau pemanggil salah memanggil, programnya langsung error di tengah jalan. Artinya anak tidak bisa menggantikan induknya dengan aman.

**(c) Hasil refactor**

Tidak ada lagi method yang melempar `UnsupportedOperationException`. `getBeratGram()` hanya ada di `BukuCetak`, dan `linkUnduh()` hanya ada di `BukuDigital` dan `BukuAudio`. `getDurasiMenit()` dihapus karena durasi hanya dipakai di dalam `BukuAudio.keterangan()`. Kontrak `MetodeBayar.totalBayar` juga saya tulis sebagai komentar: hasilnya tidak pernah negatif.

---

## I — Interface Segregation Principle

**(a) Kode awal yang melanggar**

```java
interface Produk {
    String getJudul();
    String getPenulis();
    int getHarga();
    int getBeratGram();      // hanya masuk akal untuk buku cetak
    String linkUnduh();      // hanya masuk akal untuk buku digital & audio
    int getDurasiMenit();    // hanya masuk akal untuk buku audio
}
```

**(b) Masalahnya**

Interface `Produk` terlalu gemuk. Setiap buku dipaksa punya enam method, padahal dua sampai tiga di antaranya tidak masuk akal untuk jenisnya. Karena dipaksa, method yang tidak cocok itu akhirnya diisi dengan melempar exception (ini juga akar masalah di prinsip L).

**(c) Hasil refactor**

`Produk` dihapus dan diganti dua interface kecil: `BisaDikirim` (hanya `getBeratGram()`, dipakai `BukuCetak`) dan `BisaDiunduh` (hanya `linkUnduh()`, dipakai `BukuDigital` dan `BukuAudio`). Karena itu `Checkout` menerima array `BisaDikirim[]` untuk menghitung berat dan `BisaDiunduh[]` untuk daftar link, tanpa `instanceof`.

---

## D — Dependency Inversion Principle

**(a) Kode awal yang melanggar**

```java
class Toko {
    ArrayList<Buku> keranjang = new ArrayList<>();
    EmailSender email = new EmailSender();
    FileRiwayat riwayat = new FileRiwayat();
```

**(b) Masalahnya**

`Toko` membuat sendiri class konkret `EmailSender` dan `FileRiwayat` dengan `new`. Akibatnya `Toko` terikat pada dua class itu. Kalau toko ingin mengirim SMS atau menyimpan riwayat ke database, saya harus membongkar isi `Toko`. Class ini juga sulit diuji karena langsung mencetak ke layar.

**(c) Hasil refactor**

Dibuat interface `PenyimpanRiwayat` (diimplementasi `FileRiwayat`) dan `Notifikasi` (diimplementasi `EmailNotifikasi`). `Checkout` menerima `StrukPrinter`, `PenyimpanRiwayat`, dan `Notifikasi` lewat constructor. Satu-satunya tempat yang memanggil `new` untuk class-class itu adalah `main` di `TokoBukuSolid`.

---

## Bagian yang sengaja tidak diberi abstraksi

Saya tidak membuat interface untuk `Rupiah`, `Keranjang`, dan `StrukPrinter`. `Rupiah` hanya fungsi pembantu kecil dengan satu cara format yang pasti tidak berubah. `Keranjang` hanya pembungkus daftar buku. `StrukPrinter` saat ini hanya punya satu bentuk struk, jadi membuat interface `Printer` sekarang justru menambah berkas tanpa manfaat nyata. Kalau suatu hari toko butuh struk PDF, interface baru bisa dibuat saat itu (prinsip YAGNI dan KISS).