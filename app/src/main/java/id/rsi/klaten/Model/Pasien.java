package id.rsi.klaten.Model;

public class Pasien {

    String no_reg, nama, jenis_kel, alamat;

    public Pasien(){}
    public Pasien(String no_reg, String nama, String jenis_kel, String alamat){

        this.no_reg = no_reg;
        this.nama = nama;
        this.jenis_kel = jenis_kel;
        this.alamat = alamat;

    }

    public String getNo_reg() {
        return no_reg;
    }

    public String getNama(){
        return nama;
    }

    public String getJenis_kelamin() {
        return jenis_kel;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setNo_reg(String no_reg) {
        this.no_reg = no_reg;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kel = jenis_kelamin;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
