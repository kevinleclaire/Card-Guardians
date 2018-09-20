package com.v41.tp3mobile;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;


public class NFCReadActivity extends AppCompatActivity {

    private Animation longshake;

    private String cardJson;

    private CardsVisualizer cardsVisualizer;

    private Card cardToAdd;
    private Properties properties;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nfc_read);

        longshake = AnimationUtils.loadAnimation(this,R.anim.longshake);
        cardsVisualizer = new CardsVisualizer(1,this);
        cardsVisualizer.getCardLayouts()[0] = findViewById(R.id.cardLayout);

        properties = ((Properties) this.getApplication());

    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(
                    NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage message = (NdefMessage) rawMessages[0]; // only one message transferred
            cardJson = new String(message.getRecords()[0].getPayload());
        }

        cardToAdd = deserializeCard();

        if (cardToAdd != null) {

            cardsVisualizer.initCardVisualizer(0,cardsVisualizer.getCardLayouts()[0],cardToAdd);
            cardsVisualizer.getCardLayouts()[0].startAnimation(longshake);
            addCardToDB();

        }
    }

    private Card deserializeCard() {
        try {
            if (cardJson != null && !cardJson.equals("")) {
                Card newCard = new ObjectMapper().readValue(cardJson, Card.class);

                return newCard;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void addCardToDB() {
        properties.createCard(cardToAdd);
        properties.getPlayerInfo().addCard(cardToAdd);
    }

    public void onClickReturnButton(View view) {
        finish();
    }
}