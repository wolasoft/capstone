package com.wolasoft.maplenou.views.account.subscribe;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.wolasoft.maplenou.MaplenouApplication;
import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.api.errors.APIError;
import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Token;
import com.wolasoft.maplenou.data.repositories.AccountRepository;
import com.wolasoft.maplenou.databinding.FragmentSubscribeBinding;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.utils.PhoneNumberFormattingTextWatcher;
import com.wolasoft.waul.utils.PhoneNumberUtils;
import com.wolasoft.waul.validators.EmailValidator;
import com.wolasoft.waul.validators.PasswordValidator;

import javax.inject.Inject;


public class SubscribeFragment extends SimpleFragment {

    private OnFragmentSubscribeInteractionListener mListener;
    private FragmentSubscribeBinding dataBinding;
    @Inject
    public AccountRepository repository;

    public SubscribeFragment() { }

    public static SubscribeFragment newInstance() {
        SubscribeFragment fragment = new SubscribeFragment();
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
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_subscribe,
                container, false);
        setTitle(R.string.account_account_creation_title);
        MaplenouApplication.app().getAppComponent().inject(this);

        initViews();

        return dataBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentSubscribeInteractionListener) {
            mListener = (OnFragmentSubscribeInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentSubscribeInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void initViews() {
        dataBinding.subscribeButton.setOnClickListener(v -> subscribe());
        attachWatchers();
    }

    private void subscribe() {
        CallBack<Token> subscribeCallBack = new CallBack<Token>() {
            @Override
            public void onSuccess(Token data) {
                repository.saveToken(data);
                getCreatedAccount();
            }

            @Override
            public void onError(APIError error) {
                // TODO handle error
            }
        };

        dataBinding.contentHolder.setVisibility(View.GONE);
        dataBinding.progressBar.setVisibility(View.VISIBLE);
        this.repository.create(
                dataBinding.emailEdit.getText().toString(),
                dataBinding.passwordEdit.getText().toString(),
                dataBinding.passwordEdit.getText().toString(),
                dataBinding.lastNameEdit.getText().toString(),
                dataBinding.firstNameEdit.getText().toString(),
                subscribeCallBack
        );
    }

    private void getCreatedAccount() {
        this.repository.getOne(new CallBack<Account>() {
            @Override
            public void onSuccess(Account data) {
                repository.saveAccount(data);
                dataBinding.progressBar.setVisibility(View.GONE);
                if (mListener != null) {
                    mListener.onAccountCreated();
                }
            }

            @Override
            public void onError(APIError error) {
                // TODO handle error
            }
        });
    }

    private void attachWatchers() {
        TextWatcher emailWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                enableButton();
            }
        };
        dataBinding.emailEdit.addTextChangedListener(emailWatcher);
        dataBinding.emailEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && dataBinding.emailEdit.getText().toString().length() > 0) {
                dataBinding.emailEdit.setError(!EmailValidator.isValid(
                        dataBinding.emailEdit.getText().toString()));
            }
        });
        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                enableButton();
            }
        };
        dataBinding.passwordEdit.addTextChangedListener(passwordWatcher);
        dataBinding.passwordEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && dataBinding.passwordEdit.getText().toString().length() > 0) {
                dataBinding.passwordEdit.setError(!PasswordValidator.isValid(
                        dataBinding.passwordEdit.getText().toString()));
            }
        });
        TextWatcher phoneNumberWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                enableButton();
            }
        };
        dataBinding.phoneNumberEdit.addTextChangedListener(phoneNumberWatcher);
        dataBinding.phoneNumberEdit.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && dataBinding.phoneNumberEdit.getText().toString().length() > 0) {
                dataBinding.phoneNumberEdit.setError(!PhoneNumberUtils.isValidNumber(
                        dataBinding.phoneNumberEdit.getText().toString()));
            }
        });
        TextWatcher idWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                enableButton();
            }
        };
        dataBinding.phoneNumberEdit.addTextChangedListener(
                new PhoneNumberFormattingTextWatcher(PhoneNumberUtils.DIALLING_CODE_TOGO));
        dataBinding.firstNameEdit.addTextChangedListener(idWatcher);
        dataBinding.lastNameEdit.addTextChangedListener(idWatcher);
    }

    private void enableButton() {
        dataBinding.subscribeButton.setEnabled(
                EmailValidator.isValid(dataBinding.emailEdit.getText().toString())
                && PasswordValidator.isValid(dataBinding.passwordEdit.getText().toString())
                && PhoneNumberUtils.isValidNumber(dataBinding.phoneNumberEdit.getText().toString())
                && isValidInput(dataBinding.firstNameEdit.getText().toString())
                && isValidInput(dataBinding.lastNameEdit.getText().toString())
        );
    }

    private boolean isValidInput(String text) {
        if (text != null) {
            return text.length() >= 3;
        }

        return false;
    }

    public interface OnFragmentSubscribeInteractionListener {
        void onAccountCreated();
    }
}
