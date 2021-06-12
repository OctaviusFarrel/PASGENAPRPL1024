package com.example.pasrpl1024;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pasrpl1024.utils.ImageGetter;

public class ProfileAnime extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MainActivity.getUserAnime() == null) {
            Toast.makeText(this, "You're already logged out!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, Login.class));
            return;
        }
        setContentView(R.layout.activity_profile_anime);
        getSupportActionBar().setTitle("Profile");
        ((TextView) findViewById(R.id.profile_anime_detail_username)).setText(MainActivity.getUserAnime().getUser().getUsername());
        ImageGetter image = new ImageGetter(MainActivity.getUserAnime().getUser().getProfilePicture(), (ImageView) findViewById(R.id.profile_anime_detail_photo));
        image.execute();
    }
}