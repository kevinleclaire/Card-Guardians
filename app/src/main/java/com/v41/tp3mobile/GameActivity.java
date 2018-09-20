package com.v41.tp3mobile;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.plattysoft.leonids.ParticleSystem;
import java.util.ArrayList;
import java.util.LinkedList;

public class GameActivity extends AppCompatActivity {

    private final int NB_MAX_CARD_IN_HAND = 6;
    private final int NB_CARD_STARTING_HAND = 3;

    private LinearLayout cards;
    private LayoutInflater card;

    private Properties properties;

    private CardsVisualizer handVisualiser;

    private View currentDraggingView;
    private View currentClickedCard;
    private Card currentCardDragged;

    private Deck currentPlayingDeck;
    private Deck currentDeckOnBoard;
    private LinkedList<Card> hand;

    private ArrayList<View> cardTargets;
    private boolean[] cardTargetsCanAttack;

    // Boss dependent variables
    private ImageView background;
    private ImageView sliderBackground;
    private ImageView bossImage;

    private ImageView bossHearthImage;
    private TextView txtBossHP;
    private int bossHealthPoints;

    private TextView txtBossName;
    private String bossName;

    private Boss currentBoss;

    private Animation longShake;
    private Animation shake;
    private Animation[] jumpOnBoss;
    private Animation jumpOnPlayer;
    private Animation[] jumpOnTarget;
    private Animation die;
    private Animation canAttack;

    private boolean playerCanDrag;
    private boolean canClickOnEndTurnButton;

    private TextView txtPlayerHP;
    private ImageView imgPlayerHP;
    private int playerHP;

    private int playerGoldAmount;
    private ImageView goldImage;
    private TextView goldText;

    private final int STARTING_GOLD_AMOUNT = 300;
    private final int GOLD_AMOUNT_PER_TURN = 100;
    private final int STARTING_HP_PLAYER = 25;

    private SoundManager soundManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        if (intent.hasExtra("BOSS")) {
            currentBoss = (Boss)intent.getSerializableExtra("BOSS");
        }

        properties = ((Properties) this.getApplication());
        soundManager = new SoundManager(this);

        hand = new LinkedList<Card>();
        currentPlayingDeck = properties.getPlayerInfo().getPlayingDeck();
        currentDeckOnBoard = currentPlayingDeck.clone();

        drawNewHand();

        handVisualiser = new CardsVisualizer(NB_CARD_STARTING_HAND,this);

        cardTargets = new ArrayList<View>();
        cardTargets.add(findViewById(R.id.target_1));
        cardTargets.add(findViewById(R.id.target_2));
        cardTargets.add(findViewById(R.id.target_3));
        cardTargets.add(findViewById(R.id.target_4));

        card = LayoutInflater.from(this);

        background = findViewById(R.id.background);
        sliderBackground = findViewById(R.id.scroller_bg);

        bossHearthImage = findViewById(R.id.hearth);
        bossImage = findViewById(R.id.boss_img);
        txtBossName = findViewById(R.id.boss_name);
        bossName = currentBoss.getName();
        txtBossHP = findViewById(R.id.boss_health);
        bossHealthPoints = currentBoss.getDefense();

        longShake = AnimationUtils.loadAnimation(this, R.anim.longshake);
        shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        jumpOnPlayer = AnimationUtils.loadAnimation(this, R.anim.jumponplayer);
        die = AnimationUtils.loadAnimation(this, R.anim.die);
        canAttack = AnimationUtils.loadAnimation(this, R.anim.canattack);

        jumpOnBoss = new Animation[4];
        jumpOnBoss[0] = AnimationUtils.loadAnimation(this,R.anim.jumponboss4);
        jumpOnBoss[1] = AnimationUtils.loadAnimation(this,R.anim.jumponboss1);
        jumpOnBoss[2] = AnimationUtils.loadAnimation(this,R.anim.jumponboss2);
        jumpOnBoss[3] = AnimationUtils.loadAnimation(this,R.anim.jumponboss3);

        jumpOnTarget = new Animation[4];
        jumpOnTarget[0] = AnimationUtils.loadAnimation(this,R.anim.jumpontarget2);
        jumpOnTarget[1] = AnimationUtils.loadAnimation(this,R.anim.jumpontarget1);
        jumpOnTarget[2] = AnimationUtils.loadAnimation(this,R.anim.jumpontarget4);
        jumpOnTarget[3] = AnimationUtils.loadAnimation(this,R.anim.jumpontarget3);

        cardTargetsCanAttack = new boolean[cardTargets.size()];

        txtPlayerHP = findViewById(R.id.health_txt);
        imgPlayerHP = findViewById(R.id.health_img);
        playerHP = STARTING_HP_PLAYER;
        txtPlayerHP.setText(": " + playerHP);

        goldImage = findViewById(R.id.gold_img);
        goldText = findViewById(R.id.gold_txt);
        playerGoldAmount = STARTING_GOLD_AMOUNT;
        goldText.setText(": " + playerGoldAmount);
        ResetPlayerActions();
        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }


    private void initView() {

        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.parseColor("#000000"));

        cards = findViewById(R.id.id_gallery);
        initHand();

        bossImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentClickedCard != null){

                    cardTargetsCanAttack[cardTargets.indexOf(currentClickedCard)] = false;

                    final View attackingCard = currentClickedCard;
                    UnsetCurrentClickedCard();

                    CardAttackManager(attackingCard);

                }
            }
        });

        View.OnDragListener onDragCardListener = new View.OnDragListener(){

            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DROP) {

                    if (playerGoldAmount >= currentCardDragged.getMana()) {

                        if (currentCardDragged.getType().equals("minion") && v.getAlpha() == 0) {

                            handVisualiser.copyCardInfosOnOtherView(v, currentCardDragged);
                            v.setAlpha(1);
                            v.startAnimation(canAttack);


                            hand.remove(currentCardDragged);
                            cards.removeView(currentDraggingView);
                            GivePlayerGoldPieces(-currentCardDragged.getMana());

                        }
                        else if(currentCardDragged.getType().equals("buff") && v.getAlpha() == 1){

                            handVisualiser.MergeCardInfosOnOtherView(v, currentCardDragged);

                            hand.remove(currentCardDragged);
                            cards.removeView(currentDraggingView);
                            GivePlayerGoldPieces(-currentCardDragged.getMana());

                        }

                        soundManager.playCardPlacingSound();
                    }
                    else{

                        PlayCantAffordAnimation();
                    }
                }
                return true;
            }
        };

        View.OnClickListener onClickCardListener = new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                if(v.getAlpha() != 0 && cardTargetsCanAttack[cardTargets.indexOf(v)] == true) {

                    if (currentClickedCard != null) {
                        UnsetCurrentClickedCard();
                    }

                    setCurrentClickedCard(v);
                }
            }
        };

        for (int i=0;i<cardTargets.size();i++) {
            cardTargets.get(i).setOnDragListener(onDragCardListener);
            cardTargets.get(i).setOnClickListener(onClickCardListener);

            cardTargets.get(i).setAlpha(0);
        }

        bossImage.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DROP) {

                    if (playerGoldAmount >= currentCardDragged.getMana()) {

                        if (currentCardDragged.getType().equals("spell")) {

                            DamageBoss(currentCardDragged.getAttack());

                            hand.remove(currentCardDragged);
                            cards.removeView(currentDraggingView);
                            GivePlayerGoldPieces(-currentCardDragged.getMana());

                            soundManager.playCardPlacingSound();
                        }
                    }
                    else{

                        PlayCantAffordAnimation();
                    }
                }
                return true;
            }
        });

        // Boss dependent variables
        background.setBackground(getResources().getDrawable(getResources().getIdentifier(currentBoss.getBackgroundImgPath(), "drawable", getPackageName())));
        bossImage.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(currentBoss.getImgPath(), "drawable", getPackageName())));
        sliderBackground.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(currentBoss.getSliderImgPath(), "drawable", getPackageName())));

        soundManager.playFightMusic(currentBoss.getMusicPath());

        txtBossHP.setText(Integer.toString(bossHealthPoints));
        txtBossName.setText(bossName);
    }

    private void CardAttackManager(final View attackingCard) {

        TextView cardSpecial = attackingCard.findViewById(R.id.tv_cardSpecial);
        TextView cardAttack = attackingCard.findViewById(R.id.tv_cardAttack);

        if(cardSpecial.getText().equals("double")) {
            jumpOnBoss[cardTargets.indexOf(attackingCard)].setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {soundManager.playCardHitSound();}
                @Override
                public void onAnimationRepeat(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    attackingCard.startAnimation(jumpOnBoss[cardTargets.indexOf(attackingCard)]);
                    jumpOnBoss[cardTargets.indexOf(attackingCard)].setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {soundManager.playCardHitSound();}
                        @Override
                        public void onAnimationRepeat(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }});
                }});
            attackingCard.startAnimation(jumpOnBoss[cardTargets.indexOf(attackingCard)]);
            DamageBoss(Integer.parseInt(cardAttack.getText().toString()));
            DamageBoss(Integer.parseInt(cardAttack.getText().toString()));
        }
        else if(cardSpecial.getText().equals("healer")) {
            soundManager.playCardHitSound();
            attackingCard.startAnimation(jumpOnBoss[cardTargets.indexOf(attackingCard)]);
            DamageBoss(Integer.parseInt(cardAttack.getText().toString()));
            playerHP += Integer.parseInt(cardAttack.getText().toString());
            txtPlayerHP.setText(": " + playerHP);
        }
        else {
            soundManager.playCardHitSound();
            attackingCard.startAnimation(jumpOnBoss[cardTargets.indexOf(attackingCard)]);
            DamageBoss(Integer.parseInt(cardAttack.getText().toString()));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.stopFightMusic();
    }

    private void PlayCantAffordAnimation() {
        goldImage.startAnimation(shake);
        goldText.startAnimation(shake);
    }

    private void DamageBoss(int damage) {

        bossHealthPoints -= damage;
        bossHearthImage.startAnimation(shake);

        if (bossHealthPoints <= 0) {

            txtBossHP.setText("0");
            bossHealthPoints = 0;

            EndGame();
        }
        else{

            txtBossHP.setText(String.valueOf(bossHealthPoints));
        }

        GivePlayerGoldPieces(50);
    }

    private void DamagePlayer(int damage){

        playerHP -= damage;
        imgPlayerHP.startAnimation(shake);
        txtPlayerHP.startAnimation(shake);

        if (playerHP <= 0) {

            txtPlayerHP.setText("0");
            playerHP = 0;

            EndGame();
        }
        else{

            txtPlayerHP.setText(String.valueOf(playerHP));
        }
    }

    private void setCurrentClickedCard(View v){
        v.setScaleX(1.1f);
        v.setScaleY(1.1f);
        v.startAnimation(longShake);
        currentClickedCard = v;

        bossImage.setAnimation(longShake);
        txtBossName.setAnimation(longShake);
        txtBossHP.startAnimation(longShake);
        bossHearthImage.setAnimation(longShake);

    }

    private void UnsetCurrentClickedCard(){

        if(currentClickedCard != null) {

            currentClickedCard.setScaleX(1);
            currentClickedCard.setScaleY(1);
            currentClickedCard.clearAnimation();

            if(cardTargetsCanAttack[cardTargets.indexOf(currentClickedCard)]){
                currentClickedCard.startAnimation(canAttack);
            }

            currentClickedCard = null;

            bossImage.clearAnimation();
            txtBossName.clearAnimation();
            txtBossHP.clearAnimation();
            bossHearthImage.clearAnimation();
        }
    }

    private void drawNewHand() {
        currentDeckOnBoard.shuffleDeck();
        for(int i = 0;i<NB_CARD_STARTING_HAND;i++)
        {
            hand.add(currentDeckOnBoard.getCard(i));
            currentDeckOnBoard.getCards().remove(hand.get(i));
        }

    }

    private void refreshHand() {
        cards.removeAllViews();
        handVisualiser = new CardsVisualizer(hand.size(),this);
        initHand();
    }

    private void initHand() {

        for (int i = 0; i < hand.size(); i++) {

            final View view = card.inflate(R.layout.card_layout, cards, false);
            handVisualiser.initCardVisualizer(i,view,hand.get(i));

            final Card currentCard = hand.get(i);
            view.setOnLongClickListener(new View.OnLongClickListener() {

                public boolean onLongClick(View v) {

                    if(playerCanDrag) {
                        ClipData.Item item = new ClipData.Item((String) v.getTag());
                        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                        ClipData data = new ClipData((String) v.getTag(), mimeTypes, item);
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);

                        v.startDrag(data, shadowBuilder, null, 0);

                        currentDraggingView = v;
                        currentCardDragged = currentCard;

                        UnsetCurrentClickedCard();
                    }

                    return true;
                }
            });
            cards.addView(view);
        }
    }

    public void onClickEndTurnButton(View view){

        if(canClickOnEndTurnButton) {

            drawCard();
            DoBossTurn();
        }
    }

    public void drawCard() {
        if(currentDeckOnBoard.getCards().size() > 0) {
            Card cardPicked = currentDeckOnBoard.pickRandomCard();
            currentDeckOnBoard.getCards().remove(cardPicked);
            if (hand.size() < NB_MAX_CARD_IN_HAND) {
                hand.add(cardPicked);
                refreshHand();
            }
        }
        else {
            currentDeckOnBoard = currentPlayingDeck.clone();
            currentDeckOnBoard.shuffleDeck();
        }
    }

    private void ResetPlayerActions() {

        for(int i=0;i<cardTargetsCanAttack.length;i++){
            cardTargetsCanAttack[i] = true;
            cardTargets.get(i).startAnimation(canAttack);
        }

        playerCanDrag = true;
        canClickOnEndTurnButton = true;
    }

    private void DisablePlayerActions(){

        for(int i=0;i<cardTargetsCanAttack.length;i++){
            cardTargetsCanAttack[i] = false;
        }

        playerCanDrag = false;
        canClickOnEndTurnButton = false;

    }

    private void GivePlayerGoldPieces(int amount){
        playerGoldAmount += amount;
        goldText.setText(": "+ playerGoldAmount);
        goldText.startAnimation(AnimationUtils.loadAnimation(this,R.anim.blink));
        goldImage.startAnimation(AnimationUtils.loadAnimation(this,R.anim.blink));
    }

    private void DoBossTurn() {

        DisablePlayerActions();

        Boolean cardHasTaunt = false;
        int tauntTarget = 0;

        for (int i = 0; i<cardTargets.size();i++) {
            TextView special = cardTargets.get(i).findViewById(R.id.tv_cardSpecial);
            if(special.getText().equals("taunt"))
            {
                cardHasTaunt = true;
                tauntTarget = i;
            }
        }

        if(cardHasTaunt == false) {
            final int MAX_TARGET = 5;
            int randomTarget = (int) (Math.random() * (MAX_TARGET + 1));

            if (randomTarget >= 5) {
                BossUsesUltimate();
            } else if (randomTarget >= 4) {
                BossDamagesPlayer(currentBoss.getAttack());
            } else if (cardTargets.get(randomTarget).getAlpha() != 0) {
                TextView special = cardTargets.get(randomTarget).findViewById(R.id.tv_cardSpecial);
                if(!special.getText().toString().equals("hidden")) {
                    BossDamagesCard(currentBoss.getAttack(), randomTarget);
                }
                else{
                    BossDamagesPlayer(currentBoss.getAttack());
                }
            } else {
                BossDamagesPlayer(currentBoss.getAttack());
            }
        }
        else {
            BossDamagesCard(currentBoss.getAttack(), tauntTarget);
        }

    }

    private void BossUsesUltimate() {
        final ConstraintLayout gameLayout = findViewById(R.id.main_layout);

        soundManager.playBossUltimateSound();

        Animation bossUltimateAnim = AnimationUtils.loadAnimation(this, R.anim.bossultimate);
        bossUltimateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                gameLayout.startAnimation(longShake);
                txtBossName.setAlpha(0);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                gameLayout.clearAnimation();
                txtBossName.setAlpha(1);
                GivePlayerGoldPieces(GOLD_AMOUNT_PER_TURN);

                DamagePlayer(currentBoss.getAttack()/2);
                for(int i=0;i<cardTargets.size();i++) {

                    if(cardTargets.get(i).getAlpha() != 0) {
                        DamageCardTarget(currentBoss.getAttack() / 2, i);
                    }

                }

                ResetPlayerActions();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        bossImage.startAnimation(bossUltimateAnim);
        txtBossHP.startAnimation(bossUltimateAnim);
        bossHearthImage.startAnimation(bossUltimateAnim);
    }

    private void BossDamagesCard(int damage, int index) {

        AnimateBoss(jumpOnTarget[index]);
        DamageCardTarget(damage, index);
        showRedParticles(cardTargets.get(index));
        soundManager.playBossHitSound(currentBoss.getHitSoundPath());
    }

    private void DamageCardTarget(int damage, int index) {

        TextView cardDef = cardTargets.get(index).findViewById(R.id.tv_cardDefense);
        TextView cardSpecial = cardTargets.get(index).findViewById(R.id.tv_cardSpecial);

        if(cardSpecial.getText().equals("thorns")) {
            if(Integer.parseInt(cardDef.getText().toString()) > 1) {
                DamageBoss(Integer.parseInt(cardDef.getText().toString()) / 2);
            }
            else {
                DamageBoss(Integer.parseInt(cardDef.getText().toString()));
            }

        }

        int cardHealthPoints = Integer.valueOf(cardDef.getText().toString()) - damage;

        if (cardHealthPoints <= 0){
            cardDef.setText("0");

            PlayCardDeath(cardTargets.get(index));
        }
        else{
            cardDef.setText(Integer.toString(cardHealthPoints));
            cardDef.setTextColor(0xffd81717);
            cardTargets.get(index).startAnimation(shake);
        }
    }

    private void PlayCardDeath(final View cardTarget) {
        die.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                cardTarget.setAlpha(0);
            }
        });
        ResetCardTargetStats(cardTarget);
        cardTarget.startAnimation(die);
        cardTarget.setAlpha(0);
    }

    private void ResetCardTargetStats(View cardTarget) {
        TextView temp = cardTarget.findViewById(R.id.tv_cardSpecial);
        temp.setText("");
        temp = cardTarget.findViewById(R.id.tv_cardDefense);
        temp.setTextColor(0xFF000000);
        temp = cardTarget.findViewById(R.id.tv_cardAttack);
        temp.setTextColor(0xFF000000);
    }

    private void BossDamagesPlayer(Integer damage) {
        AnimateBoss(jumpOnPlayer);
        DamagePlayer(damage);
        soundManager.playBossHitSound(currentBoss.getHitSoundPath());
    }

    private void AnimateBoss(Animation animation) {

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                DisablePlayerActions();
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ResetPlayerActions();
                GivePlayerGoldPieces(GOLD_AMOUNT_PER_TURN);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        bossImage.startAnimation(animation);
        txtBossName.startAnimation(animation);
        txtBossHP.startAnimation(animation);
        bossHearthImage.startAnimation(animation);
    }

    private void EndGame() {

        //https://www.mkyong.com/android/android-custom-dialog-example/
        final Dialog endGameDialog = new Dialog(this);
        endGameDialog.setContentView(R.layout.dialog_end_game);

        final Button okButton = endGameDialog.findViewById(R.id.Ok_button);
        ImageView bossImg = endGameDialog.findViewById(R.id.boss_img);
        TextView title = endGameDialog.findViewById(R.id.title);
        ImageView background = endGameDialog.findViewById(R.id.background);

        title.setText(title.getText() + " " + bossName);

        bossImg.setBackground(getResources().getDrawable(getResources().getIdentifier(currentBoss.getRoundImgPath(), "drawable", getPackageName())));

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(bossHealthPoints == 0) {

                     if(currentBoss.getId() >= properties.getPlayerInfo().getLevel()) {
                         properties.getPlayerInfo().setLevel(currentBoss.getId() + 1);
                         properties.updatePlayerLevel(currentBoss.getId() + 1);
                     }

                     intent = new Intent(getBaseContext(), CardWonActivity.class);
                }
                else {
                     intent = new Intent(getBaseContext(), MainActivity.class);
                }
                startActivity(intent);
            }
        });

        if (bossHealthPoints == 0){
            // Player Victory

            AnimateBoss(die);
            endGameDialog.show();

        }
        else{
            // Player Defeat

            title.setText(bossName + " " + getString(R.string.text_defeat));
            okButton.setText("Back To Main Menu");
            background.setBackground(getResources().getDrawable(getResources().getIdentifier("defeat", "drawable", getPackageName())));
            endGameDialog.show();
        }

    }

    private void showRedParticles(View anchorView) {
        new ParticleSystem(this, 1000, R.drawable.img_red_particle, 500)
                .setSpeedRange(0.2f, .5f)
                .oneShot(anchorView, 40);
    }

    public void onClickSurrenderButton(View v) {

       EndGame();
    }
}