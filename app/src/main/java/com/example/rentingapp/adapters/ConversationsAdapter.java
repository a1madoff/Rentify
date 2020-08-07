package com.example.rentingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.rentingapp.ChatActivity;
import com.example.rentingapp.R;
import com.example.rentingapp.models.Listing;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class ConversationsAdapter extends RecyclerView.Adapter<ConversationsAdapter.ViewHolder> {
    Context context;
    List<Listing> listings;

    public ConversationsAdapter(Context context, List<Listing> listings) {
        this.context = context;
        this.listings = listings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_conversation_card, parent, false);
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

    // Adds the list of items
    public void addAll(List<Listing> listingsList) {
        listings.addAll(listingsList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivListingImage;
        public TextView tvSellerName;
        public ImageView ivSellerImage;
        public TextView tvListingName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            ivListingImage = itemView.findViewById(R.id.ivListingImage);
            tvSellerName = itemView.findViewById(R.id.tvSellerName);
            ivSellerImage = itemView.findViewById(R.id.ivSellerImage);
            tvListingName = itemView.findViewById(R.id.tvListingName);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Listing currentListing = listings.get(position);
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("listing", Parcels.wrap(currentListing));
                context.startActivity(intent);
            }
        }

        public void bind(Listing listing) {
            ParseFile profilePictureFile = null;
            try {
                profilePictureFile = listing.getSeller().fetchIfNeeded().getParseFile("profilePicture");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (profilePictureFile != null) {
                Glide.with(context)
                        .load(profilePictureFile.getUrl())
                        .transform(new CircleCrop())
                        .into(ivSellerImage);
            } else {
                Glide.with(context)
                        .load(R.drawable.profile_empty)
                        .transform(new CircleCrop())
                        .into(ivSellerImage);
            }

            String abbrevName = String.format("%s %s.", listing.getSeller().getString("firstName"), listing.getSeller().getString("lastName").charAt(0));
            tvSellerName.setText(abbrevName);

            Glide.with(context)
                    .load(listing.getImage().getUrl())
//                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(30, 10)))
                    .transform(new CenterCrop())
                    .into(ivListingImage);
        }
    }
}
