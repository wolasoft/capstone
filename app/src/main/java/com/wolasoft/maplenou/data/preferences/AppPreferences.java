package com.wolasoft.maplenou.data.preferences;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.wolasoft.maplenou.data.entities.Account;
import com.wolasoft.maplenou.data.entities.Token;
import com.wolasoft.waul.utils.BasePreferences;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppPreferences extends BasePreferences {
    private static final String PREFS_NAME = "com.wolasoft.maplenou.preferences";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";

    @Inject
    public AppPreferences(Context context) {
        super(context, PREFS_NAME);
    }


    public void saveToken(Token token) {
        this.saveObject(KEY_TOKEN, token, new TypeToken<Token>(){}.getType());
    }

    public Token getToken() {
        return this.getObject(KEY_TOKEN, new TypeToken<Token>(){}.getType(), null);
    }

    public void saveAccount(Account account) {
        this.saveObject(KEY_ACCOUNT, account, new TypeToken<Account>(){}.getType());
    }

    public Account getAccount() {
        return this.getObject(KEY_ACCOUNT, new TypeToken<Account>(){}.getType(), null);
    }

    public boolean isAccountConnected() {
        Token token = this.getToken();

        if (token == null) {
            return false;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(token.getExpiresIn());

        return !calendar.after(Calendar.getInstance());
    }
}
