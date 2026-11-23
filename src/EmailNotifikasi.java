public class EmailNotifikasi implements Notifikasi {
    @Override
    public void kirim(String tujuan, String pesan) {
        System.out.println("[EMAIL " + tujuan + "] " + pesan);
    }
}