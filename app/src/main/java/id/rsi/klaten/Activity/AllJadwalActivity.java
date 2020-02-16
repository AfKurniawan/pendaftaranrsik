package id.rsi.klaten.Activity;

import android.app.Dialog;
import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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

import id.rsi.klaten.Adapter.AllJadwalAdapter;
import id.rsi.klaten.Adapter.JadwalAdapter;
import id.rsi.klaten.Model.Jadwal;
import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.SessionManager;
import id.rsi.klaten.Utils.VolleySingleton;
import id.rsi.klaten.rsik.R;


public class AllJadwalActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<Jadwal> mItems;
    Context mContext;

    ProgressBar progressBar;
    EditText etFilterHari, etEmail;
    RequestQueue queue;
    private SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_jadwal);

        sessionManager = new SessionManager(getApplicationContext());

        mRecyclerView = findViewById(R.id.recyclerView);
        //mRecyclerView.setNestedScrollingEnabled(false);
        mItems = new ArrayList<>();

        mManager = new LinearLayoutManager(AllJadwalActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mManager);
        mAdapter = new AllJadwalAdapter(AllJadwalActivity.this, mItems);
        mRecyclerView.setAdapter(mAdapter);
        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);

        progressBar = findViewById(R.id.jadwal_progressbar);
        etFilterHari = findViewById(R.id.filter_day);

        etEmail =findViewById(R.id.et_email);
        etEmail.setText(sessionManager.getSavedUserName());
        etEmail.setVisibility(View.GONE);


        progressBar = findViewById(R.id.jadwal_progressbar);
        mRecyclerView = findViewById(R.id.recyclerView);





        initToolbar();

        getAllJadwal();

        initDateNow();

        //initFilterDay();

        //filterPoli();

    }


    /*private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("PILIH DOKTER");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }*/

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.toolbar_all_jadwal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Tools.setSystemBarColor(this, R.color.blue_700);
    }


    public void initDateNow() {

        TextView tvTanggal = findViewById(R.id.tv_today);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggal = df.format(tomorrow);
        String[] days = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)];
        tvTanggal.setText(day+"," + tanggal);

    }


    public void initFilterDay(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        String tanggal = df.format(tomorrow);
        String[] days = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)];
        etFilterHari.setText(day);
    }


    // misal tanggal 14 oktober pasien melakukan booking
    // sedangkan tanggal kontrol yang ditentukan dokter adalah tanggal 20 oktober...
    // ketika melakukan simpan, system akan mengecek kondisi :

    // jika tanggal pesan lebih kecil dari tanggal kontrol, maka akan di tolak
    // jika tanggal pesan melebihi 7 hari dari tanggal kontrol maka di tolak
    // jika tanggal pesan sesuai tanggal kontrol + 7 hari maka di ijinkan

    private void filterPoli() {

        final String keyword = etFilterHari.getText().toString();

        progressBar = findViewById(R.id.jadwal_progressbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        progressBar.setVisibility(View.VISIBLE);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_GET_JADWAL_BY_DAY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //hiding the progressbar after completion
                        progressBar.setVisibility(View.GONE);


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

                            /*}else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }*/


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
                params.put("hari", keyword);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public void showNoDataDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }



    public void getAllJadwal() {

        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET, Const.URL_ALL_JADWAL, null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        progressBar.setVisibility(View.GONE);

                        Log.d("volley", "response : " + response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                Jadwal jadwal = new Jadwal();

                                jadwal.setId_jadwal(data.getString("id_jadwal"));
                                jadwal.setNama_lengkap(data.getString("nama_lengkap"));
                                jadwal.setNama_poli(data.getString("nama_poli"));
                                jadwal.setHari(data.getString("hari"));
                                jadwal.setJam(data.getString("jam"));

                                mItems.add(jadwal);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        VolleySingleton.getInstance(this).addToRequestQueue(reqData);

    }

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


        progressBar.setVisibility(View.VISIBLE);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CARI_DOKTER_ALL_JADWAL_NEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        /*if (response == null)
                            return;
                        mItems.clear();*/

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

                                        hObject.getString("id_jadwal"),
                                        hObject.getString("nama_lengkap"),

                                        hObject.getString("id_poli"),
                                        hObject.getString("id_dokter"),
                                        hObject.getString("kd_dokter"),
                                        hObject.getString("kd_poli"),
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

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }
}
