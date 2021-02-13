package com.example.maplechat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.maplechat.models.User;
import com.example.maplechat.view.UsersAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    UsersAdapter.OnUserClickListener onUserClickListener;
    private RecyclerView recyclerView;
    private ArrayList<User> users;
    private ProgressBar progressBar;
    private UsersAdapter usersAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String myImage;
    private String userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Swipe to refresh to fetch new data
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getUsers();
            swipeRefreshLayout.setRefreshing(false);
        });

        // Progress bar to showing loading spinner
        progressBar = findViewById(R.id.progressBar);

        // All users data container
        users = new ArrayList<>();

        // Recycler View for all users
        recyclerView = findViewById(R.id.users_rv);

        onUserClickListener = position -> {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class)
                    .putExtra("username", users.get(position).getUserName())
                    .putExtra("email", users.get(position).getEmail())
                    .putExtra("image", users.get(position).getImageUrl())
                    .putExtra("my_image", myImage);

            startActivity(intent);
        };

        getUsers();
    }

    // Custom menu icons with menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.user_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.user_account) {
            Intent intent = new Intent(MainActivity.this, UserAccountActivity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.sign_out) {
            FirebaseAuth authentication = FirebaseAuth.getInstance();
            authentication.signOut();
            finish();

            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // Method to get all users from the server (Firebase)
    private void getUsers() {
        users.clear();
        FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    users.add(dataSnapshot.getValue(User.class));
                }
                usersAdapter = new UsersAdapter(users, MainActivity.this, onUserClickListener);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                recyclerView.setAdapter(usersAdapter);
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                // Looping throgh all users
                for (User user : users) {
                    myImage = user.getImageUrl();
                    if (user.getEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        users.remove(user);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}