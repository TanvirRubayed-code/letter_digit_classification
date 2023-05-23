package com.example.letterdigitrecognition.java;

public class PostComments_model {
    private String name, imageURL, comment ;

    public PostComments_model(String name, String imageURL, String comment) {
        this.name = name;
        this.imageURL = imageURL;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
