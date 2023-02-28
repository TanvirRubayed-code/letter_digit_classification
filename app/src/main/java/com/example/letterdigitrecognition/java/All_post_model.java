package com.example.letterdigitrecognition.java;

public class All_post_model {
    private String name ;
    private String post ;
    private int likeFlag ;
    private String imageUrl ;
    private int likeCounter ;
    private String uid;


    public All_post_model(String uid, String name, String post,String imageUrl, int likeFlag, int likeCounter) {
        this.name = name;
        this.post = post;
        this.imageUrl = imageUrl;
        this.likeFlag = likeFlag ;
        this.likeCounter = likeCounter;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getLikeFlag() {
        return likeFlag;
    }

    public void setLikeFlag(int likeFlag) {
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
