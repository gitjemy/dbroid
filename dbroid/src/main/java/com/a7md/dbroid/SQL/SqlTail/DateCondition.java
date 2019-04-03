package com.a7md.dbroid.SQL.SqlTail;


import com.a7md.dbroid.SQL.JColumn.JColumn;

public class DateCondition extends Condition {

    public DateCondition(JColumn DateCol, String FromDate, String ToDate) {
        super("(" + DateCol.Name + ") Between '" + FromDate + "' and '" + ToDate + "'");
    }

    public DateCondition(String ColName, String FromDate, String ToDate) {
        super("(" + ColName + ") Between '" + FromDate + "' and '" + ToDate + "'");
    }

}
