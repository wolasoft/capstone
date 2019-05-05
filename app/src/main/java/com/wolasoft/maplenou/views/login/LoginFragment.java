package com.wolasoft.maplenou.views.login;

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
import com.wolasoft.maplenou.data.repositories.AuthRepository;
import com.wolasoft.maplenou.databinding.FragmentLoginBinding;
import com.wolasoft.waul.fragments.SimpleFragment;
import com.wolasoft.waul.validators.EmailValidator;

import javax.inject.Inject;

public class LoginFragment extends SimpleFragment {
    private OnLoginFragmentInteractionListener mListener;
    private FragmentLoginBinding dataBinding;
    @Inject
    public AuthRepository authRepository;
    @Inject
    public AccountRepository accountRepository;

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
        MaplenouApplication.app().getAppComponent().inject(this);
        replaceToolbar(dataBinding.toolbarHolder.toolbar);
        setTitle(R.string.login_login_title);
        initViews();
        return dataBinding.getRoot();
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

    private void addWatchers() {
        TextWatcher emailWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataBinding.emailEdit.setError(!EmailValidator.isValid(s.toString()));
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        dataBinding.emailEdit.addTextChangedListener(emailWatcher);
        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        dataBinding.passwordEdit.addTextChangedListener(passwordWatcher);
    }

    private void initViews() {
        addWatchers();
        dataBinding.loginButton.setOnClickListener(v -> {
            dataBinding.progressBar.setVisibility(View.VISIBLE);
            dataBinding.contentHolder.setVisibility(View.GONE);
            String email = dataBinding.emailEdit.getText().toString();
            String password = dataBinding.passwordEdit.getText().toString();
            CallBack<Account> accountCallBack = new CallBack<Account>() {
                @Override
                public void onSuccess(Account data) {
                    accountRepository.saveAccount(data);
                    dataBinding.progressBar.setVisibility(View.GONE);

                    if (mListener != null) {
                        mListener.onLoginSucceeded();
                    }
                }

                @Override
                public void onError(APIError error) {
                    handleError(error);
                }
            };
            CallBack<Token> tokenCallBack = new CallBack<Token>() {
                @Override
                public void onSuccess(Token data) {
                    accountRepository.saveToken(data);
                    accountRepository.getOne(accountCallBack);
                }

                @Override
                public void onError(APIError error) {
                    handleError(error);
                }
            };
            authRepository.login(email, password, tokenCallBack);
        });
        dataBinding.createAccountTV.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onSubscribeClicked();
            }
        });
    }

    private void enableButton() {
        dataBinding.loginButton.setEnabled(
                EmailValidator.isValid(dataBinding.emailEdit.getText().toString())
                && (dataBinding.passwordEdit.getText().toString().length() > 0));
    }

    private void handleError(APIError error) {
        dataBinding.progressBar.setVisibility(View.GONE);
        dataBinding.contentHolder.setVisibility(View.VISIBLE);
        dataBinding.errorTV.setVisibility(View.VISIBLE);
        if (error == null) {
            dataBinding.errorTV.setText(getString(R.string.common_network_error));
        } else {
            // TODO enhance handling
            dataBinding.errorTV.setText(error.getMessage());
        }
    }

    public interface OnLoginFragmentInteractionListener {
        void onLoginSucceeded();
        void onSubscribeClicked();
    }
}
