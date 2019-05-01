package com.wolasoft.maplenou.data.api.errors;

public class APIError {
    private int statusCode;
    private String errorCode;
    private String message;
    private Object details;

    public int getStatusCode() {
        return statusCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Object getDetails() {
        return details;
    }
}
