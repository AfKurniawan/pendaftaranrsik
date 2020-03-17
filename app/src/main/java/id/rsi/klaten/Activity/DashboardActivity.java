package id.rsi.klaten.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.Utils.SessionManager;
import id.rsi.klaten.rsik.R;

public class DashboardActivity extends AppCompatActivity {

    CardView cvToday, cvTomorrow, cvExec, cvHistory;
    private ActionBar actionBar;
    private Toolbar toolbar;
    private SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sessionManager = new SessionManager(getApplicationContext());

        initCardView();
        initToolbar();
        initNavigationMenu();
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_dashboard);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setTitle("History Pendaftaran Pasien");
        getSupportActionBar().setTitle("");
        Tools.setSystemBarColor(this, R.color.green_700);
    }

    private void initNavigationMenu() {

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setItemIconTintList(null);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_history) {
                    Intent intent = new Intent(DashboardActivity.this, HistoryListActivity.class);
                    startActivity(intent);

                }else if (id == R.id.menu_hari_ini){
                    Intent intent = new Intent(DashboardActivity.this, PilihHariPoliReguler.class);
                    startActivity(intent);

                } else if(id == R.id.nav_exec) {

                    Intent intent = new Intent(DashboardActivity.this, PilihHariPoliEksekutif.class);
                    startActivity(intent);


                } else if (id == R.id.nav_all_jadwal_poli) {
                    Intent intent = new Intent(DashboardActivity.this, AllJadwalActivity.class);
                    startActivity(intent);

                } else if(id == R.id.nav_ganti_password) {

                    Intent intent = new Intent(DashboardActivity.this, GantiPasswordActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_home){

                    Intent intent = new Intent(DashboardActivity.this, DashboardActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_antrian){

                    Intent intent = new Intent(DashboardActivity.this, WebAntrianActivity.class);
                    startActivity(intent);



                }  else if (id == R.id.nav_logout){
                    logoutConfirmDialog();

                }

                drawer.closeDrawers();
                return true;
            }
        });
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

    private void logoutConfirmDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_logout_confirm);
        dialog.setCancelable(true);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_content)).setMovementMethod(LinkMovementMethod.getInstance());

        ((Button) dialog.findViewById(R.id.bt_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.clearSession();
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();

            }
        });

        ((Button) dialog.findViewById(R.id.bt_decline)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
