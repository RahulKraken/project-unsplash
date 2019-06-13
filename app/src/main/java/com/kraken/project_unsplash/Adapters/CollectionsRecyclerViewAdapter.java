package com.kraken.project_unsplash.Adapters;

import android.content.Context;
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
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.R;

public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.CollectionsViewHolder> {

    private static final String TAG = "CollectionsRecyclerView";

    private Collection[] collections;
    private Context context;

    public CollectionsRecyclerViewAdapter(Collection[] collections, Context context) {
        this.collections = collections;
        this.context = context;
        Log.d(TAG, "CollectionsRecyclerViewAdapter: " + collections.length);
    }

    @NonNull
    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_collections_grid_item,
                viewGroup, false);
        return new CollectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int i) {
        Glide.with(context)
                .load(collections[i].getCover_photo().getUrls().getSmall())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background))
                .into(holder.image);
        holder.title.setText(collections[i].getTitle());
    }

    @Override
    public int getItemCount() {
        return collections.length;
    }

    class CollectionsViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;

        CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.rv_collection_img);
            title = itemView.findViewById(R.id.rv_collection_title);
        }
    }
}
