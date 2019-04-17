package com.wolasoft.maplenou.utils;

import android.widget.TextView;

import java.util.Date;

import androidx.databinding.BindingAdapter;

public class DateBindingAdapter {

    @BindingAdapter({"app:date"})
    public static void format(TextView view, Date date) {
        view.setText(DateUtilities.format(date, view.getContext()));
    }
}