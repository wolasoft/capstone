package com.wolasoft.maplenou.views.about;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.databinding.FragmentAboutBinding;

public class AboutFragment extends Fragment {


    public AboutFragment() { }

    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
        FragmentAboutBinding dataBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false);

        String versionName = "";

        try {
            PackageInfo packageInfo = getContext().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        dataBinding.appInfo.setText(getString(R.string.app_version) + versionName);

        return dataBinding.getRoot();
    }

}
