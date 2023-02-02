package com.example.letterdigitrecognition.java;

public class All_post_model {
    private String name ;
    private String post ;
    private Boolean likeFlag ;


    public All_post_model(String name, String post, Boolean likeFlag) {
        this.name = name;
        this.post = post;
        this.likeFlag = likeFlag ;
    }

    public Boolean getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(Boolean likeFlag) {
        this.likeFlag = likeFlag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }
}
