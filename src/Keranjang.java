import java.util.ArrayList;
import java.util.List;

public class Keranjang {
    private ArrayList<Buku> isi = new ArrayList<>();

    public void tambah(Buku buku) {
        isi.add(buku);
    }

    public List<Buku> getIsi() {
        return isi;
    }

    public int hitungTotal() {
        int total = 0;
        for (Buku b : isi) {
            total += b.hargaAkhir();
        }
        return total;
    }
}