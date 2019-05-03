package com.wolasoft.maplenou.data.repositories;

import com.google.gson.JsonObject;
import com.wolasoft.maplenou.data.api.errors.ErrorUtils;
import com.wolasoft.maplenou.data.api.services.AuthService;
import com.wolasoft.maplenou.data.api.services.CallBack;
import com.wolasoft.maplenou.data.entities.Token;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository implements IAuthRepository {

    private AuthService service;

    @Inject
    public AuthRepository(AuthService service) {
        this.service = service;
    }

    @Override
    public void login(String email, String password, CallBack<Token> callBack) {
        JsonObject object = new JsonObject();
        object.addProperty("email", email);
        object.addProperty("password", password);

        this.service.login(object).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.isSuccessful()) {
                    if (callBack != null) {
                        callBack.onSuccess(response.body());
                    } else {
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
}
