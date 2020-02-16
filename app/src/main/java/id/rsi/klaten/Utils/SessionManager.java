package id.rsi.klaten.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "login";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_LOGIN = "isLogin";

    private static final String SHARED_PREF_NAME = "FCMSharedPref";
    private static final String TAG_TOKEN = "tagtoken";

    private static SessionManager mInstance;


    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManager(Context context) {
        this.context = context;
        pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SessionManager(context);
        }
        return mInstance;
    }

    //save user name to SharedPref
    public void setSavedUserName(String userName) {
        editor.putString(KEY_USER_NAME, userName);
        editor.commit();
    }

    //retrieve username frome pref
    public String getSavedUserName() {
        return pref.getString(KEY_USER_NAME, "");
    }

    public void setSavedPassword(String pass) {
        editor.putString(KEY_PASSWORD, pass);
        editor.commit();
    }


    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
        return true;
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }

    public boolean isUserLogin() {
        return pref.getBoolean(KEY_IS_LOGIN, false);
    }

    public void setUserLoggedIn(boolean isLogin) {
        editor.putBoolean(KEY_IS_LOGIN, isLogin);
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }
}
