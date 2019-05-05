package com.wolasoft.maplenou.views.announcement.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.FragmentCreateAnnouncementBinding;
import com.wolasoft.waul.fragments.SimpleFragment;

public class CreateAnnouncementFragment extends SimpleFragment {

    private FragmentCreateAnnouncementBinding dataBinding;

    public CreateAnnouncementFragment() { }

    public static CreateAnnouncementFragment newInstance() {
        CreateAnnouncementFragment fragment = new CreateAnnouncementFragment();
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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_announcement, container, false);
        replaceToolbar(dataBinding.toolbarHolder.toolbar);
        setTitle(R.string.announcement_announcement_creation_title);

        return dataBinding.getRoot();
    }

}
