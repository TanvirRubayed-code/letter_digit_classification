package com.example.letterdigitrecognition.java;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.letterdigitrecognition.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class GSAP_Activity extends AppCompatActivity {

    TextView pageNumber, userName, userEmail  ;
    Button previousButton, nextButton ;
ImageView userImageView ;
    Integer pagenum ;
    String firstname, lastname ,imageurl, useremail ;

    @SuppressLint({"SetJavaScriptEnabled", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsap);



        WebView web_view = findViewById(R.id.web_view);
        pageNumber = findViewById(R.id.pagenumber);
        previousButton = findViewById(R.id.previousbutton);
        nextButton = findViewById(R.id.nextbutton);
        userName = findViewById(R.id.username);
        userEmail = findViewById(R.id.useremail);
        userImageView = findViewById(R.id.userImage);

        String currentpage = String.valueOf(pageNumber.getText());
         pagenum = Integer.parseInt(currentpage);

        if(pagenum.equals(1)){
            previousButton.setEnabled(false);
        }


       getRestAPIData(pagenum);



        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pagenum++;

                getRestAPIData(pagenum);

                pageNumber.setText(pagenum.toString());

                if(pagenum > 1) {
                    previousButton.setEnabled(true);
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pagenum--;

                getRestAPIData(pagenum);

                pageNumber.setText(pagenum.toString());


                if(pagenum.equals(1)){
                    previousButton.setEnabled(false);
                }
            }
        });



        web_view.getSettings().setJavaScriptEnabled(true);

        web_view.loadUrl("file:///android_asset/index.html");
//        web_view.loadData("<html><body>Hello, world!</body></html>",
//                "text/html", "UTF-8");
    }

    private void getRestAPIData(Integer pagenum) {

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://reqres.in/api/users/"+pagenum;

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject singleData = new JSONObject(response);

                            JSONObject data = singleData.getJSONObject("data");
                            firstname = data.getString("first_name");
                            lastname = data.getString("last_name");
                            imageurl = data.getString("avatar");
                            useremail = data.getString("email");


                            Glide.with(getApplicationContext()).load(imageurl).into(userImageView);
                            userName.setText(firstname+ " " + lastname);
                            userEmail.setText(useremail);
                            Log.e("api", "result"+singleData.getJSONObject("data").getString("avatar"));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("api", "onErrorResponse"+error.getLocalizedMessage());

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}