package id.rsi.klaten.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.*;

import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.rsik.R;
import net.glxn.qrgen.android.QRCode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailHistoryActivity extends AppCompatActivity {

    EditText etNomorAntrian, etNamaPasien, etNamaPoli, etNamaDokter, etTglKontrol;
    ImageView ivBarcode;
    TextView tvSyarat, tvNamaPasien, tvNoRm, tvHari, tvSyaratBpjs, tvPenjamin, tvJnsDaftar, tvNoBarcode, tvSyaratSurat, tvSyaratKtp;
    RequestQueue queue;
    ProgressBar pb;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    public static final String PREFS_NAME = "LoginPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);

        initComponent();
        getAntrian();
        generateBarcode();
        initSharedPreferences();
        initToolbar();
        //initDateTomorrow();


    }


    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("DETAIL PENDAFTARAN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.green_A400);
    }




    public void initSharedPreferences(){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences("rsiklaten", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

    }


    public void initComponent(){

        etNomorAntrian = findViewById(R.id.et_nomor_antri);
        tvNamaPasien = findViewById(R.id.et_nama_pasien);
        tvNoRm =findViewById(R.id.tv_no_rm);
        tvNoBarcode = findViewById(R.id.tv_no_barcode);
        tvNoBarcode.setVisibility(View.GONE);
        etNamaPoli = findViewById(R.id.et_nama_poli);
        etNamaDokter = findViewById(R.id.et_nama_dokter);
        tvSyarat = findViewById(R.id.tv_syarat);
        tvHari = findViewById(R.id.et_jadwal_history);
        tvPenjamin = findViewById(R.id.tv_penjamin);
        tvJnsDaftar = findViewById(R.id.tv_jns_daftar);
        tvSyaratBpjs = findViewById(R.id.tv_syarat_bpjs);
        tvSyaratSurat = findViewById(R.id.tv_syarat_surat);
        tvSyaratKtp = findViewById(R.id.tv_syarat_ktp);







    }

   /* public void initDateTomorrow() {

        tvSyarat = findViewById(R.id.tv_syarat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggal = df.format(tomorrow);
        String[] days = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)];
        tvSyarat.setText("Anda akan dilayani pada hari " + day +"," + tanggal + ", Tunjukkan Barcode dan Nomor Antrian ini kepada Petugas pendaftaran.");

    }*/


    public void getAntrian() {

        etNomorAntrian = findViewById(R.id.et_nomor_antri);

        Intent data = getIntent();

        final int update = data.getIntExtra("update", 0);

        String intentAntrian = data.getStringExtra("no_antrian");
        String intentBarcode = data.getStringExtra("no_barcode");
        String intentNamaPasien = data.getStringExtra("nama_pasien");
        String intentPenjamin = data.getStringExtra("penjamin");
        String intentJnsDaftar = data.getStringExtra("jns_daftar");
        String intentNoRm = data.getStringExtra("no_rm");
        String intentNamaPoli = data.getStringExtra("nama_poli");
        String intentNamaDokter = data.getStringExtra("nama_lengkap");
        String intentTglBooking = data.getStringExtra("tgl_booking");
        String intentHari = data.getStringExtra("hari");
        String intentJam = data.getStringExtra("jam");


        etNomorAntrian.setText(intentAntrian);
        tvNamaPasien.setText(intentNamaPasien);
        tvPenjamin.setText(intentPenjamin);
        tvNoRm.setText(intentNoRm);
        tvNoBarcode.setText(intentBarcode);
        etNamaPoli.setText(intentNamaPoli);
        etNamaDokter.setText(intentNamaDokter);
        tvHari.setText(intentHari + ", " + intentJam);



        if (intentPenjamin.equals("Pilih Penjamin")){

            tvPenjamin.setVisibility(View.GONE);
            tvJnsDaftar.setText("Poli "+ intentJnsDaftar);

        } else {

            tvJnsDaftar.setVisibility(View.GONE);
        }

        //String intentPenjaminOffice = data.getStringExtra("penjamin");

        if (intentPenjamin.equals("BPJS")) {

            tvSyarat.setText("Anda akan dilayani pada hari "+ intentHari+", "+ intentJam +", Tanggal, " +intentTglBooking + ", Tunjukkan Barcode dan Nomor Antrian ini kepada Petugas Front Office.");
            tvSyaratBpjs.setText("1. Kartu BPJS");
            tvSyaratKtp.setText("2. KTP");
            tvSyaratSurat.setText("3. Surat Kontrol");

        } else {


            tvSyarat.setText("Anda akan dilayani pada hari "+ intentHari+", "+ intentJam +", Tanggal, " + intentTglBooking + ", Tunjukkan Barcode dan Nomor Antrian ini kepada Petugas Pendaftaran.");
            tvSyaratBpjs.setVisibility(View.GONE);
            tvSyaratKtp.setText("1. KTP");
            tvSyaratSurat.setText("2. Surat Kontrol");
        }

    }




    private void generateBarcode(){

        ivBarcode = findViewById(R.id.qr_code2);
        String stNomor = tvNoBarcode.getText().toString();
        Bitmap myBitmap = QRCode.from(stNomor).bitmap();
        ivBarcode.setImageBitmap(myBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


}
