package com.example.rentingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.rentingapp.ListingDetailsActivity;
import com.example.rentingapp.R;
import com.example.rentingapp.fragments.ExploreFeedFragment;
import com.example.rentingapp.models.Listing;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ViewHolder> {
    Context context;
    List<Listing> listings;
    ExploreFeedFragment fragment;

    public ListingsAdapter(Context context, List<Listing> listings, ExploreFeedFragment fragment) {
        this.context = context;
        this.listings = listings;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listing listing = listings.get(position);
        holder.bind(listing);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull List<Object> payloads) {
        if(!payloads.isEmpty()) {
            if (payloads.get(0) instanceof Boolean) {
                boolean liked = (boolean) payloads.get(0);
                if (liked) {
                    holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart_full));
                    holder.ivHeart.setAlpha(0.7f); // 0.7 or 0.8
                } else {
                    holder.ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart_empty));
                    holder.ivHeart.setAlpha(1.0f);
                }
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        return listings.size();
    }

    public void setListings(List<Listing> newListings) {
        listings = newListings;
        notifyDataSetChanged();
    }

    // Adds the list of items
    public void addAll(List<Listing> listingsList) {
        listings.addAll(listingsList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivListingImage;
        public ImageView ivHeart;
        public TextView tvPrice;
        public TextView tvTtitle;
        public RatingBar ratingBar;
        public TextView tvNumRentals;
        public TextView tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivListingImage = itemView.findViewById(R.id.ivListingImage);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTtitle = itemView.findViewById(R.id.tvTitle);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvNumRentals = itemView.findViewById(R.id.tvNumRentals);
            tvLocation = itemView.findViewById(R.id.tvLocation);

            itemView.setOnClickListener(this);

            ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Listing currentListing = listings.get(position);
                        ParseUser currentUser = ParseUser.getCurrentUser();

                        List<String> likes = currentUser.getList("likes");
                        if (likes == null) {
                            likes = new ArrayList<>();
                        }

                        List<String> likedBy = currentListing.getLikedBy();
                        if (likedBy == null) {
                            likedBy = new ArrayList<>();
                        }

                        if (currentListing.isLiked()) {
                            ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart_empty));
                            ivHeart.setAlpha(1.0f);
                            currentListing.setLiked(false);

                            likes.remove(currentListing.getObjectId());
                            likedBy.remove(currentUser.getObjectId());
                        } else {
                            ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart_full));
                            ivHeart.setAlpha(0.7f); // 0.7 or 0.8
                            currentListing.setLiked(true);

                            likes.add(currentListing.getObjectId());
                            likedBy.add(currentUser.getObjectId());
                        }
                        currentUser.put("likes", likes);
                        currentUser.saveInBackground();

                        currentListing.setLikedBy(likedBy);
                        currentListing.saveInBackground();
                    }
                }
            });
        }

        public void bind(Listing listing) {
            if (listing.isLiked()) {
                ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart_full));
                ivHeart.setAlpha(0.7f); // 0.7 or 0.8
            } else {
                ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.heart_empty));
                ivHeart.setAlpha(1.0f);
            }

            Glide.with(context)
                    .load(listing.getImage().getUrl())
//                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(30, 10)))
                    .transform(new CenterCrop())
                    .into(ivListingImage);

            ratingBar.setRating((float) listing.getRating());
            tvPrice.setText(String.format("$%s", listing.getPrice()));
            tvTtitle.setText(listing.getTitle());
            tvLocation.setText(listing.getLocality());

            if (listing.getRating() == 0.0) {
                tvNumRentals.setText("(0)");
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Listing currentListing = listings.get(position);
                Intent intent = new Intent(context, ListingDetailsActivity.class);
                intent.putExtra("listing", Parcels.wrap(currentListing));
                intent.putExtra("liked", currentListing.isLiked());
                intent.putExtra("position", position);

                fragment.startActivityForResult(intent, ExploreFeedFragment.REQUEST_CODE_DETAILS);
            }
        }
    }
}
