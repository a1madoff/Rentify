package com.example.rentingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.parceler.Parcels;

import java.util.List;

public class ListingsAdapter extends RecyclerView.Adapter<ListingsAdapter.ViewHolder> {
    Context context;
    List<Listing> listings;

    public ListingsAdapter(Context context, List<Listing> listings) {
        this.context = context;
        this.listings = listings;
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
    public int getItemCount() {
        return listings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivListingImage;
        public ImageView ivHeart;
        public TextView tvPrice;
        public TextView tvDescription;
        public RatingBar ratingBar;
        public TextView tvNumRentals;
        public TextView tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivListingImage = itemView.findViewById(R.id.ivListingImage);
            ivHeart = itemView.findViewById(R.id.ivHeart);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            tvNumRentals = itemView.findViewById(R.id.tvNumRentals);
            tvLocation = itemView.findViewById(R.id.tvLocation);

            itemView.setOnClickListener(this);

            ivHeart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // TODO: change this to keep track of liked listings, add unlike functionality
                    ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.full_heart));
                }
            });
        }

        public void bind(Listing listing) {
            ratingBar.setRating((float) 4.2);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Listing currentListing = listings.get(position);
                Intent intent = new Intent(context, ListingDetailsActivity.class);
//                intent.putExtra(Listing.class.getSimpleName(), Parcels.wrap(currentListing));
                context.startActivity(intent);
            }
        }
    }

}
