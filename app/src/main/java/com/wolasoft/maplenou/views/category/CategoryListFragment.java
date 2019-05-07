package com.wolasoft.maplenou.views.category;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Category;
import com.wolasoft.maplenou.databinding.FragmentCategoryListBinding;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.NetworkUtils;

import javax.inject.Inject;

public class CategoryListFragment extends SimpleFragment
        implements CategoryAdapter.OnCategoryClickedListener {

    private OnFragmentCategoryListInteractionListener mListener;
    private FragmentCategoryListBinding dataBinding;
    @Inject
    public CategoryViewModelFactory factory;

    public CategoryListFragment() { }

    public static CategoryListFragment newInstance() {
        CategoryListFragment fragment = new CategoryListFragment();
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
                inflater, R.layout.fragment_category_list, container, false);

        MaplenouApplication.app().getAppComponent().inject(this);

        setTitle(R.string.category_list_title);

        initViews();

        return dataBinding.getRoot();
    }

    private void initViews() {
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
            dataBinding.progressBar.setVisibility(View.GONE);

            return;
        } else {
            CategoryViewModel viewModel =
                    ViewModelProviders.of(this, factory).get(CategoryViewModel.class);
            viewModel.getCategories().observe(this, categories -> {
                if (categories != null && categories.size() > 0) {
                    CategoryAdapter adapter = new CategoryAdapter(categories, this);
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
        if (context instanceof OnFragmentCategoryListInteractionListener) {
            mListener = (OnFragmentCategoryListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCategoryListInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void categoryClicked(Category category) {
        if (mListener != null) {
            mListener.onCategorySelected(category);
        }
    }

    public interface OnFragmentCategoryListInteractionListener {
        void onCategorySelected(Category category);
    }
}
