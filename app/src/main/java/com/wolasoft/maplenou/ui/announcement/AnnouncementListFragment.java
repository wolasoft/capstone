package com.wolasoft.maplenou.ui.announcement;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.databinding.FragmentListAnnouncementBinding;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AnnouncementListFragment extends Fragment implements
        AnnouncementAdapter.OnAnnouncementClickedListener {

    private FragmentListAnnouncementBinding dataBinding;
    private OnAnnouncementListFragmentInteractionListener mListener;
    @Inject
    public AnnouncementViewModelFactory factory;
    private AnnouncementAdapter adapter;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_list_announcement,
                container, false);
        MaplenouApplication.app().getAppComponent().inject(this);

        adapter = new AnnouncementAdapter(this);
        initViews();

        final AnnouncementViewModel announcementViewModel =
                ViewModelProviders.of(this, factory).get(AnnouncementViewModel.class);

        announcementViewModel.getItemPagedList()
                .observe(this, new Observer<PagedList<Announcement>>() {
            @Override
            public void onChanged(@Nullable PagedList<Announcement> announcements) {
                adapter.submitList(announcements);
            }
        });

        return dataBinding.getRoot();
    }

    private void initViews() {
        dataBinding.emptyListHolder.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), RecyclerView.VERTICAL, false);
        dataBinding.recyclerView.setLayoutManager(layoutManager);
        dataBinding.recyclerView.setAdapter(adapter);
        dataBinding.recyclerView.setHasFixedSize(true);
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
            mListener.onAnnouncementListFragmentInteraction(announcement);
        }
    }

    public interface OnAnnouncementListFragmentInteractionListener {
        void onAnnouncementListFragmentInteraction(Announcement announcement);
    }
}
