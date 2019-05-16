package com.wolasoft.maplenou.views.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.dto.Search;
import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.data.entities.City;
import com.wolasoft.maplenou.databinding.FragmentSearchBinding;
import com.wolasoft.maplenou.utils.Tracker;
import com.wolasoft.maplenou.views.category.CategoryListActivity;
import com.wolasoft.maplenou.views.city.CityListActivity;
import com.wolasoft.waul.utils.NetworkUtils;

import javax.inject.Inject;

public class SearchFragment extends Fragment {

    private static final int CATEGORY_LIST_REQUEST_CODE = 1;
    private static final int CITY_LIST_REQUEST_CODE = 2;
    private FragmentSearchBinding dataBinding;
    private OnSearchInteractionListener mListener;
    private Category category;
    private City city;
    private Search searchParams;
    @Inject
    public Tracker tracker;

    public SearchFragment() { }

    public static SearchFragment newInstance(Search searchParams) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putParcelable(SearchActivity.ARG_SEARCH_PARAMS, searchParams);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchParams = getArguments().getParcelable(SearchActivity.ARG_SEARCH_PARAMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        MaplenouApplication.app().getAppComponent().inject(this);
        tracker.sendFragmentOpenEvent(Tracker.Values.VALUE_SEARCH_FRAGMENT);

        initViews();

        return dataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchInteractionListener) {
            mListener = (OnSearchInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initViews() {
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
            dataBinding.mainContent.setVisibility(View.GONE);
        } else {
            if (searchParams != null) {
                category = searchParams.getCategory();
                city = searchParams.getCity();

                if (searchParams.getTitle() != null) {
                    dataBinding.titleEdit.setText(searchParams.getTitle());
                }

                if (category != null) {
                    dataBinding.categoryTV.setText(searchParams.getCategory().getName());
                }

                if (city != null) {
                    dataBinding.cityTV.setText(searchParams.getCity().getName());
                }
            }

            dataBinding.searchButton.setOnClickListener(v -> {
                if (!dataBinding.titleEdit.getText().toString().equals("")) {
                    if (searchParams == null) {
                        searchParams = new Search();
                    }
                    searchParams.setTitle(dataBinding.titleEdit.getText().toString());
                }

                Bundle bundle = new Bundle();
                bundle.putString(
                        Tracker.Values.VALUE_SEARCH_TITLE,
                        dataBinding.titleEdit.getText().toString());
                bundle.putString(
                        Tracker.Values.VALUE_SEARCH_CATEGORY,
                        dataBinding.categoryTV.getText().toString());
                bundle.putString(
                        Tracker.Values.VALUE_SEARCH_CITY,
                        dataBinding.cityTV.getText().toString());
                tracker.sendEvent(Tracker.Event.EVENT_SEARCH, bundle);
                if (mListener != null) {
                    mListener.onSearchButtonCLicked(searchParams);
                }
            });

            dataBinding.categoryTV.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CategoryListActivity.class);
                startActivityForResult(intent, CATEGORY_LIST_REQUEST_CODE);
            });

            dataBinding.cityTV.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), CityListActivity.class);
                startActivityForResult(intent, CITY_LIST_REQUEST_CODE);
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CATEGORY_LIST_REQUEST_CODE:
                    category = data.getParcelableExtra(CategoryListActivity.CATEGORY_KEY);

                    if (searchParams == null) {
                        searchParams = new Search();
                    }

                    dataBinding.categoryTV.setText(category.getName());
                    searchParams.setCategory(category);
                    break;
                case CITY_LIST_REQUEST_CODE:
                    city = data.getParcelableExtra(CityListActivity.CITY_KEY);

                    if (searchParams == null) {
                        searchParams = new Search();
                    }

                    dataBinding.cityTV.setText(city.getName());
                    searchParams.setCity(city);
                    break;
            }
        }
        return;
    }

    public interface OnSearchInteractionListener {
        void onSearchButtonCLicked(Search searchParams);
    }
}
