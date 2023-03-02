package com.example.letterdigitrecognition.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.letterdigitrecognition.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocialMedia extends AppCompatActivity {

    EditText createPost ;
    Button submitButton ;

    //    --------------recycler view to show all post of users -----------------
    RecyclerView allPostRecyclerView ;
    LinearLayoutManager linearLayoutManager;
    All_post_adapter post_adapter;
    List<All_post_model> allPost;
    
    
    RecyclerView postRV ;

    int counter = 0;


    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();

    String uid , name, imageURL;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        allPostRecyclerView = findViewById(R.id.all_post_recyclerview);
        createPost = findViewById(R.id.create_post_edittext);
        submitButton = findViewById(R.id.submit_post_button);


//        ----------collect uid of user from database ----------------
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        fetchAllPosts();

        fetchUserAllData();



        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String post = createPost.getText().toString();
                long currentTime = System.currentTimeMillis();
                String postID = String.valueOf(currentTime);

                if (post.length()>0){
                    Toast.makeText(SocialMedia.this, ""+post, Toast.LENGTH_SHORT).show();
                    HashMap<String, String > postMap = new HashMap<>();
                    postMap.put("post",post);
                    postMap.put("likes", "0");
                    postMap.put("likeFlag","0");
                    postMap.put("name",name);
                    postMap.put("imageurl",imageURL);

                    mdatabase.child("posts").child(postID).setValue(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            createPost.setText("");
                            Toast.makeText(SocialMedia.this, "Post added to database", Toast.LENGTH_SHORT).show();
                        }
                    });

                    HashMap<String, String > likesflag = new HashMap<>();
                    likesflag.put("likeFlag","0");

                    mdatabase.child("likes").child(postID).child(uid).setValue(likesflag);


                }
            }
        });





        //Change status bar coclor
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.statusbar));
    }

    private void fetchUserAllData() {
        mdatabase.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue(String.class);
                imageURL = snapshot.child("propic").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void fetchAllPosts() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                AllPostsOfDatabase((Map<String,Object>) snapshot.getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }

    private void AllPostsOfDatabase(Map<String, Object> posts) {
        ArrayList<Object> allPosts = new ArrayList<Object>();
        ArrayList<String> postIds = new ArrayList<>();

        for (Map.Entry<String, Object> entry : posts.entrySet()){
            //Get user map
            Map singlepost = (Map) entry.getValue();
            postIds.add(entry.getKey());
            //Get phone field and append to list
            allPosts.add(singlepost);
        }


        //--------------------------initialization and set data for all post  --------------------
        initAllPost(allPosts,postIds);
        initAllPostRecyclerView();

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

    private void initAllPost(ArrayList<Object> allPosts, ArrayList<String> postIds) {
        allPost = new ArrayList<>();


        for(int i=allPosts.size()-1;i>=0;i--){
            HashMap post = (HashMap) allPosts.get(i);
            String userId = (String) post.get("uid");

            allPost.add(new All_post_model(this, uid,postIds.get(i),(String) post.get("name"), (String) post.get("post"), (String) post.get("imageurl"),  Integer.parseInt(String.valueOf(post.get("likeFlag"))), Integer.parseInt(String.valueOf(post.get("likes")))));

        }

    }


}