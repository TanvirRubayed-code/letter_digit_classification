package com.example.letterdigitrecognition.java;

public class AllRatings_model {
    private String name ;
    private String rating ;

    public AllRatings_model(String name, String rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
