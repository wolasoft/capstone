package com.wolasoft.maplenou.data.api.errors;

import com.wolasoft.maplenou.data.api.ApiConnector;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

public class ErrorUtils {
    public static APIError parse(Response<?> response) {
        Converter<ResponseBody, APIError> converter = ApiConnector.retrofit()
                .responseBodyConverter(APIError.class, new Annotation[0]);

        APIError error;

        try {
            error = converter.convert((response.errorBody()));
        } catch (IOException e) {
            return new APIError();
        }

        return error;
    }
}
