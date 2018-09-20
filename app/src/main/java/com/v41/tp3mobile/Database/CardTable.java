package com.v41.tp3mobile.Database;

public class CardTable {
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE cardTable (" +
                    "id          INTEGER    PRIMARY KEY    AUTOINCREMENT," +
                    "name        TEXT," +
                    "mana        INTEGER," +
                    "attack      INTEGER," +
                    "defense     INTEGER," +
                    "special     TEXT," +
                    "rarity     TEXT," +
                    "type     TEXT," +
                    "imgPath     TEXT)";
    public static final String DROP_TABLE_SQL =
            "DROP TABLE cardTable";

    public static final String INSERT_SQL = "INSERT INTO cardTable (name, mana, attack, defense, special, rarity, type, imgPath) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SELECT_ALL_SQL = "SELECT id, name, mana, attack, defense, special,rarity,type, imgPath FROM cardTable";
    public static final String DELETE_WHERE_ID = "DELETE FROM cardTable WHERE id = ?";
    private CardTable() {
        //Private constructor to prevent instantiation
    }
}
