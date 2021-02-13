package com.example.maplechat.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.maplechat.R;
import com.example.maplechat.models.User;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserHolder> {
    public OnUserClickListener onUserClickListener;
    private ArrayList<User> users;
    private Context context;

    public UsersAdapter(ArrayList<User> users, Context context, OnUserClickListener onUserClickListener) {
        this.users = users;
        this.context = context;
        this.onUserClickListener = onUserClickListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_card, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        Glide.with(context).load(users.get(position).getImageUrl()).error(R.drawable.avatar).placeholder(R.drawable.avatar).into(holder.userImage);
        holder.userName.setText(users.get(position).getUserName());
        holder.email.setText(users.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface OnUserClickListener {
        void onUserClicked(int position);
    }

    class UserHolder extends RecyclerView.ViewHolder {
        private ImageView userImage;
        private TextView userName;
        private TextView email;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(view -> onUserClickListener.onUserClicked(getAdapterPosition()));
            userImage = itemView.findViewById(R.id.user_image_image_view);
            userName = itemView.findViewById(R.id.user_name_text_view);
            email = itemView.findViewById(R.id.email_text_view);
        }
    }
}
