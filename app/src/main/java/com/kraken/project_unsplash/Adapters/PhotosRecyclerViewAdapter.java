package com.kraken.project_unsplash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kraken.project_unsplash.Activities.ImageViewer;
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.R;

import java.util.List;

public class PhotosRecyclerViewAdapter extends RecyclerView.Adapter<PhotosRecyclerViewAdapter.PhotoViewHolder> {

    private static final String TAG = "StaggeredRecyclerViewAd";

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
        RequestOptions requestOptions = new RequestOptions().placeholder(R.color.md_grey_100);

        // Load main image
        Glide.with(context)
                .load(photos.get(i).getUrls().getSmall())
                .apply(requestOptions)
                .into(holder.mainImage);

        // load user profile image
        Glide.with(context)
                .load(photos.get(i).getUser().getProfile_image().getSmall())
                .apply(requestOptions)
                .into(holder.profileImage);

        // set user full name
        holder.fullName.setText(photos.get(i).getUser().getName());
        // set user username
        String username = "@" + photos.get(i).getUser().getUsername();
        holder.username.setText(username);
        // set likes count
        holder.likesCnt.setText(String.valueOf(photos.get(i).getLikes()));
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // views in the layout
        ImageView mainImage, profileImage;
        TextView fullName, username, likesCnt;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            mainImage = itemView.findViewById(R.id.rv_image_main);
            profileImage = itemView.findViewById(R.id.rv_image_profile_picture);
            fullName = itemView.findViewById(R.id.rv_image_name);
            username = itemView.findViewById(R.id.rv_image_username);
            likesCnt = itemView.findViewById(R.id.rv_image_like_cnt);
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
