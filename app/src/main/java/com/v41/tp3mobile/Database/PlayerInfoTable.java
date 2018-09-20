package com.v41.tp3mobile.Database;

public class PlayerInfoTable {
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE playerInfoTable (" +
                    "id          INTEGER    PRIMARY KEY AUTOINCREMENT    ," +
                    "hasAlreadyPlayed    INTEGER ," +
                    "level     INTEGER )";
    public static final String DROP_TABLE_SQL =
            "DROP TABLE playerInfoTable";

    public static final String INSERT_SQL = "INSERT INTO playerInfoTable (hasAlreadyPlayed, level) VALUES(?,?)";
    public static final String SELECT_ALL_SQL = "SELECT id, hasAlreadyPlayed, level FROM playerInfoTable";
    public static final String UPDATE_LEVEL = "UPDATE playerInfoTable SET level = ?";

    private PlayerInfoTable() {
        //Private constructor to prevent instantiation
    }
}
