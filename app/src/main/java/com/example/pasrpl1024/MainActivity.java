package com.example.pasrpl1024;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pasrpl1024.utils.ChangeEpisodeDialog;
import com.example.pasrpl1024.utils.UserAnime;
import com.example.pasrpl1024.utils.UserClass;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static UserAnime userAnime;

    public static UserAnime getUserAnime() {
        return userAnime;
    }

    public static void setUserAnime(UserAnime userAnime) {
        MainActivity.userAnime = userAnime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        findViewById(R.id.button_main).setOnClickListener(v -> startActivity(new Intent(this, SignUp.class)));
    }
}