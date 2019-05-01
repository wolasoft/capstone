package com.wolasoft.maplenou.data.entities;

public class Token {
    private String accessToken;
    private long expiresIn;
    private String refreshToken;
    private long refreshExpiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
