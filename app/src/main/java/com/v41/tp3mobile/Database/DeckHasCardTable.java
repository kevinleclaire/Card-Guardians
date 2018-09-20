package com.v41.tp3mobile.Database;

public class DeckHasCardTable {
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE deckHasCardTable (" +
                    "id     INTEGER    PRIMARY KEY  AUTOINCREMENT   ," +
                    "id_card     INTEGER   ," +
                    "id_deck     INTEGER    )";
    public static final String DROP_TABLE_SQL =
            "DROP TABLE deckHasCardTable";

    public static final String INSERT_SQL = "INSERT INTO deckHasCardTable (id_card,id_deck) VALUES(?,?)";
    public static final String SELECT_ALL_SQL = "SELECT id,id_card, id_deck FROM deckHasCardTable";
    public static final String DELETE_WHERE_CARD_ID_DECK_ID = "DELETE FROM deckHasCardTable WHERE id_card = ? AND id_deck = ?";
    public static final String DELETE_WHERE_CARD_ID = "DELETE FROM deckHasCardTable WHERE id_card = ?";
    public static final String DELETE_WHERE_DECK_ID = "DELETE FROM deckHasCardTable WHERE id_deck = ?";


    private DeckHasCardTable() {
        //Private constructor to prevent instantiation
    }
}
