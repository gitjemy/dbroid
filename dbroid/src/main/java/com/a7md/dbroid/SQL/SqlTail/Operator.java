package com.a7md.dbroid.SQL.SqlTail;

public class Operator {

    public String Name;
    public String Operator;

    public Operator(String Name, String Operator) {
        this.Name = Name;
        this.Operator = Operator;
    }

    @Override
    public String toString() {
        return Name;
    }

    public static final Operator Equal = new Operator("Equal", "=");
    public static final Operator Like = new Operator("Contain", " like ");

}
