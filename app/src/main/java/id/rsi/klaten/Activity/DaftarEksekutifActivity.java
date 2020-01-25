package id.rsi.klaten.Activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.app.DatePickerDialog;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jaredrummler.materialspinner.MaterialSpinner;

import id.rsi.klaten.Model.Jadwal;
import id.rsi.klaten.Model.NomorAntri;
import id.rsi.klaten.Model.Pasien;
import id.rsi.klaten.Model.LimitDokter;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.SessionManager;
import id.rsi.klaten.Utils.VolleySingleton;
import id.rsi.klaten.rsik.R;
import id.rsi.klaten.Tools.Tools;
import android.util.Base64;
import net.glxn.qrgen.android.QRCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static id.rsi.klaten.Utils.Const.REQCODE;
import static id.rsi.klaten.Utils.Const.REQCODECARD;
import static id.rsi.klaten.Utils.Const.REQCODEGALLERYBPJS;
import static id.rsi.klaten.Utils.Const.REQCODEGALLERYKONTROL;
import static id.rsi.klaten.Utils.Const.REQCODEKTP;


public class DaftarEksekutifActivity extends AppCompatActivity {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private EditText etNoreg, etNamaPasien, etJenkel, etAlamat;

    EditText etDateKontrol,
            etIdDokter,
            etIdPoli,
            etDateBooking,
            etIdJadwal,
            etUpload,
            etKtp,
            etUploadCard,
            etCariPasien,
            etNomorAntri,
            etNoBarcode,
            etNamaDokter,
            etKodeDokter,
            etKodePoli,
            etPraktek,
            etNamaPoli,
            etCekTglBooking,
            etTanggalKontrolBPJS,
            etJam,
            etHari,
            etJumlahDokter,
            etIdDokterIntentExtra,
            etEmail;

    Button btnSimpan,
            btnCamera,
            btnCameraKtp,
            btnDatePicker,
            btnKartu,
            btnCariPasien,
            btnSummary;

    MaterialSpinner spnPenjamin;

    TextView tvLampiranBpjs,
            tvLampiranKartu,
            tvLampiranKtp;

    ImageView fotoBpjs,
            fotoKartu,
            fotoKtp;

    CardView cvBpjs,
            cvDataPasien,
            cvKartu,
            cvKtp;

    DatePickerDialog dpd;
    private ProgressDialog dialog = null;
    ProgressBar pb;
    private JSONObject jsonObject;

    String noRmHolder, fotoBpjsHolder, fotoKtpHolder, fotoSkHolder;

    Boolean cekEditText;

    RequestQueue queue;

    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;

    public static final int MY_PERMISSION_READ_STORAGE = 105;
    public static final String ALLOW_KEY = "ALLOWED";
    public static final String CAMERA_PREF = "camera_pref";

    private SessionManager sessionManager;

    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_daftar_eksekutif);
        sessionManager = new SessionManager(getApplicationContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        pb = findViewById(R.id.main_activity_pb);
        cvDataPasien = findViewById(R.id.cv_data_pasien);
        cvDataPasien.setVisibility(GONE);
        tvLampiranBpjs = findViewById(R.id.tv_lamp_bpjs);
        tvLampiranKartu = findViewById(R.id.tv_lampiran_kartu);
        tvLampiranKtp = findViewById(R.id.tv_lampiran_ktp);
        cvBpjs = findViewById(R.id.cv_lampiran_bpjs);
        cvBpjs.setVisibility(GONE);
        cvKartu = findViewById(R.id.cv_lampiran_kartu);
        cvKartu.setVisibility(GONE);
        cvKtp = findViewById(R.id.cv_lampiran_ktp);
        cvKtp.setVisibility(GONE);
        etEmail = findViewById(R.id.et_email);
        etEmail.setText(sessionManager.getSavedUserName());
        etEmail.setVisibility(GONE);
        etJumlahDokter = findViewById(R.id.et_jumlah_dokter);
        etJumlahDokter.setVisibility(GONE);
        jsonObject = new JSONObject();




        initButton();

        initEditText();

        initImageView();

        initProgressBar();

        initToolbar();

        initSpinner();

        initSimpan();

        initTanggal();

        getIdJadwal();

        showToday();

        showCameraBpjs();

        showCameraKartu();

        showCameraKtp();

        initSharedPref();

        searchDataPasien();

        initTomorow();

        //limitDokter();

        getSingleJadwal();

        getSummaryBooking();

        initPermission();

        initPermissionReadStorage();



    }

    public void limitDokter(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.LIMIT_DOKTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(ServerResponse);

                            JSONArray heroArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject hObject = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                LimitDokter lim = new LimitDokter(

                                        hObject.getString("jml_dokter")
                                );


                                etJumlahDokter.setText(lim.getJml_dokter());
                                String jml = etJumlahDokter.getText().toString();
                                Integer jumlah = Integer.parseInt(jml);

                                if (jumlah > 35){

                                    errorDokterLimit();
                                }



                            }



                        } catch (JSONException e) {
                            e.printStackTrace();




                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("id_dokter", etIdDokterIntentExtra.getText().toString());
                params.put ("tgl_booking", etDateBooking.getText().toString());


                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(DaftarEksekutifActivity.this);

        requestQueue.add(stringRequest);



    }

    public void errorDokterLimit() {

        btnSimpan.setVisibility(View.GONE);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_error_dokter_limit);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton)dialog.findViewById(R.id.bt_close_error_tanggal))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(DaftarEksekutifActivity.this, JadwalListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void initButton(){
        btnDatePicker = findViewById(R.id.bt_date_picker);
        btnCamera = findViewById(R.id.bt_camera);
        btnCameraKtp = findViewById(R.id.bt_camera_ktp);
        btnSimpan = findViewById(R.id.bt_simpan);
        btnSimpan.setVisibility(View.GONE);
        btnKartu = findViewById(R.id.bt_kartu);
        btnCariPasien = findViewById(R.id.bt_cari_pasien);
        btnSummary =findViewById(R.id.bt_booking_summary);
        btnSummary.setVisibility(GONE);
        btnSimpan.setVisibility(GONE);
        btnDatePicker.setVisibility(GONE);
    }

    public void initEditText(){

        etNamaPasien = findViewById(R.id.et_nama_pasien);
        etJenkel = findViewById(R.id.et_jenkel);
        etAlamat = findViewById(R.id.et_alamat);
        spnPenjamin = findViewById(R.id.spinner);
        etCariPasien = findViewById(R.id.et_cari_pasien);


        etDateKontrol =  findViewById(R.id.tgl_ctl);
        etDateKontrol.setVisibility(GONE);

        etUpload = findViewById(R.id.et_upload);
        etKtp = findViewById(R.id.et_upload_ktp);
        etNomorAntri = findViewById(R.id.et_no_antrian);
        etUploadCard = findViewById(R.id.et_upload_kartu);
        etNamaDokter = findViewById(R.id.et_nama_dokter);
        etKodeDokter = findViewById(R.id.et_kd_dokter);
        etKodeDokter.setVisibility(GONE);
        etKodePoli = findViewById(R.id.et_kd_poli);
        etKodePoli.setVisibility(GONE);
        etPraktek = findViewById(R.id.et_praktek);
        etPraktek.setVisibility(GONE);
        etNamaPoli = findViewById(R.id.et_nama_poli);
        etIdDokter = findViewById(R.id.et_id_dokter);
        etIdDokter.setVisibility(GONE);
        etIdPoli = findViewById(R.id.et_id_poli);
        etTanggalKontrolBPJS = findViewById(R.id.et_tgl_kontrol_bpjs);


        etNamaDokter.setVisibility(GONE);
        etNamaPoli.setVisibility(GONE);

        etHari = findViewById(R.id.et_hari);
        etJam = findViewById(R.id.et_jam);

        etNoBarcode = findViewById(R.id.et_no_barcode);
        etNoBarcode.setVisibility(GONE);

    }

    public void initImageView(){
        fotoBpjs = findViewById(R.id.img_bpjs);
        fotoBpjs.setVisibility(GONE);
        fotoKartu = findViewById(R.id.img_kartu);
        fotoKartu.setVisibility(GONE);
        fotoKtp = findViewById(R.id.img_ktp);
        fotoKtp.setVisibility(GONE);

    }

    public void initSharedPref(){
        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mPreferences = getSharedPreferences("rsiklaten", Context.MODE_PRIVATE);
        mEditor = mPreferences.edit();

    }

    public void initProgressBar(){
        dialog = new ProgressDialog(this);
        dialog.setMessage("Tunggu Sebentar...");
        dialog.setCancelable(false);

    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pendaftaran Online Pasien");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.green_A400);
    }

    public void showToday(){


        etDateBooking = findViewById(R.id.tgl_booking);
        etDateBooking.setVisibility(GONE);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        String tanggal = df.format(tomorrow);


        String[] days = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)];


        if (day == "Minggu"){

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_WEEK, 2);
            Date senin = cal.getTime();

            SimpleDateFormat dformat = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            String harisenin = dformat.format(senin);

            etDateBooking.setText(harisenin);

        } else {

            etDateBooking.setText(tanggal);

        }


    }

    public void initTomorow(){

        etCekTglBooking = findViewById(R.id.et_cek_tgl_booking);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();

        Calendar limitmax = new GregorianCalendar();
        limitmax.add(Calendar.DAY_OF_MONTH, 1);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        String sesuk = df.format(tomorrow);

        etCekTglBooking.setText(sesuk);




    }

    public void cekTglKontrol() {

        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CEK_TGL_KONTROL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dialog.dismiss();

                        if(response.equalsIgnoreCase("Gagal")) {

                            errorDialogDateLimit();


                        } else{

                            cvBpjs.setVisibility(View.VISIBLE);
                            cvKartu.setVisibility(View.VISIBLE);
                            cvKtp.setVisibility(GONE);
                            fotoBpjs.setVisibility(GONE);
                            fotoKartu.setVisibility(GONE);
                            fotoKtp.setVisibility(GONE);
                            btnSimpan.setVisibility(VISIBLE);
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
                params.put("tgl_kontrol", etCekTglBooking.getText().toString());
                params.put("nopeserta", etCariPasien.getText().toString());
                // params.put("password", passwordHolder);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    private void cekBooking() {

        pb.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CEK_BOOKING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pb.setVisibility(View.GONE);

                        if(response.equalsIgnoreCase("MasukPakEko")) {

                            showDialogAlertSudahDaftar();

                        } /*else {

                            //dialogPersetujuan();
                            //getNomorAntrianByIdDokter();


                        }*/
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
                params.put("tgl_booking", etDateBooking.getText().toString());
                params.put("no_rm", etCariPasien.getText().toString());
                // params.put("password", passwordHolder);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public void getIdJadwal(){

        etIdJadwal = findViewById(R.id.et_id_jdwl);
        etIdDokterIntentExtra = findViewById(R.id.et_id_dokter_intent);
        etIdDokterIntentExtra.setVisibility(GONE);


        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);

        String intent_idJadwal = data.getStringExtra("id_jadwal");
        String intent_jam = data.getStringExtra("jam");
        String intent_hari = data.getStringExtra("hari");
        String intentIdDokter = data.getStringExtra("id_dokter");

        if (update==1){

            etIdJadwal.setText(intent_idJadwal);
            etHari.setText(intent_hari +", "+ intent_jam);
            etJam.setText(intent_jam);
            etJam.setVisibility(GONE);
            etIdDokterIntentExtra.setText(intentIdDokter);

        }





    }

    private void initSpinner(){

        MaterialSpinner spinner = findViewById(R.id.spinner);
        spinner.setItems( "Pilih Penjamin", "Umum", "BPJS", "Asuransi Lainnya");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                switch (position) {

                    case 0:
                        btnSimpan.setVisibility(GONE);
                        cvBpjs.setVisibility(GONE);
                        cvKtp.setVisibility(GONE);
                        cvKartu.setVisibility(GONE);
                        break;
                    case 1:
                        cvBpjs.setVisibility(View.GONE);
                        cvKartu.setVisibility(View.GONE);
                        cvKtp.setVisibility(GONE);
                        btnSimpan.setVisibility(VISIBLE);
                        break;

                    case 2:
                        cekTglKontrol();
                        break;

                    case 3:
                        cvBpjs.setVisibility(View.GONE);
                        cvKartu.setVisibility(View.GONE);
                        cvKtp.setVisibility(GONE);
                        btnSimpan.setVisibility(VISIBLE);
                        break;

                }

            }
        });

    }



    public static void saveToPreferences(Context context, String kunci, Boolean allowed) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(kunci, allowed);
        prefsEditor.commit();
    }

    public static Boolean getFromPref(Context context, String kunci) {
        SharedPreferences myPrefs = context.getSharedPreferences
                (CAMERA_PREF, Context.MODE_PRIVATE);
        return (myPrefs.getBoolean(kunci, false));
    }

    private void showAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Aplikasi ini membutuhkan akses Kamera");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tolak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Izinkan",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(DaftarEksekutifActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                    }
                });
        alertDialog.show();
    }

    private void showStorageSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Aplikasi ini membaca akses Penyimpanan");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tolak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Izinkan",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //startInstalledAppDetailsActivity(MainActivity.this);

                    }
                });
        alertDialog.show();
    }

    private void showSettingsAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Aplikasi ini membutuhkan akses Kamera");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Tolak",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //finish();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Izinkan",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //startInstalledAppDetailsActivity(MainActivity.this);

                    }
                });
        alertDialog.show();
    }

    private void showCameraBpjs() {
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQCODE);
                }*/

                dialogOptionCameraGalleryBPJS();

            }
        });
    }

    private void showCameraKtp() {
        btnCameraKtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Intent takePictureIntentKtp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntentKtp.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntentKtp, REQCODEKTP);
                }*/

                dialogOptionCameraGalleryBPJS();

            }
        });
    }

    private void showCameraKartu() {
        btnKartu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*
                Intent takePictureIntentKartu = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntentKartu.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntentKartu, REQCODECARD);

                }*/

                dialogOptionCameraGalleryKontrol();
            }
        });
    }

    public void dialogOptionCameraGalleryBPJS(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_option_camera_gallery);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        ((Button) dialog.findViewById(R.id.bt_opt_camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Button Join Clicked", Toast.LENGTH_SHORT).show();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQCODE);
                    dialog.dismiss();
                }
            }
        });

        ((Button) dialog.findViewById(R.id.bt_opt_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Button Decline Clicked", Toast.LENGTH_SHORT).show();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , REQCODEGALLERYBPJS);//one can be replaced with any action code
                dialog.dismiss();
            }
        });


        dialog.show();

    }

    public void dialogOptionCameraGalleryKontrol(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_option_camera_gallery);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(true);

        ((Button) dialog.findViewById(R.id.bt_opt_camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Button Join Clicked", Toast.LENGTH_SHORT).show();
                Intent takePictureIntentKartu = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntentKartu.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntentKartu, REQCODECARD);
                    dialog.dismiss();
                }
            }
        });

        ((Button) dialog.findViewById(R.id.bt_opt_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Button Decline Clicked", Toast.LENGTH_SHORT).show();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , REQCODEGALLERYKONTROL);//one can be replaced with any action code
                dialog.dismiss();
            }
        });


        dialog.show();

    }



    private void initSimpan() {

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(cvKartu.getVisibility()==View.VISIBLE) {
                    fotoSkHolder = etUploadCard.getText().toString().trim();

                    if (TextUtils.isEmpty(fotoSkHolder)) {
                        tvLampiranKartu.setTextColor(Color.parseColor("#D32F2F"));
                        errorDialogLampiran();
                        return;
                    } else {
                        tvLampiranKartu.setTextColor(Color.parseColor("#64DD17"));
                    }
                }

                if (cvBpjs.getVisibility()==View.VISIBLE){

                    fotoBpjsHolder = etUpload.getText().toString().trim();
                    if (TextUtils.isEmpty(fotoBpjsHolder)){
                        errorDialogLampiran();
                        tvLampiranBpjs.setTextColor(Color.parseColor("#D32F2F"));
                        return;
                    } else {
                        tvLampiranBpjs.setTextColor(Color.parseColor("#64DD17"));
                    }

                }

               /* if (cvKtp.getVisibility()==View.VISIBLE){

                    fotoKtpHolder = etKtp.getText().toString().trim();
                    if (TextUtils.isEmpty(fotoKtpHolder)){
                        errorDialogLampiran();
                        tvLampiranKtp.setTextColor(Color.parseColor("#D32F2F"));
                        return;
                    } else {
                        tvLampiranKtp.setTextColor(Color.parseColor("#64DD17"));
                    }

                }*/

                if (cvBpjs.getVisibility()==View.VISIBLE) {

                    uploadBpjs();

                }

                if (cvKtp.getVisibility()==VISIBLE){

                    uploadKtp();
                }

                if (cvKartu.getVisibility()==VISIBLE){

                    uploadKartu();

                }



                pb.setVisibility(View.VISIBLE);

                //SAVE VALUE TO SHAREDPREF
                String name = etNamaPasien.getText().toString();
                mEditor.putString(getString(R.string.pasien_baru), name);
                mEditor.commit();

                String idJadwal = etIdJadwal.getText().toString();
                mEditor.putString(getString(R.string.jadwal), idJadwal);
                mEditor.commit();

                String tanggalBooking = etDateBooking.getText().toString();
                mEditor.putString(getString(R.string.tanggal), tanggalBooking);
                mEditor.commit();

                String norm = etCariPasien.getText().toString();
                mEditor.putString(getString(R.string.no_rm), norm);
                mEditor.commit();

                String alamat = etAlamat.getText().toString();
                mEditor.putString(getString(R.string.alamat_lengkap), alamat);
                mEditor.commit();

                String penjamin = spnPenjamin.getText().toString();
                mEditor.putString(getString(R.string.penjamin), penjamin);
                mEditor.commit();

                String namaPoli = etNamaPoli.getText().toString();
                mEditor.putString(getString(R.string.nama_poli), namaPoli);
                mEditor.commit();

                String namaDokter = etNamaDokter.getText().toString();
                mEditor.putString(getString(R.string.nama_dokter), namaDokter);
                mEditor.commit();

                String idDokter = etIdDokter.getText().toString();
                mEditor.putString(getString(R.string.id_dokter), idDokter);
                mEditor.commit();

                String kdDokter = etKodeDokter.getText().toString();
                mEditor.putString(getString(R.string.kd_dokter), kdDokter);
                mEditor.commit();

                String kdPoli = etKodePoli.getText().toString();
                mEditor.putString(getString(R.string.kd_poli), kdPoli);
                mEditor.commit();

                String praktek = etPraktek.getText().toString();
                mEditor.putString(getString(R.string.praktek), praktek);
                mEditor.commit();

                String idPoli = etIdPoli.getText().toString();
                mEditor.putString(getString(R.string.id_poli), idPoli);
                mEditor.commit();

                String jam = etJam.getText().toString();
                mEditor.putString(getString(R.string.jam), jam);
                mEditor.commit();

                String hari = etHari.getText().toString();
                mEditor.putString(getString(R.string.hari), hari);
                mEditor.commit();


                dialogPersetujuan();

                /*cekBooking();*/


            }
        });
    }

    private void showDialogAlertSudahDaftar(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning_sudah_daftar);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton)dialog.findViewById(R.id.bt_close_sudah_daftar))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        Intent intent = new Intent(DaftarEksekutifActivity.this, HistoryListActivity.class);
                        startActivity(intent);

                        btnSimpan.setVisibility(GONE);
                    }
                });

        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }

    private void uploadBpjs(){


        Bitmap image = ((BitmapDrawable) fotoBpjs.getDrawable()).getBitmap();
        dialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        try {
            jsonObject.put(Const.imageName, etUpload.getText().toString().trim());
            jsonObject.put(Const.image, encodedImage);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Const.URL_UPLOAD_BPJS, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("Pesan dari Server", jsonObject.toString());
                        dialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Pesan dari Server", volleyError.toString());

                // pb.setVisibility(GONE);

                Toast.makeText(getApplication(), "Upload BPJS Gagal", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void uploadKtp(){


        Bitmap image = ((BitmapDrawable) fotoKtp.getDrawable()).getBitmap();
        dialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        try {
            jsonObject.put(Const.imageKtp, etKtp.getText().toString().trim());
            jsonObject.put(Const.ktpName, encodedImage);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Const.URL_UPLOAD_KTP, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("Pesan dari Server", jsonObject.toString());
                        dialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Pesan dari Server", volleyError.toString());

                // pb.setVisibility(GONE);

                Toast.makeText(getApplication(), "Upload KTP Gagal", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void uploadKartu(){

        pb.setVisibility(View.VISIBLE);
        Bitmap image = ((BitmapDrawable) fotoKartu.getDrawable()).getBitmap();
        dialog.show();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        try {
            jsonObject.put(Const.cardName, etUploadCard.getText().toString().trim());
            jsonObject.put(Const.imageCard,encodedImage);
        } catch (JSONException e) {
            Log.e("JSONObject Here", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Const.URL_UPLOAD_KARTU, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.e("Message from server", jsonObject.toString());
                        dialog.dismiss();
                        //Toast.makeText(getApplication(), "Berhasil Daftar", Toast.LENGTH_SHORT).show();
                        //showSuccessBooking();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("Message from server", volleyError.toString());
                dialog.dismiss();
                Toast.makeText(getApplication(), "Upload Kartu Periksa Gagal", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQCODE && resultCode == RESULT_OK) {
            fotoBpjs.setVisibility(VISIBLE);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotoBpjs.setImageBitmap(imageBitmap);
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss",Locale.US).format(new Date());
            etUpload.setText("BPJS-"+timeStamp);
            btnSimpan.setVisibility(View.VISIBLE);
            tvLampiranBpjs.setVisibility(GONE);
            tvLampiranBpjs.setTextColor(Color.parseColor("#64DD17"));

        }

        if (requestCode == REQCODEKTP && resultCode == RESULT_OK) {
            fotoKtp.setVisibility(VISIBLE);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotoKtp.setImageBitmap(imageBitmap);
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss",Locale.US).format(new Date());
            etKtp.setText("KTP-"+timeStamp);
            btnSimpan.setVisibility(View.VISIBLE);
            tvLampiranKtp.setVisibility(GONE);
            tvLampiranKtp.setTextColor(Color.parseColor("#64DD17"));

        }

        if (requestCode == REQCODEGALLERYBPJS && resultCode == RESULT_OK){
            fotoBpjs.setVisibility(VISIBLE);
            Uri selectedImage = data.getData();
            fotoBpjs.setImageURI(selectedImage);
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss",Locale.US).format(new Date());
            etUpload.setText("BPJS-"+timeStamp);
            btnSimpan.setVisibility(View.VISIBLE);
            tvLampiranBpjs.setVisibility(GONE);
            tvLampiranBpjs.setTextColor(Color.parseColor("#64DD17"));

        }

        if (requestCode == REQCODEGALLERYKONTROL && resultCode == RESULT_OK){
            fotoKartu.setVisibility(VISIBLE);
            Uri selectedImage = data.getData();
            fotoKartu.setImageURI(selectedImage);
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss",Locale.US).format(new Date());
            etUploadCard.setText("Kartu-Kontrol-" + timeStamp);
            btnSimpan.setVisibility(View.VISIBLE);
            tvLampiranKartu.setVisibility(GONE);
            tvLampiranKartu.setTextColor(Color.parseColor("#64DD17"));

        }

        if (requestCode == REQCODECARD && resultCode == RESULT_OK) {
            fotoKartu.setVisibility(VISIBLE);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            fotoKartu.setImageBitmap(imageBitmap);
            String timeStamp = new java.text.SimpleDateFormat("yyyyMMdd-HHmmss",Locale.US).format(new Date());
            etUploadCard.setText("Kartu-Kontrol-" + timeStamp);
            btnSimpan.setVisibility(View.VISIBLE);
            tvLampiranKartu.setVisibility(GONE);
            tvLampiranKartu.setTextColor(Color.parseColor("#64DD17"));


        }

    }

    private void searchDataPasien(){
        btnCariPasien =  findViewById(R.id.bt_cari_pasien);
        btnCariPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //hide keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etCariPasien.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                noRmHolder = etCariPasien.getText().toString().trim();
                if (TextUtils.isEmpty(noRmHolder)){
                    etCariPasien.setError(getString(R.string.e_cari_pasien));
                    return;

                }

                pb = findViewById(R.id.main_activity_pb);
                pb.setVisibility(View.VISIBLE);

                getDataPasien();

            }
        });

    }

    public void getDataPasien(){


        dialog.show();
        cvDataPasien.setVisibility(View.GONE);
        cvKartu.setVisibility(View.GONE);

        queue = VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_SEARCH_PASIEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Pesan dari Server", jsonObject.toString());

                        dialog.dismiss();

                        if(response.equalsIgnoreCase("Tidak ada data")) {

                            errorDialogNoData();

                        } else {

                            cekBooking();

                        }
                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONArray heroArray = obj.getJSONArray("data");

                            pb.setVisibility(GONE);
                            cvDataPasien.setVisibility(View.VISIBLE);


                            for (int i = 0; i < heroArray.length(); i++) {


                                //getting the json object of the particular index inside the array
                                JSONObject pasObj = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                Pasien pasien = new Pasien(

                                        pasObj.getString("no_reg"),
                                        pasObj.getString("nama"),
                                        pasObj.getString("jenis_kel"),
                                        pasObj.getString("alamat")


                                );

                                etNamaPasien.setText(pasien.getNama());
                                etAlamat.setText(pasien.getAlamat());
                                etJenkel.setText(pasien.getJenis_kelamin());


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorDialogLampiran();
                        cvDataPasien.setVisibility(View.GONE);
                        cvKartu.setVisibility(View.GONE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("no_reg", etCariPasien.getText().toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    public void getSingleJadwal(){

        queue = VolleySingleton.getInstance(this).getRequestQueue();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_SINGLE_JADWAL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        etNamaDokter.setVisibility(VISIBLE);
                        etNamaPoli.setVisibility(VISIBLE);


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);


                            JSONArray heroArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            for (int i = 0; i < heroArray.length(); i++) {
                                //getting the json object of the particular index inside the array
                                JSONObject pasObj = heroArray.getJSONObject(i);

                                //creating a hero object and giving them the values from json object
                                Jadwal jadwal = new Jadwal(
                                        pasObj.getString("id_jadwal"),
                                        pasObj.getString("id_poli"),
                                        pasObj.getString("id_dokter"),
                                        pasObj.getString("kd_dokter"),
                                        pasObj.getString("kd_poli"),
                                        pasObj.getString("nama_lengkap"),
                                        pasObj.getString("nama_poli"),
                                        pasObj.getString("hari"),
                                        pasObj.getString("praktek"),
                                        pasObj.getString("jam")

                                );

                                etNamaDokter.setText(jadwal.getNama_lengkap());
                                etNamaPoli.setText(jadwal.getNama_poli());
                                etKodeDokter.setText(jadwal.getKd_dokter());
                                etKodePoli.setText(jadwal.getKd_poli());
                                etPraktek.setText(jadwal.getPraktek());
                                etIdDokter.setText(jadwal.getId_dokter());
                                etIdPoli.setText(jadwal.getId_poli());
                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_jadwal", etIdJadwal.getText().toString());

                return params;
            }
        };

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }

    private void updateDataBooking(){

        dialog.show();

        String nomorAntrian = mPreferences.getString(getString(R.string.nomor_antrian),"");
        etNomorAntri.setText(nomorAntrian);

        String noBarcode = mPreferences.getString(getString(R.string.nomor_antrian_idjadwal_iddokter), "");
        etNoBarcode.setText(noBarcode);




        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.BOOKING_URL,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.e("Pesan dari Server", jsonObject.toString());



                        // Log.e("Pesan dari Server", jsonObject.toString());

                        if(response.contains("success")) {

                            showSuccessBooking();

                            dialog.dismiss();

                            //showSuccessBooking();
                        }


                    }


                },





                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("tgl_booking", etDateBooking.getText().toString());
                params.put("id_poli", etIdPoli.getText().toString());
                params.put("id_dokter", etIdDokter.getText().toString());
                params.put("id_jadwal", etIdJadwal.getText().toString());
                params.put("no_rm", etCariPasien.getText().toString());
                params.put("nama_pasien", etNamaPasien.getText().toString());
                params.put("penjamin", spnPenjamin.getText().toString());
                params.put("foto_bpjs", etUpload.getText().toString());
                params.put("foto_ktp",etKtp.getText().toString());
                params.put("foto_surat_kontrol", etUploadCard.getText().toString());
                // params.put("no_antrian", etNomorAntri.getText().toString());
                params.put("no_barcode", etNoBarcode.getText().toString());
                params.put("tgl_kontrol", etDateKontrol.getText().toString());
                params.put("email", etEmail.getText().toString());

                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(DaftarEksekutifActivity.this);

        requestQueue.add(stringRequest);
    }

    private void getSummaryBooking(){
        btnSummary = findViewById(R.id.bt_booking_summary);
        btnSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent history = new Intent(DaftarEksekutifActivity.this, HistoryListActivity.class);
                startActivity(history);


            }
        });

    }

    private void showDialogKonfirmasiNomorAntri() {


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_summary_booking);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        //DEFINE DIALOG COMPONENTS
        TextView idJadwalDialog = dialog.findViewById(R.id.tv_id_jadwal_dialog);
        final EditText namaPasienDialog = dialog.findViewById(R.id.et_nama_dialog2);
        final TextView noRmPasienDialog = dialog.findViewById(R.id.tv_no_rm);
        //EditText etJamJadwal = dialog.findViewById(R.id.et_jam_jadwal);
        final EditText etTanggalDialog = dialog.findViewById(R.id.et_tanggal_dialog);
        etTanggalDialog.setVisibility(GONE);


        TextView tvHariDialog = dialog.findViewById(R.id.tv_jam_dialog);
        EditText alamatPasienDialog = dialog.findViewById(R.id.et_alamat_dialog2);
        final EditText nomorAntrianDialog = dialog.findViewById(R.id.et_nomor_antri_dialog2);
        EditText namaPoliDialog = dialog.findViewById(R.id.et_poli_dialog2);
        EditText namaDokterDialog = dialog.findViewById(R.id.et_dokter_dialog2);
        EditText idPoliDialog = dialog.findViewById(R.id.et_id_poli_dialog2);
        final EditText penjaminDialog = dialog.findViewById(R.id.et_penjamin_dialog);

        final ProgressBar pb = dialog.findViewById(R.id.pb_dialog);
        pb.setVisibility(VISIBLE);
        final Button btnClose = dialog.findViewById(R.id.bt_booking_summary);
        btnClose.setVisibility(GONE);

        final EditText kdDokterDialog = dialog.findViewById(R.id.et_kd_dokter_dialog);
        final EditText kdPoliDialog = dialog.findViewById(R.id.et_kd_poli_dialog);
        final EditText praktekDialog = dialog.findViewById(R.id.et_praktek_dialog);
        kdDokterDialog.setVisibility(GONE);
        kdPoliDialog.setVisibility(GONE);
        praktekDialog.setVisibility(GONE);

        final EditText etParamBpjs = dialog.findViewById(R.id.et_param_bpjs);
        etParamBpjs.setVisibility(GONE);
        final EditText etParamSurat = dialog.findViewById(R.id.et_param_surat_kt);
        etParamSurat.setVisibility(GONE);
        final EditText etParamKtp = dialog.findViewById(R.id.et_param_ktp);
        etParamKtp.setVisibility(GONE);
        final EditText etParamAntrian = dialog.findViewById(R.id.et_param_noantri);
        final EditText etParamNobarcode = dialog.findViewById(R.id.et_param_nobarcode);

        TextView tvSyaratBpjs = dialog.findViewById(R.id.tv_syarat_bpjs);
        TextView tvSyaratSurat = dialog.findViewById(R.id.tv_syarat_surat);
        TextView tvSyaratKtp = dialog.findViewById(R.id.tv_syarat_ktp);



        final EditText idDokterDialog = dialog.findViewById(R.id.et_id_dokter_dialog2);
        idDokterDialog.setVisibility(GONE);

        //QR CODE GENERATOR
        ImageView qrcode = dialog.findViewById(R.id.qr_code2);
        String stNomor = nomorAntrianDialog.getText().toString();
        Bitmap myBitmap = QRCode.from(stNomor).bitmap();
        qrcode.setImageBitmap(myBitmap);


        String penjaminOffice = mPreferences.getString(getString(R.string.penjamin), "");
        String senin = mPreferences.getString(getString(R.string.hari), "");

        TextView tvSyarat = dialog.findViewById(R.id.tv_syarat);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();


        //GET DATE TOMORROW AND SHOW TO DIALOG TEXTVIEW
        SimpleDateFormat df = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
        final String tanggal = df.format(tomorrow);
        String[] days = new String[] {"", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" };
        String day = days[calendar.get(Calendar.DAY_OF_WEEK)];





        if(penjaminOffice.equals("BPJS")){

            if (day == "Minggu"){
                Calendar calendar1 = Calendar.getInstance();
                calendar1.add(Calendar.DAY_OF_YEAR, 2);
                Date besok = calendar1.getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat(" dd MMMM yyyy", Locale.UK);
                String tgl = dateFormat.format(besok);

                tvSyarat.setText("Anda akan dilayani pada hari " + senin +", Tanggal:  " + tgl + ", Tunjukkan Barcode dan Nomor Antrian ini kepada Petugas Front Office.");
                tvSyaratBpjs.setText("1. Kartu BPJS");
                tvSyaratKtp.setText("2. KTP");
                tvSyaratSurat.setText("3. Surat Kontrol");

            } else {
                tvSyarat.setText("Anda akan dilayani pada hari " + senin +", Tanggal: " + tanggal + ", Tunjukkan Barcode dan Nomor Antrian ini kepada Petugas Front Office.");
                //ktpKk.setVisibility(VISIBLE);
                tvSyaratBpjs.setText("1. Kartu BPJS");
                tvSyaratKtp.setText("2. KTP");
                tvSyaratSurat.setText("3. Surat Kontrol");

            }


        } else {

            if (day == "Minggu"){
                Calendar calendar1 = Calendar.getInstance();
                calendar1.add(Calendar.DAY_OF_YEAR, 2);
                Date besok = calendar1.getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.UK);
                String tgl = dateFormat.format(besok);

                tvSyarat.setText("Anda akan dilayani pada hari " + senin +", Tanggal: " + tgl + ", Tunjukkan Barcode dan Nomor Antrian ini kepada Petugas Front Office.");
                tvSyaratKtp.setText("1. KTP");
                tvSyaratSurat.setText("2. Surat Kontrol");

            } else {

                tvSyarat.setText("Anda akan dilayani pada hari " + senin + ", Tanggal: " + tanggal + ", Tunjukkan Barcode dan Nomor Antrian ini kepada Petugas Pendaftaran.");
                tvSyaratBpjs.setVisibility(GONE);

                tvSyaratKtp.setText("1. KTP");
                tvSyaratSurat.setText("2. Surat Kontrol");
            }


        }
        //GET VALUE FROM SHAREDPREF
        final String idJadwal = mPreferences.getString(getString(R.string.jadwal), "");
        idJadwalDialog.setText(idJadwal);

        final String namaPasien = mPreferences.getString(getString(R.string.pasien_baru), "");
        namaPasienDialog.setText(namaPasien);

        final String norm = mPreferences.getString(getString(R.string.no_rm), "");
        noRmPasienDialog.setText(norm);

        String alamatPasien = mPreferences.getString(getString(R.string.alamat_lengkap),"");
        alamatPasienDialog.setText(alamatPasien);

        String poli = mPreferences.getString(getString(R.string.nama_poli), "");
        namaPoliDialog.setText(poli);

        String dokter = mPreferences.getString(getString(R.string.nama_dokter), "");
        namaDokterDialog.setText(dokter);

        final String iddokter = mPreferences.getString(getString(R.string.id_dokter), "");
        idDokterDialog.setText(iddokter);

        final String stanggal = mPreferences.getString(getString(R.string.tanggal), "");
        etTanggalDialog.setText(stanggal);

        final String kddokter = mPreferences.getString(getString(R.string.kd_dokter), "");
        kdDokterDialog.setText(kddokter);

        final String kdpoli = mPreferences.getString(getString(R.string.kd_poli), "");
        kdPoliDialog.setText(kdpoli);

        final String spraktek = mPreferences.getString(getString(R.string.praktek), "");
        praktekDialog.setText(spraktek);

        String idPoli = mPreferences.getString(getString(R.string.id_poli),"");
        idPoliDialog.setText(idPoli);

        String jam = mPreferences.getString(getString(R.string.jam),"");


        String hari = mPreferences.getString(getString(R.string.hari),"");
        tvHariDialog.setText(hari);



        String penjamin = mPreferences.getString(getString(R.string.penjamin), "");
        penjaminDialog.setText(penjamin);

        String fotoBpjs = mPreferences.getString(getString(R.string.foto_bpjs), "");
        etParamBpjs.setText(fotoBpjs);

        String fotoSurat = mPreferences.getString(getString(R.string.surat_kt), "");
        etParamSurat.setText(fotoSurat);

        String fotoktp = mPreferences.getString(getString(R.string.foto_ktp),"");
        etParamKtp.setText(fotoktp);




        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_ANTRIAN_CHUSNUN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Pesan dari Server", jsonObject.toString());

                        pb.setVisibility(GONE);
                        btnClose.setVisibility(VISIBLE);


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            //JSONArray heroArray = obj.getJSONArray("data");

                            //now looping through all the elements of the json array
                            //for (int i = 0; i < heroArray.length(); i++) {
                            //getting the json object of the particular index inside the array
                            //JSONObject hObject = heroArray.getJSONObject(i);

                            //creating a hero object and giving them the values from json object
                            //NomorAntri antri = new NomorAntri(

                            //hObject.getString("antrian")

                            //);


                            String getantrian = obj.getString("antrian");

                            EditText etJamAntrian = dialog.findViewById(R.id.et_jam_antrian);
                            EditText nomorAntrianIdJadwalIdDokterNorm = dialog.findViewById(R.id.et_nomor_antri_dialog2);
                            nomorAntrianIdJadwalIdDokterNorm.setVisibility(GONE);
                            EditText nomorantrianthokDialog = dialog.findViewById(R.id.et_nomor_antrian_thok);



                                /*String jam = mPreferences.getString(getString(R.string.jam), "");
                                StringTokenizer tokens = new StringTokenizer(jam, "-");
                                String jammulai = tokens.nextToken();
                                double ijammulai = Double.parseDouble(jammulai);
                                etJamAntrian.setText(Double.toString(ijammulai));
                                etJamAntrian.setVisibility(GONE);*/




                            // if (ijammulai >= 13) {

                            nomorantrianthokDialog.setText(getantrian);

                            //nomorAntrianIdJadwalIdDokterNorm.setText(getantrian + "-" + idJadwal + "-" + iddokter + "-" + norm);
                            nomorAntrianIdJadwalIdDokterNorm.setText(getantrian + "-" + idJadwal + "-" + kddokter + "-" + kdpoli + "-" + norm);

                            ImageView qrcode = dialog.findViewById(R.id.qr_code2);
                            String stNomor = nomorAntrianIdJadwalIdDokterNorm.getText().toString();
                            Bitmap myBitmap = QRCode.from(stNomor).bitmap();
                            qrcode.setImageBitmap(myBitmap);

                            String antrian = nomorantrianthokDialog.getText().toString();
                            mEditor.putString(getString(R.string.nomor_antrian), antrian);
                            mEditor.commit();

                            String antrianIdjadwalIddokter = nomorAntrianIdJadwalIdDokterNorm.getText().toString();
                            mEditor.putString(getString(R.string.nomor_antrian_idjadwal_iddokter), antrianIdjadwalIddokter);
                            mEditor.commit();


                                   /* String paramAntrian = mPreferences.getString(getString(R.string.nomor_antrian), "");
                                    etParamAntrian.setText(paramAntrian);

                                    String paramNobarcode = mPreferences.getString(getString(R.string.nomor_antrian_idjadwal_iddokter), "");
                                    etParamNobarcode.setText(paramNobarcode);*/


                               /* } else {

                                    nomorantrianthokDialog.setText("1B"+antri.getAntri());
                                    nomorAntrianIdJadwalIdDokterNorm.setText("1B" + antri.getAntri() + "-" + idJadwal + "-" + iddokter + "-" + norm);

                                    ImageView qrcode = dialog.findViewById(R.id.qr_code2);
                                    String stNomor = nomorAntrianIdJadwalIdDokterNorm.getText().toString();
                                    Bitmap myBitmap = QRCode.from(stNomor).bitmap();
                                    qrcode.setImageBitmap(myBitmap);

                                    String antrian = nomorantrianthokDialog.getText().toString();
                                    mEditor.putString(getString(R.string.nomor_antrian), antrian);
                                    mEditor.commit();

                                    String antrianIdjadwalIddokter = nomorAntrianIdJadwalIdDokterNorm.getText().toString();
                                    mEditor.putString(getString(R.string.nomor_antrian_idjadwal_iddokter), antrianIdjadwalIddokter);
                                    mEditor.commit();

                                }*/

                            // }



                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                //params.put("id_dokter", idDokterDialog.getText().toString());
                //params.put("tanggal", etDateBookingDialog.getText().toString());
                params.put("norm", noRmPasienDialog.getText().toString());
                params.put("tanggal", etTanggalDialog.getText().toString());
                params.put("dokter", kdDokterDialog.getText().toString());
                params.put("poli", kdPoliDialog.getText().toString());
                params.put("praktek", praktekDialog.getText().toString());

               /* params.put("nama_pasien", namaPasienDialog.getText().toString());
                params.put("penjamin", penjaminDialog.getText().toString());
                params.put("foto_bpjs", etParamBpjs.getText().toString());
                params.put("foto_ktp", etParamKtp.getText().toString());
                params.put("foto_surat_kontrol", etParamSurat.getText().toString());
                params.put("no_antrian", etParamAntrian.getText().toString());
                params.put("no_barcode", etParamNobarcode.getText().toString());*/
                //params.put("tgl_kontrol", etDateKontrol.getText().toString());
                //params.put("email", etEmailDialog.getText().toString());


                return params;
            }


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("auth", "4NN4B3L");
                return params;
            }


        };


        RequestQueue requestQueue = Volley.newRequestQueue(DaftarEksekutifActivity.this);

        requestQueue.add(stringRequest);


        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //showSuccessBooking();
                updateDataBooking();

                dialog.dismiss();


            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void dialogPersetujuan(){


        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.booking_konfirmasi_dialog);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        //DEFINE DIALOG COMPONENTS
        EditText namaPasienDialog = dialog.findViewById(R.id.et_nama_konfirmasi);
        EditText alamatPasienDialog = dialog.findViewById(R.id.et_alamat_konfirmasi);
        EditText namaPoliDialog = dialog.findViewById(R.id.et_poli_konfirmasi);
        EditText namaDokterDialog = dialog.findViewById(R.id.et_dokter_konfirmasi);
        TextView penjaminDialog = dialog.findViewById(R.id.tv_penjamin_konfirmasi);
        TextView noRmDialog = dialog.findViewById(R.id.tv_norm_konfirmasi);


        //GET VALUE FROM SHAREDPREF
        String namaPasien = mPreferences.getString(getString(R.string.pasien_baru), "");
        namaPasienDialog.setText(namaPasien);
        String alamatPasien = mPreferences.getString(getString(R.string.alamat_lengkap),"");
        alamatPasienDialog.setText(alamatPasien);
        String poli = mPreferences.getString(getString(R.string.nama_poli), "");
        namaPoliDialog.setText(poli);
        String dokter = mPreferences.getString(getString(R.string.nama_dokter), "");
        namaDokterDialog.setText(dokter);
        String norm = mPreferences.getString(getString(R.string.no_rm), "");
        noRmDialog.setText(norm);
        String penjamin = mPreferences.getString(getString(R.string.penjamin), "");
        penjaminDialog.setText(penjamin);

        Button btnYa = dialog.findViewById(R.id.bt_accept);
        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogKonfirmasiNomorAntri();
                //insertDataBooking();
                dialog.dismiss();


            }
        });


        Button btnTidak = dialog.findViewById(R.id.bt_decline);
        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();


            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void initTanggal() {
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();

            }
        });
    }

    public void errorDialogDateLimit() {

        btnSimpan.setVisibility(View.GONE);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning_daftar_poli);
        dialog.setCancelable(false);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton)dialog.findViewById(R.id.bt_close_error_tanggal))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                /*cvBpjs.setVisibility(GONE);
                cvKartu.setVisibility(GONE);
                getDataPasien();*/
                        Intent intent = new Intent(DaftarEksekutifActivity.this, JadwalListActivity.class);
                        startActivity(intent);
                        finish();

                        //showDateDialog();
                        //btnSimpan.setVisibility(GONE);
                    }
                });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void errorDialogNoData() {

        btnSimpan.setVisibility(View.GONE);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_error_tidak_ada_data);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton)dialog.findViewById(R.id.bt_close_error_tanggal))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void errorDialogLampiran() {

        btnSimpan.setVisibility(View.GONE);

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning_upload);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((AppCompatButton)dialog.findViewById(R.id.bt_close_error_tanggal))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        btnSimpan.setVisibility(GONE);
                        tvLampiranBpjs.setTextColor(Color.parseColor("#D32F2F"));
                    }
                });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void showDateDialog(){
        final Calendar cal = Calendar.getInstance();
        dpd = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar tglKontrol = new GregorianCalendar(year,month,day);

                Calendar today = new GregorianCalendar();
                today.getTime();

                Calendar limitmax = new GregorianCalendar();
                limitmax.add(Calendar.DAY_OF_MONTH, 1);

                Calendar limitmin = new GregorianCalendar();
                limitmin.add(Calendar.DAY_OF_MONTH, -7);




                etDateKontrol.setText(year + "-" + (month + 1) + "-" + (day));

                if (tglKontrol.after(limitmax)) {

                    errorDialogDateLimit();


                }

                if (tglKontrol.before(limitmin)){

                    errorDialogDateLimit();


                } else {

                    btnSimpan.setVisibility(View.VISIBLE);

                }



            }

        },cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));

        dpd.getDatePicker().setMinDate(Calendar.DAY_OF_WEEK -6);
        dpd.show();

    }

    private void showSuccessBooking(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_booking_success);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSummary.setVisibility(View.VISIBLE);
                btnSimpan.setVisibility(GONE);


                Intent listBooking = new Intent(DaftarEksekutifActivity.this, HistoryListActivity.class);
                startActivity(listBooking);
                finish();

                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void initPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(this, ALLOW_KEY)) {

                showSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } else {
            showCameraKartu();
            showCameraBpjs();
            showCameraKtp();
        }
    }

    public void initPermissionReadStorage(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (getFromPref(this, ALLOW_KEY)) {

                showStorageSettingsAlert();

            } else if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showAlert();
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSION_READ_STORAGE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult
            (int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                for (int i = 0, len = permissions.length; i < len; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale =
                                ActivityCompat.shouldShowRequestPermissionRationale
                                        (this, permission);
                        if (showRationale) {
                            showAlert();
                        } else if (!showRationale) {
                            saveToPreferences(DaftarEksekutifActivity.this, ALLOW_KEY, true);
                        }
                    }
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_done, menu);
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
}
