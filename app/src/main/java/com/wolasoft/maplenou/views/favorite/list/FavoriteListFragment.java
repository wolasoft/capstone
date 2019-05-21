package com.wolasoft.maplenou.views.favorite.list;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Photo;
import com.wolasoft.maplenou.data.repositories.AnnouncementRepository;
import com.wolasoft.maplenou.databinding.FragmentAnnouncementFavoriteListBinding;
import com.wolasoft.maplenou.services.UpdateWidgetService;
import com.wolasoft.maplenou.utils.Constants;
import com.wolasoft.maplenou.utils.SwipeToDeleteCallback;
import com.wolasoft.maplenou.views.about.AboutActivity;
import com.wolasoft.maplenou.views.favorite.details.FavoriteDetailsActivity;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.ExecutorUtils;
import com.wolasoft.waul.utils.ImageUtils;

import javax.inject.Inject;

public class FavoriteListFragment extends SimpleFragment
        implements FavoriteAdapter.OnAnnouncementClickedListener {

    private FragmentAnnouncementFavoriteListBinding dataBinding;
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
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_announcement_favorite_list, container, false);
        MaplenouApplication.app().getAppComponent().inject(this);
        setTitle(R.string.announcement_favorite_announcement_list_title);
        initViews();
        setItemTouchHelper();

        return dataBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.favorite_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
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
                executorUtils.diskIO().execute(() -> {

                    for (Photo photo: announcement.getPhotos()) {
                        String localImageName = photo.getUuid() + Constants.LOCAL_IMAGE_EXT;
                        ImageUtils.deleteFromDisk(
                                getActivity().getApplicationContext(),
                                Constants.LOCAL_IMAGE_DIR,localImageName);
                    }

                    announcementRepository.delete(announcement);
                    updateAppWidget();
                });
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(dataBinding.recyclerView);
    }

    @Override
    public void announcementClicked(Announcement announcement, View imageView) {
        Intent intent = new Intent(getContext(), FavoriteDetailsActivity.class);
        intent.putExtra(FavoriteDetailsActivity.ARG_ANNOUNCEMENT_UUID, announcement.getUuid());
        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        getActivity(), imageView, "thumbnailTransition");
        startActivity(intent, optionsCompat.toBundle());
    }

    private void updateAppWidget() {
        Intent appWidgetService = new Intent(getContext(), UpdateWidgetService.class);
        appWidgetService.setAction(UpdateWidgetService.SHOW_LAST_FAVORITE);
        getActivity().startService(appWidgetService);
    }

    public interface OnFavoriteListFragmentInteractionListener { }
}
