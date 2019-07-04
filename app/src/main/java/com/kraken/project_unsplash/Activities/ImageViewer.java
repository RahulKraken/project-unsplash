package com.kraken.project_unsplash.Activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.kraken.project_unsplash.Database.DatabaseContract;
import com.kraken.project_unsplash.Database.DatabaseHelper;
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Params;
import com.kraken.project_unsplash.Utils.Serializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImageViewer Activity class
 */
public class ImageViewer extends AppCompatActivity {

    private static final String TAG = "ImageViewer";

    // widgets
    private ImageView imageView, profileImage;
    private TextView tvUserName, tvName, tvLikesCnt;
    private LinearLayout addFavoritesBtn, addCollectionsBtn, downloadBtn;

    // photo object
    private Photo photo;
    private ArrayList<String> collectionTitles;
    private List<Collection> collectionsArrayList;
    private ArrayAdapter<String> collectionsDialogAdapter;

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
        tvLikesCnt = findViewById(R.id.tv_likes_count);
        profileImage = findViewById(R.id.img_profile_picture);
        addFavoritesBtn = findViewById(R.id.img_btn_add_favorites);
        addCollectionsBtn = findViewById(R.id.img_btn_add_collections);
        downloadBtn = findViewById(R.id.img_btn_download);

        collectionTitles = new ArrayList<>();

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
        handleCollectionsBtn();
        handleDownloadBtn();
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
                // todo : add unlike functionality
                Log.d(TAG, "onClick: Adding to favorites");
                if (MyApplication.AUTHENTICATED) {
                    likePhoto();
                } else {
                    startActivity(new Intent(ImageViewer.this, LoginActivity.class));
                }
            }
        });
    }

    private void likePhoto() {
        StringRequest likePhotoRequest = new StringRequest(Request.Method.POST, UrlBuilder.likePhoto(photo.getId()), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Params.getParams(ImageViewer.this);
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.statusCode == 201) Log.d(TAG, "parseNetworkResponse: 200 OK : photo liked");
                else Log.d(TAG, "parseNetworkResponse: " + response.statusCode);
                return super.parseNetworkResponse(response);
            }
        };

        MyApplication.getLocalRequestQueue().add(likePhotoRequest);
    }

    private void handleCollectionsBtn() {
        addCollectionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.AUTHENTICATED) {
                    fetchCollectionsList();
                } else {
                    startActivity(new Intent(ImageViewer.this, LoginActivity.class));
                }
            }
        });
    }

    private void fetchCollectionsList() {
        Log.d(TAG, "fetchCollectionsList: ");
        collectionsArrayList = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, UrlBuilder.getUserCollections(MyApplication.me.getUsername(), 1), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
                collectionsArrayList = new Serializer().listCollections(response);
                Log.d(TAG, "onResponse: " + collectionsArrayList.size());
                collectionTitles.clear();
                for (Collection c : collectionsArrayList) {
                    collectionTitles.add(c.getTitle());
                }
                Log.d(TAG, "onResponse: just test statement");
                launchCollectionChooserDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Params.getParams(ImageViewer.this);
            }
        };

        MyApplication.getLocalRequestQueue().add(request);
    }

    private void launchCollectionChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        collectionsDialogAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        collectionsDialogAdapter.addAll(collectionTitles);

        builder.setTitle("Collections");
        builder.setAdapter(collectionsDialogAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToCollection(collectionsArrayList.get(which).getId());
            }
        }).show();
    }

    private void addToCollection(final int collectionId) {
        Log.d(TAG, "addToCollection: " + UrlBuilder.addPhotoToCollection(collectionId));

        StringRequest request = new StringRequest(Request.Method.POST, UrlBuilder.addPhotoToCollection(collectionId), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse: 200 OK\n" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return Params.getParams(ImageViewer.this);
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("photo_id", photo.getId());
                map.put("collection_id", String.valueOf(collectionId));
                return map;
            }
        };

        MyApplication.getLocalRequestQueue().add(request);
    }

    private void handleDownloadBtn() {
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Downloading photo");
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
//    private void initSetWallpaperBtn() {
//        // add onClickListener to set wallpaper btn
//        setWallpaperBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // image request to download image
//                ImageRequest imageRequest = new ImageRequest(photo.getLinks().getDownload(), new Response.Listener<Bitmap>() {
//                    @Override
//                    public void onResponse(Bitmap response) {
//                        Log.d(TAG, "onResponse: 200 OK\n" + response.toString());
//                        // wallpaper manager instance
//                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
//                        try {
//                            // set downloaded bitmap as wallpaper
//                            wallpaperManager.setBitmap(response);
//                            Toast.makeText(ImageViewer.this, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Toast.makeText(ImageViewer.this, "Failed to set wallpaper!", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }, 0, 0, ImageView.ScaleType.FIT_CENTER,  null,
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d(TAG, "onErrorResponse: " + error.getMessage());
//                            }
//                        });
//                MyApplication.getLocalRequestQueue().add(imageRequest);
//            }
//        });
//    }

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
                intent.putExtra(getResources().getString(R.string.user_intent_pass_key), photo.getUser().getUsername());
                startActivity(intent);
            }
        });
    }
}
