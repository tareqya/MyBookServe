package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.calback.AuthCallBack;
import com.example.myapplication.calback.BookCallBack;
import com.example.myapplication.calback.UserCallBack;
import com.example.myapplication.data.Book;
import com.example.myapplication.data.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Database {
    public static final String USERS_TABLE = "Users";
    private static final String BOOKS_TABLE = "Books";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private AuthCallBack authCallBack;
    private UserCallBack userCallBack;
    private BookCallBack bookCallBack;


    public Database(){
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.storage = FirebaseStorage.getInstance();
    }

    public void setAuthCallBack(AuthCallBack authCallBack){
        this.authCallBack = authCallBack;
    }

    public void setUserCallBack(UserCallBack userCallBack){
        this.userCallBack = userCallBack;
    }
    public void setBookCallBack(BookCallBack bookCallBack){
        this.bookCallBack = bookCallBack;
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


    public void fetchUserByUid(String uid){
        this.db.collection(USERS_TABLE).document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    User user = value.toObject(User.class);
                    user.setKey(value.getId());
                    if(user.getImagePath() != null){
                        // download image url
                        String imageUrl = downloadImageUrl(user.getImagePath());
                        user.setImageUrl(imageUrl);
                    }

                    userCallBack.onUserFetchDataComplete(user);
                }

            }
        });
    }

    public String downloadImageUrl(String imagePath){
        Task<Uri> downloadImageTask = storage.getReference().child(imagePath).getDownloadUrl();
        while (!downloadImageTask.isComplete() && !downloadImageTask.isCanceled());
        return downloadImageTask.getResult().toString();
    }

    public boolean uploadImage(Uri imageUri, String imagePath){
        try{
            UploadTask uploadTask = storage.getReference(imagePath).putFile(imageUri);
            while (!uploadTask.isComplete() && !uploadTask.isCanceled());
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage().toString());
            return false;
        }

    }

    public void logout() {
        this.mAuth.signOut();
    }

    public void saveBook(Book book) {
        this.db.collection(BOOKS_TABLE).document().set(book).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                bookCallBack.onBookUploadComplete(task);
            }
        });
    }

    public void updateUserScore(String uid, int score){
        this.db.collection(USERS_TABLE).document(uid)
                .update("score", FieldValue.increment(score));
    }
}
