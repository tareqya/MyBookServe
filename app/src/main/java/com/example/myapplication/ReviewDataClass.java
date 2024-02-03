package com.example.myapplication;

public class ReviewDataClass {
    private String reviewDesc;
    private String imageURL;

    public ReviewDataClass(String reviewDesc, String imageURL) {
        this.reviewDesc = reviewDesc;
        this.imageURL = imageURL;
    }

    // Getter methods
    public String getReviewDesc() {
        return reviewDesc;
    }

    public String getImageURL() {
        return imageURL;
    }

    // Setter methods (if needed)
    public void setReviewDesc(String reviewDesc) {
        this.reviewDesc = reviewDesc;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
