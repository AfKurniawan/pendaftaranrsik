package id.rsi.klaten.Model;

public class Jadwal {

    String id_jadwal, id_dokter, id_poli, kd_dokter, kd_poli, nama_lengkap, nama_poli, hari, praktek, jam;
    public Jadwal(){}


    public Jadwal(String id_jadwal, String id_poli, String id_dokter, String kd_dokter,
                  String kd_poli, String nama_lengkap, String nama_poli, String hari, String praktek, String jam) {
        this.id_jadwal = id_jadwal;
        this.id_dokter = id_dokter;
        this.id_poli = id_poli;
        this.kd_dokter = kd_dokter;
        this.kd_poli = kd_poli;
        this.nama_lengkap = nama_lengkap;
        this.nama_poli = nama_poli;
        this.hari = hari;
        this.praktek = praktek;
        this.jam = jam;

    }

    public String getId_jadwal() {
        return id_jadwal;
    }

    public String getId_poli() {
        return id_poli;
    }

    public String getId_dokter(){
        return id_dokter;
    }

    public String getKd_dokter() {
        return kd_dokter;
    }

    public String getKd_poli() {
        return kd_poli;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getNama_poli(){
        return nama_poli;
    }

    public String getHari() {
        return hari;
    }

    public String getPraktek() {
        return praktek;
    }

    public String getJam() {
        return jam;
    }

   // SET VOID


    public void setId_jadwal(String id_jadwal) {
        this.id_jadwal = id_jadwal;
    }

    public void setId_poli(String id_poli) {
        this.id_poli = id_poli;
    }

    public void setId_dokter(String id_dokter) {
        this.id_dokter = id_dokter;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public void setNama_poli(String nama_poli) {
        this.nama_poli = nama_poli;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
