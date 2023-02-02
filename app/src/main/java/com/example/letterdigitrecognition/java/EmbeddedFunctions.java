package com.example.letterdigitrecognition.java;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import com.example.letterdigitrecognition.R;

public class EmbeddedFunctions extends AppCompatActivity {


    private WebView googleMapWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_embedded_functions);


        String iframe = "<iframe src=\"https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3686.8685955588435!2d91.78549061450578!3d22.471571785234858!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x30acd79724ebe62b%3A0x8ee8ca561536f410!2sIt%20faculty!5e0!3m2!1sen!2sbd!4v1675233007379!5m2!1sen!2sbd\" width=\"400\" height=\"400\" style=\"border:0;\" allowfullscreen=\"\" loading=\"lazy\" referrerpolicy=\"no-referrer-when-downgrade\"></iframe>";
        googleMapWebView = (WebView) findViewById(R.id.googlemap_webView);
        googleMapWebView.getSettings().setJavaScriptEnabled(true);
        googleMapWebView.loadData(iframe, "text/html", "utf-8");





        //Change status bar coclor
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));

    }
}