package com.wolasoft.maplenou.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ExecutorUtils {
    private final Executor diskIO;

    public ExecutorUtils() {
        this.diskIO = Executors.newSingleThreadExecutor();
    }

    public Executor diskIO() {
        return diskIO;
    }
}
