package com.a7md.dbroid.SQL.Variables;

import com.a7md.dbroid.SQL.DBMS.SQLHelper;
import com.a7md.dbroid.SQL.DTable;
import com.a7md.dbroid.SQL.JColumn.JColumn;

public class VariablesTableModel extends DTable {

    public static JColumn ID = new JColumn("ID");
    public static JColumn Name = new JColumn("Name");
    public static JColumn Value = new JColumn("Value");
    public static JColumn Lable = new JColumn("Lable");
    public static JColumn Description = new JColumn("Description");
    public static JColumn LastUpdate = new JColumn("LastUpdate");

    public VariablesTableModel(SQLHelper JDB, String TabName) {
        super(JDB, TabName);
        CreateTable();
    }

    @Override
    public String GetCreateTableStatement(SQLHelper Helper) {
        return "CREATE TABLE `" + TableName + "` (\n"
                + "  `ID` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n"
                + "  `Name` VARCHAR(100) UNIQUE,\n"
                + "  `Value` VARCHAR(1024) NULL,\n"
                + "  `Lable` VARCHAR(45) NULL,\n"
                + "  `Description` VARCHAR(1024) NULL,\n"
                + "  `LastUpdate` VARCHAR(45) NULL);";
    }

}
