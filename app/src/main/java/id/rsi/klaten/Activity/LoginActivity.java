package id.rsi.klaten.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import id.rsi.klaten.Fcm.FcmVolley;
import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.SessionManager;
import id.rsi.klaten.Utils.VolleySingleton;
import id.rsi.klaten.rsik.R;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private ProgressDialog pDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    private SharedPreferences.Editor mEditor;

    private Boolean saveLogin;


    private View parent_view;

    EditText etEmail, etPassword, etGoogleEmail, etGooglePass;
    Button btnLogin, signInButton;
    ProgressBar progressBar;

    SharedPreferences sp;
    private SessionManager sessionManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();


        sessionManager = new SessionManager(getApplicationContext());

        if (sessionManager.isUserLogin()) {
            Intent i = new Intent(this, DashboardActivity.class);
            startActivity(i);
            finish();

        }

            setContentView(R.layout.activity_login);
            parent_view = findViewById(android.R.id.content);

            etEmail = findViewById(R.id.et_email);
            etPassword = findViewById(R.id.et_password);

            etGoogleEmail = findViewById(R.id.et_g_email);
            etGooglePass = findViewById(R.id.et_g_pass);
            progressBar =  findViewById(R.id.login_progress);

            etGoogleEmail.setVisibility(View.GONE);
            etGooglePass.setVisibility(View.GONE);

            btnLogin = findViewById(R.id.bt_login);
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (etEmail.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Please enter your user name...", Toast.LENGTH_SHORT).show();
                    } else if (etPassword.getText().toString().equals("")) {
                        Toast.makeText(LoginActivity.this, "Please enter your password...", Toast.LENGTH_SHORT).show();
                    }  else {
                        sendToken();
                    }

                }
            });




        Tools.setSystemBarColor(this, android.R.color.white);
        Tools.setSystemBarLight(this);



        //initComponent();

        initGoogleLogin2();

        initSharedPref();



        ((View) findViewById(R.id.tv_btn_daftar)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(parent_view, "Sign up for an account", Snackbar.LENGTH_SHORT).show();
                keRegisterActivity();
            }
        });
    }


    public void sendToken() {

        progressBar.setVisibility(View.VISIBLE);

        final String token = SessionManager.getInstance(this).getDeviceToken();
        final String email = etEmail.getText().toString();

        if (token == null) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Token not generated", Toast.LENGTH_LONG).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_REGISTER_TOKEN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Pesan dari Server", response);

                        progressBar.setVisibility(View.GONE);

                        try {

                            JSONObject obj = new JSONObject(response);
                            Toast.makeText(LoginActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();


                            userLogin();

                            //menyimpan value id user ke shared preference
                            String iduser = etEmail.getText().toString();
                            mEditor.putString("userEmail", iduser);
                            mEditor.commit();

                        } catch (
                                JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("token", token);
                return params;
            }
        };

        FcmVolley.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void initSharedPref(){

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp = getSharedPreferences("rsiklaten", Context.MODE_PRIVATE);
        mEditor = sp.edit();

    }

    public void initGoogleLogin2(){


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
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email);
            etGoogleEmail.setText(email);
            etGooglePass.setText("Default01201" + personName);

            userLoginGoogle();

        }
    }

    private void displayProgressDialog() {
        pDialog.setMessage("Logging In.. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

    }



    private void hideProgressDialog() {
        pDialog.dismiss();
    }





    public void userLogin() {

        sessionManager.setSavedPassword(etPassword.getText().toString());
        sessionManager.setSavedUserName(etEmail.getText().toString());




        sessionManager.setUserLoggedIn(true);

        progressBar.setVisibility(View.VISIBLE);

        final String username = etEmail.getText().toString();
        final String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            etEmail.setError(getString(R.string.e_form_user_kosong));
            etEmail.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError(getString(R.string.e_form_pass_kosong));
            etPassword.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }





        //progressBar.setVisibility(View.GONE);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.VISIBLE);

                        if(response.equalsIgnoreCase("Berhasilnull")) {

                            sendToken();

                            progressBar.setVisibility(View.GONE);

                            finish();

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();

                            progressBar.setVisibility(View.GONE);

                        } else {

                            showErrorDialog();

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
                params.put("alamat_email", username);
                params.put("password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }



    public void userLoginGoogle() {

        progressBar.setVisibility(View.VISIBLE);

        sessionManager.setSavedPassword(etGooglePass.getText().toString());
        sessionManager.setSavedUserName(etGoogleEmail.getText().toString());
        sessionManager.setUserLoggedIn(true);



        final String gmail = etGoogleEmail.getText().toString();
        final String gpass = etGooglePass.getText().toString();


        if (TextUtils.isEmpty(gmail)) {
            return;
        }

        if (TextUtils.isEmpty(gpass)) {
            return;
        }



        //progressBar.setVisibility(View.GONE);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressBar.setVisibility(View.VISIBLE);

                        if(response.equalsIgnoreCase("Berhasilnull")) {

                            progressBar.setVisibility(View.GONE);

                            finish();

                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);

                            startActivity(intent);
                            finish();

                            progressBar.setVisibility(View.GONE);

                        } else {

                            showErrorDialog();

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
                params.put("password", gpass);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }




    private void keRegisterActivity(){
        TextView tvDaftar =  findViewById(R.id.tv_btn_daftar);
        tvDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void showErrorDialog() {
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
                //  Toast.makeText(getApplicationContext(), ((AppCompatButton) v).getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                progressBar.setVisibility(View.GONE);
                /*Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(intent);*/
            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

}
