package com.example.rentingapp.tools;

import android.app.Application;

import com.example.rentingapp.models.Listing;
import com.example.rentingapp.models.Message;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Registers the parse models
        ParseObject.registerSubclass(Listing.class);
        ParseObject.registerSubclass(Message.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("renting-server") // should correspond to APP_ID env variable
                .clientKey("fbu2020")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://renting-server.herokuapp.com/parse/").build());
    }
}


