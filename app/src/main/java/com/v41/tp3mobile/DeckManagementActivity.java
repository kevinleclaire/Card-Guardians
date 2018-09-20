package com.v41.tp3mobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class DeckManagementActivity extends AppCompatActivity {

    private LinearLayout deckGallery;
    private LayoutInflater deckInflater;

    private View[] deckLayouts;

    private TextView[] tv_DeckNames;
    private TextView tv_deckNameTopRight;

    private int nbDecks;

    private Properties properties;
    private Button btn_editDeck;
    private Button btn_deleteDeck;
    private Button btn_createDeck;
    private Button btn_setPlayingDeck;

    private EditText deckName;
    private Animation shake;
    private Animation bounce;
    private Animation longshake;


    private int indexDeckClicked;
    private Deck currentDeckClicked;

    private SoundManager soundManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_deck_management);

        deckInflater = LayoutInflater.from(this);
        soundManager = new SoundManager(this);

        shake = AnimationUtils.loadAnimation(this,R.anim.shake);
        bounce = AnimationUtils.loadAnimation(this,R.anim.bounce);
        longshake = AnimationUtils.loadAnimation(this,R.anim.longshake);


        properties = ((Properties) this.getApplication());

        indexDeckClicked = -1;

        deckName = findViewById(R.id.editText_DeckName);
        btn_editDeck = findViewById(R.id.btn_editDeck);
        btn_deleteDeck = findViewById(R.id.btn_deleteDeck);
        btn_createDeck = findViewById(R.id.btn_createDeck);
        btn_setPlayingDeck = findViewById(R.id.btn_playingDeck);
        tv_deckNameTopRight = findViewById(R.id.tv_deckNameTop);


        nbDecks = properties.getPlayerInfo().getDecks().size();

        deckLayouts = new View[nbDecks]; //TODO: change depending on deck clicked
        tv_DeckNames = new TextView[nbDecks];

        if(nbDecks == 5)
        {
            btn_createDeck.setVisibility(View.GONE);
            deckName.setVisibility(View.GONE);
        }

        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();

    }

    private void initView() {
        deckGallery = findViewById(R.id.id_gallery);

        for (int i = 0; i < deckLayouts.length; i++) {

            View deckView = deckInflater.inflate(R.layout.deck_layout, deckGallery, false);
            final Deck deckClicked = properties.getPlayerInfo().getDecks().get(i);
            final int indexDeck = i;

            deckView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    deckButtonVisibilityManaging();
                    tv_deckNameTopRight.setText(deckClicked.getName());
                    indexDeckClicked = indexDeck;
                    currentDeckClicked = deckClicked;
                    view.startAnimation(bounce);
                }
            });

            createDeckList(i, deckView);
            deckGallery.addView(deckView);
        }

    }
    private void createDeckList(int i, View view) {

        deckLayouts[i] = findViewById(R.id.deckLayout);
        tv_DeckNames[i] = view.findViewById(R.id.tv_deckName);

        tv_DeckNames[i].setText(properties.getPlayerInfo().getDecks().get(i).getName());

        if(properties.getPlayerInfo().getDecks().get(i) == properties.getPlayerInfo().getPlayingDeck()) {
            view.startAnimation(longshake);
            tv_DeckNames[i].setTextColor(Color.RED);
        }

        view.setVisibility(View.VISIBLE);
    }

    public void deckButtonVisibilityManaging() {
        btn_editDeck.startAnimation(shake);
        btn_deleteDeck.startAnimation(shake);
        btn_setPlayingDeck.startAnimation(shake);
        btn_editDeck.setBackgroundColor(0xff000000);
        btn_editDeck.setTextColor(0xffffffff);
        btn_deleteDeck.setBackgroundColor(0xff000000);
        btn_deleteDeck.setTextColor(0xffffffff);
        btn_setPlayingDeck.setBackgroundColor(0xffba0e0e);

    }

    public void OnClickBtnDeleteDeck(View view) {
        if(indexDeckClicked != -1) {
            properties.deleteDeck(properties.getPlayerInfo().getDecks().get(indexDeckClicked));
            properties.deleteDeckhasCard_where_Deck_ID(properties.getPlayerInfo().getDecks().get(indexDeckClicked));
            properties.getPlayerInfo().removeDeck(properties.getPlayerInfo().getDecks().get(indexDeckClicked));
            deckButtonVisibilityManaging();
            refresh();
        }
        soundManager.playButtonClickSound();
    }

    public void onClickEditButton(View view) {
        if(indexDeckClicked != -1) {
            Intent intent = new Intent(this, EditDeckActivity.class);

            intent.putExtra("DECK_INDEX", indexDeckClicked);

            startActivity(intent);
        }
        soundManager.playButtonClickSound();

    }

    public void OnClickBtnSetPlayingDeck(View view) {
        view.startAnimation(bounce);
        if (currentDeckClicked != null) {
            if (currentDeckClicked.getCards().size() == 20) {
                properties.getPlayerInfo().setPlayingDeck(currentDeckClicked);
                refresh();
            } else {
                Toast.makeText(this, "You need at least 20 cards in your playing deck", Toast.LENGTH_SHORT).show();
            }
        }
        soundManager.playButtonClickSound();

    }

    public void OnClickBtnCreateDeck(View view) {
        if(!deckName.getText().equals("")) {
            Random rand = new Random();
            Boolean goodId = true;
            int randId = 0;
            do {
                goodId = true;
                randId = rand.nextInt(200);
                for (Deck deck : properties.getPlayerInfo().getDecks()) {
                    if (deck.getId() == randId) {
                        goodId = false;
                    }
                }
            } while (goodId == false);


            Deck deck = new Deck(randId, deckName.getText().toString());
            properties.createDeck(deck);
            properties.getPlayerInfo().addDeck(deck);

            refresh();
        }
        else
        {
            Toast.makeText(this, "Met un nom ", Toast.LENGTH_SHORT).show();
        }
        soundManager.playButtonClickSound();
    }

    public void refresh() {
        finish();
        startActivity(getIntent());
    }

    public void OnClickBtnReturn(View view) {
        soundManager.playButtonClickSound();
        finish();
    }
}
