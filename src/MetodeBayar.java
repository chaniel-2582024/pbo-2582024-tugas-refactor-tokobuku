public interface MetodeBayar {
    String nama();

    // KONTRAK totalBayar: hasilnya tidak pernah negatif
    // (selama totalBelanja tidak negatif).
    int totalBayar(int totalBelanja);
}