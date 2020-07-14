package com.example.rentingapp;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@ParseClassName("Listing")
public class Listing extends ParseObject {
    public static final String KEY_PRICE = "price";
    public static final String KEY_SELLER = "seller";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_RATING = "rating";

    public Listing() {}

    public int getPriceCents() {
        return getInt(KEY_PRICE);
    }

    public double getPriceDollars() {
        return getInt(KEY_PRICE) / 100.0;
    }

    public void setPriceCents(int priceCents) {
        put(KEY_PRICE, priceCents);
    }

    public void setPriceDollars(double priceDollars) {
        int priceCents = (int) (priceDollars * 100);
        put(KEY_PRICE, priceCents);
    }

    public ParseUser getSeller() {
        return getParseUser(KEY_SELLER);
    }

    public void setSeller(ParseUser seller) {
        put(KEY_SELLER, seller);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public int getRating() {
        return getInt(KEY_RATING);
    }

    public void setRating(int rating) {
        put(KEY_RATING, rating);
    }
}
