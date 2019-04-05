package com.a7md.zdb;

import android.database.Cursor;

import com.a7md.zdb.Cols.ACol;
import com.a7md.zdb.Cols._ID_AI;

public abstract class DTable<Item extends ZSqlRow> extends Table<Item, Cursor, AndroidLink, _ID_AI<Item>, ACol<Item, ?>> {
    public DTable(AndroidLink link, String TName, _ID_AI<Item> ID) {
        super(link, TName, ID);
    }
}
