package com.example.letterdigitrecognition.java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letterdigitrecognition.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostComments_adapter extends RecyclerView.Adapter<PostComments_adapter.ViewHolder> {

    private List<PostComments_model> post_comments;

    public PostComments_adapter(List<PostComments_model> post_comments) {
        this.post_comments = post_comments;
    }

    @NonNull
    @Override
    public PostComments_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_post_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostComments_adapter.ViewHolder holder, int position) {
        String name = post_comments.get(position).getName();
        String imageURL = post_comments.get(position).getImageURL();
        String comment = post_comments.get(position).getComment();

        holder.setData(name, imageURL, comment);
    }

    @Override
    public int getItemCount() {
        return post_comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView username, usercomment;
        private ImageView userPic ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.comment_user_name);
            usercomment = itemView.findViewById(R.id.comment_of_user);
            userPic = itemView.findViewById(R.id.comment_image);


        }


        public void setData(String name, String imageURL, String comment) {
            username.setText(name);
            usercomment.setText(comment);
            Picasso.get().load(imageURL).into(userPic);
        }
    }
}
