package com.a7md.dbroid.SQL;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;

import com.a7md.dbroid.SQL.DBMS.SQLHelper;
import com.a7md.dbroid.SQL.JColumn.JColumn;
import com.a7md.dbroid.SQL.JColumn.Key;
import com.a7md.dbroid.SQL.SqlTail.Tail;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;


public abstract class DTable {

    public SQLHelper Helper;
    public String TableName;


    public DTable(SQLHelper JDB, String TName) {
        this.Helper = JDB;
        this.TableName = TName;
    }

    abstract public String GetCreateTableStatement(SQLHelper Helper);

    final public boolean CreateTable() {
        if (Helper.TableExist(TableName)) {
            return false;
        } else {
            Helper.ExecuteUpdate(GetCreateTableStatement(Helper).replaceAll("\n", ""));
            return true;
        }
    }

    final static public String GetSQLColumns(JColumn[] SColumns) {
        String FinalVal = "";
        for (int i = 0; i < SColumns.length; i++) {
            FinalVal += SColumns[i].GetSelectStatment();
            if (i + 1 != SColumns.length) {
                FinalVal += ",";
            }
        }
        return FinalVal;
    }

    final public void DropTable() throws SQLException {
        Helper.DropTable(TableName);
    }

    final public void DeleteRow(Tail ID) {
        Helper.DeleteRow(TableName, ID);
    }

    final public void ClearRows() throws SQLException {
        Helper.ClearRows(TableName);
    }

    final public Cursor SelectAllData(Tail ID) {
        return Helper.SelectAllData(TableName, ID);
    }

    final public Cursor SelectDISTINCT(JColumn Col, Tail ID) {
        return Helper.SelectDISTINCT(TableName, Col, ID);
    }

    final public ArrayList<String> GetRow(Tail RowIDent, JColumn[] RColumns) throws SQLException {
        return Helper.GetRow(TableName, RowIDent, RColumns);
    }

    final public Object GetCell(Tail ID, JColumn Column) {
        return Helper.GetCell(TableName, ID, Column);
    }

    final public Object GetCell(Key ID, JColumn Column) throws SQLException {
        return Helper.GetCell(TableName, new Tail(ID), Column);
    }

    final public long AddRow(Key... RowArray) {
        return Helper.AddRow(TableName, RowArray);
    }

    final public long insert_or_update(ContentValues contentValues) {
        return Helper.insert_or_update(TableName, contentValues);
    }

    final public boolean UpdateRow(Tail Rowid, Key... RowArray) {
        return Helper.UpdateRow(TableName, Rowid, RowArray);
    }

    final public boolean selectExists(Key kc) {
        return Helper.SELECTEXISTS(TableName, new Tail(kc));
    }

    final public boolean selectExists(Tail Ident) throws SQLException {
        return Helper.SELECTEXISTS(TableName, Ident);
    }

    //    <editor-fold defaultstate="collapsed" desc="Statements">
    public final static void BeginTransactions(DTable... table) throws SQLException {
        for (DTable dTable : table) {
            dTable.BeginTransactions();
        }
    }

    public final static void EndTtansactions(DTable... table) throws SQLException {
        for (DTable dTable : table) {
            dTable.EndTtansactions();
        }
    }

    public final static void SetTransactionSuccessful(DTable... table) throws SQLException {
        for (DTable dTable : table) {
            dTable.SetTransactionSuccessful();
        }
    }

    final public void BeginTransactions() throws SQLException {
        Helper.BeginTransactions();
    }

    final public void EndTtansactions() throws SQLException {
        Helper.EndTtansactions();
    }

    final public void SetTransactionSuccessful() throws SQLException {
        Helper.SetTransactionSuccessful();
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Other Functions">
    final public int GetSumInt(JColumn Col, Key ID) throws SQLException {
        if (ID == null) {
            return GetSumInt(Col, (Tail) null);
        } else {
            return GetSumInt(Col, new Tail(ID));
        }
    }

    final public double GetSumDouble(JColumn Col, Key ID) throws SQLException {
        if (ID == null) {
            return GetSumDouble(Col, (Tail) null);
        } else {
            return GetSumDouble(Col, new Tail(ID));
        }
    }

    final public String GetValue(String SQL) throws SQLException {
        return Helper.GetValue(SQL);
    }

    final public int GetSumInt(JColumn Col, Tail Where) throws SQLException {
        return Helper.GetSumInt(TableName, Col, Where);
    }

    final public double GetSumDouble(JColumn Col, Tail Where) throws SQLException {
        return Helper.GetSumDouble(TableName, Col, Where);
    }

    final public double GetSumDouble(String InSum, Tail Where) throws SQLException {
        return Helper.GetSumDouble(TableName, InSum, Where);
    }

    final public int GetCount(Tail Where) {
        return Helper.GetCount(TableName, Where);
    }

    public Cursor SelectJoinWith(DTable AnotherTable, JColumn FristTableJoinCol, JColumn SecondTableJoinCol) throws SQLException {
        return Helper.SelectJoinWith(TableName, AnotherTable, FristTableJoinCol, SecondTableJoinCol);
    }

    public Cursor SelectJoinWith(DTable AnotherTable, JColumn FristTableJoinCol, JColumn SecondTableJoinCol, Tail ID) throws SQLException {
        return Helper.SelectJoinWith(TableName, AnotherTable, FristTableJoinCol, SecondTableJoinCol, ID);

    }

    public Cursor SelectJoinWith(String SelectedCols, DTable AnotherTable, JColumn FristTableJoinCol, JColumn SecondTableJoinCol, Tail ID) throws SQLException {
        return Helper.SelectJoinWith(TableName, SelectedCols, AnotherTable, FristTableJoinCol, SecondTableJoinCol, ID);
    }

    public Cursor SelectJoinWith(DTable AnotherTable, JColumn FristTableJoinCol, JColumn SecondTableJoinCol, Key ID) throws SQLException {
        return Helper.SelectJoinWith(TableName, AnotherTable, FristTableJoinCol, SecondTableJoinCol, ID);
    }
    //</editor-fold>


    ///////////////new functions


    protected static Key[] jsonToKeyPairs(JSONObject object, JColumn... cols) throws JSONException {
        Key[] keys = new Key[cols.length];
        for (int i = 0; i < cols.length; i++) {
            keys[i] = cols[i].GetKey(object);
        }
        return keys;
    }

    public Activity getActivity() {
        return Helper.mainActivity;
    }

}
