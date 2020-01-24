package id.rsi.klaten.Model;

public class Booking {

    String id_booking, nama_pasien, no_rm, no_antrian, no_barcode, penjamin, nama_poli, nama_lengkap, tgl_booking, hari, jam;
    public Booking(){}


    public Booking(String id_booking, String nama_pasien, String no_rm, String no_antrian, String no_barcode, String penjamin, String nama_poli, String nama_lengkap, String tgl_booking, String hari, String jam) {
        this.id_booking = id_booking;
        this.nama_pasien = nama_pasien;
        this.no_rm = no_rm;
        this.no_antrian = no_antrian;
        this.no_barcode = no_barcode;
        this.penjamin = penjamin;
        this.nama_poli = nama_poli;
        this.nama_lengkap = nama_lengkap;
        this.tgl_booking = tgl_booking;
        this.jam = jam;
        this.hari = hari;


    }

    public String getId_booking() {
        return id_booking;
    }

    public String getNama_pasien() {
        return nama_pasien;
    }

    public String getNo_rm() {
        return no_rm;
    }

    public String getNo_antrian() {
        return no_antrian;
    }

    public String getNo_barcode() {
        return no_barcode;
    }

    public String getPenjamin(){
        return penjamin;
    }

    public String getNama_poli() {
        return nama_poli;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getTgl_booking() {
        return tgl_booking;
    }

    public String getJam() {
        return jam;
    }

    public String getHari() {
        return hari;
    }

    // SET VOID


    public void setId_booking(String id_booking) {
        this.id_booking = id_booking;
    }

    public void setNama_pasien(String id_jadwal) {
        this.nama_pasien = nama_pasien;
    }

    public void setNo_rm(String no_rm) {
        this.no_rm = no_rm;
    }

    public void setNo_antrian(String no_rm) {
        this.no_antrian = no_antrian;
    }

    public void setNo_barcode(String no_barcode) {
        this.no_barcode = no_barcode;
    }

    public void setPenjamin(String penjamin) {
        this.penjamin = penjamin;
    }

    public void setNama_poli(String nama_poli) {
        this.nama_poli = nama_poli;
    }

    public void setNama_lengkap(String nama_lengkap) {
        this.nama_lengkap = nama_lengkap;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }
}
