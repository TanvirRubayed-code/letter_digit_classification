package com.example.letterdigitrecognition.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letterdigitrecognition.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RatingActivity extends AppCompatActivity {

    TextView noThanksButton ;
    RatingBar ratingbar;
    Button submitRating;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        noThanksButton = findViewById(R.id.no_thanks_text);
        ratingbar=(RatingBar)findViewById(R.id.ratingBar);
        submitRating=(Button)findViewById(R.id.submit_rating);



        submitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating=String.valueOf(ratingbar.getRating());
                setRatingTodatabase(rating);
            }
        });



        noThanksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RatingActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });




        //Change status bar coclor
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.rating_background));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RatingActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void setRatingTodatabase(String rating) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        HashMap<String, String > ratingData = new HashMap<>();

        HashMap<String,String> userDetails = new HashMap<String,String>();

        if(user != null ) {
//            progressBar.setVisibility(View.VISIBLE);
            String uid = user.getUid();
            mDatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
//                        progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        for (DataSnapshot data : task.getResult().getChildren()){
                            userDetails.put(data.getKey(),(String) data.getValue());
                        }
                        ratingData.put("name",userDetails.get("name"));
                        ratingData.put("rating",rating);
                        uploadDataToDatabase(ratingData,user.getUid());
                    }
                }
            });



        }


    }

    private void uploadDataToDatabase(HashMap<String, String> ratingData, String uid) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        database.child("ratings").child(uid).setValue(ratingData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RatingActivity.this, "Rating update successfully", Toast.LENGTH_SHORT).show();
                            openAllRatings();

                        }
                        else {
                            Toast.makeText(RatingActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RatingActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void openAllRatings() {
        Intent intent = new Intent(RatingActivity.this, AllUsersRatings.class);
        startActivity(intent);
        finish();
    }


}