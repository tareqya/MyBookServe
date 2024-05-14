package com.example.myapplication.data;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Book extends FirebaseKey implements Serializable {
    private String name;
    private String language;
    private String category;
    private String imagePath;
    private String imageUrl;
    private String description;
    private double rate;
    private String contact;
    private ArrayList<Comment> comments;

    public Book() {
        comments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public String getLanguage() {
        return language;
    }

    public Book setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Book setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Book setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    @Exclude
    public String getImageUrl() {
        return imageUrl;
    }

    public Book setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Book setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getRate(){
        return this.rate;
    }

    public Book setRate(double rate){
        this.rate = rate;
        return this;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public Book setComments(ArrayList<Comment> comments) {
        this.comments = comments;
        return this;
    }
    @Exclude
    public void addComment(Comment comment){
        this.comments.add(comment);
    }

    public Book setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public String getContact() {
        return contact;
    }
}
