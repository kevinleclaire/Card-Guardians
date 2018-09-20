package com.v41.tp3mobile;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int NB_MAX_CARDS_IN_DECK = 20;

    private TextView tv_tap;
    private TextView[] tv_buttonTexts;
    private ImageView[] iv_buttons;
    private ImageView iv_background2;

    private Properties properties;

    private ArrayList<Boss> bossList;

    private SoundManager soundManager;
    private boolean isCollectionButtonClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        properties = ((Properties) this.getApplication());

        initView();

        startBlinkingAnimation();

        properties.setAppli();

        properties.setAllCards(generateCardList("cardlist.json"));
        bossList = generateBossList("bosslist.json");

        setPlayerInfo();

        //If he has never played we set his first deck
        if (properties.retrievePlayerInfo() == null) {
            initiatePlayerInfo();
        }

        soundManager = new SoundManager(this);
    }

    private void initView() {
        tv_buttonTexts = new TextView[3];
        iv_buttons = new ImageView[3];
        tv_tap = findViewById(R.id.tv_tap);
        iv_background2 = findViewById(R.id.iv_background2);

        isCollectionButtonClicked = false;

        tv_buttonTexts[0] = findViewById(R.id.tv_play);
        tv_buttonTexts[1] = findViewById(R.id.tv_collection);
        tv_buttonTexts[2] = findViewById(R.id.tv_trade);

        iv_buttons[0] = findViewById(R.id.btn_play);
        iv_buttons[1] = findViewById(R.id.btn_collection);
        iv_buttons[2] = findViewById(R.id.btn_trade);

        //Make them invisible when first launching the game
        for (TextView tv_buttonText : tv_buttonTexts) {
            tv_buttonText.setVisibility(View.INVISIBLE);
        }
        for (ImageView iv_button : iv_buttons) {
            iv_button.setVisibility(View.INVISIBLE);
        }

        tv_tap.setVisibility(View.VISIBLE);
        iv_background2.setVisibility(View.INVISIBLE);
    }

    private void startBlinkingAnimation() {
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the time of the blink with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        tv_tap.startAnimation(anim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ((Properties)this.getApplication()).destroyAppli();
        System.gc();
    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.resetAllSounds();
        soundManager.playIntroMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isCollectionButtonClicked) {
            soundManager.stopIntroMusic();
        }
    }

    public void onClickPlayButton(View view) {
        isCollectionButtonClicked = false;

        Intent intent = new Intent(this, BossSelectionActivity.class);

        intent.putExtra("BOSS_LIST", bossList);

        startActivity(intent);
        soundManager.playButtonClickSound();
    }

    public void onClickDeckButton(View view) {
        isCollectionButtonClicked = true;

        Intent intent = new Intent(this, DeckManagementActivity.class);

        startActivity(intent);
        soundManager.playButtonClickSound();
    }

    public void onClickTradeButton(View view) {
        isCollectionButtonClicked = false;

        Intent intent = new Intent(this, TradeActivity.class);

        startActivity(intent);
        soundManager.playButtonClickSound();
    }

    @Nullable
    private ArrayList<Card> generateCardList(String fileName) {

        try {
            Gson gson = new Gson();

            JSONObject obj = new JSONObject(loadJSONFromAsset(fileName));
            JSONArray m_jArry = obj.getJSONArray("cardList");

            Type collectionType = new TypeToken<List<Card>>(){}.getType();
            return gson.fromJson(m_jArry.toString(), collectionType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    private ArrayList<Boss> generateBossList(String fileName) {

        try {
            Gson gson = new Gson();

            JSONObject obj = new JSONObject(loadJSONFromAsset(fileName));
            JSONArray m_jArry = obj.getJSONArray("bossList");

            Type collectionType = new TypeToken<List<Boss>>(){}.getType();
            return gson.fromJson(m_jArry.toString(), collectionType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = this.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void setPlayerInfo(){
        if (properties.retrievePlayerInfo() != null) {
            properties.playerInfo = properties.retrievePlayerInfo().get(0);
            properties.playerInfo.setCards(properties.retrieveAllCards());
            properties.playerInfo.setDecks(properties.retrieveAllDecks());
            LinkedList<DeckHasCard> deckCardAssociation = properties.retrieveDeckHasCard();

            for (DeckHasCard deckHasCard : deckCardAssociation) {
                for (Deck deck : properties.playerInfo.getDecks()) {
                    if (deckHasCard.getDeckId() == deck.getId()) {
                        for (Card card : properties.playerInfo.getCards()) {
                            if (deckHasCard.getCardId() == card.getId()) {
                                deck.addCard(card);
                            }
                        }
                    }
                }
            }

            for (Deck deck : properties.getPlayerInfo().getDecks()) {
                if(properties.getPlayerInfo().getPlayingDeck() == null) {
                    if (deck.getCards().size() == NB_MAX_CARDS_IN_DECK) {
                        properties.getPlayerInfo().setPlayingDeck(deck);
                        break;
                    }
                }
            }

        }
    }

    private void initiatePlayerInfo() {

        LinkedList<DeckHasCard> deckHasCards = new LinkedList<>();

        LinkedList<Card> basicCards = new LinkedList<>();
        for (int i = 0; i<NB_MAX_CARDS_IN_DECK;i++) {
            basicCards.add(properties.getAllCards().get(i));
        }

        Deck deck = new Deck(1, basicCards, "Basic Deck");

        properties.playerInfo.setDecks(new LinkedList<Deck>());
        properties.playerInfo.addDeck(deck);
        properties.playerInfo.setCards(basicCards);
        properties.playerInfo.setId(1);
        properties.playerInfo.setHasAlreadyPlayed(1);
        properties.playerInfo.setLevel(1);

        for (Card card : properties.playerInfo.getCards()) {
            properties.createCard(card);

            deckHasCards.add(new DeckHasCard(deck.getId(), card.getId()));
        }

        for (Deck deck1 : properties.playerInfo.getDecks()) {
            properties.createDeck(deck1);
        }

        for (DeckHasCard deckHasCard : deckHasCards) {
            properties.createDeckHasCard(deckHasCard);
        }

        properties.getPlayerInfo().setPlayingDeck(properties.getPlayerInfo().getDecks().get(0));

        properties.createPlayerInfo(properties.playerInfo);
    }

    public void onClickBackgroundImage(View view) {
        showButtonsMenu();
    }

    private void showButtonsMenu() {
        for (TextView tv_buttonText : tv_buttonTexts) {
            tv_buttonText.setVisibility(View.VISIBLE);
        }

        for (ImageView iv_button : iv_buttons) {
            iv_button.setVisibility(View.VISIBLE);
        }

        tv_tap.clearAnimation();
        tv_tap.setVisibility(View.INVISIBLE);
        iv_background2.setVisibility(View.VISIBLE);
    }

}
