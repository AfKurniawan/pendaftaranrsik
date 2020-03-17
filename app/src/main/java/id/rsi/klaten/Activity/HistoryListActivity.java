package id.rsi.klaten.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.rsi.klaten.Adapter.HistoryAdapter;
import id.rsi.klaten.Model.Booking;
import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.SessionManager;
import id.rsi.klaten.Utils.VolleySingleton;
import id.rsi.klaten.rsik.R;

import static android.view.View.GONE;

public class HistoryListActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<Booking> mItems;
    Context mContext;

    private View parent_view;

    private ProgressDialog dialog = null;
    ProgressBar pb;
    RequestQueue queue;

    EditText etEmail, etStatus, etIdBooking, etStatusDelete;
    private SessionManager sessionManager;
    private ActionBar actionBar;
    private Toolbar toolbar;
    TextView tvDataKosong;
    LinearLayout ll;
   /* private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_list);
        parent_view = findViewById(android.R.id.content);

        sessionManager = new SessionManager(getApplicationContext());

        etEmail = findViewById(R.id.et_email);


        etEmail.setText(sessionManager.getSavedUserName());
        etEmail.setVisibility(GONE);





        etStatus = findViewById(R.id.et_status);
        etStatus.setText("1");
        etStatus.setVisibility(GONE);


        pb = findViewById(R.id.history_progressbar);

        tvDataKosong = findViewById(R.id.tv_data_kosong);
        //tvDataKosong.setVisibility(GONE);

        ll = findViewById(R.id.ll_nodata);
        //ll.setVisibility(GONE);

        initToolbar();

        initRecycleView();




        //getIdBooking();

        initNavigationMenu();



        initProgressBar();

        getHistoryData();





    }

    public void initProgressBar(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Tunggu Sebentar...");
        dialog.setCancelable(false);

    }



    public void initRecycleView(){

        mRecyclerView = findViewById(R.id.recyclerView);
        mItems = new ArrayList<>();

        mManager = new LinearLayoutManager(HistoryListActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new HistoryAdapter(HistoryListActivity.this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        //actionBar.setTitle("History Pendaftaran Pasien");
        getSupportActionBar().setTitle(R.string.toolbar_history_list);
        Tools.setSystemBarColor(this, R.color.overlay_dark_40);
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
                    Intent intent = new Intent(HistoryListActivity.this, HistoryListActivity.class);
                    startActivity(intent);

                }else if (id == R.id.menu_hari_ini){
                    Intent intent = new Intent(HistoryListActivity.this, PilihHariPoliReguler.class);
                    startActivity(intent);

                } else if(id == R.id.nav_exec) {

                    Intent intent = new Intent(HistoryListActivity.this, PilihHariPoliEksekutif.class);
                    startActivity(intent);


                } else if (id == R.id.nav_all_jadwal_poli) {
                    Intent intent = new Intent(HistoryListActivity.this, AllJadwalActivity.class);
                    startActivity(intent);

                } else if(id == R.id.nav_ganti_password) {

                    Intent intent = new Intent(HistoryListActivity.this, GantiPasswordActivity.class);
                    startActivity(intent);

                } else if (id == R.id.nav_home){

                    Intent intent = new Intent(HistoryListActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();

                } else if (id == R.id.nav_antrian){

                    Intent intent = new Intent(HistoryListActivity.this, WebAntrianActivity.class);
                    startActivity(intent);



                }  else if (id == R.id.nav_logout){
                    logoutConfirmDialog();

                }

                drawer.closeDrawers();
                return true;
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
                // Toast.makeText(getApplicationContext(), "Button Accept Clicked", Toast.LENGTH_SHORT).show();
                sessionManager.clearSession();
                Intent intent = new Intent(HistoryListActivity.this, LoginActivity.class);
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






    public void getIdBooking(){

        etIdBooking = findViewById(R.id.tmp_et_id_booking);
        etIdBooking.setVisibility(GONE);

        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);

        String intent_idBooking = data.getStringExtra("id_booking");


        if (update == 1) {
            etIdBooking.setText(intent_idBooking);

            /*new AlertDialog.Builder(this)
                    .setTitle("Hapus")
                    .setMessage("Anda yakin akan menghapus pendaftaran pasien ?")
                    //.setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {


                            updateStatusBooking();





                        }})
                    .setNegativeButton(android.R.string.no, null).show();*/






        }



    }




    private void updateStatusBooking() {

        pb.setVisibility(View.VISIBLE);


        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.UPDATE_STATUS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pb.setVisibility(GONE);
                        dialog.dismiss();

                        try {

                            JSONObject obj = new JSONObject(response);

                            if (response.equalsIgnoreCase("success")) {

                                Toast.makeText(getApplicationContext(), "Pendaftaran Pasien sudah dihapus", Toast.LENGTH_LONG).show();


                            } else {

                                //pb.setVisibility(GONE);

                                ll.setVisibility(View.GONE);
                                tvDataKosong.setVisibility(View.GONE);


                                JSONArray poliArray = obj.getJSONArray("data");

                                for (int i = 0; i < poliArray.length(); i++) {

                                    JSONObject hObject = poliArray.getJSONObject(i);


                                    Booking bk = new Booking(

                                            hObject.getString("id_booking"),
                                            hObject.getString("nama_pasien"),
                                            hObject.getString("no_rm"),
                                            hObject.getString("no_antrian"),
                                            hObject.getString("penjamin"),
                                            hObject.getString("nama_poli"),
                                            hObject.getString("nama_lengkap"),
                                            hObject.getString("tgl_booking")

                                    );

                                    mItems.add(bk);
                                }

                                mRecyclerView.setAdapter(mAdapter);
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       pb.setVisibility(View.GONE);
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("id_booking", etIdBooking.getText().toString());


                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(HistoryListActivity.this);

        requestQueue.add(stringRequest);*/

    }

    private void getHistoryData() {

        final String email = etEmail.getText().toString();
        final String status = etStatus.getText().toString();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        pb.setVisibility(View.VISIBLE);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        pb.setVisibility(GONE);



                            try {

                                JSONObject obj = new JSONObject(response);

                                if (response.equalsIgnoreCase("{\"error\":true}")) {

                                    Toast.makeText(getApplicationContext(), "Belum ada history", Toast.LENGTH_LONG).show();
                                    ll.setVisibility(View.VISIBLE);
                                    tvDataKosong.setVisibility(View.VISIBLE);

                                } else {



                                    ll.setVisibility(View.GONE);
                                    tvDataKosong.setVisibility(View.GONE);


                                    JSONArray poliArray = obj.getJSONArray("data");

                                    for (int i = 0; i < poliArray.length(); i++) {

                                        JSONObject hObject = poliArray.getJSONObject(i);


                                        Booking bk = new Booking(

                                                hObject.getString("id_booking"),
                                                hObject.getString("nama_pasien"),
                                                hObject.getString("no_rm"),
                                                hObject.getString("no_antrian"),
                                                hObject.getString("no_barcode"),
                                                hObject.getString("penjamin"),
                                                hObject.getString("jns_daftar"),
                                                hObject.getString("nama_poli"),
                                                hObject.getString("nama_lengkap"),
                                                hObject.getString("tgl_booking"),
                                                hObject.getString("hari"),
                                                hObject.getString("jam")

                                        );

                                        mItems.add(bk);
                                    }

                                    mRecyclerView.setAdapter(mAdapter);
                                    mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Server tidak merespon", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("status_muncul", status);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_setting, menu);
        return true;
    }*/

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
