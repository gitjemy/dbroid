package com.a7md.dbroid.SQL.Utility;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JDateTime {

    final public static String Format = "yyyy-MM-dd HH:mm:ss.S";
    final public static SimpleDateFormat DateTimeFormater = new SimpleDateFormat(Format);
//


    final static SimpleDateFormat DateFormater = new SimpleDateFormat("yyyy-MM-dd");
    final static SimpleDateFormat TimeFormater = new SimpleDateFormat("h:m a");

    static public String FormattedNow() {
        return DateTimeFormater.format(new Date());
    }

    static public String toStringTime(Date value) {
        return TimeFormater.format(value);
    }

    static public String toStringDate(Date value) {
        return DateFormater.format(value);
    }

    //    static public String toStringDate(LocalDateTime value) {
//        return value.toLocalDate().format(DateFormater);
//    }
//
//
    static public String toString(Date value) {
        return DateTimeFormater.format(value);
    }

    //
    static public Date fromString(String value) {
        try {
            return DateTimeFormater.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

//    static public String toString(LocalDate value) {
//        return LocalDateTime.of(value, LocalTime.now()).format(DateTimeFormater);
//    }

}
