package com.example.letterdigitrecognition.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letterdigitrecognition.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    MaterialToolbar materialToolbar;
    NavigationView navigationView ;
    CardView Cardview1 , CurrentLocation ,socialMediaCardView, embeddedFunctions, gsapCardView ;

    ImageView drawerProfileImage ;
//    CircleImageView drawerProfileImage ;
    TextView drawerName, drawerUsername ;
    ProgressBar progressBar ;
    RelativeLayout relativeLayout;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    SharedPreferences sharedPreferences ;



    private BroadcastReceiver broadcastReceiver;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        materialToolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Cardview1 = findViewById(R.id.cardview_1);
        socialMediaCardView = findViewById(R.id.social_media_cardview);
        gsapCardView = findViewById(R.id.gsap_cardview);
        embeddedFunctions = findViewById(R.id.embedded_functions);
        CurrentLocation = findViewById(R.id.location_card);


        progressBar = findViewById(R.id.login_progress_home);
        relativeLayout = findViewById(R.id.progressbar_relative_layout);



        sharedPreferences = this.getSharedPreferences("pass", Context.MODE_PRIVATE);


//--------------------to set name and username into header --------------------

        NavigationView navView = findViewById(R.id.nav_view);
        View header = navView.getHeaderView(0);
        drawerName = header.findViewById(R.id.drawer_name);
        drawerUsername = header.findViewById(R.id.drawer_username);
        drawerProfileImage = header.findViewById(R.id.drawer_image);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();




        //-------------- read name and user name from database ------------

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null ) {
            progressBar.setVisibility(View.VISIBLE);
            relativeLayout.bringToFront();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            String uid = user.getUid();
            HashMap<String,String> userDetails = new HashMap<String,String>();
//            drawerName.setText(uid);
            mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        for (DataSnapshot data : task.getResult().getChildren()){
                            userDetails.put(data.getKey(),(String) data.getValue());
                        }
                        drawerName.setText(""+userDetails.get("name"));
                        drawerUsername.setText(userDetails.get("username"));
                        Picasso.get().load(userDetails.get("propic")).into(drawerProfileImage);

                        progressBar.setVisibility(View.INVISIBLE);

                    }
                }
            });



        }





        //         -----------------Internet connection check a            drawerName.setText(name);nd show alert dialog ----------

        broadcastReceiver = new NetworkBroadcast();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));



        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.home){
                    if(drawerLayout.isDrawerOpen(GravityCompat.START)){
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
                if(id==R.id.logoutmenu){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("savepass", false);
                    editor.apply();

                    mAuth.signOut();
                    startActivity(new Intent(getApplicationContext(), login_activity.class));
                    finish();
                    Toast.makeText(HomeActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                }
                if(id==R.id.rate_us){
                    Intent intent = new Intent(getApplicationContext(), com.example.letterdigitrecognition.java.RatingActivity.class);
                    startActivity(intent);
                    finish();
                }
                if(id==R.id.my_profile){
                    Intent intent = new Intent(getApplicationContext(), com.example.letterdigitrecognition.java.MyProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                if(id==R.id.all_ratings){
                    Intent intent = new Intent(getApplicationContext(), com.example.letterdigitrecognition.java.AllUsersRatings.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });


        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });
        
        Cardview1.setOnClickListener(this);
        socialMediaCardView.setOnClickListener(this);
        gsapCardView.setOnClickListener(this);
        CurrentLocation.setOnClickListener(this);
        embeddedFunctions.setOnClickListener(this);

        //Change status bar coclor
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));


    }


    //    -----------this method is called for unregisteredReceiver of internet connection check --------------

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.cardview_1){
            Intent intent = new Intent(HomeActivity.this, com.example.letterdigitrecognition.kotlin.ClassifyDigitActivity.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.social_media_cardview){
            Intent intent = new Intent(this, com.example.letterdigitrecognition.java.SocialMedia.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.location_card){
            Intent intent = new Intent(this, com.example.letterdigitrecognition.java.CurrentLocation.class);
            startActivity(intent);
        }
        if(view.getId()==R.id.embedded_functions){
            Intent intent = new Intent(this, com.example.letterdigitrecognition.java.EmbeddedFunctions.class);
            startActivity(intent);
        }if(view.getId()==R.id.gsap_cardview){
            Intent intent = new Intent(this, com.example.letterdigitrecognition.java.GSAP_Activity.class);
            startActivity(intent);
        }

    }
}