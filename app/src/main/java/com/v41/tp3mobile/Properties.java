package com.v41.tp3mobile;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;

import com.v41.tp3mobile.Database.ApplicationDatabaseHelper;
import com.v41.tp3mobile.Database.CardTable;
import com.v41.tp3mobile.Database.DeckHasCardTable;
import com.v41.tp3mobile.Database.DeckTable;
import com.v41.tp3mobile.Database.PlayerInfoTable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Properties extends Application {


    private ApplicationDatabaseHelper applicationDatabaseHelper;
    private int deckCardsScrollView = 0;
    private int playerCardsScrollView = 0;
    private ArrayList<Card> allCards = new ArrayList<>();

    public PlayerInfo playerInfo;
    public SQLiteDatabase gameDB;




    public void setAppli() {
        applicationDatabaseHelper = new ApplicationDatabaseHelper(this, "gameDB");
        gameDB = applicationDatabaseHelper.getWritableDatabase();
        playerInfo = new PlayerInfo();
    }

    public void destroyAppli() {
        if (applicationDatabaseHelper != null) {
            applicationDatabaseHelper.close();
        }
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public void createCard(Card card) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(CardTable.INSERT_SQL, new String[]{
                    card.getName(),
                    card.getMana().toString(),
                    card.getAttack().toString(),
                    card.getDefense().toString(),
                    card.getSpecial(),
                    card.getRarity(),
                    card.getType(),
                    card.getImgPath()

            });
            cursor.moveToLast(); //Nécessaire pour effectuer l'écriture
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public void createDeck(Deck deck) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(DeckTable.INSERT_SQL, new String[]{
                    deck.getId().toString(),
                    deck.getName()
            });
            cursor.moveToLast(); //Nécessaire pour effectuer l'écriture
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public void createPlayerInfo(PlayerInfo playerInfo) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(PlayerInfoTable.INSERT_SQL, new String[]{
                    playerInfo.getHasAlreadyPlayed().toString(),
                    playerInfo.getLevel().toString()
            });
            cursor.moveToLast(); //Nécessaire pour effectuer l'écriture
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public void createDeckHasCard(DeckHasCard deckHasCard) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(DeckHasCardTable.INSERT_SQL, new String[]{
                    deckHasCard.getCardId().toString(),
                    deckHasCard.getDeckId().toString()
            });
            cursor.moveToLast(); //Nécessaire pour effectuer l'écriture
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public LinkedList<Card> retrieveAllCards() {
        Cursor cursor = null;
        try {
            cursor = gameDB.rawQuery(CardTable.SELECT_ALL_SQL,
                    new String[]{});

            LinkedList<Card> cards = new LinkedList<>();
            while (cursor.moveToNext()) {
                Card card = new Card(cursor.getInt(0),cursor.getString(1),cursor.getInt(2),cursor.getInt(3)
                        ,cursor.getInt(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8));
                cards.add(card);
            }
            return cards;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public LinkedList<Deck> retrieveAllDecks() {
        Cursor cursor = null;
        try {
            cursor = gameDB.rawQuery(DeckTable.SELECT_ALL_SQL,
                    new String[]{});

            LinkedList<Deck> decks = new LinkedList<>();
            while (cursor.moveToNext()) {
                Deck deck = new Deck(cursor.getInt(0),cursor.getString(1));
                decks.add(deck);
            }
            return decks;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public List<PlayerInfo> retrievePlayerInfo() {
        Cursor cursor = null;
        try {
            cursor = gameDB.rawQuery(PlayerInfoTable.SELECT_ALL_SQL,
                    new String[]{});
            List<PlayerInfo> playerInfos = new LinkedList<>();
            while (cursor.moveToNext()) {
                PlayerInfo playerInfo = new PlayerInfo();
                playerInfo.setId(cursor.getInt(0));
                playerInfo.setHasAlreadyPlayed(cursor.getInt(1));
                playerInfo.setLevel(cursor.getInt(2));
                playerInfos.add(playerInfo);
            }
            if (playerInfos.size() > 0) {
                return playerInfos;
            }
            else {
                return null;
            }
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public LinkedList<DeckHasCard> retrieveDeckHasCard() {
        Cursor cursor = null;
        try {
            cursor = gameDB.rawQuery(DeckHasCardTable.SELECT_ALL_SQL,
                    new String[]{});
            LinkedList<DeckHasCard> decksHasCards = new LinkedList<>();
            while (cursor.moveToNext()) {
                DeckHasCard deckHasCard = new DeckHasCard();
                deckHasCard.setId(cursor.getInt(0));
                deckHasCard.setCardId(cursor.getInt(1));
                deckHasCard.setDeckId(cursor.getInt(2));
                decksHasCards.add(deckHasCard);
            }
            return decksHasCards;
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void deleteCard(Card card) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(CardTable.DELETE_WHERE_ID, new String[]{
                    String.valueOf(card.getId())
            });
            cursor.moveToLast();
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public void deleteDeck(Deck deck) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(DeckTable.DELETE_WHERE_ID, new String[]{
                    String.valueOf(deck.getId())
            });
            cursor.moveToLast();
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public void deleteDeckhasCard_whereCard_id_Deck_id(Card card,Deck deck) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(DeckHasCardTable.DELETE_WHERE_CARD_ID_DECK_ID, new String[]{
                    String.valueOf(card.getId()),
                    String.valueOf(deck.getId())
            });
            cursor.moveToLast();
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public void deleteDeckhasCard_where_Deck_ID(Deck deck) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(DeckHasCardTable.DELETE_WHERE_DECK_ID, new String[]{
                    String.valueOf(deck.getId())
            });
            cursor.moveToLast();
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public void deleteDeckhasCard_where_Card_ID(Card card) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(DeckHasCardTable.DELETE_WHERE_CARD_ID, new String[]{
                    String.valueOf(card.getId())
            });
            cursor.moveToLast();
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public void updatePlayerLevel(Integer level) {
        Cursor cursor = null;
        gameDB.beginTransaction();
        try {
            cursor = gameDB.rawQuery(PlayerInfoTable.UPDATE_LEVEL, new String[]{
                    level.toString()
            });
            cursor.moveToLast();
            gameDB.setTransactionSuccessful(); //Pour confirmer que l'écriture peut se faire
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            gameDB.endTransaction();
        }
    }

    public  ArrayList<Card> getAllCards() {
        return allCards;
    }

    public void setAllCards( ArrayList<Card> allCards) {
        this.allCards = allCards;
    }

    public int getDeckCardsScrollView() {
        return deckCardsScrollView;
    }

    public int getPlayerCardsScrollView() {
        return playerCardsScrollView;
    }

}
