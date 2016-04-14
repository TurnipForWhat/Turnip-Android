package turnip.turnip;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.webkit.WebView;

import java.net.URL;

public class WebViewChat extends Activity {
    static final String TAG = "WebViewChat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_chat);

        Intent intent = getIntent();
        String withUser = intent.getStringExtra("with");

        WebView webview = (WebView) findViewById(R.id.webView);
        webview.getSettings().setJavaScriptEnabled(true);
        try {
            webview.loadUrl(API.API_URL + "/chat/" + API.authkey + "/" + withUser);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
