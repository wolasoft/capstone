package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.ApiResponse;
import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.data.entities.Token;

import retrofit2.Call;

public interface IAccountRepository {
    void create(
            String email, String phoneNumber, String password, String lastName, String firstName,
            CallBack<Token> callBack);
    void getOne(CallBack<Account> callBack);
    Call<ApiResponse<Announcement>> getAnnouncements(String uuid, final int page);
    void saveToken(Token token);
    void clearUserData();
    Account getAccount();
    void saveAccount(Account account);
}
