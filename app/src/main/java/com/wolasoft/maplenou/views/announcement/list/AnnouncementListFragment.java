package com.wolasoft.maplenou.views.announcement.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.dto.Search;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.databinding.FragmentListAnnouncementBinding;
import com.wolasoft.maplenou.views.search.SearchActivity;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.NetworkUtils;

import javax.inject.Inject;

public class AnnouncementListFragment extends SimpleFragment implements
        AnnouncementAdapter.OnAnnouncementClickedListener {

    private static final int SEARCH_REQUEST_CODE = 1;
    private FragmentListAnnouncementBinding dataBinding;
    private OnAnnouncementListFragmentInteractionListener mListener;
    @Inject
    public AnnouncementViewModelFactory factory;
    private AnnouncementAdapter adapter;
    private AnnouncementViewModel announcementViewModel;
    private Search searchParams;

    public AnnouncementListFragment() { }

    public static AnnouncementListFragment newInstance() {
        AnnouncementListFragment fragment = new AnnouncementListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_announcement,
                container, false);
        MaplenouApplication.app().getAppComponent().inject(this);
        setTitle(R.string.announcement_list_title);
        initViews();

        return dataBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.announcement_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra(SearchActivity.ARG_SEARCH_PARAMS, searchParams);
                startActivityForResult(intent, SEARCH_REQUEST_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
        } else {
            dataBinding.networkErrorHolder.setVisibility(View.GONE);
            adapter = new AnnouncementAdapter(this);

            announcementViewModel = ViewModelProviders.of(this, factory).get(AnnouncementViewModel.class);
            startObserving();
            boolean isTablet = getBoolean(R.bool.is_tablet);
            int orientation = getOrientation();
            RecyclerView.LayoutManager layoutManager;

            if (isTablet) {
                int rows = getInteger(R.integer.list_view_rows);
                layoutManager = new GridLayoutManager(getContext(), rows, RecyclerView.VERTICAL, false);
            } else {
                if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                    layoutManager = new LinearLayoutManager(
                            getContext(), RecyclerView.VERTICAL, false);
                } else {
                    int rows = getInteger(R.integer.list_view_rows);
                    layoutManager = new GridLayoutManager(getContext(), rows, RecyclerView.VERTICAL, false);
                }
            }

            dataBinding.recyclerView.setLayoutManager(layoutManager);
            dataBinding.recyclerView.setAdapter(adapter);
            dataBinding.recyclerView.setHasFixedSize(true);
        }
    }

    private void startObserving() {
        announcementViewModel.getItemPagedList()
                .observe(this, announcements -> adapter.submitList(announcements));

        announcementViewModel.getProgressLiveStatus().observe(this, loadingState -> {
            switch (loadingState) {
                case LOADING:
                    dataBinding.progressBar.setVisibility(View.VISIBLE);
                    return;
                case LOADED:
                    dataBinding.progressBar.setVisibility(View.GONE);
                    return;
                case ERROR:
                    dataBinding.progressBar.setVisibility(View.GONE);
                    dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
                    return;
                case LOADING_MORE:
                    return;
                case LOADED_MORE:
                    return;
                case ERROR_LOADING_MORE:
                    return;
            }
        });
    }
    private void search() {
        if (searchParams == null) {
            return;
        }

        announcementViewModel.search(this, searchParams);
        startObserving();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAnnouncementListFragmentInteractionListener) {
            mListener = (OnAnnouncementListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAnnouncementListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void announcementClicked(Announcement announcement) {
        if (mListener != null) {
            mListener.onAnnouncementClicked(announcement);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SEARCH_REQUEST_CODE) {
                searchParams = data.getParcelableExtra(SearchActivity.ARG_SEARCH_PARAMS);
                search();
            }
        }
        return;
    }

    public interface OnAnnouncementListFragmentInteractionListener {
        void onAnnouncementClicked(Announcement announcement);
    }
}
