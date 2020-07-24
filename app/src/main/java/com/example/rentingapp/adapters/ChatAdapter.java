package com.example.rentingapp.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
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
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private List<Message> mMessages;
    private Context mContext;

    public ChatAdapter(Context context, List<Message> messages) {
        mMessages = messages;
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
//        final boolean fromMe = message.getUserId() != null && message.getUserId().equals(mUserId);
        boolean fromMe = message.getFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId());
        String abbrevName = String.format("%s %s.", message.getFromUser().getString("firstName"), message.getFromUser().getString("lastName").charAt(0));

        ImageView profileView;
        if (fromMe) {
            // Renter
            holder.layoutRenter.setVisibility(View.VISIBLE);
            holder.layoutSeller.setVisibility(View.GONE);
            profileView = holder.ivProfileRenter;

            holder.tvNameRenter.setText(abbrevName);
            holder.tvBodyRenter.setText(message.getBody());
        } else {
            // Seller
            holder.layoutSeller.setVisibility(View.VISIBLE);
            holder.layoutRenter.setVisibility(View.GONE);
            profileView = holder.ivProfileSeller;

            holder.tvNameSeller.setText(abbrevName);
            holder.tvBodySeller.setText(message.getBody());
        }

        ParseFile profilePictureFile = message.getFromUser().getParseFile("profilePicture");
        if (profilePictureFile != null) {
            Glide.with(mContext)
                    .load(profilePictureFile.getUrl())
                    .transform(new CircleCrop())
                    .into(profileView);
        } else {
//            float size = 50.0f;
//            profileView.getLayoutParams().height = (int) convertDpToPixel(size, mContext);
//            profileView.getLayoutParams().width = (int) convertDpToPixel(size, mContext);
            profileView.setPadding(0, (int) convertDpToPixel(4, mContext), 0, 0);
            profileView.requestLayout();
            Glide.with(mContext)
                    .load(R.drawable.no_profile_picture)
                    .transform(new CircleCrop())
                    .into(profileView);
        }

    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
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
