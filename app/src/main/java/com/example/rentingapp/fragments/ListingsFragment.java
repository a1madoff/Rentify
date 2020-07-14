package com.example.rentingapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rentingapp.Listing;
import com.example.rentingapp.ListingsAdapter;
import com.example.rentingapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListingsFragment extends Fragment {
    public static final String TAG = "ListingsFragment";

    List<Listing> listings;

    public ListingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvListings = view.findViewById(R.id.rvListings);
        listings = new ArrayList<>();
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());
        listings.add(new Listing());

        ListingsAdapter adapter = new ListingsAdapter(getContext(), listings);
        rvListings.setAdapter(adapter);

//        rvListings.setLayoutManager(new LinearLayoutManager(this));
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvListings.setLayoutManager(layoutManager);
    }
}