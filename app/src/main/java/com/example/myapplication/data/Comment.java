package com.example.myapplication.data;

import java.io.Serializable;

public class Comment implements Serializable {
    private String uid;
    private String opinion;

    public Comment(){}

    public String getUid() {
        return uid;
    }

    public Comment setUid(String uid) {
        this.uid = uid;
        return this;
    }

    public String getOpinion() {
        return opinion;
    }

    public Comment setOpinion(String opinion) {
        this.opinion = opinion;
        return this;
    }
}
