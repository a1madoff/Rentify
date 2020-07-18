package com.example.rentingapp;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
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
        final boolean isMe = message.getUserId() != null && message.getUserId().equals(mUserId);

        if (isMe) {
            holder.imageMe.setVisibility(View.VISIBLE);
            holder.imageOther.setVisibility(View.GONE);
            holder.layoutMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);

//            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
//            holder.tvName.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        } else {
            holder.imageOther.setVisibility(View.VISIBLE);
            holder.imageMe.setVisibility(View.GONE);
            holder.layoutMessage.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

//            holder.body.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//            holder.tvName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        }

        if (isMe) {
            ImageView profileView = holder.imageMe;
            Glide.with(mContext)
                    .load(R.drawable.profile)
                    .transform(new CircleCrop())
                    .into(profileView);
        } else {
            ImageView profileView = holder.imageOther;
            Glide.with(mContext)
                    .load(R.drawable.random_prof)
                    .transform(new CircleCrop())
                    .into(profileView);
        }
        holder.body.setText(message.getBody());
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
        ImageView imageOther;
        ImageView imageMe;
        TextView body;
        TextView tvName;
        RelativeLayout layoutMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            imageOther = itemView.findViewById(R.id.ivProfileOther);
            imageMe = itemView.findViewById(R.id.ivProfileMe);
            body = itemView.findViewById(R.id.tvBody);
            tvName = itemView.findViewById(R.id.tvName);
            layoutMessage = itemView.findViewById(R.id.layoutMessage);
        }
    }
}
