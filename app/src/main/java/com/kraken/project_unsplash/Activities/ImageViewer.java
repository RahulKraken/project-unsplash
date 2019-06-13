package com.kraken.project_unsplash.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.R;

public class ImageViewer extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        imageView = findViewById(R.id.img_viewer);
        textView = findViewById(R.id.tv_user_name);

        LoadImage();
    }

    private void LoadImage() {
        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra(getResources().getString(R.string.photo_intent_transfer_key));

        // load image with glide
        Glide.with(this)
                .load(photo.getUrls().getRegular())
                .into(imageView);

        textView.setText(photo.getUser().getName());
    }
}
