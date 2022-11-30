package com.example.TAppbFATIH;

import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Webview extends AppCompatActivity {

    WebView myWebView;
    LinearLayout lay_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);

        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("http://commiserable-sunset.000webhostapp.com/index.php");
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

    }

}
