package com.kraken.project_unsplash.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kraken.project_unsplash.Activities.CategoryActivity;
import com.kraken.project_unsplash.R;
import com.kraken.project_unsplash.Utils.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private static final String TAG = "CategoriesAdapter";

    private Context context;
    private List<String> categories;

    public CategoriesAdapter(Context context) {
        this.context = context;
        categories = new ArrayList<>();
        categories.addAll(Arrays.asList(Constants.CATEGORIES));
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_categories_item, viewGroup, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int i) {
        holder.title.setText(categories.get(i));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class CategoriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;

        CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.categories_item_tv);
            title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, CategoryActivity.class);
            intent.putExtra(context.getResources().getString(R.string.category_intent_pass_key), categories.get(getAdapterPosition()));
            context.startActivity(intent);
        }
    }
}
