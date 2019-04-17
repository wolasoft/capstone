package com.wolasoft.maplenou.utils;

import android.widget.TextView;

import com.wolasoft.maplenou.R;

import java.text.DecimalFormat;

import androidx.databinding.BindingAdapter;

public class PriceBindingAdapter {

    @BindingAdapter({"app:amount"})
    public static void format(TextView view, int amount) {
        final String currency = view.getContext().getString(R.string.currency_cfa);
        DecimalFormat formatter = new  DecimalFormat("###,###.###");
        String formattedAmount = formatter.format(amount) + " " +currency;
        view.setText(formattedAmount);
    }
}
