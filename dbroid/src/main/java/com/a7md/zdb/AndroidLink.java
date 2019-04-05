package com.a7md.zdb;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.a7md.zdb.Query.Join;
import com.a7md.zdb.Query.JoinHandler;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.COL;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.helpers.Link;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AndroidLink extends Link<Cursor> {

    private final SQLiteOpenHelper helper;
    private final Activity mainActivity;
    private String dbName;

    public AndroidLink(Activity mainActivity, String dbName) {
        this.dbName = dbName;
        this.mainActivity = mainActivity;

        this.helper = new SQLiteOpenHelper(mainActivity, dbName, null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            }
        };
    }

    private final SQLiteDatabase getWritableDb() {
        return helper.getWritableDatabase();
    }

    @Override
    public void createTransaction(DBTransaction dbTransaction) throws Throwable {
        throw new Exception("not supported");
    }

    @Override
    public String getDbName() {
        return this.dbName;
    }

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
    public <Item extends ZSqlRow> Item fromResultSet(Table<Item, Cursor, ?, ?, ?> zTable, Cursor cursor) throws Exception {
        return null;
    }

    @Override
    public void DeleteDbIfExists() {
        this.mainActivity.deleteDatabase(dbName);
    }

    @Override
    public void update(String s) throws SQLException {
        getWritableDb().execSQL(s);
    }

    @Override
    public int AddRow(Table table, List<Key> list) throws SQLException {
        StringBuilder SQl = new StringBuilder("INSERT INTO " + table.TableName + "(");
        StringBuilder Values = new StringBuilder(" VALUES ( ");

        for (int i = 0; i < list.size(); i++) {
            Key get = list.get(i);
            SQl.append(get.ColName);
            Values.append("?");
            if (i + 1 != list.size()) {
                SQl.append(",");
                Values.append(",");
            }
        }
        SQl.append(")").append(Values).append(")");
        SQLiteStatement PS = getWritableDb().compileStatement(SQl.toString());
        for (int i = 0; i < list.size(); i++) {
            Key key = list.get(i);
            PS.bindString(i + 1, key.getValue().toString());
        }
        int ID = (int) PS.executeInsert();
        PS.close();
        return ID;
    }

    @Override
    public boolean UpdateRow(Equal equal, List<Key> list) throws SQLException {
        StringBuilder SQl = new StringBuilder("UPDATE " + equal.col.mtable.TableName + " SET ");
        for (int i = 0; i < list.size(); i++) {
            SQl.append(list.get(i).ColName);
            SQl.append("=");
            SQl.append("?");
            if (i + 1 != list.size()) {
                SQl.append(",");
            }
        }
        SQl.append(new Selector(equal).get());
        int RowsAffected;
        SQLiteStatement PS = getWritableDb().compileStatement(SQl.toString());
        for (int i = 0; i < list.size(); i++) {
            Key key = list.get(i);
            PS.bindString(i + 1, key.getValue().toString());
        }
        RowsAffected = PS.executeUpdateDelete();
        return RowsAffected != 0;
    }

    @Override
    public void clearTable(Table zTable) throws SQLException {
        String SQL = "DELETE FROM " + zTable.TableName;
        update(SQL);
    }

    @Override
    public void deleteTable(String s) throws SQLException {
        update("DROP TABLE IF EXISTS " + s);
    }

    @Override
    public <U extends ZSqlRow> List<U> list(final Table<U, Cursor, ?, ?, ?> zTable, Selector selector) throws Exception {
        String sql = "SELECT * FROM " + zTable.TableName;
        if (selector != null) {
            sql += selector.get();
        }
        return getResult(sql, (ResultHandler<List<U>>) r -> {
            LinkedList<U> list = new LinkedList<>();
            while (r.moveToNext()) {
                list.add(fromResultSet(zTable, r));
            }
            return list;
        });
    }

    private <E> E getResult(String sql, ResultHandler<E> handler) throws Exception {
        Cursor e = getWritableDb().rawQuery(sql, new String[]{});
        try {
            return handler.handle(e);
        } finally {
            e.close();
        }
    }

    public interface ResultHandler<Return> {
        Return handle(Cursor r) throws Exception;
    }

    @Override
    public void query(JoinHandler joinHandler, Selector selector) throws Exception {
        throw new Exception("not supported");
    }

    @Override
    public <T> T value(COL<Cursor, ?, T> sqlCol, Selector selector) throws Exception {
        String SQl = "SELECT " + sqlCol.name + " From " + sqlCol.mtable.TableName + selector.get();
        return getResult(SQl,
                r -> {
                    if (r.moveToNext()) {
                        return sqlCol.get(r);
                    } else {
                        return null;
                    }
                });
    }


    @Override
    public int count(Table table, Selector selector) throws Exception {
        String SQL = "select count(0) from " + table.TableName + (selector == null ? "" : selector.get());
        return getResult(SQL, r -> {
            if (r.moveToNext()) {
                return r.getInt(1);
            } else {
                return -1;
            }
        });
    }

    @Override
    public int count(Join join, Selector selector) throws Exception {
        String SQL = build_join(join, "count(0)") + (selector == null ? "" : selector.get());
        return getResult(SQL, r -> {
            if (r.moveToNext()) {
                return r.getInt(1);
            } else {
                return -1;
            }
        });
    }

    @Override
    public boolean exist(Equal equal) throws Exception {
        return getResult("SELECT EXISTS(SELECT 1 FROM " + equal.col.mtable.TableName + new Selector(equal).get() + ")",
                r -> {
                    if (r.moveToNext()) {
                        return r.getInt(1) == 1;
                    } else {
                        return false;
                    }
                }
        );
    }

    @Override
    public boolean exist(Table table, Selector selector) throws Exception {
        return getResult("SELECT EXISTS(SELECT 1 FROM " + table.TableName +
                        selector.get() + ")",
                r -> {
                    if (r.moveToNext()) {
                        return r.getInt(1) == 1;
                    } else {
                        return false;
                    }
                }
        );
    }

    @Override
    public <V> ArrayList<V> distinctValues(COL<Cursor, ?, V> sqlCol) throws Exception {
        return getResult("SELECT DISTINCT " + sqlCol.name + " from " + sqlCol.mtable.TableName,
                r -> {
                    ArrayList<V> list = new ArrayList<>();
                    while (r.moveToNext()) {
                        list.add(sqlCol.get(r));
                    }
                    return list;
                }
        );
    }

    public SQLiteDatabase getReadableDatabase() {
        return helper.getReadableDatabase();
    }
}
