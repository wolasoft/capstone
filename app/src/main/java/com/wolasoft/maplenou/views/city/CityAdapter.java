package com.wolasoft.maplenou.views.city;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.City;
import com.wolasoft.maplenou.databinding.ListItemCityBinding;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private final List<City> cities;
    private final OnCityClickedListener listener;

    public CityAdapter(List<City> cities, OnCityClickedListener listener) {
        this.cities = cities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ListItemCityBinding dataBinding =
                DataBindingUtil.inflate(inflater, R.layout.list_item_city, parent, false);

        return new ViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        City city = this.cities.get(position);
        holder.bind(city);
    }

    @Override
    public int getItemCount() {
        return this.cities == null ? 0 : this.cities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ListItemCityBinding dataBinding;

        ViewHolder(@NonNull ListItemCityBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            itemView.setOnClickListener(this);
        }

        public void bind(City city) {
            this.dataBinding.setCity(city);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            City city = cities.get(position);
            listener.cityClicked(city);
        }
    }

    public interface OnCityClickedListener {
        void cityClicked(City city);
    }
}
