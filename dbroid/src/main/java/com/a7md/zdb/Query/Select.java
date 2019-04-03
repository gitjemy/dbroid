package com.a7md.zdb.Query;

import com.a7md.zdb.Query.ZQ.Condition;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZCOL._Decimal;
import com.a7md.zdb.ZCOL._Number;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.ZTable;
import com.a7md.zdb.helpers.Link;
import com.a7md.zdb.utility.ZSystemError;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Select {

    private static String build_join(Join join, String sql_cols) {
        SqlCol c1 = join.first_col;
        SqlCol c2 = join.Second_col;
        return "SELECT " + sql_cols + " FROM " +
                c1.mtable.TableName + " join " + c2.mtable.TableName + " on "
                + c1.mtable.TableName + "." + c1.name + "="
                + c2.mtable.TableName + "." + c2.name + " ";
    }


    public static <U extends ZSqlRow> List<U> list(final ZTable<U> table, Selector where) throws Exception {
        String sql = "SELECT * FROM " + table.TableName;
        if (where != null) {
            sql += where.get();
        }

        return table.db.getResult(sql, new Link.ResultHandler<List<U>>() {
            @Override
            public List<U> handle(ResultSet r) throws Exception {
                LinkedList<U> list = new LinkedList<>();
                while (r.next()) {
                    list.add(table.fromResultSet(r));
                }
                return list;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public static <U extends ZSqlRow> U row(Equal where) throws Exception {
        List<U> list = list(where.col.mtable, new Selector(where));
        if (list.size() != 1) {
            throw new ZSystemError("query of " + new Selector(where).get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public static <U extends ZSqlRow> U row(ZTable<U> table, Selector where) throws Exception {
        List<U> list = list(table, where);
        if (list.size() != 1) {
            throw new ZSystemError("query of " + where.get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public static <U extends ZSqlRow> U lastRow(ZTable<U> table, Selector where) throws Exception {
        where.setLimits(0, 1);
        where.orderDescBy(table.getID());
        List<U> list = list(table, where);
        if (list.size() != 1) {
            throw new ZSystemError("query of " + where.get() + " return " + list.size() + " values");
        } else {
            return list.get(0);
        }
    }

    public static void query(JoinHandler bind, Selector where) throws Exception {
        String sql = build_join(bind, "*") + (where != null ? where.get() : "");
        bind.first_col.mtable.db.getResult(sql, bind);
    }

    public static <U extends ZSqlRow> List<U> list(ZTable<U> table) throws Exception {
        return list(table, null);
    }

    static public <T> T value(final SqlCol<?, T> of_col, Selector where) throws Exception {
        String SQl = "SELECT " + of_col.name + " From " + of_col.mtable.TableName + where.get();

        return of_col.mtable.db.getResult(SQl, new Link.ResultHandler<T>() {
            @Override
            public T handle(ResultSet r) throws Exception {
                if (r.next()) {
                    return of_col.get(r);
                } else {
                    return null;
                }
            }
        });
    }

    static public <T> T value(SqlCol<?, T> col, int id) throws Exception {
        return value(col, new Selector(col.mtable.getID().equal(id)));
    }

    static public BigDecimal sum(_Decimal col, Equal where) throws Exception {
        return sum(col, new Selector(where));
    }

    static public BigDecimal sum(_Decimal col, Selector where) throws Exception {
        final _Decimal decimal = new _Decimal<>("sum(" + col.name + ")", null);
        decimal.setMtable(col.mtable);
        String sql = "select sum(" + col.name + ") from " + col.mtable.TableName
                + (where == null ? "" : where.get());

        return col.mtable.db.getResult(sql, new Link.ResultHandler<BigDecimal>() {
            @Override
            public BigDecimal handle(ResultSet r) throws Exception {
                if (r.next()) {
                    BigDecimal decimal1 = decimal.get(r);
                    return decimal1 == null ? BigDecimal.ZERO : decimal1;
                } else {
                    return BigDecimal.ZERO;
                }
            }
        });
    }

    static public BigDecimal sum(_Decimal col, Join join, Selector where) throws Exception {
        final _Decimal decimal = new _Decimal<>("sum(" + col.name + ")", null);
        decimal.setMtable(col.mtable);
        String sql = build_join(join, decimal.name) + where.get();

        return col.mtable.db.getResult(sql, new Link.ResultHandler<BigDecimal>() {
            @Override
            public BigDecimal handle(ResultSet r) throws Exception {
                if (r.next()) {
                    return decimal.get(r);
                } else {
                    return BigDecimal.ZERO;
                }
            }
        });
    }


    static public long sum(_Number col, Equal where) throws Exception {
        return sum(col, new Selector(where));
    }

    static public long sum(_Number col, Selector where) throws Exception {
        final _Number decimal = new _Number<>("sum(" + col.name + ")", null);
        decimal.setMtable(col.mtable);
        String sql = "select sum(" + col.name + ") from " + col.mtable.TableName
                + (where == null ? "" : where.get());
        return col.mtable.db.getResult(sql, new Link.ResultHandler<Long>() {
            @Override
            public Long handle(ResultSet r) throws Exception {
                if (r.next()) {
                    return decimal.get(r).longValue();
                } else {
                    return 0L;
                }
            }
        });
    }

    static public BigDecimal sum(_Decimal col) throws Exception {
        return sum(col, (Selector) null);
    }

    static public int count(ZTable table, Selector Where) throws Exception {
        String SQL = "select count(0) from " + table.TableName + (Where == null ? "" : Where.get());
        return table.db.getResult(SQL, new Link.ResultHandler<Integer>() {
            @Override
            public Integer handle(ResultSet r) throws Exception {
                if (r.next()) {
                    return r.getInt(1);
                } else {
                    return -1;
                }
            }
        });
    }

    static public int count(ZTable table, Condition condition) throws Exception {
        return count(table, new Selector(condition));
    }


    static public int count(Join join, Selector Where) throws Exception {
        String SQL = build_join(join, "count(0)") + (Where == null ? "" : Where.get());
        return join.first_col.mtable.db.getResult(SQL, new Link.ResultHandler<Integer>() {
            @Override
            public Integer handle(ResultSet r) throws Exception {
                if (r.next()) {
                    return r.getInt(1);
                } else {
                    return -1;
                }
            }
        });
    }


    static public boolean exist(Equal Ident) throws Exception {
        return Ident.col.mtable.db.getResult("SELECT EXISTS(SELECT 1 FROM " + Ident.col.mtable.TableName + new Selector(Ident).get() + ")",
                new Link.ResultHandler<Boolean>() {
                    @Override
                    public Boolean handle(ResultSet r) throws Exception {
                        if (r.next()) {
                            return r.getInt(1) == 1;
                        } else {
                            return false;
                        }
                    }
                }
        );
    }

    static public boolean exist(ZTable table, Selector selector) throws Exception {
        return table.db.getResult("SELECT EXISTS(SELECT 1 FROM " + table.TableName +
                        selector.get() + ")",
                new Link.ResultHandler<Boolean>() {
                    @Override
                    public Boolean handle(ResultSet r) throws Exception {
                        if (r.next()) {
                            return r.getInt(1) == 1;
                        } else {
                            return false;
                        }
                    }
                }
        );
    }


    static public <V> ArrayList<V> distinctValues(final SqlCol<?, V> col) throws Exception {
        return col.mtable.db.getResult("SELECT DISTINCT " + col.name + " from " + col.mtable.TableName,
                new Link.ResultHandler<ArrayList<V>>() {
                    @Override
                    public ArrayList<V> handle(ResultSet r) throws Exception {
                        ArrayList<V> list = new ArrayList<>();
                        while (r.next()) {
                            list.add(col.get(r));
                        }
                        return list;
                    }
                }
        );
    }

}
