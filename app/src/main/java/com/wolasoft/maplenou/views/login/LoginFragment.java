package com.wolasoft.maplenou.views.login;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.FragmentLoginBinding;
import com.wolasoft.waul.fragments.SimpleFragment;

import androidx.databinding.DataBindingUtil;

public class LoginFragment extends SimpleFragment {
    private OnLoginFragmentInteractionListener mListener;
    private FragmentLoginBinding dataBinding;

    public LoginFragment() { }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,
                container, false);
        setTitle(R.string.login_login_title);
        initViews();
        return dataBinding.getRoot();
    }

    private void initViews() {
        dataBinding.createAccountTV.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onSubscribeClicked();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnLoginFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoginSucceeded();
        void onSubscribeClicked();
    }
}
