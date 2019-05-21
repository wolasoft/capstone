package com.wolasoft.maplenou.views.account.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.repositories.AccountRepository;
import com.wolasoft.maplenou.databinding.FragmentAccountBinding;
import com.wolasoft.maplenou.views.about.AboutActivity;
import com.wolasoft.maplenou.views.announcement.details.AnnouncementDetailsActivity;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.NetworkUtils;

import javax.inject.Inject;

public class AccountFragment extends SimpleFragment
        implements AccountAnnouncementAdapter.OnAnnouncementClickedListener {

    private FragmentAccountBinding dataBinding;
    @Inject
    public AccountRepository repository;
    @Inject
    public AccountAnnouncementViewModelFactory factory;
    private AccountAnnouncementAdapter adapter;
    private AccountAnnouncementViewModel announcementViewModel;
    private OnFragmentAccountInteractionListener mListener;
    private Account account;

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
        setHasOptionsMenu(true);
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
            case R.id.action_about:
                Intent intent = new Intent(getContext(), AboutActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentAccountInteractionListener) {
            mListener = (OnFragmentAccountInteractionListener) context;
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

    @Override
    public void announcementClicked(Announcement announcement) {
        Intent intent = new Intent(getContext(), AnnouncementDetailsActivity.class);
        intent.putExtra(AnnouncementDetailsActivity.ARG_ANNOUNCEMENT_UUID, announcement.getUuid());
        startActivity(intent);
    }

    private void initViews() {
        account = this.repository.getAccount();
        dataBinding.setAccount(account);
        String birthDate = account.getPerson().getBirthdayDate();
        String placeOfBirth = account.getPerson().getPlaceOfBirth();
        if (birthDate != null) {
            String birthInfo = getString(R.string.account_details_birthdate_info, birthDate, placeOfBirth);
            dataBinding.birthInfo.setText(birthInfo);
            dataBinding.birthInfo.setVisibility(View.VISIBLE);
        }

        dataBinding.imageHolder.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onUserDetailsClicked(dataBinding.imageHolder, "layoutTransition");
            }
        });

        initAccountAnnouncement();
    }

    private void initAccountAnnouncement() {
        if (!NetworkUtils.isInternetAvailable(getContext())) {
            dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
        } else {
            dataBinding.networkErrorHolder.setVisibility(View.GONE);
            adapter = new AccountAnnouncementAdapter(this);
            announcementViewModel = ViewModelProviders
                            .of(this, factory).get(AccountAnnouncementViewModel.class);

            announcementViewModel.getItemPagedList(account.getUuid())
                    .observe(this, announcements -> adapter.submitList(announcements));

            announcementViewModel.getProgressLiveStatus().observe(this, loadingState -> {
                switch (loadingState) {
                    case LOADING:
                        dataBinding.progressBar.setVisibility(View.VISIBLE);
                        return;
                    case LOADED:
                        dataBinding.progressBar.setVisibility(View.GONE);
                        return;
                    case ERROR:
                        dataBinding.progressBar.setVisibility(View.GONE);
                        dataBinding.networkErrorHolder.setVisibility(View.VISIBLE);
                        return;
                    case LOADING_MORE:
                        return;
                    case LOADED_MORE:
                        return;
                    case ERROR_LOADING_MORE:
                        return;
                }
            });

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                    getContext(), RecyclerView.VERTICAL, false);
            dataBinding.recyclerView.setLayoutManager(layoutManager);
            dataBinding.recyclerView.setAdapter(adapter);
            dataBinding.recyclerView.setHasFixedSize(true);
        }
    }

    public interface OnFragmentAccountInteractionListener {
        void onDisconnect();
        void onUserDetailsClicked(View transitionView, String transitionName);
    }
}
