package com.wolasoft.maplenou.views.account.details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.repositories.AccountRepository;
import com.wolasoft.maplenou.databinding.FragmentAccountDetailsBinding;
import com.wolasoft.maplenou.views.account.main.AccountFragment;
import com.wolasoft.waul.fragments.SimpleFragment;

import javax.inject.Inject;

public class AccountDetailsFragment extends SimpleFragment {

    private FragmentAccountDetailsBinding dataBinding;
    @Inject
    public AccountRepository repository;
    private AccountFragment.OnFragmentAccountInteractionListener mListener;

    public AccountDetailsFragment() { }

    public static AccountDetailsFragment newInstance() {
        AccountDetailsFragment fragment = new AccountDetailsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account_details, container, false);
        setTitle("");
        MaplenouApplication.app().getAppComponent().inject(this);
        initViews();
        return dataBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.account, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Toast.makeText(getContext(), R.string.message_disconnection_success, Toast.LENGTH_SHORT).show();
                repository.clearUserData();
                if (mListener != null) {
                    mListener.onDisconnect();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountFragment.OnFragmentAccountInteractionListener) {
            mListener = (AccountFragment.OnFragmentAccountInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentAccountInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
