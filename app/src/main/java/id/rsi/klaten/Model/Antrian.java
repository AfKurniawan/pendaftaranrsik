package id.rsi.klaten.Model;

public class Antrian {

    String no_antrian, nama_pasien, /*no_rm,*/ penjamin, tgl_kontrol;

    public Antrian(){}
    public Antrian(String no_antrian, String nama_pasien, /*String no_rm,*/ String penjamin, String tgl_kontrol){

        this.no_antrian = no_antrian;
        this.nama_pasien = nama_pasien;
        //this.no_rm = no_rm;
        this.penjamin = penjamin;
        this.tgl_kontrol = tgl_kontrol;

    }

    public String getNo_antrian() {
        return no_antrian;
    }

    public String getNama_pasien(){
        return nama_pasien;
    }

   /* public String getNo_rm() {
        return no_rm;
    }*/

    public String getPenjamin() {
        return penjamin;
    }

    public String getTgl_kontrol() {
        return tgl_kontrol;
    }

    public void setNo_antrian(String no_antrian) {
        this.no_antrian = no_antrian;
    }

    public void setNama_pasien(String nama_pasien) {
        this.nama_pasien = nama_pasien;
    }

   /* public void setNo_rm(String no_rm) {
        this.no_rm = no_rm;
    }*/

    public void setTgl_kontrol(String tgl_kontrol) {
        this.tgl_kontrol = tgl_kontrol;
    }
}
