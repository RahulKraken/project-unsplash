package com.kraken.project_unsplash.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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
import com.kraken.project_unsplash.Models.Photo;
import com.kraken.project_unsplash.MyApplication;
import com.kraken.project_unsplash.R;

import java.util.List;
import java.util.Objects;

public class CollectionsRecyclerViewAdapter extends RecyclerView.Adapter<CollectionsRecyclerViewAdapter.CollectionsViewHolder> implements SharedPreferences.OnSharedPreferenceChangeListener {

  // TAG for log messages
  private static final String TAG = "CollectionsRecyclerView";

  // collection list
  private List<Collection> collections;
  private Context context;

  // request options for glide
  private RequestOptions requestOptions;

  /**
   * constructor
   * @param collections : collection[]
   * @param context : activity context
   */
  public CollectionsRecyclerViewAdapter(Context context, List<Collection> collections) {
    this.collections = collections;
    this.context = context;
    Log.d(TAG, "CollectionsRecyclerViewAdapter: " + collections.size());
  }

  @NonNull
  @Override
  public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    // inflate view from layout and return a view holder with that view
    View view = LayoutInflater.from(context).inflate(R.layout.rv_collections_item,
      viewGroup, false);
    requestOptions = new RequestOptions()
      .placeholder(R.color.md_grey_300);

    return new CollectionsViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int i) {
    // load the images
    loadImage(getUrl(collections.get(i).getCover_photo()), holder.largeImage);
    loadImage(getUrl(collections.get(i).getPreview_photos()[1]), holder.smallImageTop);
    if (collections.get(i).getPreview_photos().length > 1)
      loadImage(getUrl(collections.get(i).getPreview_photos()[2]), holder.smallImageBottom);

    // set details in text view
    holder.title.setText(collections.get(i).getTitle());
    holder.username.setText("by @" + collections.get(i).getUser().getUsername());
    holder.photoCnt.setText(String.valueOf(collections.get(i).getTotal_photos()) + " Photos");
  }

  @Override
  public int getItemCount() {
    // return how many items are there in the recycler view
    return collections.size();
  }

  /**
   * load image into imageView using url
   * @param url : URL of image
   * @param imageView : target view
   */
  private void loadImage(String url, ImageView imageView) {
    // load image using Glide
    Glide.with(context)
      .load(url)
      .apply(requestOptions)
      .into(imageView);
  }

  @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    if (key.equals("pref_load_quality")) {
      Log.d(TAG, "onSharedPreferenceChanged: Load quality chnage triggered");
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

  class CollectionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    /**
     * View holder class for Collection recycler view
     */

    // widgets in one recycler view item
    ImageView largeImage, smallImageTop, smallImageBottom;
    TextView title, username, photoCnt;

    /**
     * constructor
     * @param itemView : View
     */
    CollectionsViewHolder(@NonNull View itemView) {
      super(itemView);

      largeImage = itemView.findViewById(R.id.rv_collection_large_img);
      smallImageTop = itemView.findViewById(R.id.rv_collection_small_img_top);
      smallImageBottom = itemView.findViewById(R.id.rv_collection_small_img_bottom);

      title = itemView.findViewById(R.id.rv_collection_title);
      username = itemView.findViewById(R.id.rv_collection_username);
      photoCnt = itemView.findViewById(R.id.rv_collection_photo_cnt);

      // set on click listener : "this" passed as parameter because class implements onClickListener
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      // create intent to CollectionView class and put collection as intent extra
      Intent intent = new Intent(context, CollectionView.class);
      intent.putExtra(context.getResources().getString(R.string.collection_parcelable_intent_extra),
        collections.get(getAdapterPosition()));
      // start the activity
      context.startActivity(intent);
    }
  }
}
