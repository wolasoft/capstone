package com.wolasoft.maplenou.views.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.repositories.AccountRepository;
import com.wolasoft.maplenou.databinding.FragmentAccountBinding;
import com.wolasoft.waul.fragments.SimpleFragment;

import javax.inject.Inject;

public class AccountFragment extends SimpleFragment {

    private FragmentAccountBinding dataBinding;
    @Inject
    public AccountRepository repository;

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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        setTitle("");
        MaplenouApplication.app().getAppComponent().inject(this);
        initViews();
        return dataBinding.getRoot();
    }

    private void initViews() {
        Account account = this.repository.getAccount();
        dataBinding.setAccount(account);
        String birthDate = account.getPerson().getBirthdayDate();
        String placeOfBirth = account.getPerson().getPlaceOfBirth();
        if (birthDate != null) {
            String birthInfo = getString(R.string.account_details_birthdate_info, birthDate, placeOfBirth);
            dataBinding.birthInfo.setText(birthInfo);
            dataBinding.birthInfo.setVisibility(View.VISIBLE);
        }
    }
}
