package com.a7md.zdb.helpers;

import com.a7md.zdb.DBErrorHandler;
import com.a7md.zdb.Query.JoinHandler;
import com.a7md.zdb.Query.ZQ.Equal;
import com.a7md.zdb.Query.ZQ.Selector;
import com.a7md.zdb.ZCOL.CreateTable;
import com.a7md.zdb.ZCOL.Key;
import com.a7md.zdb.ZCOL.SqlCol;
import com.a7md.zdb.ZSqlRow;
import com.a7md.zdb.ZTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class Link {

    private DBErrorHandler errorHandler;

    public Link(DBErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public void createTransaction(DBTransaction dbTransaction) throws Throwable {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        try {
            dbTransaction.run();
        } catch (Throwable e) {
            connection.rollback();
            throw e;
        } finally {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    public abstract String getDbName();

    final public <E extends ZSqlRow> void registerTable(ZTable<E> table, SqlCol<E, ?>[] cols) {
        try {
            boolean tableExist = false;
            ResultSet rs = getConnection().getMetaData().getTables(null, null, "%", null);
            while (rs.next()) {
                if (rs.getString("TABLE_NAME").equalsIgnoreCase(table.TableName)) {
                    tableExist = true;
                    break;
                }
            }
            rs.close();
            if (!tableExist) {
                new CreateTable(table, this, cols);
                table.onTableCreation();
                System.out.println("Database - " + getDbName() + ". -> creating table " + table.TableName + " ...");
            }
        } catch (Throwable e) {
            this.errorHandler.handle_error(e);
        }
    }

    public abstract Connection getConnection();

    abstract public void DeleteDbIfExists() throws Exception;

    public int update(String SQL) throws SQLException {
        Statement statement = getConnection().createStatement();
        int i = statement.executeUpdate(SQL);
        statement.close();
        return i;
    }

    public void DeleteRow(Equal id) throws SQLException {
        String SQL = "DELETE FROM " + id.col.mtable.TableName + new Selector(id).get();
        update(SQL);
    }

    public int AddRow(ZTable table, List<Key> RowArray) throws SQLException {
        StringBuilder SQl = new StringBuilder("INSERT INTO " + table.TableName + "(");
        StringBuilder Values = new StringBuilder(" VALUES ( ");

        for (int i = 0; i < RowArray.size(); i++) {
            Key get = RowArray.get(i);
            SQl.append(get.ColName);
            Values.append("?");
            if (i + 1 != RowArray.size()) {
                SQl.append(",");
                Values.append(",");
            }
        }
        SQl.append(")").append(Values).append(")");
        try (PreparedStatement PS = getConnection().prepareStatement(SQl.toString(), Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < RowArray.size(); i++) {
                Key key = RowArray.get(i);
                PS.setObject(i + 1, key.getValue());
            }
            int RowsAffected = PS.executeUpdate();
            if (RowsAffected != 0) {
                ResultSet rs = PS.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        // if faild
        return -1;
    }

    public boolean UpdateRow(Equal id, List<Key> RowArray) throws SQLException {
        StringBuilder SQl = new StringBuilder("UPDATE " + id.col.mtable.TableName + " SET ");
        for (int i = 0; i < RowArray.size(); i++) {
            SQl.append(RowArray.get(i).ColName);
            SQl.append("=");
            SQl.append("?");
            if (i + 1 != RowArray.size()) {
                SQl.append(",");
            }
        }
        SQl.append(new Selector(id).get());
        int RowsAffected;
        try (PreparedStatement PS = getConnection().prepareStatement(SQl.toString())) {
            for (int i = 0; i < RowArray.size(); i++) {
                Key key = RowArray.get(i);
                PS.setObject(i + 1, key.getValue());
            }
            RowsAffected = PS.executeUpdate();
        }
        return RowsAffected != 0;
    }

    public <E> E getResult(String sql, ResultHandler<E> handler) throws Exception {
        try (Statement e = getConnection().createStatement()) {
            try (ResultSet res = e.executeQuery(sql)) {
                return handler.handle(res);
            }
        }
    }

    public void getResult(String sql, JoinHandler handler) throws Exception {
        try (Statement e = getConnection().createStatement()) {
            try (ResultSet res = e.executeQuery(sql)) {
                while (res.next()) {
                    handler.handleRow(res);
                }
            }
        }
    }

    public void clearTable(ZTable zTable) throws SQLException {
        String SQL = "DELETE FROM " + zTable.TableName + ";";
        update(SQL);
    }

    public void deleteTable(String tableName) throws SQLException {
        update("DROP TABLE " + tableName + ";");
    }

    public interface ResultHandler<Return> {
        Return handle(ResultSet r) throws Exception;
    }

    public interface DBTransaction {
        void run() throws Throwable;
    }
}
