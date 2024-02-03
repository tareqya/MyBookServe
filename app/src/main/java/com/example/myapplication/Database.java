package com.example.myapplication;

import androidx.annotation.NonNull;

import com.example.myapplication.calback.AuthCallBack;
import com.example.myapplication.calback.UserCallBack;
import com.example.myapplication.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Database {
    public static final String USERS_TABLE = "Users";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private AuthCallBack authCallBack;
    private UserCallBack userCallBack;

    public Database(){
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    public void setAuthCallBack(AuthCallBack authCallBack){
        this.authCallBack = authCallBack;
    }

    public void setUserCallBack(UserCallBack userCallBack){
        this.userCallBack = userCallBack;
    }
    public FirebaseUser getCurrentUser(){
        return this.mAuth.getCurrentUser();
    }

    public void login(String email, String password){
       this.mAuth.signInWithEmailAndPassword(email, password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
           @Override
           public void onComplete(@NonNull Task<AuthResult> task) {
                authCallBack.onLoginComplete(task);
           }
       });
    }

    public void createNewAccount(String email, String password) {
        this.mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                authCallBack.onCreateAccountComplete(task);
            }
        });
    }

    public void saveUserData(User user){
        this.db.collection(USERS_TABLE).document(user.getKey()).set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userCallBack.onUserSaveDataComplete(task);
            }
        });
    }


}
