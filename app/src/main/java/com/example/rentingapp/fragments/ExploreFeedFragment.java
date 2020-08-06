package com.example.rentingapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rentingapp.models.Listing;
import com.example.rentingapp.adapters.ListingsAdapter;
import com.example.rentingapp.MapsActivity;
import com.example.rentingapp.R;
import com.example.rentingapp.tools.Recommendations;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFeedFragment extends Fragment {
    public static final String TAG = "ExploreFeedFragment";
    public static final int REQUEST_CODE_DETAILS = 10;

    List<Listing> mListings;
    ListingsAdapter adapter;

    FloatingActionButton floatingButton;

    public ExploreFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for the fragment
        return inflater.inflate(R.layout.fragment_explore_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvListings = view.findViewById(R.id.rvListings);
        floatingButton = view.findViewById(R.id.floatingButton);

        mListings = new ArrayList<>();

        adapter = new ListingsAdapter(getContext(), mListings, ExploreFeedFragment.this);
        rvListings.setAdapter(adapter);

//        rvListings.setLayoutManager(new LinearLayoutManager(getContext()));
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvListings.setLayoutManager(layoutManager);

        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), MapsActivity.class);
                startActivity(i);
            }
        });
        getListings();
    }

    private void getListings() {
        // Specifies which class to query
        ParseQuery<Listing> query = ParseQuery.getQuery(Listing.class);
        query.include(Listing.KEY_SELLER); // TODO: need to include seller?
        query.include(Listing.KEY_LIKEDBY);
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<Listing>() {
            @Override
            public void done(List<Listing> listings, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting listings", e);
                    return;
                }
                for (Listing listing : listings) {
                    List<Object> usersWhoLike = listing.getList(Listing.KEY_LIKEDBY);
                    if (usersWhoLike != null && usersWhoLike.contains(ParseUser.getCurrentUser().getObjectId())) {
                        listing.setLiked(true);
                    }
                }

                mListings = listings;
//                adapter.setListings(listings);
//                adapter.addAll(listings);


            //        BEGIN
                Recommendations recommendations = new Recommendations(mListings, adapter);
                recommendations.driver(ParseUser.getCurrentUser().getObjectId());
            //        END
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_DETAILS && resultCode == RESULT_OK) {
            boolean liked = data.getBooleanExtra("liked", false);
            int position = data.getIntExtra("position", -1);

            Listing listing = mListings.get(position);
            if (listing.isLiked() != liked) {
                listing.setLiked(liked);
                adapter.notifyItemChanged(position, liked);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}