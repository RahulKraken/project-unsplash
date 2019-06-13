package com.kraken.project_unsplash.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.R;

public class FeaturedPhotosRecyclerViewAdapter extends RecyclerView.Adapter<FeaturedPhotosRecyclerViewAdapter.ScatteredRecyclerViewHolder> {

    private static final String TAG = "StaggeredRecyclerViewAd";

    private Context context;
    private Photo[] photos;

    public FeaturedPhotosRecyclerViewAdapter(Context context, Photo[] photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public ScatteredRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_featured_grid_item, viewGroup,
                false);
        return new ScatteredRecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScatteredRecyclerViewHolder holder, int i) {
        Log.d(TAG, "onBindViewHolder: called");
        Glide.with(context)
                .load(photos[i].getUrls().getRegular())
                .apply(new RequestOptions()
                            .placeholder(R.drawable.ic_launcher_background))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photos.length;
    }

    class ScatteredRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        ScatteredRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rv_item_image_view);
        }
    }
}
