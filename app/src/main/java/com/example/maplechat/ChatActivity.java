package com.example.maplechat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.maplechat.models.Message;
import com.example.maplechat.models.User;
import com.example.maplechat.view.MessagesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private ImageView profileImage;
    private TextView userName;
    private RecyclerView messagesRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private ProgressBar progressBar;
    private ArrayList<Message> messages;
    private MessagesAdapter messagesAdapter;
    private String userNameOfFriend;
    private String email;
    private String chatRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        userNameOfFriend = getIntent().getStringExtra("username");
        email = getIntent().getStringExtra("email");

        messagesRecyclerView = findViewById(R.id.messages_rv);
        messageEditText = findViewById(R.id.message_edit_text);
        userName = findViewById(R.id.user_name_text_view);
        sendButton = findViewById(R.id.send_button);
        progressBar = findViewById(R.id.progressBar);
        profileImage = findViewById(R.id.user_image_image_view);

        userName.setText(userNameOfFriend);

        messages = new ArrayList<>();

        // Send button for sending the messages
        sendButton.setOnClickListener(v -> {
            FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).push().setValue(new Message(FirebaseAuth.getInstance().getCurrentUser().getEmail(), email, messageEditText.getText().toString()));
            messageEditText.setText("");
        });

        messagesAdapter = new MessagesAdapter(messages, getIntent().getStringExtra("my_image"), getIntent().getStringExtra("image"), ChatActivity.this);

        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messagesRecyclerView.setAdapter(messagesAdapter);

        Glide.with(ChatActivity.this).load(getIntent().getStringExtra("image")).placeholder(R.drawable.avatar).error(R.drawable.avatar).into(profileImage);
        setUpChatRoom();
    }

    // Creating chat rooms for user to chat with each other
    private void setUpChatRoom() {
        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String myUserName = snapshot.getValue(User.class).getUserName();

                        if (userNameOfFriend.compareTo(myUserName) > 0) {
                            chatRoomId = myUserName + userNameOfFriend;
                        } else if (userNameOfFriend.compareTo(myUserName) == 0) {
                            chatRoomId = myUserName + userNameOfFriend;
                        } else {
                            chatRoomId = userNameOfFriend + myUserName;
                        }

                        attachMessageListener(chatRoomId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    // Sending the message and receive the message from firebase
    private void attachMessageListener(String chatRoomId) {
        FirebaseDatabase.getInstance().getReference("messages/" + chatRoomId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messages.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    messages.add(dataSnapshot.getValue(Message.class));
                }
                messagesAdapter.notifyDataSetChanged();
                messagesRecyclerView.scrollToPosition(messages.size() - 1);
                messagesRecyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}