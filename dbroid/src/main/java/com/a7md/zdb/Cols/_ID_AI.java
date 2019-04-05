package com.a7md.zdb.Cols;

import android.database.Cursor;

import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.NotEqual;
import com.a7md.zdb.ZCOL.CreateTable;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.helpers.MysqlHelper;
import com.a7md.zdb.properties.WritableProperty;

import java.sql.SQLException;

public class _ID_AI<E extends ZSqlRow> extends ACol<E, Integer> {

    public _ID_AI() {
        this("id");
    }

    public _ID_AI(String title) {
        super(title, new WritableProperty<>("المعرف", E::getId, E::setId));
    }

    @Override
    public Equal equal(Integer val) {
        return new Equal(this, val);
    }

    public NotEqual not_equal(Integer val) {
        return new NotEqual(this, val);
    }

    @Override
    protected void create(CreateTable CreateTable, Link link) {
        if (link instanceof MysqlHelper) {
            CreateTable.first.add("`" + name + "` INT NOT NULL AUTO_INCREMENT");
            CreateTable.last.add("PRIMARY KEY (`" + name + "`)");
        } else {
            CreateTable.first.add("`" + name + "` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT");
        }
    }


    @Override
    final public Integer get(Cursor resultSet) throws SQLException {
        return resultSet.getInt(resultSet.getColumnIndex(name));
    }
}
