package com.kraken.project_unsplash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kraken.project_unsplash.Activities.ImageViewer;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.Network.UrlBuilder;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;
import com.kraken.project_unsplash.Utils.SquareImageView;

import java.util.List;
import java.util.Objects;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotoViewHolder> implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "PhotosRecyclerViewAd";

    // class variables
    private Context context;
    private List<Photo> photos;

    /**
     * constructor
     * @param context : activity
     * @param photos : Photo[]
     */
    public PhotosRecyclerViewAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
        MyApplication.preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // inflate layout and return new viewHolder
        View view = LayoutInflater.from(context).inflate(R.layout.rv_image_item, viewGroup, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int i) {
        Log.d(TAG, "onBindViewHolder: called");

        // Request options for glide
        Log.d(TAG, "onBindViewHolder: color -> " + photos.get(i).getColor());
        RequestOptions requestOptions = new RequestOptions().placeholder(new ColorDrawable(Color.parseColor(photos.get(i).getColor())));

        // Load main image
        Glide.with(context)
                .load(getUrl(photos.get(i)))
                .apply(requestOptions)
                .into(holder.mainImage);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("pref_load_quality")) {
            Log.d(TAG, "onSharedPreferenceChanged: load quality change triggered");
        }
    }

    private String getUrl(Photo photo) {
        Log.d(TAG, "getUrl: " + MyApplication.preferences.getString("pref_load_quality", ""));
        switch (Objects.requireNonNull(MyApplication.preferences.getString("pref_load_quality", ""))) {
            case "Small":
                return photo.getUrls().getSmall();
            case "Regular":
                return photo.getUrls().getRegular();
            case "Full":
                return photo.getUrls().getFull();
            case "Raw":
                return photo.getUrls().getRaw();
                default:
                    return photo.getUrls().getRegular();
        }
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // views in the layout
        SquareImageView mainImage;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage = itemView.findViewById(R.id.rv_image_main);
            int width = context.getResources().getDisplayMetrics().widthPixels;
            int img_width = width / Constants.NUM_COLUMNS;
            mainImage.setMinimumWidth(img_width);
            mainImage.setMaxWidth(img_width);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // launch Image Viewer activity with the photo
            Intent intent = new Intent(context, ImageViewer.class);
            intent.putExtra(context.getResources().getString(R.string.photo_intent_transfer_key), photos.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}
