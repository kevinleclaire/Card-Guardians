package com.v41.tp3mobile.Database;

public class DeckTable {
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE deckTable (" +
                    "id          INTEGER    PRIMARY KEY    AUTOINCREMENT," +
                    "name     TEXT)";
    public static final String DROP_TABLE_SQL =
            "DROP TABLE IF EXISTS deckTable";

    public static final String INSERT_SQL = "INSERT INTO deckTable (id,name) VALUES(?,?)";
    public static final String SELECT_ALL_SQL = "SELECT id, name FROM deckTable";
    public static final String DELETE_WHERE_ID = "DELETE FROM deckTable WHERE id = ?";

    private DeckTable() {
        //Private constructor to prevent instantiation
    }
}
