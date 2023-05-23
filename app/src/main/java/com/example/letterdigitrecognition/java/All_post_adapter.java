package com.example.letterdigitrecognition.java;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letterdigitrecognition.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class All_post_adapter extends RecyclerView.Adapter<All_post_adapter.viewHolder> {

    int likecounter;


    private List<All_post_model> posts;
    DatabaseReference updatePostDatabase = FirebaseDatabase.getInstance().getReference("likes");

    public All_post_adapter(List<All_post_model> posts) {
        this.posts = posts;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_post_card,parent,false);
        return new All_post_adapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        String name = posts.get(position).getName();
        String post = posts.get(position).getPost();
        String imageUrl = posts.get(position).getImageUrl();

        Context mContext = posts.get(position).getContext();

        String postID = posts.get(position).getPostId();



        // -------- number of likes fetching and counting ------------

        holder.getLikeStatus(mContext,postID, posts.get(position).getUid());




        holder.setData(mContext,name,post,imageUrl);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singlePostPage(v,name,post,imageUrl,postID);

            }
        });
        
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singlePostPage(v,name,post,imageUrl,postID);
            }
        });


        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag  = posts.get(position).getLikeFlag();
                if(flag==0){

                    HashMap<String, String > likesflag = new HashMap<>();
                    likesflag.put("likeFlag","1");

                    ((ImageView)v).setImageResource(R.drawable.thumb_up);
                    posts.get(position).setLikeFlag(1);

                    updatePostDatabase.child(posts.get(position).getPostId()).child(posts.get(position).getUid()).setValue(likesflag);
                }
                else if(flag==1) {

                    HashMap<String, String > likesflag = new HashMap<>();
                    likesflag.put("likeFlag","0");

                    ((ImageView) v).setImageResource(R.drawable.thumb_up_off);
                    posts.get(position).setLikeFlag(0);

                    updatePostDatabase.child(posts.get(position).getPostId()).child(posts.get(position).getUid()).setValue(likesflag);

                }

            }
        });

    }

    private void singlePostPage(View v, String name, String post, String imageUrl, String postID) {
        Intent intent = new Intent(v.getContext(),PostComments.class);
        intent.putExtra("username", name);
        intent.putExtra("main_post", post);
        intent.putExtra("imageURL", imageUrl);
        intent.putExtra("postId",postID);
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView username,likeCounter;
        private TextView userspost;
        private ImageView likeButton  ;
        private ImageButton commentButton;
        private CircleImageView postUserImage ;




        public viewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.users_name);
            userspost = itemView.findViewById(R.id.user_post);
            likeButton = itemView.findViewById(R.id.like_button);
            commentButton = itemView.findViewById(R.id.comment_button);
            postUserImage = itemView.findViewById(R.id.user_post_image);
            likeCounter = itemView.findViewById(R.id.like_counter);

        }

        public void setData(Context mContext, String name, String post, String imageUrl) {
            username.setText(name);
            userspost.setText(post);
            Picasso.get().load(imageUrl).into(postUserImage);


        }

        public void getLikeStatus(Context mContext,String postId, String uid) {
            DatabaseReference mdatabase = FirebaseDatabase.getInstance().getReference();
            mdatabase.child("likes").child(postId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                     likecounter = 0;

                    for (DataSnapshot data : snapshot.getChildren()){
                        int checkdata = Integer.parseInt(String.valueOf(data.child("likeFlag").getValue()));
                        if(checkdata==1){
                            likecounter = likecounter + 1;
                        }

                        if(data.getKey().equals(uid) && data.child("likeFlag").getValue().equals("1")){
//                            Toast.makeText(mContext, ""+data.getValue(), Toast.LENGTH_SHORT).show();
                            likeButton.setImageResource(R.drawable.thumb_up);
                        }
                        else if(data.getKey().equals(uid) && data.child("likeFlag").getValue().equals("0")){
                            likeButton.setImageResource(R.drawable.thumb_up_off);
                        }

                    }

                    likeCounter.setText(likecounter+" likes");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
