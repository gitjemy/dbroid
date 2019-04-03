package com.a7md.dbroid.SQL.SqlTail;

import com.a7md.dbroid.SQL.DTable;
import com.a7md.dbroid.SQL.JColumn.JColumn;
import com.a7md.dbroid.SQL.JColumn.Key;

import java.util.ArrayList;
import java.util.Arrays;

public class Tail {

    ArrayList<Condition> Conditions = new ArrayList<>();
    String JoinStatement;
    String OrderStatement;
    String Limits;
    boolean Distinct;

    public Tail(Condition Condition, String JoinStatement, String OrderStatement) {
        this.Conditions.add(Condition);
        this.JoinStatement = JoinStatement;
        this.OrderStatement = OrderStatement;
    }

    public Tail() {
    }

    //<editor-fold defaultstate="collapsed" desc="Conditions">
    public Tail(Condition... Conditions) {
        this.Conditions.addAll(Arrays.asList(Conditions));
    }

    public Tail(long ID) {
        Conditions.add(new Condition(ID));
    }

    public Tail(Key... Conditions) {
        for (Key Condition : Conditions) {
            this.Conditions.add(new Condition(Condition));
        }
    }

    public Tail(Key Condition, Operator operator) {
        this.Conditions.add(new Condition(Condition, operator));
    }

    public Tail(String Condition) {
        this.Conditions.add(new Condition(Condition));
    }

    public void Add(Key... Conditions) {
        for (Key Condition : Conditions) {
            this.Conditions.add(new Condition(Condition));
        }
    }

    public Condition Add(Key Condition, Operator operator) {
        Condition S = new Condition(Condition, operator);
        this.Conditions.add(S);
        return S;
    }

    public void Add(Condition C) {
        this.Conditions.add(C);
    }
//</editor-fold>

    public void setOrderStatement(String OrderStatement) {
        this.OrderStatement = OrderStatement;
    }

    public void OrderBy(JColumn Col) {
        this.OrderStatement = "order by " + Col.Name;
    }

    public Tail OrderDescBy(JColumn Col) {
        this.OrderStatement = "order by " + Col.Name + " desc";
        return this;
    }

    public void OrderBy(String Col) {
        this.OrderStatement = "order by " + Col;
    }

    public void OrderDescBy(String Col) {
        this.OrderStatement = "order by " + Col + " desc";
    }

    public void setJoinStatement(String JoinStatement) {
        this.JoinStatement = JoinStatement;
    }

    //<editor-fold defaultstate="collapsed" desc="Joins">
    public void join(DTable FirstTable, DTable SecondTable, JColumn FrstTableCol, JColumn SecondTableCol) {
        JoinStatement = getJoinStatement("", FirstTable, SecondTable, FrstTableCol, SecondTableCol);
    }

    public void leftjoin(DTable FirstTable, DTable SecondTable, JColumn FrstTableCol, JColumn SecondTableCol) {
        JoinStatement = getJoinStatement("left", FirstTable, SecondTable, FrstTableCol, SecondTableCol);
    }

    public void Addjoin(DTable FirstTable, DTable SecondTable, JColumn FrstTableCol, JColumn SecondTableCol) {
        JoinStatement += getJoinStatement("", FirstTable, SecondTable, FrstTableCol, SecondTableCol);
    }

    public void AddLeftjoin(DTable FirstTable, DTable SecondTable, JColumn FrstTableCol, JColumn SecondTableCol) {
        JoinStatement += getJoinStatement("left", FirstTable, SecondTable, FrstTableCol, SecondTableCol);
    }

    public String getJoinStatement(String JoinOption, DTable FirstTable, DTable SecondTable, JColumn FrstTableCol, JColumn SecondTableCol) {
        String Jsm = " " + JoinOption + " join " + SecondTable.TableName + " on ";
        Jsm += FirstTable.TableName + "." + FrstTableCol.Name + "=" + SecondTable.TableName + "." + SecondTableCol.Name;
        return Jsm;
    }
    //</editor-fold>

    public void setLimits(String Limits) {
        this.Limits = Limits;
    }

    public void setLimits(int offset, int limit) {
        this.Limits = "Limit " + offset + "," + limit;
    }

    public void setDistinct(boolean Distinct) {
        this.Distinct = Distinct;
    }

    public String GetMysqlTail() {
        String Tail = " ";
        if (JoinStatement != null) {
            Tail += JoinStatement;
        }
        Tail += " ";
        if (!Conditions.isEmpty()) {
            Tail += " where ";
            boolean isfrist = true;
            for (Condition Condition1 : Conditions) {
                if (!isfrist) {
                    Tail += Condition1.getCondition();
                } else {
                    Tail += Condition1.getConditionContent();
                    isfrist = false;
                }
                Tail += " ";
            }
        }
        if (OrderStatement != null) {
            Tail += OrderStatement;
        }
        Tail += " ";
        if (Limits != null) {
            Tail += Limits;
        }
        return Tail;
    }

    public String GetSubConditionsString() {
        String Tail;
        if (!Conditions.isEmpty()) {
            Tail = "(";
            boolean isfrist = true;
            for (Condition Condition1 : Conditions) {
                if (!isfrist) {
                    Tail += Condition1.getCondition();
                } else {
                    Tail += Condition1.getConditionContent();
                    isfrist = false;
                }
                Tail += " ";
            }
            Tail += ")";
        } else {
            Tail = "(1=0)";
        }
        return Tail;
    }
}
