package com.dmitrijdomracev.social.Adapter;

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
import com.dmitrijdomracev.social.MessageActivity;
import com.dmitrijdomracev.social.Model.Chat;
import com.dmitrijdomracev.social.Model.User;
import com.dmitrijdomracev.social.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_RIGHT = 1;
    public static final int MSG_TYPE_LEFT = 0;

    private Context mContext;
    private List<Chat> mChats;
    private String imageURL;

    FirebaseUser firebaseUser;

    public MessageAdapter(Context mContext, List<Chat> chats, String url) {
        this.mContext = mContext;
        this.mChats = chats;
        this.imageURL = url;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        Chat chat = mChats.get(position);

        holder.messageTV.setText(chat.getMessage());

        if (imageURL.equals("default")){
            holder.profileImageIV.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(mContext).load(imageURL).into(holder.profileImageIV);
        }

    }


    @Override
    public int getItemCount() {
        return mChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView messageTV;
        public ImageView profileImageIV;


        public ViewHolder(View itemView) {
            super(itemView);

            messageTV = itemView.findViewById(R.id.show_text);
            profileImageIV = itemView.findViewById(R.id.profile_image);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChats.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else
            return MSG_TYPE_LEFT;
    }
}

