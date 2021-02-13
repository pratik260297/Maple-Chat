package com.example.maplechat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    private Button sign_in_button;
    private Button new_user_button;
    private EditText email_edit_text;
    private EditText password_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sign_in_button = findViewById(R.id.sign_in_button);
        new_user_button = findViewById(R.id.new_user_button);
        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);


        // Creating object for Firebase
        FirebaseAuth authenticate = FirebaseAuth.getInstance();

        // Validation logic for log in
        if (authenticate.getCurrentUser() != null) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Sign in in button
        sign_in_button.setOnClickListener(view -> {
            final String email = email_edit_text.getText().toString();
            final String password = password_edit_text.getText().toString();

            // Checking that if user has entered the details or not
            if ((email.isEmpty() && email.length() <= 0) || (password.isEmpty() && password.length() <= 0)) {
                validate(email_edit_text);
                validate(password_edit_text);
                return;
            } else {
                handleSignIn();
            }
        });

        // Button to redirect user to the sign up screen
        new_user_button.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    // Method to handle sign in functionality
    private void handleSignIn() {
        final String email = email_edit_text.getText().toString();
        final String password = password_edit_text.getText().toString();

        FirebaseAuth authenticate = FirebaseAuth.getInstance();

        // Authenticated user with their email and password if exists
        authenticate.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(SignInActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignInActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    // Validation logic for text fields
    private void validate(EditText input) {
        input.setError("Required");
        Toast.makeText(this, "Please fill required details!", Toast.LENGTH_LONG).show();
    }

}