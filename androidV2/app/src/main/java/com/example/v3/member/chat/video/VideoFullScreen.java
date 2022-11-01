package com.example.v3.member.chat.video;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.v3.R;

public class VideoFullScreen extends AppCompatActivity {

    static final String TAG = "VideoFullScreen";

    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mlayout_chat_video_full_screen);
        webView = findViewById(R.id.full_video);

        String link = getIntent().getStringExtra("link");
        Log.d(TAG, link);


        webView.loadUrl(link);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.getSettings().setJavaScriptEnabled(true);

    }
}
