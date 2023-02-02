package com.example.letterdigitrecognition.java;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.letterdigitrecognition.R;

import java.util.List;

public class AllRatings_adapter extends RecyclerView.Adapter<AllRatings_adapter.viewHolder>{
    private List<AllRatings_model> ratingsDetails;

    public AllRatings_adapter(List<AllRatings_model> ratingsDetails) {
        this.ratingsDetails = ratingsDetails;
    }

    @NonNull
    @Override
    public AllRatings_adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_ratings_card,parent,false);
        return new AllRatings_adapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllRatings_adapter.viewHolder holder, int position) {
        String name = ratingsDetails.get(position).getName();
        String rating = ratingsDetails.get(position).getRating();
        holder.setData(name,rating);

    }

    @Override
    public int getItemCount() {
        return ratingsDetails.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView ratings;
        private RatingBar ratingBar;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_rating);
            ratings = itemView.findViewById(R.id.ratings_number);
            ratingBar = itemView.findViewById(R.id.ratingBar);
        }

        public void setData(String name, String rating) {
            username.setText(name);
            ratings.setText(rating);
            ratingBar.setRating(Float.parseFloat(rating));
        }
    }
}
