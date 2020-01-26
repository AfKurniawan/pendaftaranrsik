package id.rsi.klaten.Model;

public class User {

    private String alamat_email, nama_lengkap, no_telpon, alamat_lengkap;


    public User(String alamat_email, String nama_lengkap, String no_telpon, String alamat_lengkap) {
        this.alamat_email = alamat_email;
        this.nama_lengkap = nama_lengkap;
        this.no_telpon = no_telpon;
        this.alamat_lengkap = alamat_lengkap;


    }

    public String getAlamat_email() {
        return alamat_email;
    }

    public String getNama_lengkap() {
        return nama_lengkap;
    }

    public String getAlamat_lengkap() {
        return alamat_lengkap;
    }

    public String getNo_telp() {
        return no_telpon;
    }

}
