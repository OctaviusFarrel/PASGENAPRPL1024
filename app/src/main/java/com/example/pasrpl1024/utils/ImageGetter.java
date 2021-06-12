package com.example.pasrpl1024.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

public class ImageGetter extends AsyncTask<Void, Void, Bitmap> {

    private String url;
    private ImageView imageView;
    private String error;

    public ImageGetter(String url, ImageView imageView) {
        this.url = url;
        this.imageView = imageView;
    }

    @Override
    public Bitmap doInBackground(Void... voids) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url)
                    .openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            error = "Image link invalid!";
        } catch (IOException e) {
            e.printStackTrace();
            error = "Cannot get image : timeout";
        }
        return null;
    }

    @Override
    public void onPostExecute(Bitmap bitmap) {
        if (bitmap == null) {
            Toast.makeText(imageView.getContext(), error, Toast.LENGTH_SHORT).show();
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }
}
