package com.wolasoft.maplenou.views.favorite.list;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.databinding.FragmentAnnouncementFavoriteListBinding;
import com.wolasoft.maplenou.utils.SwipeToDeleteCallback;
import com.wolasoft.maplenou.views.announcement.list.AnnouncementListFragment;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.ExecutorUtils;

import javax.inject.Inject;

public class FavoriteListFragment extends SimpleFragment
        implements FavoriteAdapter.OnAnnouncementClickedListener {

    private FragmentAnnouncementFavoriteListBinding dataBinding;
    private OnFavoriteListFragmentInteractionListener listener;
    @Inject
    public FavoriteViewModelFactory factory;
    private FavoriteAdapter adapter;
    @Inject
    public AnnouncementRepository announcementRepository;
    @Inject
    public ExecutorUtils executorUtils;

    public FavoriteListFragment() { }

    public static FavoriteListFragment newInstance() {
        FavoriteListFragment fragment = new FavoriteListFragment();
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
                inflater, R.layout.fragment_announcement_favorite_list, container, false);
        MaplenouApplication.app().getAppComponent().inject(this);
        replaceToolbar(dataBinding.toolbarHolder.toolbar);
        setTitle(R.string.announcement_favorite_announcement_list_title);
        initViews();
        setItemTouchHelper();

        return dataBinding.getRoot();
    }

    private void initViews() {
        adapter = new FavoriteAdapter(this);
        FavoriteViewModel favoriteViewModel = ViewModelProviders.of(this, factory)
                .get(FavoriteViewModel.class);

        favoriteViewModel.getFavoriteAnnouncements().observe(this, announcements -> {
            if (announcements == null || announcements.size() == 0) {
                dataBinding.progressBar.setVisibility(View.GONE);
                dataBinding.emptyListHolder.setVisibility(View.VISIBLE);
                dataBinding.recyclerView.setVisibility(View.GONE);
            } else {
                dataBinding.progressBar.setVisibility(View.GONE);
                dataBinding.recyclerView.setVisibility(View.VISIBLE);
                adapter.submitList(announcements);
            }
        });

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

    private void setItemTouchHelper() {
        SwipeToDeleteCallback callback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Announcement announcement = adapter.getCurrentList().get(position);
                executorUtils.diskIO().execute(
                        () -> announcementRepository.deleteFromDb(announcement));
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(dataBinding.recyclerView);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AnnouncementListFragment.OnAnnouncementListFragmentInteractionListener) {
            listener = (FavoriteListFragment.OnFavoriteListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFavoriteListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void announcementClicked(Announcement announcement) {
        if (listener != null) {
            listener.onFavoriteListFragmentInteraction(announcement);
        }
    }

    public interface OnFavoriteListFragmentInteractionListener {
        void onFavoriteListFragmentInteraction(Announcement announcement);
    }
}
