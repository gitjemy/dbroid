package com.a7md.dbroid.SQL.Variables;

import com.a7md.dbroid.SQL.SqlTail.Tail;
import com.a7md.dbroid.SQL.Utility.JDateTime;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Variable {

    private final String VariableName;
    private final VariablesTableModel table;

    public Variable(VariablesTableModel table, String Name, String DefaultValue, String Lable) {
        this.VariableName = Name;
        this.table = table;
        try {
            Tail ID = new Tail(VariablesTableModel.Name.GetKey(Name));
            ID.Add(VariablesTableModel.Lable.GetKey(Lable));
            if (!table.selectExists(ID)) {
                table.AddRow(VariablesTableModel.Name.GetKey(Name),
                        VariablesTableModel.Value.GetKey(DefaultValue),
                        VariablesTableModel.Lable.GetKey(Lable)
                );
                UpdateValue(DefaultValue);
            }
        } catch (Exception ex) {
            Logger.getLogger(Variable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Variable(VariablesTableModel table, String Name, String DefaultValue) {
        this(table, Name, DefaultValue, Name);
    }


    public Variable(VariablesTableModel table, String Name) {
        this(table, Name, "", Name);
    }


    public String getLable() throws SQLException {
        Tail ID = new Tail(VariablesTableModel.Name.GetKey(VariableName));
        String Lable = table.GetCell(ID, VariablesTableModel.Lable).toString();
        return Lable;
    }

    public String getValue() {
        Tail ID = new Tail(VariablesTableModel.Name.GetKey(VariableName));
        String GetValue = table.GetCell(ID, VariablesTableModel.Value).toString();
        if (GetValue != null) {
            return GetValue;
        } else {
            return "";
        }
    }

    public int getIntValue() throws SQLException {
        return Integer.valueOf(getValue());
    }

    public void UpdateValue(String Value) {
        table.UpdateRow(new Tail(VariablesTableModel.Name.GetKey(VariableName)),
                VariablesTableModel.Value.GetKey(Value),
                VariablesTableModel.LastUpdate.GetKey(JDateTime.FormattedNow())
        );
    }

    public void update_boolean(boolean Value) {
        table.UpdateRow(new Tail(VariablesTableModel.Name.GetKey(VariableName)),
                VariablesTableModel.Value.GetKey(Value),
                VariablesTableModel.LastUpdate.GetKey(JDateTime.FormattedNow())
        );
    }

    public boolean get_boolean() {
        String value = getValue();
        return value.equalsIgnoreCase("true");
    }
}
