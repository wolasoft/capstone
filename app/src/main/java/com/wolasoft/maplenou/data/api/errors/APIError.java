package com.wolasoft.maplenou.data.api.errors;

import android.os.Parcel;
import android.os.Parcelable;

public class APIError implements Parcelable {
    public static final int HTTP_ERROR_500 = 500;
    public static final int HTTP_ERROR_404 = 404;
    public static final int HTTP_ERROR_400 = 40;
    public static final int HTTP_ERROR_409 = 409;
    private int statusCode;
    private String errorCode;
    private String message;
    private Object details;

    protected APIError(Parcel in) {
        statusCode = in.readInt();
        errorCode = in.readString();
        message = in.readString();
    }

    public static final Creator<APIError> CREATOR = new Creator<APIError>() {
        @Override
        public APIError createFromParcel(Parcel in) {
            return new APIError(in);
        }

        @Override
        public APIError[] newArray(int size) {
            return new APIError[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(statusCode);
        dest.writeString(errorCode);
        dest.writeString(message);
    }
}
