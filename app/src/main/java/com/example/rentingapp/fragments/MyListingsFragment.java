package com.example.rentingapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingapp.R;
import com.example.rentingapp.adapters.MyListingsAdapter;
import com.example.rentingapp.models.Listing;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MyListingsFragment extends Fragment {
    public static final String TAG = "MyListingsFragment";

    List<Listing> myListings;
    MyListingsAdapter adapter;

    RecyclerView rvMyListings;
    TextView noListingsFound;
    Button btnCreateListing;
    TextView tvNumListings;
    Button btnAddListing;

    public MyListingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for the fragment
        return inflater.inflate(R.layout.fragment_my_listings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvMyListings = view.findViewById(R.id.rvMyListings);
        noListingsFound = view.findViewById(R.id.noListingsFound);
        btnCreateListing = view.findViewById(R.id.btnCreateListing);
        tvNumListings = view.findViewById(R.id.tvNumListings);
        btnAddListing = view.findViewById(R.id.btnAddListing);

        myListings = new ArrayList<>();

        adapter = new MyListingsAdapter(getContext(), myListings);
        rvMyListings.setAdapter(adapter);

        rvMyListings.setLayoutManager(new LinearLayoutManager(getContext()));

        getMyListings();

        btnAddListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creates the transaction
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // Configures the in and out animation files
                ft.setCustomAnimations(R.anim.new_slide_in_right, R.anim.new_slide_out_left);
                // Performs the fragment replacement
                CreateListingFragment createListingFragment = new CreateListingFragment();
                createListingFragment.setTargetFragment(MyListingsFragment.this, 300);
                ft.replace(((ViewGroup) getView().getParent()).getId(), createListingFragment, "fragment");
                // Starts the animated transition.
                ft.commit();
            }
        });

        btnCreateListing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creates the transaction
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                // Configures the in and out animation files
                ft.setCustomAnimations(R.anim.new_slide_in_right, R.anim.new_slide_out_left);
                // Performs the fragment replacement
                CreateListingFragment createListingFragment = new CreateListingFragment();
                createListingFragment.setTargetFragment(MyListingsFragment.this, 300);
                ft.replace(((ViewGroup) getView().getParent()).getId(), createListingFragment, "fragment");
                // Starts the animated transition.
                ft.commit();
            }
        });
    }

    private void getMyListings() {
        // Specifies which class to query
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_SELLER);
        query.whereEqualTo(Listing.KEY_SELLER, ParseUser.getCurrentUser());
        query.addDescendingOrder(Listing.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting listings", e);
                    return;
                }

                if (listings.isEmpty()) {
                    noListingsFound.setVisibility(View.VISIBLE);
                    btnCreateListing.setVisibility(View.VISIBLE);
                    rvMyListings.setVisibility(View.GONE);

                    tvNumListings.setText("0 Listings");
                } else {
                    adapter.addAll(listings);

                    if (listings.size() == 1) {
                        tvNumListings.setText("1 Listing");
                    } else {
                        tvNumListings.setText(String.format("%d Listings", listings.size()));
                    }
                }
            }
        });
    }
}
