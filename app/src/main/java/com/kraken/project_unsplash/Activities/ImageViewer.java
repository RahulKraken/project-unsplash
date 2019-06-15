package com.kraken.project_unsplash.Activities;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
import com.kraken.project_unsplash.Database.DatabaseContract;
import com.kraken.project_unsplash.Database.DatabaseHelper;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Serializer;

/**
 * ImageViewer Activity class
 */
public class ImageViewer extends AppCompatActivity {

    private static final String TAG = "ImageViewer";

    // widgets
    private ImageView imageView;
    private TextView tvUserName, tvDescription, tvLikesCnt;
    private Button setWallpaperBtn;
    private ImageButton addFavoritesBtn;

    // photo object
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide status bar
        hideStatusBar();
        setContentView(R.layout.activity_image_viewer);

        // refer to the widgets
        imageView = findViewById(R.id.img_viewer);
        tvUserName = findViewById(R.id.tv_user_name);
        tvDescription = findViewById(R.id.tv_description);
        setWallpaperBtn = findViewById(R.id.setWallpaperBtn);
        addFavoritesBtn = findViewById(R.id.img_btn_add_favorites);
        tvLikesCnt = findViewById(R.id.tv_likes_count);

        // get the intent from invoking activity
        Intent intent = getIntent();
        // extract the stored photo with the intent
        photo = (Photo) intent.getSerializableExtra(getResources().getString(R.string.photo_intent_transfer_key));

        // load image into the image view
        LoadImage();
        // activate the set wallpaper button
        initSetWallpaperBtn();
        // activate the add to favorites button
        initAddToFavoritesBtn();
    }

    /**
     * adds current photo to the fav_table in database
     */
    private void initAddToFavoritesBtn() {
        // add onClickListener on addToFavoritesBtn
        addFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: add to favorites " + photo.getId());

                // get the byte[] from photo
                byte[] bytes = Serializer.objectToByteArray(photo);
                Log.d(TAG, "onClick: converted to byte array");

                // get the database
                DatabaseHelper helper = new DatabaseHelper(ImageViewer.this);
                SQLiteDatabase database = helper.getWritableDatabase();

                // create the content values
                ContentValues values = new ContentValues();
                values.put(DatabaseContract.FavoritesEntry.COLUMN_PHOTO, bytes);

                // insert into the database
                database.insert(DatabaseContract.FavoritesEntry.TABLE_NAME, null, values);
                database.close();
            }
        });
    }

    /**
     * download the image and set it as wallpaper
     */
    private void initSetWallpaperBtn() {
        // add onClickListener to set wallpaper btn
        setWallpaperBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // image request to download image
                ImageRequest imageRequest = new ImageRequest(photo.getLinks().getDownload(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        Log.d(TAG, "onResponse: 200 OK\n" + response.toString());
                        // wallpaper manager instance
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
                        try {
                            // set downloaded bitmap as wallpaper
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

    /**
     * load image into image view
     */
    @SuppressLint("SetTextI18n")
    private void LoadImage() {
        // load image with glide
        Glide.with(this)
                .load(photo.getUrls().getRegular())
                .into(imageView);

        // set text in the text fields
        tvUserName.setText(photo.getUser().getName());
        if (photo.getDescription() != null || photo.getAlt_description() != null) {
            tvDescription.setText(photo.getDescription() != null ? photo.getDescription() : photo.getAlt_description());
        }
        tvLikesCnt.setText(photo.getLikes() + " Likes");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // hide status bar
        hideStatusBar();
    }

    /**
     * hides the status bar
     */
    private void hideStatusBar() {
        // hide the status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
}
