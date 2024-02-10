package com.example.myapplication.data;

import com.google.firebase.firestore.Exclude;

public class Book extends FirebaseKey{
    private String name;
    private String language;
    private String category;
    private String imagePath;
    private String imageUrl;
    private String description;

    public Book() {}

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
}
