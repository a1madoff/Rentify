package com.example.rentingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.rentingapp.ListingDetailsActivity;
import com.example.rentingapp.R;
import com.example.rentingapp.models.Listing;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MyListingsAdapter extends RecyclerView.Adapter<MyListingsAdapter.ViewHolder> {
    Context context;
    List<Listing> myListings;

    public MyListingsAdapter(Context context, List<Listing> myListings) {
        this.context = context;
        this.myListings = myListings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_listing, parent, false);
        return new MyListingsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Listing listing = myListings.get(position);
        holder.bind(listing);
    }

    @Override
    public int getItemCount() {
        return myListings.size();
    }

    // Adds the list of items
    public void addAll(List<Listing> listingsList) {
        myListings.addAll(listingsList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivMyListingImage;
        public TextView tvMyListingName;
        public TextView tvMyListingPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMyListingImage = itemView.findViewById(R.id.ivMyListingImage);
            tvMyListingName = itemView.findViewById(R.id.tvMyListingName);
            tvMyListingPrice = itemView.findViewById(R.id.tvMyListingPrice);

            itemView.setOnClickListener(this);
        }

        public void bind(Listing listing) {
            Glide.with(context)
                    .load(listing.getImage().getUrl())
//                    .transform(new MultiTransformation(new CenterCrop(), new RoundedCornersTransformation(30, 10)))
                    .transform(new CenterCrop())
                    .into(ivMyListingImage);

            String boldText = String.format("$%s", listing.getPrice());
            String normalText = " / day";
            SpannableString str = new SpannableString(boldText + normalText);
            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvMyListingPrice.setText(str);

            tvMyListingName.setText(listing.getTitle());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Listing currentListing = myListings.get(position);
                Intent intent = new Intent(context, ListingDetailsActivity.class);
                intent.putExtra("listing", Parcels.wrap(currentListing));
                context.startActivity(intent);
            }
        }
    }

}
