package com.v41.tp3mobile;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditDeckActivity extends AppCompatActivity {

    Card previousCardAdded;
    Card previousCardRemoved;

    private Animation anim;

    private Card cardDraged;
    private Card[] currentCards;
    private View[] cardViews;

    private LinearLayout cardInDeckGallery;
    private LayoutInflater cardInDeckInflater;

    private LinearLayout cardGallery;
    private LayoutInflater cardInflater;

    private HorizontalScrollView hsv_deckCards;
    private HorizontalScrollView hsv_playerCards;
    private Integer indexCurrentDeck;
    private Deck currentDeck;
    private int indexCurrentCardLayout = 0;

    private Properties properties;

    CardsVisualizer cardsInDeckVisualizer;
    CardsVisualizer cardsVisualizer;

    private TextView tv_nbCarteDansDeck;

    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_deck);
        properties = ((Properties) this.getApplication());
        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        anim = AnimationUtils.loadAnimation(this,R.anim.longshake);

        soundManager = new SoundManager(this);

        setHorizontalScrollViews();

        if(b!=null)
        {
            indexCurrentDeck = b.getInt("DECK_INDEX");
        }

        currentDeck = properties.getPlayerInfo().getDecks().get(indexCurrentDeck);
        int cardsInDeck = currentDeck.getCards().size();
        int nb_Card = countCardsNotInDeck();


        tv_nbCarteDansDeck = findViewById(R.id.tv_nbCarteDansDeck);

        cardViews = new View[(cardsInDeck + nb_Card)];

        cardsVisualizer = new CardsVisualizer(nb_Card,this);

        currentCards = new Card[nb_Card];

        cardInflater = LayoutInflater.from(this);

        cardsInDeckVisualizer = new CardsVisualizer(cardsInDeck, this);

        cardInDeckInflater = LayoutInflater.from(this);
        initView();

        hsv_playerCards.getBackground().setAlpha(80);
        hsv_deckCards.getBackground().setAlpha(80);

        dragListenerInit();

        tv_nbCarteDansDeck.setText(currentDeck.getCards().size()+"/20");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("DECK_VIEW_SCROLL_X", hsv_deckCards.getScrollX());
        outState.putInt("CARDS_VIEW_SCROLL_X", hsv_playerCards.getScrollX());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hsv_deckCards.scrollTo(savedInstanceState.getInt("DECK_VIEW_SCROLL_X"), 0);
        hsv_playerCards.scrollTo(savedInstanceState.getInt("CARDS_VIEW_SCROLL_X"), 0);
    }

    private void initView() {
        cardInDeckGallery = findViewById(R.id.id_gallery);
        cardGallery = findViewById(R.id.id_gallery_cards);

        for (int i = 0; i < cardsInDeckVisualizer.getCardLayouts().length; i++) {
            View cardInDeckView = cardInDeckInflater.inflate(R.layout.card_layout, cardInDeckGallery, false);
            createDeckList(i, cardInDeckView);

            cardInDeckGallery.addView(cardInDeckView);
        }

        for (int i = 0; i < properties.getPlayerInfo().getCards().size(); i++) {
            if(!currentDeck.getCards().contains(properties.getPlayerInfo().getCards().get(i))) {
                View cardView = cardInflater.inflate(R.layout.card_layout, cardGallery, false);
                createList(i, cardView);

                cardGallery.addView(cardView);
            }
        }
    }

    public void onClickReturnButton(View view) {
        soundManager.playButtonClickSound();
        finish();
    }

    private void createDeckList(int i, View view) {

        cardsInDeckVisualizer.initCardVisualizer(i,view,currentDeck.getCards().get(i));

        final Card cardInDeckClicked = currentDeck.getCard(i);
        view.setOnLongClickListener(new View.OnLongClickListener() {


            public boolean onLongClick(View v) {

                ClipData.Item item = new ClipData.Item((String)v.getTag());
                String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
                ClipData data = new ClipData((String) v.getTag(), mimeTypes, item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                v.startDrag(data, shadowBuilder, null, 0);

                cardDraged = cardInDeckClicked;

                return true;
            }
        });
        if(previousCardAdded != null) {
            if (previousCardAdded.getId().equals(currentDeck.getCards().get(i).getId())) {
                cardsInDeckVisualizer.getTv_cardNames()[i].setTextColor(Color.BLUE);
                cardsInDeckVisualizer.getTv_cardNames()[i].setAllCaps(true);
                view.startAnimation(anim);
            }
        }

        cardViews[i] = view;

        view.setVisibility(View.VISIBLE);
    }

    private void createList(int i, View view) {

        cardsVisualizer.initCardVisualizer(indexCurrentCardLayout,view,properties.getPlayerInfo().getCards().get(i));

        currentCards[indexCurrentCardLayout] =  properties.getPlayerInfo().getCards().get(i);

        final Card cardClicked = currentCards[indexCurrentCardLayout];

        view.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {

                ClipData.Item item = new ClipData.Item((String)v.getTag());
                String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
                ClipData data = new ClipData((String) v.getTag(), mimeTypes, item);
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                v.startDrag(data, shadowBuilder, null, 0);

                cardDraged = cardClicked;

                return true;
            }
        });
        if(previousCardRemoved != null) {
            if (previousCardRemoved.getId().equals(properties.getPlayerInfo().getCards().get(i).getId())) {
                cardsVisualizer.getTv_cardNames()[indexCurrentCardLayout].setTextColor(Color.RED);
                cardsVisualizer.getTv_cardNames()[indexCurrentCardLayout].setAllCaps(true);
                view.startAnimation(anim);
            }
        }
        cardViews[currentDeck.getCards().size() + indexCurrentCardLayout] = view;
        indexCurrentCardLayout++;
    }

    private int countCardsNotInDeck() {

        int nb_card = 0;
        for (Card card : properties.getPlayerInfo().getCards()) {
            if(!currentDeck.getCards().contains(card)) {
                nb_card++;
            }
        }
        return nb_card;
    }

    private void removeCardFromDeck(Deck deck, Card card) {
        previousCardRemoved = card;
        properties.deleteDeckhasCard_whereCard_id_Deck_id(card, deck);
        properties.getPlayerInfo().getDecks().get(indexCurrentDeck).removeCard(card);
        currentDeck = properties.getPlayerInfo().getDecks().get(indexCurrentDeck);
        manualRefresh();
    }

    private void addCardInDeck(Deck deck, Card card) {
        if(!deck.getCards().contains(card)) {
            if(deck.getCards().size() < 20) {
                previousCardAdded = card;
                DeckHasCard deckHasCard = new DeckHasCard(deck.getId(), card.getId());
                properties.createDeckHasCard(deckHasCard);

                currentDeck.addCard(card);
                manualRefresh();
            }
            else{
                Toast.makeText(this, "You already have 20 cardViews in your deck", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(this, "Your deck already contains this card", Toast.LENGTH_SHORT).show();
        }
    }

    private void setHorizontalScrollViews() {
        hsv_deckCards = findViewById(R.id.hsv_deckCards);
        hsv_playerCards = findViewById(R.id.hsv_playerCards);

        if (properties.getDeckCardsScrollView() != 0) {
            hsv_deckCards.scrollTo(properties.getDeckCardsScrollView(), properties.getDeckCardsScrollView());
        }
        if (properties.getPlayerCardsScrollView() != 0) {
            hsv_playerCards.scrollTo(properties.getPlayerCardsScrollView(), properties.getPlayerCardsScrollView());
        }
    }

    View.OnDragListener onDragCardListener = new View.OnDragListener(){

        public boolean onDrag(View v, DragEvent event) {

            if(event.getAction() == DragEvent.ACTION_DROP) {
                if(v.getY() == hsv_playerCards.getY())
                {
                    removeCardFromDeck(currentDeck,cardDraged);
                }
                else if(v.getY() == hsv_deckCards.getY())
                {
                    addCardInDeck(currentDeck,cardDraged);
                }
                soundManager.playCardPlacingSound();
            }
            return true;
        }
    };

    private void dragListenerInit() {

        hsv_deckCards.setOnDragListener(onDragCardListener);
        hsv_playerCards.setOnDragListener(onDragCardListener);

    }

    public void manualRefresh() {
        for (View card : cardViews) {
            if(card != null) {
                card.setVisibility(View.GONE);
                card.clearAnimation();
            }
        }
        cardGallery.removeAllViews();
        cardInDeckGallery.removeAllViews();

        int cardsInDeck = currentDeck.getCards().size();
        int nb_Card = countCardsNotInDeck();

        tv_nbCarteDansDeck = findViewById(R.id.tv_nbCarteDansDeck);
        tv_nbCarteDansDeck.setText(currentDeck.getCards().size()+"/20");

        cardViews = new View[(nb_Card + cardsInDeck)];

        cardsVisualizer = new CardsVisualizer(nb_Card,this);

        currentCards = new Card[nb_Card];

        cardsInDeckVisualizer = new CardsVisualizer(cardsInDeck,this);

        indexCurrentCardLayout = 0;
        initView();
    }
}