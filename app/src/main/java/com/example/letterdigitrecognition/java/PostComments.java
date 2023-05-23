package com.example.letterdigitrecognition.java;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.letterdigitrecognition.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostComments extends AppCompatActivity {

    EditText commentEdittext ;
    Button commentsubmit;
    private CircleImageView postUserImage ;
    TextView singlePostUsername, mainPost, likeCounter, commentCounter ;

    DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();


    String commentString, uid, currentusername, currentuserPic, postId  ;
    String username = null, main_post = null, like_counter=null, imageURL, comment_counter ;

    RecyclerView commentRv;
    LinearLayoutManager layoutManager ;
    PostComments_adapter adapter ;
    List<PostComments_model> post_comment;
    List<PostComments_model> comment_array;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_comments);

        commentsubmit = findViewById(R.id.submit_comment_button);
        commentEdittext = findViewById(R.id.comment_edittext);
        postUserImage = findViewById(R.id.single_post_image);
        singlePostUsername = findViewById(R.id.singlepost_username);
        mainPost = findViewById(R.id.single_post);
        likeCounter = findViewById(R.id.like_counter);
        commentCounter = (TextView)findViewById(R.id.single_post_comment);




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        HashMap<String,String> userDetails = new HashMap<String,String>();

        mdatabase.child("users").child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    for (DataSnapshot data : task.getResult().getChildren()){
                        userDetails.put(data.getKey(),(String) data.getValue());
                    }
                    currentusername = userDetails.get("name");
                    currentuserPic = userDetails.get("propic");
                }
            }
        });









//        -----------------get post information from post adapter ------------

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null){
            username = bundle.getString("username");
            main_post = bundle.getString("main_post");
            like_counter = String.valueOf(bundle.getInt("likeCounter"));
            imageURL = bundle.getString("imageURL");

        }
        postId = bundle.getString("postId");

//        Toast.makeText(this, ""+currentusername, Toast.LENGTH_SHORT).show();

        Picasso.get().load(imageURL).into(postUserImage);
        singlePostUsername.setText(username);
        mainPost.setText(main_post);



        likecounterfunc();




//        -------------comment submit button work --------------


        HashMap<String, String > commentMap = new HashMap<>();


        commentsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long currentTime = System.currentTimeMillis();
                String commentTime = String.valueOf(currentTime);


                commentString = commentEdittext.getText().toString();
                commentMap.put("name",currentusername);
                commentMap.put("propic",currentuserPic);
                commentMap.put("comment",commentString);
//                Toast.makeText(PostComments.this, " "+username, Toast.LENGTH_SHORT).show();

                mdatabase.child("comments").child(postId).child(commentTime).setValue(commentMap);

                commentEdittext.setText("");
            }
        });

        fetchAllComments();








    }

    private void likecounterfunc() {
         DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("likes").child(postId);
         reference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                 likeCounter.setText((int) snapshot.getChildrenCount()+" likes");
             }

             @Override
             public void onCancelled(@NonNull DatabaseError error) {

             }
         })      ;
    }

    private void fetchAllComments() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("comments").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                     AllCommentsOfDatabase((Map<String,Object>) snapshot.getValue());
                     comment_counter= String.valueOf(snapshot.getChildrenCount());
                     commentCounter.setText(comment_counter + " comments");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private void AllCommentsOfDatabase(Map<String, Object> value) {
        ArrayList<Object> allcomments = new ArrayList<Object>();

        for (Map.Entry<String, Object> entry : value.entrySet()){
           Map singlepost = (Map) entry.getValue();
            allcomments.add(singlepost);
        }
        initData(allcomments);
        initCommentRecyclerView();
    }

    private void initData(ArrayList<Object> allcomments) {
        post_comment = new ArrayList<>();
        for(int i=allcomments.size()-1;i>=0;i--) {
            HashMap comment = (HashMap) allcomments.get(i);
            post_comment.add(new PostComments_model((String) comment.get("name"), (String) comment.get("propic"), (String) comment.get("comment")));
        }
    }

    private void initCommentRecyclerView() {
        commentRv = findViewById(R.id.post_comments);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        commentRv.setLayoutManager(layoutManager);

        adapter = new PostComments_adapter(post_comment);
        commentRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();


    }
}