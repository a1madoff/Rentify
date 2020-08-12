package com.example.rentingapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentingapp.R;
import com.example.rentingapp.adapters.ConversationsAdapter;
import com.example.rentingapp.models.Listing;
import com.example.rentingapp.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {
    public static final String TAG = "InboxFragment";

    List<Listing> listings;
    ConversationsAdapter adapter;

    public InboxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflates the layout for this fragment
        return inflater.inflate(R.layout.fragment_inbox, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvConversations = view.findViewById(R.id.rvConversations);

        listings = new ArrayList<>();

        adapter = new ConversationsAdapter(getContext(), listings);
        rvConversations.setAdapter(adapter);

        rvConversations.setLayoutManager(new LinearLayoutManager(getContext()));

        getConversations();
    }

    private void getConversations() {
        // first query for listings attached to all messages
        // then for each listing, query for all messages that match that particular listing (and are relevant to user)

        // Constructs queries to execute
        ParseQuery<Message> queryFromUser = ParseQuery.getQuery(Message.class);
        queryFromUser.whereEqualTo(Message.KEY_FROM_USER, ParseUser.getCurrentUser());

        ParseQuery<Message> queryToUser = ParseQuery.getQuery(Message.class);
        queryToUser.whereEqualTo(Message.KEY_TO_USER, ParseUser.getCurrentUser());

        List<ParseQuery<Message>> queries = new ArrayList<>();
        queries.add(queryFromUser);
        queries.add(queryToUser);

        ParseQuery<Message> combinedQuery = ParseQuery.or(queries);
        combinedQuery.include(Message.KEY_LISTING);
        combinedQuery.orderByDescending("createdAt");
        combinedQuery.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting messages", e);
                    return;
                }

                LinkedHashSet<Listing> listingSet = new LinkedHashSet<>();
                for (Message message : messages) {
                    listingSet.add(message.getListing());
                }
                listings.addAll(listingSet);
                adapter.notifyDataSetChanged();
            }
        });
    }
}