package com.wolasoft.maplenou.data.api;

import java.util.List;

public class ApiResponse<T> {
    public List<T> data;
    public boolean hasMore;
    public int maxData;
    public int dataRemaining;
}
