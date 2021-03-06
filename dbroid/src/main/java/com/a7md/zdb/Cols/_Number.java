package com.a7md.zdb.Cols;

import android.database.Cursor;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.ZCOL.CreateTable;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.properties.WritableProperty;

import java.sql.SQLException;

public class _Number<E extends ZSqlRow> extends ACol<E, Integer> {

    private final short Size;

    public _Number(String name, boolean not_null, WritableProperty<E, Integer> property) {
        super(name, property, not_null);
        this.Size = 11;
    }

    public _Number(String name, WritableProperty<E, Integer> property) {
        super(name, property, false);
        this.Size = 11;
    }

    @Override
    public Equal equal(Integer val) {
        return new Equal(this, val);
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        CreateTable.first.add("`" + name + "` INTEGER(" + Size + ")" + (not_null ? " NOT NULL" : ""));
    }

    @Override
    public Integer get(Cursor cursor) throws SQLException {
        return cursor.getInt(cursor.getColumnIndex(name));
    }

}
