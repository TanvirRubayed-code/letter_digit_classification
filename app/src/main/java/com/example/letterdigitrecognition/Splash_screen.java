package com.example.letterdigitrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class Splash_screen extends AppCompatActivity {

    TextView splashText ;
    Animation SplashTextAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SplashTextAnimation = AnimationUtils.loadAnimation(this,R.anim.splash_text);

        SplashTextAnimation.setDuration(1500);

        splashText =(TextView)findViewById(R.id.splashText);

        splashText.setAnimation(SplashTextAnimation);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(getApplicationContext(),login_activity.class);
                startActivity(intent);
                finish();
            }
        },1600);



        //Change status bar coclor
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.white));
        }



    }
}