package com.coded.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class YoutubeLiveStream extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView  = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(webView,params);
        String d = getIntent().getStringExtra("data");
        if(d == null)
        {
            finish();
            return ;
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.startsWith("www.youtube.Com") ){
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;}view.loadUrl(url); return false; } });
        webView.loadUrl(d);
    }
}
