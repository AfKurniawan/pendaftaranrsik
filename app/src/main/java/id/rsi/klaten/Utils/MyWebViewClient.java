package id.rsi.klaten.Utils;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressWarnings("unused")
public class MyWebViewClient extends WebViewClient {

//    @Override
//    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        Uri uri = Uri.parse(url);
//        if (uri.getHost() != null && uri.getHost().contains("103.247.9.253/antri")) {
//            return false;
//        }
//
//        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//        view.getContext().startActivity(intent);
//        return true;
//    }


        WebView mWebview;
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            mWebview.loadUrl("file:///assets/index.html");

        }

}
