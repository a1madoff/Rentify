package com.example.rentingapp.models;

public class RecommendedListing {
    String listingID;
    Double score;

    public RecommendedListing(String listingID, Double score) {
        this.listingID = listingID;
        this.score = score;
    }

    public Double getScore() {
        return score;
    }
}
