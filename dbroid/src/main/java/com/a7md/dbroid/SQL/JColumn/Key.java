package com.a7md.dbroid.SQL.JColumn;

public class Key {

    public final String ColName;
    private final Object Value;

    public Key(String Col, Object Value) {
        this.ColName = Col;
        this.Value = Value;
    }

    public String getValue() {
        if (Value == null) {
            return "";
        }
        return Value.toString();
    }
}
