package com.example.rentingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.rentingapp.R;
import com.example.rentingapp.models.Message;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;
    private String mUserId;

    public ChatAdapter(Context context, String userId, List<Message> messages) {
        mMessages = messages;
        this.mUserId = userId;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = mMessages.get(position);
        final boolean isRenter = message.getUserId() != null && message.getUserId().equals(mUserId);

        if (isRenter) {
            // Renter
            holder.layoutRenter.setVisibility(View.VISIBLE);
            holder.layoutSeller.setVisibility(View.GONE);
            ImageView profileView = holder.ivProfileRenter;
            Glide.with(mContext)
                    .load(R.drawable.random_prof)
                    .transform(new CircleCrop())
                    .into(profileView);

            holder.tvBodyRenter.setText(message.getBody());
        } else {
            // Seller
            holder.layoutSeller.setVisibility(View.VISIBLE);
            holder.layoutRenter.setVisibility(View.GONE);
            ImageView profileView = holder.ivProfileSeller;
            Glide.with(mContext)
                    .load(R.drawable.profile)
                    .transform(new CircleCrop())
                    .into(profileView);

            holder.tvBodySeller.setText(message.getBody());
        }
    }

    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "https://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutRenter;
        ImageView ivProfileRenter;
        TextView tvNameRenter;
        TextView tvBodyRenter;

        RelativeLayout layoutSeller;
        ImageView ivProfileSeller;
        TextView tvNameSeller;
        TextView tvBodySeller;


        public ViewHolder(View itemView) {
            super(itemView);
            layoutRenter = itemView.findViewById(R.id.layoutRenter);
            ivProfileRenter = itemView.findViewById(R.id.ivProfileRenter);
            tvNameRenter = itemView.findViewById(R.id.tvNameRenter);
            tvBodyRenter = itemView.findViewById(R.id.tvBodyRenter);

            layoutSeller = itemView.findViewById(R.id.layoutSeller);
            ivProfileSeller = itemView.findViewById(R.id.ivProfileSeller);
            tvNameSeller = itemView.findViewById(R.id.tvNameSeller);
            tvBodySeller = itemView.findViewById(R.id.tvBodySeller);
        }
    }
}
