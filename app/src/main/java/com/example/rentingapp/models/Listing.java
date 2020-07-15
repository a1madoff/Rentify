package com.example.rentingapp.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

@ParseClassName("Listing")
public class Listing extends ParseObject {
    public static final String KEY_PRICE = "price";
    public static final String KEY_SELLER = "seller";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_RATING = "rating";
    public static final String KEY_COORDINATES = "coordinates";
    public static final String KEY_TITLE = "title";

    public Listing() {}

    public int getPrice() {
        return getInt(KEY_PRICE);
    }

    public void setPrice(int price) {
        put(KEY_PRICE, price);
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

    public ParseGeoPoint getCoordinates() {
        return getParseGeoPoint(KEY_COORDINATES);
    }

    public void setCoordinates(ParseGeoPoint geoPoint) {
        put(KEY_COORDINATES, geoPoint);
    }

    public String getTitle() {
        return getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        put(KEY_TITLE, title);
    }
}
