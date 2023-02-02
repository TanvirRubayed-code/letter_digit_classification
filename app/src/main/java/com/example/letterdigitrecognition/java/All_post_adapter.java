package com.example.letterdigitrecognition.java;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

import java.util.List;

public class All_post_adapter extends RecyclerView.Adapter<All_post_adapter.viewHolder> {
    private List<All_post_model> posts;

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
        Boolean likeFlag = posts.get(position).getLikeFlag();
        holder.setData(name,post,likeFlag);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "main post "+position, Toast.LENGTH_SHORT).show();
            }
        });


        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!posts.get(position).getLikeFlag()){
                    ((ImageView)v).setImageResource(R.drawable.thumb_up);
                    posts.get(position).setLikeFlag(true);

                }
                else if(posts.get(position).getLikeFlag()){
                    ((ImageView)v).setImageResource(R.drawable.thumb_up_off);
                    posts.get(position).setLikeFlag(false);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private TextView userspost;
        private ImageView likeButton ;




        public viewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.users_name);
            userspost = itemView.findViewById(R.id.user_post);
            likeButton = itemView.findViewById(R.id.like_button);
        }

        public void setData(String name, String post, Boolean likeFlag) {
            username.setText(name);
            userspost.setText(post);
            if(likeFlag){
                likeButton.setImageResource(R.drawable.thumb_up);
            }
            else {
                likeButton.setImageResource(R.drawable.thumb_up_off);

            }
        }
    }
}
