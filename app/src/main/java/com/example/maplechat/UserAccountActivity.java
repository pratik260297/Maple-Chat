package com.example.maplechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.UUID;

public class UserAccountActivity extends AppCompatActivity {
    private ImageView user_image;
    private TextView user_name_text_view;
    private TextView email_text_view;
    private Uri imageUrl;
    private Button upload_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        user_image = findViewById(R.id.user_image);
        upload_image = findViewById(R.id.upload_image_button);

        // User Image selection
        user_image.setOnClickListener(view -> {
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setType("image/*");
            startActivityForResult(gallery, 1);
        });

        // Open gallery
        upload_image.setOnClickListener(view -> {
            uploadImage();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUrl = data.getData();
            getImage();
        }
    }

    // Getting and setting image
    private void getImage() {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        user_image.setImageBitmap(bitmap);
    }

    // Upload the image on the firebase
    private void uploadImage() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Uploading...");
        dialog.show();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        storage.getReference("images/" + UUID.randomUUID().toString()).putFile(imageUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                task.getResult().getStorage().getDownloadUrl().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        updateProfilePicture(task1.getResult().toString());
                        Toast.makeText(UserAccountActivity.this, "Image uploaded!", Toast.LENGTH_LONG).show();
                    }
                });

            } else {
                Toast.makeText(UserAccountActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }).addOnProgressListener(snapshot -> {
            // Progress for image upload
            final double progress = 100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
            dialog.setMessage("Image Uploaded  " + (int) progress + "%");
        });
    }

    // Updating profile picture for the user on the server
    private void updateProfilePicture(String Url) {
        FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/imageUrl").setValue(Url);
    }
}