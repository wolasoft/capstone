package com.wolasoft.maplenou.views.announcement.create;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.maplenou.R;
import com.wolasoft.waul.fragments.SimpleFragment;

public class CreateAnnouncementFragment extends SimpleFragment {

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
        setTitle(R.string.announcement_announcement_creation_title);
        return inflater.inflate(R.layout.fragment_create_announcement, container, false);
    }

}
