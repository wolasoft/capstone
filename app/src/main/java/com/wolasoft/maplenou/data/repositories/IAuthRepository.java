package com.wolasoft.maplenou.data.repositories;

import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.entities.Token;

public interface IAuthRepository {
    void login(String email, String password, CallBack<Token> callBack);
}
