package com.example.pasrpl1024;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pasrpl1024.utils.AnimeData;
import com.example.pasrpl1024.utils.AnimeUserData;
import com.example.pasrpl1024.utils.ImageGetter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class AnimeDetail extends AppCompatActivity {
    private static AnimeDetail animeDetail;

    public static AnimeDetail getAnimeDetail() {
        return animeDetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animeDetail = this;
        setContentView(R.layout.activity_anime_detail);
        if (getIntent().getExtras().getSerializable("data").getClass() == AnimeData.class) {
            AnimeData data = (AnimeData) getIntent().getExtras().getSerializable("data");
            getSupportActionBar().setTitle("Anime Info");
            ((TextView) findViewById(R.id.anime_detail_title)).setText(data.getTitle());
            ((TextView) findViewById(R.id.anime_detail_desc_text)).setText(data.getDesc());
            ImageGetter imageGetter = new ImageGetter(data.getPosterLink(), (ImageView) findViewById(R.id.anime_detail_photo));
            imageGetter.execute();
        } else {
            AnimeUserData data = (AnimeUserData) getIntent().getExtras().getSerializable("data");
            getSupportActionBar().setTitle("Anime Info");
            ((TextView) findViewById(R.id.anime_detail_title)).setText(data.getTitle());
            ((TextView) findViewById(R.id.anime_detail_desc_text)).setText(data.getDesc());
            ImageGetter imageGetter = new ImageGetter(data.getPosterLink(), (ImageView) findViewById(R.id.anime_detail_photo));
            imageGetter.execute();
        }

    }

}