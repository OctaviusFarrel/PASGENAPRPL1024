package com.example.pasrpl1024;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pasrpl1024.utils.AnimeAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class WatchedAnime extends AppCompatActivity {
    private static WatchedAnime watchedAnime;

    public static WatchedAnime getWatchedAnime() {
        return watchedAnime;
    }

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        watchedAnime = this;
        setContentView(R.layout.activity_list_anime);
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setSubtitle("Watched Anime");
        DrawerLayout layout = findViewById(R.id.seasoned_anime_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, layout, R.string.nav_open, R.string.nav_close);
        ((NavigationView) findViewById(R.id.seasoned_anime_navigation)).setNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.drawer_season_anime:
                    startActivity(new Intent(this, SeasonedAnime.class));
                    return false;
                case R.id.drawer_profile:
                    startActivity(new Intent(this, ProfileAnime.class));
                    return false;
                case R.id.drawer_watching:
                    startActivity(new Intent(this, WatchingAnime.class));
                    return false;
                case R.id.drawer_plant:
                    return false;
                case R.id.drawer_logout:
                    if (MainActivity.getUserAnime() == null) {
                        Toast.makeText(this, "You're already logged out!", Toast.LENGTH_SHORT).show();
                    } else {
                        MainActivity.getUserAnime().setAnimeData(null);
                        MainActivity.getUserAnime().setUser(null);
                        MainActivity.setUserAnime(null);
                        Toast.makeText(this, "You're logged out", Toast.LENGTH_SHORT).show();
                    }
                    startActivity(new Intent(this, Login.class));
                    return false;
            }
            return false;
        });
        layout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView recyclerView = findViewById(R.id.seasoned_anime_recycler);
        recyclerView.setAdapter(AnimeAdapter.getAdapter(AnimeAdapter.AdapterType.WATCHED_ANIME));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        actionBarDrawerToggle.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }
}