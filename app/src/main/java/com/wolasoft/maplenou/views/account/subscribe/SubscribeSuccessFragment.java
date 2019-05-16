package com.wolasoft.maplenou.views.account.subscribe;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.FragmentSubscribeSuccessBinding;
import com.wolasoft.maplenou.utils.Tracker;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import javax.inject.Inject;


public class SubscribeSuccessFragment extends Fragment {

    private OnSuccessFragmentInteractionListener mListener;
    @Inject
    public Tracker tracker;
    private static final String EVENT_SUBSCRIPTION_SUCCESS = "subscription_succeeded";

    public SubscribeSuccessFragment() { }


    public static SubscribeSuccessFragment newInstance() {
        SubscribeSuccessFragment fragment = new SubscribeSuccessFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentSubscribeSuccessBinding dataBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_subscribe_success, container, false);
        MaplenouApplication.app().getAppComponent().inject(this);
        tracker.sendEvent(EVENT_SUBSCRIPTION_SUCCESS, null);
        dataBinding.terminateButton.setOnClickListener(v -> close());

        return dataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSuccessFragmentInteractionListener) {
            mListener = (OnSuccessFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSuccessFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void close() {
        if (mListener != null) {
            mListener.onTerminateButtonClicked();
        }
    }

    public interface OnSuccessFragmentInteractionListener {
        void onTerminateButtonClicked();
    }
}
