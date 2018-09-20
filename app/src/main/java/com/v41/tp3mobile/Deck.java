package com.v41.tp3mobile;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class Deck {
    private final Integer NB_MAX_CARDS = 20;

    private Integer id;
    private String name;
    private LinkedList<Card> cards;

    public Deck(Integer id, String name) {
        this.name = name;
        this.id = id;
        cards = new LinkedList<>();
    }

    public Deck(Integer id, LinkedList<Card> cards, String name) {
        this.name = name;
        this.id = id;
        this.cards = new LinkedList<>();
        for (Card card : cards) {
            this.cards.add(card);
        }
    }
    public void removeCard(Card card)
    {
        cards.remove(card);
    }

    public LinkedList<Card> getCards() {
        return cards;
    }

    public void addCard(Card card) {
        if(cards.size() < NB_MAX_CARDS) {
            cards.add(card);
        }
    }

    @Override
    public Deck clone()
    {
        Deck deck = new Deck(1,"currentdeck");
        for (Card card : this.cards) {
            deck.addCard(card);
        }

        return deck;
    }


    public Card getCard(int index) {
        return cards.get(index);
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    public Card pickRandomCard() {
        Random rand = new Random();
        return cards.get(rand.nextInt(cards.size()));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
