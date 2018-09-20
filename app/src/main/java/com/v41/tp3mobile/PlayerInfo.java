package com.v41.tp3mobile;

import java.util.LinkedList;

public class PlayerInfo {

    private Integer id;
    private Integer hasAlreadyPlayed;
    private Integer level;
    private LinkedList<Deck> decks;
    private LinkedList<Card> cards;
    private Deck playingDeck;

    public PlayerInfo(Integer hasAlreadyPlayed, LinkedList<Deck> decks, LinkedList<Card> cards) {
        this.cards = cards;
        this.decks = decks;
        this.hasAlreadyPlayed = hasAlreadyPlayed;
    }
    public PlayerInfo(){
        cards = new LinkedList<>();
        decks = new LinkedList<>();
    }

    public Deck getPlayingDeck() {
        return playingDeck;
    }

    public void setPlayingDeck(Deck playingDeck) {
        this.playingDeck = playingDeck;
    }

    public void addCard (Card card) {
        cards.add(card);
    }
    public void addDeck (Deck deck) {
        decks.add(deck);
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void removeDeck(Deck deck) {
        decks.remove(deck);
    }

    public Integer getHasAlreadyPlayed() {
        return hasAlreadyPlayed;
    }

    public void setHasAlreadyPlayed(Integer hasAlreadyPlayed) {
        this.hasAlreadyPlayed = hasAlreadyPlayed;
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    public void setCards(LinkedList<Card> cards) {
        this.cards = cards;
    }

    public LinkedList<Deck> getDecks() {
        return decks;
    }

    public void setDecks(LinkedList<Deck> decks) {
        this.decks = decks;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
