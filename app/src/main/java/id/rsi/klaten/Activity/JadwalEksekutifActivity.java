package id.rsi.klaten.Activity;

import android.app.Dialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import id.rsi.klaten.Adapter.JadwalAdapter;
import id.rsi.klaten.Model.Jadwal;
import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.SessionManager;
import id.rsi.klaten.Utils.VolleySingleton;
import id.rsi.klaten.rsik.R;


public class JadwalEksekutifActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<Jadwal> mItems;
    Context mContext;
    private ActionBar actionBar;
    private Toolbar toolbar;

    ProgressBar progressBar;
    EditText etFilterHari, etEmail, etVersion;
    TextView textDialogUpdate;
    RequestQueue queue;
    private SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jadwal_eksekutif);

        mSwipeRefreshLayout = findViewById(R.id.swipe_container);

        sessionManager = new SessionManager(getApplicationContext());

        mRecyclerView = findViewById(R.id.recyclerView);
        mItems = new ArrayList<>();

        mManager = new LinearLayoutManager(JadwalEksekutifActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mAdapter = new JadwalAdapter(JadwalEksekutifActivity.this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);

        progressBar = findViewById(R.id.jadwal_progressbar);
        etFilterHari = findViewById(R.id.filter_day);
        etFilterHari.setVisibility(View.GONE);


        etEmail =findViewById(R.id.et_email);
        etEmail.setText(sessionManager.getSavedUserName());
        etEmail.setVisibility(View.GONE);

        etVersion = findViewById(R.id.et_version);
        etVersion.setVisibility(View.GONE);




        initSwipeRefresh();

        initToolbar();

        initJadwalBesok();

        initJadwalByDay();

        initNavigationMenu();

        cekVersion();




    }


    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Jadwal Poli Eksekutif");
        Tools.setSystemBarColor(this, R.color.overlay_dark_40);
    }

    public void initSwipeRefresh(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.

                cekVersion();
                mRecyclerView.setVisibility(View.GONE);
            }
        });
        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_purple,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
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
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.nav_history) {
                    Intent intent = new Intent(JadwalEksekutifActivity.this, HistoryListActivity.class);
                    startActivity(intent);

                }else if (id == R.id.menu_hari_ini){
                    Intent intent = new Intent(JadwalEksekutifActivity.this, JadwalPoliHariIni.class);
                    startActivity(intent);


                } else if (id == R.id.nav_all_jadwal_poli) {
                    Intent intent = new Intent(JadwalEksekutifActivity.this, AllJadwalActivity.class);
                    startActivity(intent);

                } else if(id == R.id.nav_ganti_password){

                    Intent intent = new Intent(JadwalEksekutifActivity.this, GantiPasswordActivity.class);
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
                sessionManager.clearSession();
                Intent intent = new Intent(JadwalEksekutifActivity.this, LoginActivity.class);
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






    public void initJadwalBesok() {

        TextView tvTanggal = findViewById(R.id.tv_today);

        /*TODAY*/
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, 0);
        Date hariini = today.getTime();

        SimpleDateFormat sdfToday = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggalHariIni = sdfToday.format(hariini);

        String[] sekarangs = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String Harihariini = sekarangs[today.get(Calendar.DAY_OF_WEEK)];

        /*BESOK*/
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        Date haribesok = tomorrow.getTime();

        SimpleDateFormat sdfHariIni = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggalBesok = sdfHariIni.format(haribesok);

        String[] besoks = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String hariBesok = besoks[tomorrow.get(Calendar.DAY_OF_WEEK)];



        /*LUSA*/
        Calendar lusa = Calendar.getInstance();
        lusa.add(Calendar.DAY_OF_YEAR, 2);
        Date besokLusa = lusa.getTime();

        SimpleDateFormat dfsenin = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggalLusa = dfsenin.format(besokLusa);

        String[] lusas = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String hariLusa = lusas[lusa.get(Calendar.DAY_OF_WEEK)];




        if (Harihariini == "Sabtu") {

            tvTanggal.setText("Senin, " + tanggalLusa);



        }else if(Harihariini == "Minggu"){


            tvTanggal.setText("Senin, " + tanggalBesok);



        } else {

            tvTanggal.setText(hariBesok + ", " + tanggalBesok);
        }

    }


    public void initJadwalByDay() {


        /*TODAY*/
        Calendar today = Calendar.getInstance();
        today.add(Calendar.DAY_OF_YEAR, 0);
        Date hariini = today.getTime();

        SimpleDateFormat sdfToday = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggalHariIni = sdfToday.format(hariini);

        String[] sekarangs = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String Harihariini = sekarangs[today.get(Calendar.DAY_OF_WEEK)];

        /*BESOK*/
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        Date haribesok = tomorrow.getTime();

        SimpleDateFormat sdfHariIni = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggalBesok = sdfHariIni.format(haribesok);

        String[] besoks = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String hariBesok = besoks[tomorrow.get(Calendar.DAY_OF_WEEK)];



        /*LUSA*/
        Calendar lusa = Calendar.getInstance();
        lusa.add(Calendar.DAY_OF_YEAR, 2);
        Date besokLusa = lusa.getTime();

        SimpleDateFormat dfsenin = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggalLusa = dfsenin.format(besokLusa);

        String[] lusas = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String hariLusa = lusas[lusa.get(Calendar.DAY_OF_WEEK)];




        if (Harihariini == "Sabtu") {

            etFilterHari.setText("Senin");



        }else if(Harihariini == "Minggu"){


            etFilterHari.setText("Senin");



        } else {

            etFilterHari.setText(hariBesok);
        }
    }


    public void cekVersion(){

        etVersion.setText(getString(R.string.version));
        mSwipeRefreshLayout.setRefreshing(false);

        //progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CEK_VERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.GONE);



                        if(response.equalsIgnoreCase("Gagal")) {

                            updateDialog();


                        } else {

                            filterPoli();
                        }





                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("version", etVersion.getText().toString().trim());
                // params.put("password", passwordHolder);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }



    // misal tanggal 14 oktober pasien melakukan booking
    // sedangkan tanggal kontrol yang ditentukan dokter adalah tanggal 20 oktober...
    // ketika melakukan simpan, system akan mengecek kondisi :

    // jika tanggal pesan lebih kecil dari tanggal kontrol, maka akan di tolak
    // jika tanggal pesan melebihi 7 hari dari tanggal kontrol maka di tolak
    // jika tanggal pesan sesuai tanggal kontrol + 7 hari maka di ijinkan

    private void filterPoli() {

        final String jadwalpoli = etFilterHari.getText().toString();

        System.out.println(jadwalpoli.substring(0,3));

        progressBar = findViewById(R.id.jadwal_progressbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        progressBar.setVisibility(View.VISIBLE);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_GET_JADWAL_EKSEKUTIF,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRecyclerView.setVisibility(View.VISIBLE);



                        try {

                            JSONObject obj = new JSONObject(response);

                           /* if (!obj.getBoolean("error")) {

                                //Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                                showNoDataDialog();*/



                            JSONArray poliArray = obj.getJSONArray("results");

                            for (int i = 0; i < poliArray.length(); i++) {

                                JSONObject hObject = poliArray.getJSONObject(i);

                                Jadwal poli = new Jadwal(

                                        hObject.getString("id_jadwal"),
                                        hObject.getString("id_poli"),
                                        hObject.getString("id_dokter"),
                                        hObject.getString("kd_dokter"),
                                        hObject.getString("kd_poli"),
                                        hObject.getString("nama_lengkap"),
                                        hObject.getString("nama_poli"),
                                        hObject.getString("hari"),
                                        hObject.getString("praktek"),
                                        hObject.getString("jam")

                                );

                                mItems.add(poli);
                            }

                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

                           /* }else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressBar.setVisibility(View.INVISIBLE);

                        updateDialog();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("hari", jadwalpoli.substring(0, 3));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void updateDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning_update);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        textDialogUpdate = dialog.findViewById(R.id.content_warning_update);
        textDialogUpdate.setText(getString(R.string.error_update));


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();


                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }



//    public void showNoDataDialog(){
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
//        dialog.setContentView(R.layout.dialog_warning);
//        dialog.setCancelable(true);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
//
//
//        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//        dialog.getWindow().setAttributes(lp);
//    }



//    public void getAllJadwal() {
//
//        progressBar.setVisibility(View.VISIBLE);
//
//        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, Const.URL_ALL_JADWAL, null,
//
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                        progressBar.setVisibility(View.GONE);
//
//                        Log.d("volley", "response : " + response.toString());
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                JSONObject data = response.getJSONObject(i);
//                                Jadwal jadwal = new Jadwal();
//
//                                jadwal.setId_jadwal(data.getString("id_jadwal"));
//                                jadwal.setNama_lengkap(data.getString("nama_lengkap"));
//                                jadwal.setNama_poli(data.getString("nama_poli"));
//                                jadwal.setHari(data.getString("hari"));
//                                jadwal.setJam(data.getString("jam"));
//
//                                mItems.add(jadwal);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        mAdapter.notifyDataSetChanged();
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        Log.d("volley", "error : " + error.getMessage());
//                    }
//                });
//
//        VolleySingleton.getInstance(this).addToRequestQueue(reqData);
//
//    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        cariData(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basic, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Cari Dokter");
        searchView.setOnQueryTextListener(this);
        searchView.setIconified(true);

        return true;
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


    private void cariData(final String keyword) {


        final String hari = etFilterHari.getText().toString();

        progressBar = findViewById(R.id.jadwal_progressbar);
        mRecyclerView = findViewById(R.id.recyclerView);


        progressBar.setVisibility(View.VISIBLE);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CARI_DOKTER_NEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null)
                            return;
                        mItems.clear();
                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.GONE);


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);


                            //we have the array named poli inside the object
                            //so here we are getting that json array
                            JSONArray poliArray = obj.getJSONArray("results");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < poliArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject hObject = poliArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                Jadwal poli = new Jadwal(
                                        // hObject.getString("id_nasabah"),
                                        hObject.getString("id_jadwal"),
                                        hObject.getString("id_poli"),
                                        hObject.getString("id_dokter"),
                                        hObject.getString("kd_dokter"),
                                        hObject.getString("kd_poli"),
                                        hObject.getString("nama_lengkap"),
                                        hObject.getString("nama_poli"),
                                        hObject.getString("hari"),
                                        hObject.getString("praktek"),
                                        hObject.getString("jam")

                                );

                                //adding the hero to herolist
                                mItems.add(poli);
                            }

                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), "Server tidak merespon", Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama_lengkap", keyword);
                params.put("hari", hari);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }
}
