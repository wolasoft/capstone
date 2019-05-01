package com.wolasoft.maplenou.data.api.services;

import com.wolasoft.maplenou.data.api.errors.APIError;

public interface CallBack<T> {
    void onSuccess(T data);
    void onError(APIError error);
}
