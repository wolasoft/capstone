package com.wolasoft.maplenou.views.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.FragmentMessageBinding;
import com.wolasoft.waul.fragments.SimpleFragment;

public class MessageFragment extends SimpleFragment {

    private FragmentMessageBinding dataBinding;
    public MessageFragment() { }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        replaceToolbar(dataBinding.toolbarHolder.toolbar);
        setTitle(R.string.message_message_title);

        return dataBinding.getRoot();
    }
}
