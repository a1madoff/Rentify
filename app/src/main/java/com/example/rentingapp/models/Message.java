package com.example.rentingapp.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String KEY_BODY = "body";
    public static final String KEY_LISTING = "listing";
    public static final String KEY_FROM_USER = "fromUser";
    public static final String KEY_TO_USER = "toUser";

    public String getBody() {
        return getString(KEY_BODY);
    }
    public void setBody(String body) {
        put(KEY_BODY, body);
    }

    public Listing getListing() {
        return (Listing) getParseObject(KEY_LISTING);
    }
    public void setListing(Listing listing) {
        put(KEY_LISTING, listing);
    }

    public ParseUser getFromUser() {
        return getParseUser(KEY_FROM_USER);
    }
    public void setFromUser(ParseUser parseUser) {
        put(KEY_FROM_USER, parseUser);
    }

    public ParseUser getToUser() {
        return getParseUser(KEY_TO_USER);
    }
    public void setToUser(ParseUser parseUser) {
        put(KEY_TO_USER, parseUser);
    }
}
