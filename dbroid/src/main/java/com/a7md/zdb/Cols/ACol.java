package com.a7md.zdb.Cols;

import android.database.Cursor;

import com.a7md.zdb.ZCOL.COL;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.properties.WritableProperty;

public abstract class ACol<E extends ZSqlRow, V> extends COL<Cursor, E, V> {

    protected ACol(String Name, WritableProperty<E, V> property, boolean not_null) {
        super(Name, property, not_null);
    }

    protected ACol(String Name, WritableProperty<E, V> property) {
        super(Name, property);
    }

    @Override
    public void assign(E e, Cursor cursor) throws Exception {
        V v = get(cursor);
        property.setValue(e, v);
    }
}
