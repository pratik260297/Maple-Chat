package com.example.maplechat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.maplechat.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    private Button sign_in_button;
    private Button sign_up_button;
    private EditText user_name_edit_text;
    private EditText email_edit_text;
    private EditText password_edit_text;
    private EditText confirm_password_edit_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        sign_in_button = findViewById(R.id.sign_in_button);
        sign_up_button = findViewById(R.id.sign_up_button);
        user_name_edit_text = findViewById(R.id.user_name_edit_text);
        email_edit_text = findViewById(R.id.email_edit_text);
        password_edit_text = findViewById(R.id.password_edit_text);
        confirm_password_edit_text = findViewById(R.id.confirm_password_edit_text);


        // Sign in button to redirect users on the sign in screen
        sign_in_button.setOnClickListener(view -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });


        // Sign up button for register a new user
        sign_up_button.setOnClickListener(view -> {
            final String userName = user_name_edit_text.getText().toString();
            final String email = email_edit_text.getText().toString();
            final String password = password_edit_text.getText().toString();
            final String confirmPassword = confirm_password_edit_text.getText().toString();

            // Validation for text fields
            if (userName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                validate(user_name_edit_text);
                validate(email_edit_text);
                validate(password_edit_text);
                validate(confirm_password_edit_text);
                return;
            } else {
                handleSignUp();
            }
        });
    }

    // Method for sign up new user in database
    private void handleSignUp() {
        final String userName = user_name_edit_text.getText().toString();
        final String email = email_edit_text.getText().toString();
        final String password = password_edit_text.getText().toString();
        final String imageUrl = "";

        FirebaseAuth authenticate = FirebaseAuth.getInstance();

        // Sign up logic with email and password
        authenticate.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                database.getReference("users/" + authenticate.getCurrentUser().getUid()).setValue(new User(userName, email, imageUrl));

                Toast.makeText(SignUpActivity.this, "Sign up successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(SignUpActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Textfild validation method
    private void validate(EditText input) {
        input.setError("Required");
        Toast.makeText(this, "Please fill required details!", Toast.LENGTH_LONG).show();
    }
}