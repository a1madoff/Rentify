package com.example.rentingapp.models;

import androidx.annotation.Nullable;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

@ParseClassName("Listing")
public class Listing extends ParseObject {
    public static final String KEY_PRICE = "price";
    public static final String KEY_SELLER = "seller";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_RATING = "rating";
    public static final String KEY_COORDINATES = "coordinates";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_FULLADDRESS = "fullAddress";
    public static final String KEY_LOCALITY = "locality";
    public static final String KEY_LIKEDBY = "likedBy";

    public boolean liked = false;
    public double score = Double.NaN;

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

    public double getRating() {
        return getDouble(KEY_RATING);
    }
    public void setRating(double rating) {
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

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public String getfullAddress() {
        return getString(KEY_FULLADDRESS);
    }
    public void setfullAddress(String fullAddress) {
        put(KEY_FULLADDRESS, fullAddress);
    }

    public String getLocality() { return getString(KEY_LOCALITY); }
    public void setLocality(String locality) {
        put(KEY_LOCALITY, locality);
    }

    public List<String> getLikedBy() { return getList(KEY_LIKEDBY); }
    public void setLikedBy(List<String> likedBy) { put(KEY_LIKEDBY, likedBy); }

    public boolean isLiked() { return liked; }
    public void setLiked(boolean liked) { this.liked = liked; }

    public Double getScore() { return score; }

    public void setScore(double score) { this.score = score; }

    @Override
    public int hashCode() {
        return getObjectId().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Listing)) {
            return false;
        }

        Listing other = (Listing) obj;
        return this.hashCode() == other.hashCode();
    }
}
