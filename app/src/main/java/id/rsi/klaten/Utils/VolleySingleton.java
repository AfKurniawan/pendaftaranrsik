package id.rsi.klaten.Utils;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

import static com.android.volley.VolleyLog.TAG;

public class VolleySingleton extends Application{

    //private static final String TAG = AppController.class.getSimpleName();
    private static VolleySingleton mInstance;
    RequestQueue mRequestQueue;
    private static Context mCtx;

    @Override
    public void onCreate() {
        CookieHandler.setDefault(new CookieManager());
        super.onCreate();
        mInstance = this;
    }

    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue (Request<T> req)
    {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelAllRequest(Object req)
    {
        if (mRequestQueue != null)
        {
            mRequestQueue.cancelAll(req);
        }
    }
}
