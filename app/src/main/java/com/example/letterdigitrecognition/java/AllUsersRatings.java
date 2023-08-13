package com.example.letterdigitrecognition.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letterdigitrecognition.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AllUsersRatings extends AppCompatActivity {

    RecyclerView allratingsRV ;
    LinearLayoutManager linearLayoutManager;
    AllRatings_adapter ratings_adapter;
    List<AllRatings_model> all_ratings;

    TextView averageRating ;

    int totaluser = 0 ;
    float totalRating = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users_ratings);

        allratingsRV = findViewById(R.id.all_ratings_RV);

        averageRating = findViewById(R.id.averagerate);

        fetchAllRatingData();

        //Change status bar coclor
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));


    }

    private void fetchAllRatingData() {
//        HashMap<String, Object> allratingsData = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ratings");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                collectPhoneNumbers((Map<String,Object>) snapshot.getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void collectPhoneNumbers(Map<String, Object> users) {
        ArrayList<Object> allRatings = new ArrayList<Object>();

        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            allRatings.add(singleUser);
        }

        initAllRatings(allRatings);
        initAllRatingsRecyclerView();




    }

    private void initAllRatingsRecyclerView() {
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        allratingsRV.setLayoutManager(linearLayoutManager);
        ratings_adapter = new AllRatings_adapter(all_ratings);
        allratingsRV.setAdapter(ratings_adapter);
        ratings_adapter.notifyDataSetChanged();
    }

    private void initAllRatings(ArrayList<Object> allRatings) {
        all_ratings = new ArrayList<>();
        for(int i=0;i<allRatings.size();i++){
            HashMap rating = (HashMap) allRatings.get(i);
            totaluser++;
            totalRating += Float.parseFloat((String) Objects.requireNonNull(rating.get("rating")));
            all_ratings.add(new AllRatings_model((String) rating.get("name"), (String) rating.get("rating")));
        }
        DecimalFormat dec = new DecimalFormat("#0.00");
        averageRating.setText(String.valueOf(dec.format(totalRating/totaluser)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AllUsersRatings.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}