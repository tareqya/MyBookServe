package com.example.myapplication.data;


import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class FirebaseKey implements Serializable {
    protected String key;

    public FirebaseKey(){}

    @Exclude
    public String getKey(){
        return this.key;
    }

    public FirebaseKey setKey(String key){
        this.key = key;
        return this;
    }


}
