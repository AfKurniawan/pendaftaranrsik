package id.rsi.klaten.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import id.rsi.klaten.rsik.R;

public class PilihHariPoliReguler extends AppCompatActivity {

    CardView cvToday, cvTomorrow;
    private ActionBar actionBar;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_hari_poli_reguler);

        initCardView();


    }


    public void initCardView() {

        cvToday = findViewById(R.id.cv_today);
        cvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PilihHariPoliReguler.this, JadwalPoliRegulerHariIni.class);
                startActivity(i);
            }
        });


        cvTomorrow = findViewById(R.id.cv_tomorrow);
        cvTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PilihHariPoliReguler.this, JadwalPoliRegulerBesok.class);
                startActivity(i);
            }
        });

    }
}