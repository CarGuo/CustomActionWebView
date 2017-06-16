package com.shuyu;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    View mLadingView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLadingView = findViewById(R.id.loadingView);
        final CustomActionWebView customActionWebView = (CustomActionWebView)findViewById(R.id.customActionWebView);


        List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");

        customActionWebView.setWebViewClient(new CustomWebViewClient());
        customActionWebView.setActionList(list);
        customActionWebView.linkJSInterface();
        customActionWebView.getSettings().setBuiltInZoomControls(true);
        customActionWebView.getSettings().setDisplayZoomControls(false);
        customActionWebView.getSettings().setJavaScriptEnabled(true);
        customActionWebView.getSettings().setDomStorageEnabled(true);

        customActionWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                customActionWebView.loadUrl("http://www.jianshu.com/p/b32187d6e0ad");
            }
        }, 1000);
    }


    private class CustomWebViewClient extends WebViewClient {

        private boolean mLastLoadFailed = false;

        @Override
        public void onPageFinished(WebView webView, String url) {
            super.onPageFinished(webView, url);
            if (!mLastLoadFailed) {
                CustomActionWebView customActionWebView = (CustomActionWebView) webView;
                customActionWebView.linkJSInterface();
                mLadingView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onPageStarted(WebView webView, String url, Bitmap favicon) {
            super.onPageStarted(webView, url, favicon);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            mLastLoadFailed = true;
            mLadingView.setVisibility(View.GONE);
        }
    }
}
