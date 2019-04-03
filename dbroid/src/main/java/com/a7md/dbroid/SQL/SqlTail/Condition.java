package com.a7md.dbroid.SQL.SqlTail;


import com.a7md.dbroid.SQL.JColumn.Key;

public class Condition {

    String MainOperator = "And";
    String ConditionContent;

    public Condition(String ConditionContent) {
        this.ConditionContent = ConditionContent;
    }

    public Condition(long id) {
        this.ConditionContent = GetOne("ID", id + "", Operator.Equal);
    }

    public Condition(Key Key, Operator Op) {
        this.ConditionContent = GetOne(Key.ColName, Key.getValue(), Op);
    }

    public Condition(Key Key) {
        this.ConditionContent = GetOne(Key.ColName, Key.getValue(), Operator.Equal);
    }

    public String getCondition() {
        return MainOperator + " " + ConditionContent;
    }

    public String getConditionContent() {
        return ConditionContent;
    }

    private String GetOne(String column, String Value, Operator OPERATOR) {
        if (OPERATOR == null) {
            OPERATOR = Operator.Equal;
        }
        String CondCntnt;
        if (OPERATOR == Operator.Like) {
            CondCntnt = column;
            CondCntnt += " like ('%" + Value + "%')";
        } else {
            CondCntnt = column;
            CondCntnt += OPERATOR.Operator + "'" + Value + "'";
        }
        return CondCntnt;
    }

    public void setMainOperator(String MainOperator) {
        this.MainOperator = MainOperator;
    }

}
