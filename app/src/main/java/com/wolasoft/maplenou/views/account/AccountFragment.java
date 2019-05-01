package com.wolasoft.maplenou.views.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.maplenou.R;
import com.wolasoft.waul.fragments.SimpleFragment;

public class AccountFragment extends SimpleFragment {

    public AccountFragment() { }

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
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
        setTitle(R.string.account_account_title);
        return inflater.inflate(R.layout.fragment_account, container, false);
    }
}
