package com.apps.wound_fairy.uis.activity_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ActivityAppBinding;
import com.apps.wound_fairy.uis.activity_base.BaseActivity;
import com.apps.wound_fairy.uis.activity_payment.PaypalwebviewActivity;

public class AppActivity extends BaseActivity {
    private ActivityAppBinding binding;
    private String type;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_app);
        getDataFromIntent();
        initView();
    }
    private void getDataFromIntent(){
        Intent intent=getIntent();
        type = intent.getStringExtra("data");
        url=intent.getStringExtra("url");

    }

    private void initView() {
        binding.setLang(getLang());

        setUpWebView();
    }
    private void setUpWebView() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        binding.webView.getSettings().setBuiltInZoomControls(false);
        binding.webView.loadUrl(url);
        binding.webView.setWebViewClient(new WebViewClient() {
                                             @Override
                                             public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                                 super.onPageStarted(view, url, favicon);
                                             }

                                             @Override
                                             public void onPageFinished(WebView view, String url) {
                                                 binding.progBar.setVisibility(View.GONE);
                                                 binding.webView.setVisibility(View.VISIBLE);


                                             }

                                             @Override
                                             public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                                 super.onReceivedError(view, request, error);
                                                   binding.webView.setVisibility(View.INVISIBLE);
                                             }

                                             @Override
                                             public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                                                 super.onReceivedHttpError(view, request, errorResponse);
                                                 binding.webView.setVisibility(View.INVISIBLE);
                                             }
                                         }

        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.webView.onPause();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.webView.onResume();
    }


    public void back() {
        finish();
    }
}