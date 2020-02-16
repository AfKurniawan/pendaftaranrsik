package id.rsi.klaten.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;
import id.rsi.klaten.Model.User;
import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.SessionManager;
import id.rsi.klaten.Utils.VolleySingleton;
import id.rsi.klaten.rsik.R;

import static android.view.View.GONE;

public class GantiPasswordActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    TextView tvNama;
    RequestQueue queue;
    JSONObject jsonObject;
    Button btnGanti;
    ProgressBar pb;

    private ProgressDialog dialog = null;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganti_password);

        sessionManager = new SessionManager(getApplicationContext());

        initView();
        initToolbar();

        getUserDetail();
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GANTI PASSWORD");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Tools.setSystemBarColor(this, R.color.blue_A700);

    }

    void initView(){

        etEmail = findViewById(R.id.et_email);
        etEmail.setFocusable(false);
        etPassword = findViewById(R.id.et_password);
        tvNama = findViewById(R.id.tv_nama);

        String email = sessionManager.getSavedUserName();

        etEmail.setText(email);
        pb = findViewById(R.id.progress_bar);

        btnGanti = findViewById(R.id.btn_ganti);

        pb.setVisibility(GONE);


        btnGanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gantiPassword();
            }
        });


    }

    public void getUserDetail(){


        String email = sessionManager.getSavedUserName();

        etEmail.setText(email);


            queue = VolleySingleton.getInstance(this).getRequestQueue();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_USER_DETAIL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                           // Log.e("Pesan dari Server", jsonObject.toString());


                            try {

                                JSONObject obj = new JSONObject(response);
                                JSONArray heroArray = obj.getJSONArray("data");


                                for (int i = 0; i < heroArray.length(); i++) {


                                    //getting the json object of the particular index inside the array
                                    JSONObject usrObj = heroArray.getJSONObject(i);

                                    //creating a hero object and giving them the values from json object
                                    User usr = new User(

                                            usrObj.getString("alamat_email"),
                                            usrObj.getString("nama_lengkap"),
                                            usrObj.getString("no_telpon"),
                                            usrObj.getString("alamat_lengkap")


                                    );

                                    tvNama.setText(usr.getNama_lengkap());
//                                    etAlamat.setText(pasien.getAlamat());
//                                    etJenkel.setText(pasien.getJenis_kelamin());


                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            errorDialogLampiran();
//                            cvDataPasien.setVisibility(View.GONE);
//                            cvKartu.setVisibility(View.GONE);
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("email", sessionManager.getSavedUserName());

                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);

    }


    public void gantiPassword(){


        String email = sessionManager.getSavedUserName();
        etEmail.setText(email);

        pb.setVisibility(View.VISIBLE);


        queue = VolleySingleton.getInstance(this).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.GANTI_PASSWORD_URL,

                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        if(response.contains("success")) {

                            pb.setVisibility(View.GONE);

                            showSuccessBooking();


                            //showSuccessBooking();
                        }


                    }


                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                            errorDialogLampiran();
//                            cvDataPasien.setVisibility(View.GONE);
//                            cvKartu.setVisibility(View.GONE);
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", sessionManager.getSavedUserName());
                params.put("password", etPassword.getText().toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void showSuccessBooking(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_ganti_passsword_success);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent listBooking = new Intent(GantiPasswordActivity.this, JadwalPoliRegulerBesok.class);
                startActivity(listBooking);
                finish();

                dialog.dismiss();

            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
