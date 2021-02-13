package com.example.maplechat.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.maplechat.R;
import com.example.maplechat.models.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    private ArrayList<Message> messages;
    private String senderImage;
    private String receiverImage;
    private Context context;

    public MessagesAdapter(ArrayList<Message> messages, String senderImage, String receiverImage, Context context) {
        this.messages = messages;
        this.senderImage = senderImage;
        this.receiverImage = receiverImage;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_holder, parent, false);
        return new MessagesViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MessagesViewHolder holder, int position) {
        holder.messages.setText(messages.get(position).getMessage());

        LinearLayout layout = holder.layout;

        if (messages.get(position).getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            Glide.with(context).load(senderImage).error(R.drawable.avatar).placeholder(R.drawable.avatar).into(holder.profileImage);
        } else {
            Glide.with(context).load(receiverImage).error(R.drawable.avatar).placeholder(R.drawable.avatar).into(holder.profileImage);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessagesViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;
        TextView messages;
        ImageView profileImage;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);

            layout = itemView.findViewById(R.id.messages_liner_layout);
            messages = itemView.findViewById(R.id.message_text);
            profileImage = itemView.findViewById(R.id.small_image);
        }
    }
}
