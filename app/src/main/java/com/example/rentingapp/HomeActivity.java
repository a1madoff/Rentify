package com.example.rentingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.graphics.Movie;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String TAG = "HomeActivity";

    List<Listing> listings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView rvListings = findViewById(R.id.rvListings);
        listings = new ArrayList<>();
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());

        ListingsAdapter adapter = new ListingsAdapter(this, listings);
        rvListings.setAdapter(adapter);

//        rvListings.setLayoutManager(new LinearLayoutManager(this));
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvListings.setLayoutManager(layoutManager);
    }
}