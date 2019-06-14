package com.kraken.project_unsplash.Activities;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.bumptech.glide.Glide;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.R;

public class ImageViewer extends AppCompatActivity {

    private static final String TAG = "ImageViewer";

    private ImageView imageView;
    private TextView tvUserName, tvDescription, tvLikesCnt;
    private Button setWallpaperBtn;
    private ImageButton addFavoritesBtn;
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_image_viewer);

        imageView = findViewById(R.id.img_viewer);
        tvUserName = findViewById(R.id.tv_user_name);
        tvDescription = findViewById(R.id.tv_description);
        setWallpaperBtn = findViewById(R.id.setWallpaperBtn);
        addFavoritesBtn = findViewById(R.id.img_btn_add_favorites);
        tvLikesCnt = findViewById(R.id.tv_likes_count);

        Intent intent = getIntent();
        photo = (Photo) intent.getSerializableExtra(getResources().getString(R.string.photo_intent_transfer_key));

        LoadImage();
        initSetWallpaperBtn();
        initAddToFavoritesBtn();
    }

    private void initAddToFavoritesBtn() {
        addFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: add to favorites " + photo.getId());
                Toast.makeText(ImageViewer.this, "Adding to favorites", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSetWallpaperBtn() {
        setWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageRequest imageRequest = new ImageRequest(photo.getLinks().getDownload(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.d(TAG, "onResponse: 200 OK\n" + response.toString());
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            wallpaperManager.setBitmap(response);
                            Toast.makeText(ImageViewer.this, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ImageViewer.this, "Failed to set wallpaper!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 0, 0, ImageView.ScaleType.FIT_CENTER,  null,
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "onErrorResponse: " + error.getMessage());
                            }
                        });
                MyApplication.getLocalRequestQueue().add(imageRequest);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void LoadImage() {
        // load image with glide
        Glide.with(this)
                .load(photo.getUrls().getRegular())
                .into(imageView);

        tvUserName.setText(photo.getUser().getName());
        if (photo.getDescription() != null || photo.getAlt_description() != null) {
            tvDescription.setText(photo.getDescription() != null ? photo.getDescription() : photo.getAlt_description());
        }
        tvLikesCnt.setText(photo.getLikes() + " Likes");
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideStatusBar();
    }

    private void hideStatusBar() {
        // hide the status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
