package com.wolasoft.maplenou.ui.announcement.favorite.list;


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.databinding.FragmentAnnouncementFavoriteListBinding;
import com.wolasoft.maplenou.utils.SwipeToDeleteCallback;
import com.wolasoft.waul.utils.ExecutorUtils;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementFavoriteListFragment extends Fragment
        implements FavoriteAnnouncementAdapter.OnAnnouncementClickedListener {

    private FragmentAnnouncementFavoriteListBinding dataBinding;
    @Inject
    public AnnouncementFavoriteViewModelFactory factory;
    private FavoriteAnnouncementAdapter adapter;
    @Inject
    public AnnouncementRepository announcementRepository;
    @Inject
    public ExecutorUtils executorUtils;

    public AnnouncementFavoriteListFragment() { }

    public static AnnouncementFavoriteListFragment newInstance() {
        AnnouncementFavoriteListFragment fragment = new AnnouncementFavoriteListFragment();
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
        initViews();
        setItemTouchHelper();

        return dataBinding.getRoot();
    }

    private void initViews() {
        adapter = new FavoriteAnnouncementAdapter(this);
        AnnouncementFavoriteViewModel favoriteViewModel = ViewModelProviders.of(this, factory)
                .get(AnnouncementFavoriteViewModel.class);

        favoriteViewModel.getFavoriteAnnouncements().observe(this, announcements -> {
            if (announcements == null || announcements.size() == 0) {
                dataBinding.progressBar.setVisibility(View.GONE);
                dataBinding.emptyListHolder.setVisibility(View.VISIBLE);
            } else {
                dataBinding.progressBar.setVisibility(View.GONE);
                adapter.submitList(announcements);
            }
        });

        boolean isTablet = getContext().getResources().getBoolean(R.bool.is_tablet);
        int orientation = getContext().getResources().getConfiguration().orientation;
        RecyclerView.LayoutManager layoutManager;

        if (isTablet) {
            int rows = getContext().getResources().getInteger(R.integer.list_view_rows);
            layoutManager = new GridLayoutManager(getContext(), rows, RecyclerView.VERTICAL, false);
        } else {
            if (orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                layoutManager = new LinearLayoutManager(
                        getContext(), RecyclerView.VERTICAL, false);
            } else {
                int rows = getContext().getResources().getInteger(R.integer.list_view_rows);
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
    public void announcementClicked(Announcement announcement) {
        // TODO open details
    }
}
