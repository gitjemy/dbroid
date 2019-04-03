package com.a7md.dbroid.SQL.DBMS;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.a7md.dbroid.SQL.DTable;
import com.a7md.dbroid.SQL.JColumn.JColumn;
import com.a7md.dbroid.SQL.JColumn.Key;
import com.a7md.dbroid.SQL.SqlTail.Tail;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLHelper extends SQLiteOpenHelper {

    SQLiteDatabase WDB;
    public Activity mainActivity;

    public SQLHelper(Activity activity, String FilePath) {
        super(activity, FilePath, null, 1);
        WDB = getWritableDatabase();
        this.mainActivity = activity;
    }

    //<editor-fold defaultstate="collapsed" desc="Query">
    Cursor ExecutePrivateQuery(String Sql) {
        return GetResultSet(Sql);
    }

    final public Cursor GetResultSet(String SQLQuery) {
        return getReadableDatabase().rawQuery(SQLQuery, new String[]{});
    }


    public Cursor getResult(String SQLQuery) {
        return getReadableDatabase().rawQuery(SQLQuery, new String[]{});
    }

    public ArrayList<String> GetRow(String TableName, Tail RowIDent, JColumn[] RColumns) {
        ArrayList<String> FinalVal = null;
        String SQl = "SELECT " + DTable.GetSQLColumns(RColumns) + " From " + TableName + RowIDent.GetMysqlTail();
        Cursor C = ExecutePrivateQuery(SQl);
        if (C.moveToFirst()) {
            FinalVal = new ArrayList<>();
            for (int columnIndex = 0; columnIndex <= C.getColumnCount() - 1; columnIndex++) {
                FinalVal.add(C.getString(columnIndex));
            }
        }
        return FinalVal;
    }

    public String GetCell(String TableName, Tail ID, JColumn Column) {
        String FinalVal = null;
        String SQl = "SELECT " + Column.GetSelectStatment() + " From " + TableName + ID.GetMysqlTail();
        Cursor C = ExecutePrivateQuery(SQl);
        if (C.moveToFirst()) {
            FinalVal = C.getString(0);
        }
        return FinalVal;
    }

    public boolean SELECTEXISTS(String TableName, Tail Ident) {
        Cursor Res = ExecutePrivateQuery("SELECT EXISTS(SELECT 1 FROM " + TableName + Ident.GetMysqlTail() + ")");
        boolean Val;
        if (Res.moveToFirst()) {
            Val = (Res.getInt(0) == 1);
        } else {
            Val = false;
        }
        Res.close();
        return Val;
    }

    final public int GetCount(String TableName, String SQL) throws SQLException {
        Cursor C = ExecutePrivateQuery(SQL);
        if (C.moveToFirst()) {
            int Val = C.getInt(0);
            return Val;
        }
        return -1;
    }

    public int GetSumInt(String TableName, JColumn Col, Tail Where) throws SQLException {
        String SQL;
        if (Where == null) {
            SQL = "select sum(" + Col.Name + ") from " + TableName;
        } else {
            SQL = "select sum(" + Col.Name + ") from " + TableName + Where.GetMysqlTail();
        }
        Cursor C = ExecutePrivateQuery(SQL);
        if (C.moveToFirst()) {
            int Val = C.getInt(0);
            return Val;
        }
        return -1;
    }

    public double GetSumDouble(String TableName, JColumn Col, Tail Where) throws SQLException {
        String SQL;
        if (Where == null) {
            SQL = "select sum(" + Col.Name + ") from " + TableName;
        } else {
            SQL = "select sum(" + Col.Name + ") from " + TableName + Where.GetMysqlTail();
        }
        Cursor C = ExecutePrivateQuery(SQL);
        if (C.moveToFirst()) {
            double Val = C.getDouble(0);
            return Val;
        }
        return -1;
    }

    public double GetSumDouble(String TableName, String InSum, Tail Where) throws SQLException {
        String SQL;
        if (Where == null) {
            SQL = "select sum(" + InSum + ") from " + TableName;
        } else {
            SQL = "select sum(" + InSum + ") from " + TableName + Where.GetMysqlTail();
        }
        Cursor C = ExecutePrivateQuery(SQL);
        if (C.moveToFirst()) {
            double Val = C.getDouble(0);
            C.close();
            return Val;
        } else {
            C.close();
        }
        return -1;
    }

    public int GetCount(String TableName, Tail Where) {
        String SQL;
        if (Where == null) {
            SQL = "select count(0) from " + TableName;
        } else {
            SQL = "select count(0) from " + TableName + Where.GetMysqlTail();
        }
        Cursor C = ExecutePrivateQuery(SQL);
        if (C.moveToFirst()) {
            int Val = C.getInt(0);
            C.close();
            return Val;
        } else {
            C.close();
        }
        return -1;
    }

    public String GetValue(String SQL) throws SQLException {
        Cursor C = ExecutePrivateQuery(SQL);
        C.moveToFirst();
        String Val = C.getString(0);
        return Val;
    }

    public Cursor SelectAllData(String TableName, Tail ID) {
        String SQLQuery;
        if (ID == null) {
            SQLQuery = "SELECT * FROM " + TableName;
        } else {
            SQLQuery = "SELECT * FROM " + TableName + ID.GetMysqlTail();
        }
        return GetResultSet(SQLQuery);
    }

    public Cursor SelectDISTINCT(String TableName, JColumn Col, Tail ID) {
        String SQLQuery;
        if (ID == null) {
            SQLQuery = "SELECT DISTINCT " + Col.Name + " FROM " + TableName;
        } else {
            SQLQuery = "SELECT DISTINCT " + Col.Name + " FROM " + TableName + ID.GetMysqlTail();
        }
        return GetResultSet(SQLQuery);
    }

    public Cursor SelectJoinWith(String TableName, DTable AnotherTable, JColumn FristTableJoinCol, JColumn SecondTableJoinCol) throws SQLException {
        String Sql = "SELECT * FROM " + TableName + " join " + AnotherTable.TableName + " on ";
        Sql += TableName + "." + FristTableJoinCol.Name + "=" + AnotherTable.TableName + "." + SecondTableJoinCol.Name;
        return GetResultSet(Sql);
    }

    public Cursor SelectJoinWith(String TableName, DTable AnotherTable, JColumn FristTableJoinCol, JColumn SecondTableJoinCol, Tail ID) throws SQLException {
        String Sql = "SELECT * FROM " + TableName + " join " + AnotherTable.TableName + " on ";
        Sql += TableName + "." + FristTableJoinCol.Name + "=" + AnotherTable.TableName + "." + SecondTableJoinCol.Name;
        if (ID != null) {
            Sql += ID.GetMysqlTail();
        }
        return GetResultSet(Sql);
    }

    public Cursor SelectJoinWith(String TableName, String SelectedCols, DTable AnotherTable, JColumn FristTableJoinCol, JColumn SecondTableJoinCol, Tail ID) throws SQLException {
        String Sql = "SELECT " + SelectedCols + " FROM " + TableName + " join " + AnotherTable.TableName + " on ";
        Sql += TableName + "." + FristTableJoinCol.Name + "=" + AnotherTable.TableName + "." + SecondTableJoinCol.Name;
        if (ID != null) {
            Sql += ID.GetMysqlTail();
        }
        return GetResultSet(Sql);
    }

    public Cursor SelectJoinWith(String TableName, DTable AnotherTable, JColumn FristTableJoinCol, JColumn SecondTableJoinCol, Key ID) throws SQLException {
        String Sql = "SELECT * FROM " + TableName + " join " + AnotherTable.TableName + " on ";
        Sql += TableName + "." + FristTableJoinCol.Name + "=" + AnotherTable.TableName + "." + SecondTableJoinCol.Name;
        if (ID != null) {
            Sql += new Tail(ID).GetMysqlTail();
        }
        return GetResultSet(Sql);
    }

    //</editor-fold>
    //
    //<editor-fold defaultstate="collapsed" desc="Update">

    public void ExecuteUpdate(String SQL) {
        WDB.execSQL(SQL);
    }

    public void DeleteRow(String TableName, Tail ID) {
        String SQL = "DELETE FROM " + TableName + ID.GetMysqlTail();
        ExecuteUpdate(SQL);
    }

    public void DropTable(String TableName) throws SQLException {
        ExecuteUpdate("DROP TABLE IF EXISTS " + TableName);
    }

    public void ClearRows(String TableName) throws SQLException {
        String SQL = "DELETE FROM " + TableName;
        ExecuteUpdate(SQL);
    }

    public long AddRow(String TableName, Key... RowArray) {
        String SQl = "INSERT INTO " + TableName + "(";
        String Values = " VALUES ( ";
        for (int i = 0; i < RowArray.length; i++) {
            Key get = RowArray[i];
            SQl += get.ColName;
            Values += "?";
            if (i + 1 != RowArray.length) {
                SQl += ",";
                Values += ",";
            }
        }
        SQl += ")" + Values + ")";

        SQLiteStatement PS = WDB.compileStatement(SQl);
        for (int i = 0; i < RowArray.length; i++) {
            Key key = RowArray[i];
            PS.bindString(i + 1, key.getValue());
        }
        long ID = PS.executeInsert();
        PS.close();
        return ID;
    }

    public boolean UpdateRow(String TableName, Tail Rowid, Key... RowArray) {
        String SQl = "UPDATE " + TableName + " SET ";
        for (int i = 0; i < RowArray.length; i++) {
            SQl += RowArray[i].ColName;
            SQl += "=";
            SQl += "?";
            if (i + 1 != RowArray.length) {
                SQl += ",";
            }
        }
        SQl += Rowid.GetMysqlTail();
        int RowsAffected;
        SQLiteStatement PS = WDB.compileStatement(SQl);
        for (int i = 0; i < RowArray.length; i++) {
            Key key = RowArray[i];
            PS.bindString(i + 1, key.getValue());
        }
        RowsAffected = PS.executeUpdateDelete();
        return RowsAffected != 0;
    }

    public long insert_or_update(String TableName, ContentValues contentValues) {
        return WDB.insertWithOnConflict(TableName, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
    }

    //</editor-fold>

    //    <editor-fold defaultstate="collapsed" desc="Statements">
    final public void BeginTransactions() {
        WDB.beginTransaction();
    }

    final public void EndTtansactions() {
        WDB.endTransaction();
    }

    final public void SetTransactionSuccessful() {
        WDB.setTransactionSuccessful();
    }
//    </editor-fold>

    public boolean TableExist(String tableName) {
        Cursor cursor = getReadableDatabase().rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
