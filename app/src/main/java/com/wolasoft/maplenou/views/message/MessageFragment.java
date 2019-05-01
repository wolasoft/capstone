package com.wolasoft.maplenou.views.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.maplenou.R;
import com.wolasoft.waul.fragments.SimpleFragment;

public class MessageFragment extends SimpleFragment {

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
        setTitle(R.string.message_message_title);
        return inflater.inflate(R.layout.fragment_message, container, false);
    }
}
