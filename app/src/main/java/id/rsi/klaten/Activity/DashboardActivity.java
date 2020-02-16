package id.rsi.klaten.Activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.rsik.R;

public class DashboardActivity extends AppCompatActivity {

    CardView cvToday, cvTomorrow, cvExec, cvHistory;
    private ActionBar actionBar;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initCardView();
        //initToolbar();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //actionBar = getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeButtonEnabled(true);
        //actionBar.setTitle("Halaman Utama");
        Tools.setSystemBarColor(this, R.color.overlay_dark_40);
    }

    public void initCardView(){

        cvToday = findViewById(R.id.cv_pilih_jadwal);
        cvToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, PilihHariPoliReguler.class);
                startActivity(i);
            }
        });


        cvExec = findViewById(R.id.cv_exec);
        cvExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, PilihHariPoliEksekutif.class);
                startActivity(i);
            }
        });

        cvHistory = findViewById(R.id.cv_history);
        cvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(DashboardActivity.this, HistoryListActivity.class);
                startActivity(i);

            }
        });
    }
}
