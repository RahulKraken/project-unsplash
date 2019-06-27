package com.kraken.project_unsplash.Activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kraken.project_unsplash.Database.DatabaseContract;
import com.kraken.project_unsplash.Database.DatabaseHelper;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.Objects;

/**
 * ImageViewer Activity class
 */
public class ImageViewer extends AppCompatActivity {

    private static final String TAG = "ImageViewer";

    // widgets
    private ImageView imageView, addFavoritesBtn, profileImage;
    private TextView tvUserName, tvName, tvLikesCnt;

    // photo object
    private Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        // set app bar
        Toolbar toolbar = findViewById(R.id.toolbar_image);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // refer to the widgets
        imageView = findViewById(R.id.img_viewer);
        tvName = findViewById(R.id.tv_name);
        tvUserName = findViewById(R.id.tv_user_name);
        addFavoritesBtn = findViewById(R.id.img_btn_add_favorites);
        tvLikesCnt = findViewById(R.id.tv_likes_count);
        profileImage = findViewById(R.id.img_profile_picture);

        // get the intent from invoking activity
        Intent intent = getIntent();
        // extract the stored photo with the intent
        photo = (Photo) intent.getSerializableExtra(getResources().getString(R.string.photo_intent_transfer_key));

        // load image into the image view
        LoadImage();
        // activate the set wallpaper button
//        initSetWallpaperBtn();
        // activate the add to favorites button
        handleFavoritesBtn();
        setProfileListener();
    }

    /**
     * adds/deletes current photo to/from the fav_table in database
     */
    private void handleFavoritesBtn() {
        // add onClickListener on addToFavoritesBtn
        addFavoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: add to favorites " + photo.getId());

                // get readable database
                DatabaseHelper helper = new DatabaseHelper(ImageViewer.this);
                SQLiteDatabase database = helper.getReadableDatabase();

                // columns to be read
                String[] columns = {getResources().getString(R.string.database_photo_id_col_name),
                        DatabaseContract.FavoritesEntry.COLUMN_PHOTO};

                // query database
                Cursor cursor = database.query(DatabaseContract.FavoritesEntry.TABLE_NAME, columns, null,
                        null, null, null, null);

                Photo currPhoto;
                int foundIdx = 0;
                boolean favPhoto = false;

                // loop through all photos and if found the photo already in database then delete it otherwise add it
                while (cursor.moveToNext()) {
                    currPhoto = (Photo) Serializer.photoFromByteArray(cursor.getBlob(
                            cursor.getColumnIndex(DatabaseContract.FavoritesEntry.COLUMN_PHOTO)
                    ));
                    if (Objects.requireNonNull(currPhoto).getId().equals(photo.getId())) {
                        favPhoto = true;
                        foundIdx = cursor.getInt(cursor.getColumnIndex(getResources().getString(R.string.database_photo_id_col_name)));
                        Log.d(TAG, "onClick: index -> " + foundIdx);
                        break;
                    }
                }

                // close cursor and database
                cursor.close();
                database.close();

                if (!favPhoto) addPhotoToDatabase();
                else deletePhotoFromDatabase(foundIdx);
            }
        });
    }

    /**
     * add photo to database
     */
    private void addPhotoToDatabase() {
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

    /**
     * delete photo from database
     * @param index : _id of photo
     */
    private void deletePhotoFromDatabase(int index) {
        DatabaseHelper helper = new DatabaseHelper(ImageViewer.this);
        SQLiteDatabase database = helper.getWritableDatabase();

        // query to delete
        database.delete(DatabaseContract.FavoritesEntry.TABLE_NAME, "_id = " + index, null);
        database.close();
    }

    /**
     * download the image and set it as wallpaper
     */
    /*private void initSetWallpaperBtn() {
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
    }*/

    /**
     * load image into image view
     */
    @SuppressLint("SetTextI18n")
    private void LoadImage() {
        // load image with glide
        Glide.with(this)
                .load(photo.getUrls().getRegular())
                .into(imageView);

        // load profile image
        Glide.with(this)
                .load(photo.getUser().getProfile_image().getSmall())
                .into(profileImage);

        // set text in the text fields
        tvName.setText(photo.getUser().getName());
        tvUserName.setText(photo.getUser().getUsername());
        tvLikesCnt.setText(photo.getLikes() + " Likes");
    }

    private void setProfileListener() {
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ImageViewer.this, UserActivity.class);
                intent.putExtra(getResources().getString(R.string.user_itent_pass_key), photo.getUser().getUsername());
                startActivity(intent);
            }
        });
    }
}
