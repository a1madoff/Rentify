package com.example.rentingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.rentingapp.adapters.ChatAdapter;
import com.example.rentingapp.models.Listing;
import com.example.rentingapp.models.Message;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ChatActivity extends AppCompatActivity {
    static final String TAG = ChatActivity.class.getSimpleName();

    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;

    EditText etMessage;
    Button btSend;

    RecyclerView rvChat;
    ArrayList<Message> mMessages;
    ChatAdapter mAdapter;
    // Keeps track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;

    Context context;
    Listing listing;

    ImageView ivListingImage;
    TextView tvListingTitle;
    CardView cvTopCard;

    // Creates a handler which refreshes the messages
    static final int POLL_INTERVAL = 1000; // milliseconds
    Handler myHandler = new android.os.Handler();
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        context = this;
        listing = Parcels.unwrap(getIntent().getParcelableExtra("listing"));

        ivListingImage = findViewById(R.id.ivListingImage);
        Glide.with(context)
                .load(listing.getImage().getUrl())
                .transform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(30, 10)))
                .into(ivListingImage);


        tvListingTitle = findViewById(R.id.tvListingTitle);
        tvListingTitle.setText(listing.getTitle());

        cvTopCard = findViewById(R.id.cvTopCard);
        cvTopCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ListingDetailsActivity.class);
                intent.putExtra("listing", Parcels.wrap(listing));
                context.startActivity(intent);
            }
        });

        startWithCurrentUser();
        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    // Gets the userId from the cached currentUser object
    void startWithCurrentUser() {
        setupMessagePosting();
    }

    // Sets up message field and posting
    void setupMessagePosting() {
        // Finds the text field and button
        etMessage = findViewById(R.id.etMessage);
        btSend = findViewById(R.id.btSend);
        rvChat = findViewById(R.id.rvChat);
        mMessages = new ArrayList<>();
        mFirstLoad = true;
        mAdapter = new ChatAdapter(ChatActivity.this, mMessages);
        rvChat.setAdapter(mAdapter);

        // Associates the LayoutManager with the RecylcerView
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rvChat.setLayoutManager(linearLayoutManager);

        // When send button is clicked, creates message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();

                Message message = new Message();
                message.setBody(data);
                message.setListing(listing);
                message.setFromUser(ParseUser.getCurrentUser());
                message.setToUser(listing.getSeller());
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
//                        Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
//                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });
    }

    // Queries messages from Parse to load them into the chat adapter
    void refreshMessages() {
        // Constructs queries to execute
        ParseQuery<Message> queryFromUser = ParseQuery.getQuery(Message.class);
        queryFromUser.whereEqualTo(Message.KEY_LISTING, listing);
        queryFromUser.whereEqualTo(Message.KEY_FROM_USER, ParseUser.getCurrentUser());

        ParseQuery<Message> queryToUser = ParseQuery.getQuery(Message.class);
        queryToUser.whereEqualTo(Message.KEY_LISTING, listing);
        queryToUser.whereEqualTo(Message.KEY_TO_USER, ParseUser.getCurrentUser());

        List<ParseQuery<Message>> queries = new ArrayList<>();
        queries.add(queryFromUser);
        queries.add(queryToUser);

        ParseQuery<Message> combinedQuery = ParseQuery.or(queries);
        combinedQuery.include(Message.KEY_FROM_USER);
        combinedQuery.include(Message.KEY_TO_USER);

        // Configures limit and sort order
        combinedQuery.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);

        // Gets the latest 50 messages, order shows newest to oldest
        combinedQuery.orderByDescending("createdAt");
        // Executes query to fetch all messages from Parse asynchronously
        combinedQuery.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // Updates adapter
                    // Scrolls to the bottom of the list on initial load
                    if (mFirstLoad) {
                        rvChat.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
//    void login() {
//        ParseAnonymousUtils.logIn(new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException e) {
//                if (e != null) {
//                    Log.e(TAG, "Anonymous login failed: ", e);
//                } else {
//                    startWithCurrentUser();
//                }
//            }
//        });
//    }
}