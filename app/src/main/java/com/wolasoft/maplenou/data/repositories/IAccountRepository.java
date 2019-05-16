package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Token;

public interface IAccountRepository {
    void create(
            String email, String phoneNumber, String password, String lastName, String firstName,
            CallBack<Token> callBack);
    void getOne(CallBack<Account> callBack);
    void saveToken(Token token);
    void clearUserData();
    Account getAccount();
    void saveAccount(Account account);
}
