package com.v41.tp3mobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.LinkedList;
import java.util.Random;

public class CardWonActivity extends AppCompatActivity {

    private Animation longshake;

    private CardsVisualizer card;

    private Card cardToAdd;
    private Properties properties;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_nfc_read);
        longshake = AnimationUtils.loadAnimation(this,R.anim.longshake);

        card = new CardsVisualizer(1,this);

        properties = ((Properties) this.getApplication());
        cardToAdd = pickNewRandomCard();
        addCardToDB();

        card.getCardLayouts()[0] = findViewById(R.id.cardLayout);
        card.initCardVisualizer(0,card.getCardLayouts()[0],cardToAdd);
        card.getCardLayouts()[0].startAnimation(longshake);

        card.getCardLayouts()[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getScaleX() > 1) {
                    view.setScaleY(1);
                    view.setScaleX(1);
                }
                else {
                    view.setScaleY(2);
                    view.setScaleX(2);
                }

            }
        });
    }

    private void addCardToDB() {
        properties.createCard(cardToAdd);
        properties.getPlayerInfo().addCard(cardToAdd);
    }

    private Card pickNewRandomCard()
    {
        Random rand = new Random();
        int rarityRandomPick = rand.nextInt(100);
        int currentRarity = 0;
        LinkedList<Card> rarityPickedCard = new LinkedList<>();

        if(rarityRandomPick <= 50) {
            //common 50%
            currentRarity = 1;
        }
        else if(rarityRandomPick > 50 && rarityRandomPick <= 70) {
            //uncommon 20%
            currentRarity = 2;
        }
        else if(rarityRandomPick > 70 && rarityRandomPick <= 85) {
            //rare 15%
            currentRarity = 3;
        }
        else if(rarityRandomPick > 85 && rarityRandomPick <= 95) {
            //epic 10%
            currentRarity = 4;
        }
        else{
            //legendary 5%
            currentRarity = 5;
        }

        for (Card card : properties.getAllCards()) {
            if(card.getRarity().equals(String.valueOf(currentRarity))) {
                rarityPickedCard.add(card);
            }
        }

        return rarityPickedCard.get(rand.nextInt(rarityPickedCard.size()));
    }

    public void onClickReturnButton(View view) {
        Intent intent = new Intent(getBaseContext(), MainActivity.class);

        startActivity(intent);
    }
}