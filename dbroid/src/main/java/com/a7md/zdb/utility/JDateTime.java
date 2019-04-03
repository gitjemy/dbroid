package com.a7md.zdb.utility;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequiresApi(api = Build.VERSION_CODES.O)
public class JDateTime {

    static String db_time_stamp_format = "yyyy-MM-dd HH:mm:ss.S";
    static DateTimeFormatter db_time_stamp_formatter = DateTimeFormatter.ofPattern(db_time_stamp_format);
    static String time_format = "hh:mm a";
    static DateTimeFormatter time_formatter = DateTimeFormatter.ofPattern(time_format);
    static String date_format = "YYYY/MM/dd"; // yyyy/MM/dd
    static DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern(date_format);
    static String date_time_format = time_format + " | " + date_format;
    static DateTimeFormatter date_time_formatter = DateTimeFormatter.ofPattern(date_time_format);

    public static String str_time(LocalDateTime value) {
        return value.toLocalTime().format(time_formatter);
    }

    public static String str_date_time(LocalDateTime value) {
        if (value == null) return "غير محدد";
        return value.format(date_time_formatter);
    }

    public static String str_date(LocalDate value) {
        return value.format(date_formatter);
    }

    public static String str_date(LocalDateTime value) {
        return value.toLocalDate().format(date_formatter);
    }

    public static String FormattedNow() {
        return str_date_time(LocalDateTime.now());
    }

    public static String DB_TIMESTAMP(LocalDateTime value) {
        return value.format(db_time_stamp_formatter);
    }

}
