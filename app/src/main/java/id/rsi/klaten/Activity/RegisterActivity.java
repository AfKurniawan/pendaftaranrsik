package id.rsi.klaten.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.SessionManager;
import id.rsi.klaten.Utils.VolleySingleton;
import id.rsi.klaten.rsik.R;

public class RegisterActivity extends AppCompatActivity {


    String
            passwordHolder,
            emailHolder,
            namaHolder,
            levelHolder,
    gNamaHolder,
    gEmailHolder,
    gPassHolder,
    gUserLevelHolder;

    EditText
             etPassword,
             etEmail,
             etNamaLengkap,
             etTanggal,
             etUserLevel,
             etGmail,
             etGpass,
             etGuserLever,
             etGnamaLengkap;


    Boolean CheckEditText;

    ProgressBar pd;

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    SharedPreferences sp;
    private SharedPreferences.Editor mEditor;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initForm();

        keDokterListActivity();
        keLoginActivity();
        initGoogleRegister();
        //registerData();
        initSharedPref();

    }

    public void initSharedPref(){

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp = getSharedPreferences("rsiklaten", Context.MODE_PRIVATE);
        mEditor = sp.edit();

    }

    public void initGoogleRegister(){

        Button signInButton = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String email = acct.getEmail();

            Log.e(TAG, personName + email);


            etGnamaLengkap.setText(personName);
            etGmail.setText(email);
            etGpass.setText("Default01201" + personName);
            etGuserLever.setText("user_biasa");

            //registerDataGoogle();
            CekGoogleEmail();


        }
    }

    private void initForm(){

        pd = findViewById(R.id.reg_progress);
        etNamaLengkap = findViewById(R.id.et_nama_lengkap);
        etEmail = findViewById(R.id.reg_email);
        etPassword = findViewById(R.id.et_password);
        etUserLevel = findViewById(R.id.reg_user_level);
        etUserLevel.setText("user_biasa");


        etGnamaLengkap = findViewById(R.id.et_g_nama);
        etGmail = findViewById(R.id.et_g_email);
        etGpass = findViewById(R.id.et_g_pass);
        etGuserLever = findViewById(R.id.et_g_user_level);

        etUserLevel.setVisibility(View.GONE);

        etGnamaLengkap.setVisibility(View.GONE);
        etGmail.setVisibility(View.GONE);
        etGpass.setVisibility(View.GONE);
        etGuserLever.setVisibility(View.GONE);


    }



    private void keDokterListActivity(){

        Button btRegister = findViewById(R.id.btn_register);
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                CekEmail();


            }
        });
    }


    public void CekGoogleEmail() {

        final String gmail = etGmail.getText().toString();

        pd.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CEK_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.setVisibility(View.GONE);

                        if(response.equalsIgnoreCase("Berhasilnull")) {

                            UserExistDialog();


                        } else {

                            registerDataGoogle();
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
                params.put("alamat_email", gmail);
                // params.put("password", passwordHolder);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    public void CekEmail() {

        final String mail = etEmail.getText().toString();

        pd.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.CEK_EMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.setVisibility(View.GONE);

                        if(response.contentEquals("Berhasilnull")) {

                            UserExistDialog();

                    } else {

                            registerData();
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
                params.put("alamat_email", mail);
               // params.put("password", passwordHolder);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }


    private void registerData(){


        pd.setVisibility(View.VISIBLE);

        //CekEmail();

        final String nama = etNamaLengkap.getText().toString();
        final String username = etEmail.getText().toString();
        final String password = etPassword.getText().toString();
        final String level = etUserLevel.getText().toString();




        if (TextUtils.isEmpty(nama)) {
            etNamaLengkap.setError(getString(R.string.e_form_user_kosong));
            etNamaLengkap.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(username)) {
            etEmail.setError(getString(R.string.e_form_user_kosong));
            etEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.e_form_pass_kosong));
            etPassword.requestFocus();
            return;
        }

        //pd.setVisibility(View.VISIBLE);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        pd.setVisibility(View.GONE);

                        try {

                            JSONObject res = new JSONObject(ServerResponse);
                            Toast.makeText(RegisterActivity.this, "pesan : " + res.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(ServerResponse.equalsIgnoreCase("Berhasilnull")) {



                            SuccessDialog();

                        }else{

                            ErrorDialog();
                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //pd.setVisibility(View.GONE);
                        //ErrorDialog();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("nama_lengkap", nama);
                params.put("alamat_email", username);
                params.put("password", password);
                params.put("level", level);

                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        requestQueue.add(stringRequest);
    }



    private void registerDataGoogle(){



        pd.setVisibility(View.VISIBLE);

        CekEmail();

        final String gnama = etGnamaLengkap.getText().toString();
        final String gmail = etGmail.getText().toString();
        final String gpass = etGpass.getText().toString();
        final String glevel = etGuserLever.getText().toString();



        if (TextUtils.isEmpty(gnama)) {
            return;
        }

        if (TextUtils.isEmpty(gmail)) {
            return;
        }

        if (TextUtils.isEmpty(gpass)) {
            return;
        }

        if (TextUtils.isEmpty(glevel)) {
            return;
        }



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String ServerResponse) {

                        pd.setVisibility(View.GONE);

                        try {
                            JSONObject res = new JSONObject(ServerResponse);
                            Toast.makeText(RegisterActivity.this, "pesan : " + res.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(ServerResponse.equalsIgnoreCase("Berhasilnull")) {

                            SuccessDialog();

                        }else{

                            ErrorDialog();
                        }

                    }


                },


                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.setVisibility(View.GONE);
                        ErrorDialog();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                params.put("nama_lengkap", gnama);
                params.put("alamat_email", gmail);
                params.put("password", gpass);
                params.put("level", glevel);

                return params;
            }

        };


        RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);

        requestQueue.add(stringRequest);
    }

    private void CheckFormKosong() {

        namaHolder = etNamaLengkap.getText().toString().trim();
        emailHolder = etEmail.getText().toString().trim();
        passwordHolder = etPassword.getText().toString().trim();
        levelHolder = etUserLevel.getText().toString().trim();

        if (TextUtils.isEmpty(namaHolder)) {


            CheckEditText = false;

        } else {


            CheckEditText = true;
        }

        if (TextUtils.isEmpty(passwordHolder)) {


            CheckEditText = false;

        } else {


            CheckEditText = true;
        }

        if (TextUtils.isEmpty(emailHolder)) {


            CheckEditText = false;

        } else {


            CheckEditText = true;
        }

    }

    public void SuccessDialog(){

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.reg_dialog_success);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_reg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String useremail = etEmail.getText().toString();
                mEditor.putString(getString(R.string.login_email), useremail);
                mEditor.commit();

                String userpass = etEmail.getText().toString();
                mEditor.putString(getString(R.string.password), userpass);
                mEditor.commit();


                Intent intent = new Intent(RegisterActivity.this, JadwalListActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);

    }




    public void ErrorDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_warning_register);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    public void UserExistDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_user_sudah_ada);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        ((AppCompatButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

 private void keLoginActivity(){
     TextView tvLogin = (TextView) findViewById(R.id.toLogin);
     tvLogin.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
             startActivity(intent);
         }
     });

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
