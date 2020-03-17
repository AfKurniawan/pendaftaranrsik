package id.rsi.klaten.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import id.rsi.klaten.Tools.Tools;
import id.rsi.klaten.Utils.Const;
import id.rsi.klaten.Utils.MyWebViewClient;
import id.rsi.klaten.rsik.R;

public class WebAntrianActivity extends AppCompatActivity {

    private WebView mWebView;
    private ActionBar actionBar;
    private Toolbar toolbar;
    boolean errorOccurred = false; // Global variable
    ProgressDialog progressDialog;


    @Override
    @SuppressLint("SetJavaScriptEnabled")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_antrian);

        progressDialog = new ProgressDialog(WebAntrianActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        initToolbar();
        mWebView = findViewById(R.id.mywebview);
        mWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        // REMOTE RESOURCE
         mWebView.loadUrl(Const.URL_ANTRIAN_WEBVIEW);
        //mWebView.loadUrl("file:///android_asset/index.html");
        // mWebView.setWebViewClient(new MyWebViewClient());


        mWebView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                try {
                    progressDialog.dismiss();

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                progressDialog.dismiss();
                //Toast.makeText(WebAntrianActivity.this, "Manuk", Toast.LENGTH_SHORT).show();
                view.loadUrl("file:///android_asset/index.html");

            }


        });



//        mWebView.setWebViewClient(new WebViewClient() {
//            @Override
//            public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                super.onPageStarted(view, url, favicon);
//                //hideError();
//                //showProgress();
//                Toast.makeText(WebAntrianActivity.this, "start loading", Toast.LENGTH_SHORT).show();
//                errorOccurred=false;
//            }
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                if (!errorOccurred) {
//                    Log.d("onPage Fininsde", "erorororororr");
//                   // hideError();
//                    //mWebView.loadUrl("file:///android_assets/index.html");
//                }
//                //hideProgress();
//                //Toast.makeText(WebAntrianActivity.this, "Web view was loaded", Toast.LENGTH_SHORT).show();
//
//                view.loadUrl("file:///android_assets/index.html");
//                Log.d("onPage Fininsde", "Load HTML ra iki");
//            }
//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request,
//                                        WebResourceError error) {
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                    Toast.makeText(WebAntrianActivity.this,
//                            "WebView Error" + error.getDescription(),
//                            Toast.LENGTH_SHORT).show();
//                }
//
//                super.onReceivedError(view, request, error);
//
//            }
//        });



    }


private void initToolbar() {

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_webview);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.toolbar_webview);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    Tools.setSystemBarColor(this, R.color.green_800);
}

    @Override
    public void onBackPressed() {
        if(mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
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
