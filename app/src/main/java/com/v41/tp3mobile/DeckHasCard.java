package com.v41.tp3mobile;

public class DeckHasCard {
    private Integer id;
    private Integer deckId;
    private Integer cardId;

    public DeckHasCard() {

    }

    public DeckHasCard(Integer deckId, Integer cardId) {
        this.cardId = cardId;
        this.deckId = deckId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public Integer getDeckId() {
        return deckId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public void setDeckId(Integer deckId) {
        this.deckId = deckId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
