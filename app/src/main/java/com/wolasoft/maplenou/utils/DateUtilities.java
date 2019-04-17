package com.wolasoft.maplenou.utils;

import android.content.Context;
import android.text.format.DateUtils;

import com.wolasoft.maplenou.R;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Calendar;
import java.util.Date;

public class DateUtilities {
    public static String format(Date incomingDate, Context context) {
        final String FORMAT_SIMPLE_DAY = context.getString(R.string.date_format_simple_day);
        final String FORMAT_TODAY = context.getString(R.string.date_format_today);
        final String FORMAT_YEAR_DAY = context.getString(R.string.date_format_year_day);
        final String FORMAT_YESTERDAY = context.getString(R.string.date_format_yesterday);
        Calendar today = Calendar.getInstance();
        Calendar incoming = Calendar.getInstance();
        incoming.setTime(incomingDate);

        int currentWeek = today.get(Calendar.WEEK_OF_YEAR);
        int currentYear = today.get(Calendar.YEAR);
        int incomingWeek = incoming.get(Calendar.WEEK_OF_YEAR);
        int incomingYear = incoming.get(Calendar.YEAR);
        boolean isYesterday = (currentYear == incomingYear)
                && (today.get(Calendar.DAY_OF_YEAR) - incoming.get(Calendar.DAY_OF_YEAR) == 1);
        boolean isYearDay = incomingYear == currentYear;

        String formattedDate;

        try {
            if (DateUtils.isToday(incomingDate.getTime())) {
                formattedDate = DateFormatUtils.format(incomingDate, FORMAT_TODAY);
            } else if (isYesterday) {
                formattedDate = DateFormatUtils.format(incomingDate, FORMAT_YESTERDAY);
            }else if(isYearDay) {
                formattedDate = DateFormatUtils.format(incomingDate, FORMAT_YEAR_DAY);
            } else {
                formattedDate = DateFormatUtils.format(incomingDate, FORMAT_SIMPLE_DAY);
            }
        } catch (Exception e) {
            formattedDate = "";
        }

        return formattedDate;
    }
}
