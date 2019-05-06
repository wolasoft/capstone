package com.wolasoft.maplenou.views.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.databinding.ListItemCategoryBinding;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<Category> categories;
    private final OnCategoryClickedListener listener;

    public CategoryAdapter(List<Category> categories, OnCategoryClickedListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ListItemCategoryBinding dataBinding =
                DataBindingUtil.inflate(inflater, R.layout.list_item_category, parent, false);

        return new ViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = this.categories.get(position);
        holder.bind(category);
    }

    @Override
    public int getItemCount() {
        return this.categories == null ? 0 : this.categories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ListItemCategoryBinding dataBinding;

        ViewHolder(@NonNull ListItemCategoryBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            itemView.setOnClickListener(this);
        }

        public void bind(Category category) {
            this.dataBinding.setCategory(category);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Category category = categories.get(position);
            listener.categoryClicked(category);
        }
    }

    public interface OnCategoryClickedListener {
        void categoryClicked(Category category);
    }
}
