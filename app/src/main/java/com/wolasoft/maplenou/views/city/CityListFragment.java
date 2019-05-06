package com.wolasoft.maplenou.views.city;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.City;
import com.wolasoft.maplenou.databinding.FragmentCityListBinding;
import com.wolasoft.waul.utils.NetworkUtils;

import javax.inject.Inject;

public class CityListFragment extends Fragment implements CityAdapter.OnCityClickedListener {

    private OnFragmentCityListInteractionListener mListener;
    private FragmentCityListBinding dataBinding;
    @Inject
    public CityViewModelFactory factory;

    public CityListFragment() { }

    public static CityListFragment newInstance(String param1, String param2) {
        CityListFragment fragment = new CityListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_city_list, container, false);

        MaplenouApplication.app().getAppComponent().inject(this);

        initViews();

        return dataBinding.getRoot();
    }

    private void initViews() {
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
            dataBinding.progressBar.setVisibility(View.GONE);

            return;
        } else {
            CityViewModel viewModel =
                    ViewModelProviders.of(this, factory).get(CityViewModel.class);
            viewModel.getCities().observe(this, cities -> {
                if (cities != null && cities.size() > 0) {
                    CityAdapter adapter = new CityAdapter(cities, this);
                    dataBinding.recyclerView.setAdapter(adapter);

                    LinearLayoutManager layoutManager = new LinearLayoutManager(
                            getContext(), RecyclerView.VERTICAL, false);
                    dataBinding.recyclerView.setLayoutManager(layoutManager);

                    dataBinding.recyclerView.setHasFixedSize(true);

                    dataBinding.recyclerView.setVisibility(View.VISIBLE);
                    dataBinding.networkErrorHolder.setVisibility(View.GONE);
                    dataBinding.progressBar.setVisibility(View.GONE);
                } else {
                    dataBinding.emptyListHolder.setVisibility(View.VISIBLE);
                    dataBinding.progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCityListInteractionListener) {
            mListener = (OnFragmentCityListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCityListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void cityClicked(City city) {
        if (mListener != null) {
            mListener.onCitySelected(city);
        }
    }

    public interface OnFragmentCityListInteractionListener {
        void onCitySelected(City city);
    }
}
