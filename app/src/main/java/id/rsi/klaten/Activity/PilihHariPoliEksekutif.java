package id.rsi.klaten.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.rsik.R;

public class PilihHariPoliEksekutif extends AppCompatActivity {

    CardView cvToday, cvTomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_hari_poli_eksekutif);

        initCardView();

        initToolbar();
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.green_700);
    }

    public void initCardView() {

        cvToday = findViewById(R.id.cv_today);
        cvToday.setVisibility(View.GONE);
        cvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PilihHariPoliEksekutif.this, JadwalPoliEksekutifHariIni.class);
                startActivity(i);
                finish();
            }
        });


        cvTomorrow = findViewById(R.id.cv_tomorrow);
        cvTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PilihHariPoliEksekutif.this, JadwalPoliEksekutifBesok.class);
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(PilihHariPoliEksekutif.this, JadwalPoliRegulerBesok.class);
            startActivity(i);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
