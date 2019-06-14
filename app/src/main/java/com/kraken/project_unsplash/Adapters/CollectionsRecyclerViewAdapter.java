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
import com.kraken.project_unsplash.Activities.CollectionView;
import com.kraken.project_unsplash.Models.Collection;
import com.kraken.project_unsplash.R;

public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.CollectionsViewHolder> {

    // TAG for log messages
    private static final String TAG = "CollectionsRecyclerView";

    // collection array
    private Collection[] collections;
    private Context context;

    /**
     * constructor
     * @param collections : collection[]
     * @param context : activity context
     */
    public CollectionsRecyclerViewAdapter(Collection[] collections, Context context) {
        this.collections = collections;
        this.context = context;
        Log.d(TAG, "CollectionsRecyclerViewAdapter: " + collections.length);
    }

    @NonNull
    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // inflate view from layout and return a view holder with that view
        View view = LayoutInflater.from(context).inflate(R.layout.rv_collections_grid_item,
                viewGroup, false);
        return new CollectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int i) {
        // populate the image view with small size cover photo of the collection
        Glide.with(context)
                .load(collections[i].getCover_photo().getUrls().getSmall())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.ic_launcher_background))
                .into(holder.image);
        // and set the title of the collection card
        holder.title.setText(collections[i].getTitle());
    }

    @Override
    public int getItemCount() {
        // return how many items are there in the recycler view
        return collections.length;
    }

    class CollectionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /**
         * View holder class for Collection recycler view
         */

        // widgets in one recycler view item
        ImageView image;
        TextView title;

        /**
         * constructor
         * @param itemView : View
         */
        CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.rv_collection_img);
            title = itemView.findViewById(R.id.rv_collection_title);

            // set on click listener : "this" passed as parameter because class implements onClickListener
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // create intent to CollectionView class and put collection as intent extra
            Intent intent = new Intent(context, CollectionView.class);
            intent.putExtra(context.getResources().getString(R.string.collection_parcelable_intent_extra),
                    collections[getAdapterPosition()]);
            // start the activity
            context.startActivity(intent);
        }
    }
}
