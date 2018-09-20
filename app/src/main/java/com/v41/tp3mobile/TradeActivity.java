package com.v41.tp3mobile;

import android.content.ClipData;
import android.content.ClipDescription;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class TradeActivity extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback {

    Card cardDraged;

    private View cardTarget;

    private Card[] currentCards;

    private LinearLayout cardGallery;
    private LayoutInflater cardInflater;

    private HorizontalScrollView hsv_playerCards;
    private int indexCurrentCardLayout = 0;


    private Properties properties;

    private CardsVisualizer cardsVisualizer;

    private SoundManager soundManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_trade);
        properties = ((Properties) this.getApplication());

        soundManager = new SoundManager(this);

        setHorizontalScrollViews();

        int nb_Card = properties.getPlayerInfo().getCards().size();

        cardsVisualizer = new CardsVisualizer(nb_Card,this);

        currentCards = new Card[nb_Card];

        cardInflater = LayoutInflater.from(this);

        initView();

        hsv_playerCards.getBackground().setAlpha(80);

        NfcAdapter mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            Toast.makeText(this, "Sorry this device does not have NFC.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!mAdapter.isEnabled()) {
            Toast.makeText(this, "Please enable NFC via Settings.", Toast.LENGTH_LONG).show();
        }

        mAdapter.setNdefPushMessageCallback(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();

    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.playTradeMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.stopTradeMusic();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("CARDS_VIEW_SCROLL_X", hsv_playerCards.getScrollX());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        hsv_playerCards.scrollTo(savedInstanceState.getInt("CARDS_VIEW_SCROLL_X"), 0);
    }

    private void initView() {
        cardGallery = findViewById(R.id.id_gallery_cards);
        cardTarget = findViewById(R.id.target_1);

        for (int i = 0; i < properties.getPlayerInfo().getCards().size(); i++) {
            View cardView = cardInflater.inflate(R.layout.card_layout, cardGallery, false);
            createList(i, cardView);

            cardGallery.addView(cardView);
        }

        View.OnDragListener onDragCardListener = new View.OnDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {

                if(event.getAction() == DragEvent.ACTION_DROP) {

                    cardsVisualizer.copyCardInfosOnOtherView(v, cardDraged);

                    v.setAlpha(1);

                    soundManager.playCardPlacingSound();
                }
                return true;
            }
        };

        cardTarget.setOnDragListener(onDragCardListener);
        cardTarget.setAlpha(0);
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
        indexCurrentCardLayout++;
    }

    private void setHorizontalScrollViews() {
        hsv_playerCards = findViewById(R.id.hsv_playerCards);

        if (properties.getPlayerCardsScrollView() != 0) {
            hsv_playerCards.scrollTo(properties.getPlayerCardsScrollView(), properties.getPlayerCardsScrollView());
        }
    }

    private void removeCard(Card card) {
        properties.deleteCard(card);
        properties.deleteDeckhasCard_where_Card_ID(card);
        properties.getPlayerInfo().getCards().remove(card);
    }

    @Override
    public NdefMessage createNdefMessage(NfcEvent nfcEvent) {
        if (cardDraged != null) {
            String message = serializeCard(cardDraged);
            NdefRecord ndefRecord = NdefRecord.createMime("text/plain", message.getBytes());
            NdefMessage ndefMessage = new NdefMessage(ndefRecord);

            removeCard(cardDraged);

            finish();
            startActivity(getIntent());

            return ndefMessage;
        }
        else {
            Toast.makeText(this, "Please place a card to give.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public String serializeCard(Object card) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

            return mapper.writeValueAsString(card);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void onClickTarget(View view) {
        cardTarget.setAlpha(0);
    }

    public void onClickReturnButton(View view) {
        soundManager.playButtonClickSound();
        finish();
    }
}



