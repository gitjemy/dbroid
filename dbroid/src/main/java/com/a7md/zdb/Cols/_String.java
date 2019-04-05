package com.a7md.zdb.Cols;

import android.database.Cursor;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Like;
import com.a7md.zdb.ZCOL.CreateTable;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.properties.WritableProperty;

import java.sql.SQLException;

public class _String<E extends ZSqlRow> extends ACol<E, String> {
    public final short Size;

    public _String(String name, int Size, boolean not_null, WritableProperty<E, String> property) {
        super(name, property, not_null);
        this.Size = (short) Size;
    }

    public _String(String name, int Size, WritableProperty<E, String> property) {
        super(name, property, false);
        this.Size = (short) Size;
    }


    @Override
    public String get(Cursor resultSet) throws SQLException {
        return resultSet.getString(resultSet.getColumnIndex(name));
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` VARCHAR(" + Size + ")" + (not_null ? " NOT NULL" : ""));
    }

    @Override
    public Equal equal(String val) {
        return new Equal(this, val);
    }


    public Like like(String val) {
        return new Like(this, val);
    }
}
