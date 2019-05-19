package com.wolasoft.maplenou.data.repositories;

import com.google.gson.JsonObject;
import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.api.errors.ErrorUtils;
import com.wolasoft.maplenou.data.api.services.AccountService;
import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Announcement;
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
        JsonObject body = new JsonObject();
        body.addProperty("email", email);
        body.addProperty("phoneNumber", phoneNumber);
        body.addProperty("password", password);
        body.addProperty("lastName", lastName);
        body.addProperty("firstName", firstName);

        this.accountService.create(body).enqueue(new Callback<Token>() {
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
    public Call<ApiResponse<Announcement>> getAnnouncements(String uuid, int page) {
        return this.accountService.getAnnouncements(uuid, page);
    }

    @Override
    public void saveToken(Token token) {
        this.preferences.saveToken(token);
    }

    @Override
    public void clearUserData() {
        this.preferences.clearToken();
        this.preferences.clearAccount();
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
