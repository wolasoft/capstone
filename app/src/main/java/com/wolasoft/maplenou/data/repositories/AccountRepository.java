package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.errors.ErrorUtils;
import com.wolasoft.maplenou.data.api.services.AccountService;
import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Token;
import com.wolasoft.maplenou.data.preferences.AppPreferences;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountRepository implements IAccountRepository {

    private AccountService accountService;
    private AppPreferences preferences;

    @Inject
    public AccountRepository(AccountService service, AppPreferences preferences) {
        this.accountService = service;
        this.preferences = preferences;
    }

    @Override
    public void create(String email, String phoneNumber, String password, String lastName,
                       String firstName, CallBack<Token> callBack) {
        this.accountService.create(email, password, phoneNumber, lastName, firstName)
                .enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        callBack.onSuccess(response.body());
                    }
                } else {
                    if (callBack != null) {
                        callBack.onError(ErrorUtils.parse(response));
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                if (callBack != null) {
                    callBack.onError(null);
                }
            }
        });
    }

    @Override
    public void getOne(CallBack<Account> callBack) {
        this.accountService.getOne().enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        callBack.onSuccess(response.body());
                    } else {
                        callBack.onError(ErrorUtils.parse(response));
                    }
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                if (callBack != null) {
                    callBack.onError(null);
                }
            }
        });
    }

    @Override
    public void saveToken(Token token) {
        this.preferences.saveToken(token);
    }

    @Override
    public Account getAccount() {
        return this.preferences.getAccount();
    }

    @Override
    public void saveAccount(Account account) {
        this.preferences.saveAccount(account);
    }
}
