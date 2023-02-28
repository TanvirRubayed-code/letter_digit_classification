package com.example.letterdigitrecognition.java;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letterdigitrecognition.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class All_post_adapter extends RecyclerView.Adapter<All_post_adapter.viewHolder> {
    private List<All_post_model> posts;

    DatabaseReference updatePostDatabase = FirebaseDatabase.getInstance().getReference("posts");

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
        int likeFlag = posts.get(position).getLikeFlag();
        int likeCounter = posts.get(position).getLikeCounter();

        holder.setData(name,post,imageUrl,likeFlag,likeCounter);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "main post "+position, Toast.LENGTH_SHORT).show();
            }
        });


        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(posts.get(position).getLikeFlag()==0){
                    ((ImageView)v).setImageResource(R.drawable.thumb_up);
                    posts.get(position).setLikeFlag(1);

//                    Toast.makeText(v.getContext(), "like"+posts.get(position).getUid(), Toast.LENGTH_SHORT).show();
                    increaseLike(posts.get(position).getUid(),posts.get(position).getLikeCounter());
                }
                else if(posts.get(position).getLikeFlag()==1){
                    ((ImageView)v).setImageResource(R.drawable.thumb_up_off);
                    posts.get(position).setLikeFlag(0);
//                    Toast.makeText(v.getContext(), "dislike"+posts.get(position).getUid(), Toast.LENGTH_SHORT).show();
                      decreaseLike(posts.get(position).getUid(),posts.get(position).getLikeCounter());
                }

            }
        });

    }

    private void decreaseLike(String uid, int likeCounter) {
        updatePostDatabase.child(uid).child("likes").setValue(likeCounter-1);
        updatePostDatabase.child(uid).child("likeFlag").setValue(0);
    }

    private void increaseLike(String uid, int likeCounter) {
        updatePostDatabase.child(uid).child("likes").setValue(likeCounter+1);
        updatePostDatabase.child(uid).child("likeFlag").setValue(1);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView username,likeCounter;
        private TextView userspost;
        private ImageView likeButton ;
        private CircleImageView postUserImage ;




        public viewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.users_name);
            userspost = itemView.findViewById(R.id.user_post);
            likeButton = itemView.findViewById(R.id.like_button);
            postUserImage = itemView.findViewById(R.id.user_post_image);
            likeCounter = itemView.findViewById(R.id.like_counter);
        }

        public void setData(String name, String post, String imageUrl, int likeFlag, int likecounter) {
            username.setText(name);
            userspost.setText(post);
            String likestr = String.valueOf(likecounter)+" likes";
            likeCounter.setText(likestr);
            Picasso.get().load(imageUrl).into(postUserImage);

            if(likeFlag==1){
                likeButton.setImageResource(R.drawable.thumb_up);
            }
            else {
                likeButton.setImageResource(R.drawable.thumb_up_off);

            }
        }
    }
}
