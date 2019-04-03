package com.a7md.dbroid.SQL.JColumn;

import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

public class JColumn {

    public String Name;
    public final int index;

    public JColumn(String Name, int index) {
        this.Name = Name;
        this.index = index;
    }

    public JColumn(String Name) {
        this.Name = Name;
        this.index = -1;
    }


    public String GetSelectStatment() {
        return "`" + Name + "`";
    }

    public Key GetKey(String Value) {
        return new Key(Name, Value);
    }

    public Key GetKey(Boolean Value) {
        return new Key(Name, Value);
    }

    public Key GetKey(Long Value) {
        return new Key(Name, (Value) + "");
    }


    final public Key GetKey(JSONObject jsonObject) throws JSONException {
        return new Key(Name, jsonObject.getString(Name));
    }

    public Key GetKeyAndValue(Cursor resultSet) {
        return new Key(Name, resultSet.getString(resultSet.getColumnIndex(Name)));
    }

}
