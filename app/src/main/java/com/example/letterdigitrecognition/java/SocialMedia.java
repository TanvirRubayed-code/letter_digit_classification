package com.example.letterdigitrecognition.java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.letterdigitrecognition.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class SocialMedia extends AppCompatActivity {

    //    --------------recycler view to show all post of users -----------------
    RecyclerView allPostRecyclerView ;
    LinearLayoutManager linearLayoutManager;
    All_post_adapter post_adapter;
    List<All_post_model> allPost;
    
    
    RecyclerView postRV ;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        allPostRecyclerView = findViewById(R.id.all_post_recyclerview);



        








        //--------------------------initialization and set data for all post  --------------------
        initAllPost();
        initAllPostRecyclerView();


        //Change status bar coclor
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void initAllPostRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        allPostRecyclerView.setLayoutManager(linearLayoutManager);
        post_adapter = new All_post_adapter(allPost);
        allPostRecyclerView.setAdapter(post_adapter);
        post_adapter.notifyDataSetChanged();
    }

    private void initAllPost() {
        allPost = new ArrayList<>();
        allPost.add(new All_post_model("Tanvir Rubayed","University of Dhaka is going to start its exam for their first year exam",false));
        allPost.add(new All_post_model("Fahim Foysal","University of Dhaka is going to start its exam for their first year exam",true));
        allPost.add(new All_post_model("Farjana Abedin","University of Dhaka is going to start its exam for their first year exam",true));
        allPost.add(new All_post_model("Saifuddin Sabbir","University of Dhaka is going to start its exam for their first year exam",false));
        allPost.add(new All_post_model("Moontasir Manum","University of Dhaka is going to start its exam for their first year exam",true));
        allPost.add(new All_post_model("Touhidul Islam","University of Dhaka is going to start its exam for their first year exam",false));
        allPost.add(new All_post_model("Monir Khan","University of Dhaka is going to start its exam for their first year exam",false));


    }
}