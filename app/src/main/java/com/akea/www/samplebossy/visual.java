package com.akea.www.samplebossy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class visual extends AppCompatActivity {
    WebView visualClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);

        visualClip = (WebView) findViewById(R.id.visualClip);
        visualClip.setWebViewClient(new WebViewClient());
        visualClip.loadUrl("https://app.klipfolio.com/published/7206b4a72f916424ecee988026c1179d/sales-opportunities");
    }
}
