package id.rsi.klaten.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import id.rsi.klaten.Activity.LoginActivity;


public class SharedPrefManager {



        private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
        private static final String KEY_NOREK = "keynorek";
        private static final String KEY_NAMA = "keynama";
        private static final String KEY_ALAMAT = "keyalamat";
        private static final String KEY_ID = "keyid";
        // private static final String KEY_SALDO = "keysaldo";
        private static final String KEY_FOTO = "keyfoto";

   /* private static final String SHARED_PREF_HISTO = "myhistory";
    private static final String KEY_VALUE = "keyvalue";
    private static final String KEY_TANGGAL = "keytanggal";
    private static final String KEY_IDNAS = "keynasabah";
    private static final String KEY_JENIS = "keyjenis";
    private static final String KEY_KETERANGAN = "keketerangan";*/





        private static SharedPrefManager mInstance;
        private static Context mCtx;

        private SharedPrefManager(Context context) {
            mCtx = context;
        }

        public static synchronized SharedPrefManager getInstance(Context context) {
            if (mInstance == null) {
                mInstance = new SharedPrefManager(context);
            }
            return mInstance;
        }



        public boolean isLoggedIn() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            return sharedPreferences.getString(KEY_NOREK, null) != null;
        }

        //this method will give the logged in user


        public void logout() {
            SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
        }
}