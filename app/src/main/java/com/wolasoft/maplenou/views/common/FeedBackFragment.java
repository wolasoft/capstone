package com.wolasoft.maplenou.views.common;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.FragmentFeedBackBinding;

public abstract class FeedBackFragment extends Fragment {

    protected static final String DESCRIPTION_KEY = "DESCRIPTION_KEY";
    protected FragmentFeedBackBinding dataBinding;
    protected OnFeedBackInteractionListener mListener;
    protected int descriptionRes;

    public FeedBackFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_feed_back, container, false);
        dataBinding.titleTV.setText(getTitle());
        dataBinding.descriptionTV.setText(getDescription());
        dataBinding.icon.setImageResource(getIcon());
        dataBinding.imageHolder.setBackgroundResource(getColor());
        dataBinding.terminateButton.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onButtonClicked();
            }
        });
        return dataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFeedBackInteractionListener) {
            mListener = (OnFeedBackInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFeedBackInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFeedBackInteractionListener {
        void onButtonClicked();
    }

    protected abstract int getIcon();
    protected abstract int getColor();
    protected abstract int getTitle();
    protected abstract int getDescription();
}
