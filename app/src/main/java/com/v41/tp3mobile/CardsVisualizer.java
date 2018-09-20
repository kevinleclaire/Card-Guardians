package com.v41.tp3mobile;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CardsVisualizer {

    private Activity activity;

    private View[] cardLayouts;

    private TextView[] tv_cardIds;
    private TextView[] tv_cardNames;
    private TextView[] tv_cardManas;
    private TextView[] tv_cardAttacks;
    private TextView[] tv_cardDefenses;
    private TextView[] tv_cardSpecials;
    private TextView[] tv_cardTypes;
    private ImageView[] iv_cardImgs;
    private ImageView[] iv_cardRaritys;

    public CardsVisualizer(int nbOfCards, Activity activity) {

        this.activity = activity;

        cardLayouts = new View[nbOfCards];
        tv_cardIds = new TextView[nbOfCards];
        tv_cardAttacks = new TextView[nbOfCards];
        tv_cardNames = new TextView[nbOfCards];
        tv_cardDefenses = new TextView[nbOfCards];
        tv_cardSpecials = new TextView[nbOfCards];
        tv_cardManas = new TextView[nbOfCards];
        tv_cardTypes = new TextView[nbOfCards];
        iv_cardImgs = new ImageView[nbOfCards];
        iv_cardRaritys = new ImageView[nbOfCards];

    }

    public void initCardVisualizer(int index, View view, Card card)
    {
        ImageView iv_cardBackground = view.findViewById(R.id.iv_cardBackground);

        cardLayouts[index] = activity.findViewById(R.id.cardLayout);
        tv_cardIds[index] = view.findViewById(R.id.tv_cardId);
        tv_cardNames[index] = view.findViewById(R.id.tv_cardName);
        tv_cardSpecials[index] = view.findViewById(R.id.tv_cardSpecial);
        tv_cardAttacks[index] = view.findViewById(R.id.tv_cardAttack);
        tv_cardDefenses[index] = view.findViewById(R.id.tv_cardDefense);
        tv_cardManas[index] = view.findViewById(R.id.tv_cardMana);
        tv_cardTypes[index] = view.findViewById(R.id.tv_cardType);
        iv_cardImgs[index] = view.findViewById(R.id.iv_cardImg);
        iv_cardRaritys[index] = view.findViewById(R.id.iv_cardRarity);

        tv_cardIds[index].setText(card.getId().toString());
        tv_cardNames[index].setText(card.getName());
        tv_cardSpecials[index].setText(card.getSpecial());
        tv_cardAttacks[index].setText(card.getAttack().toString());
        tv_cardDefenses[index].setText(card.getDefense().toString());
        tv_cardManas[index].setText(card.getMana().toString());
        tv_cardTypes[index].setText(card.getType());
        iv_cardImgs[index].setImageResource(activity.getResources().getIdentifier(card.getImgPath(), "drawable", activity.getPackageName()));
        iv_cardRaritys[index].setImageResource(activity.getResources().getIdentifier(card.getRarityPathFromRarity(), "drawable", activity.getPackageName()));
        view.setVisibility(View.VISIBLE);

        if(card.getType().equals("buff")){
            iv_cardBackground.setBackground(activity.getResources().getDrawable(activity.getResources().getIdentifier("img_cardlayout_buff", "drawable", activity.getPackageName())));
        }
        else if(card.getType().equals("spell")){
            iv_cardBackground.setBackground(activity.getResources().getDrawable(activity.getResources().getIdentifier("img_cardlayout_spell", "drawable", activity.getPackageName())));
        }
    }

    public void copyCardInfosOnOtherView(View cardOnBoard, Card cardToCopy) {
        ImageView iv_cardBackground = cardOnBoard.findViewById(R.id.iv_cardBackground);

        TextView tv_cardName = cardOnBoard.findViewById(R.id.tv_cardName);
        TextView tv_cardId = cardOnBoard.findViewById(R.id.tv_cardId);
        TextView tv_cardAttack = cardOnBoard.findViewById(R.id.tv_cardAttack);
        TextView tv_cardDefense = cardOnBoard.findViewById(R.id.tv_cardDefense);
        TextView tv_cardMana = cardOnBoard.findViewById(R.id.tv_cardMana);
        TextView tv_cardSpecial = cardOnBoard.findViewById(R.id.tv_cardSpecial);
        TextView tv_cardType = cardOnBoard.findViewById(R.id.tv_cardType);
        ImageView iv_cardRarity = cardOnBoard.findViewById(R.id.iv_cardRarity);
        ImageView iv_cardImg = cardOnBoard.findViewById(R.id.iv_cardImg);

        tv_cardName.setText(cardToCopy.getName());
        tv_cardId.setText(cardToCopy.getId().toString());
        tv_cardAttack.setText(cardToCopy.getAttack().toString());
        tv_cardDefense.setText(cardToCopy.getDefense().toString());
        tv_cardMana.setText(cardToCopy.getMana().toString());
        tv_cardSpecial.setText(cardToCopy.getSpecial());
        tv_cardType.setText(cardToCopy.getType());
        iv_cardImg.setImageResource(activity.getResources().getIdentifier(cardToCopy.getImgPath(), "drawable", activity.getPackageName()));
        iv_cardRarity.setImageResource(activity.getResources().getIdentifier(cardToCopy.getRarityPathFromRarity(), "drawable", activity.getPackageName()));

        if(cardToCopy.getType().equals("buff")){
            iv_cardBackground.setBackground(activity.getResources().getDrawable(activity.getResources().getIdentifier("img_cardlayout_buff", "drawable", activity.getPackageName())));
        }
        else if(cardToCopy.getType().equals("spell")){
            iv_cardBackground.setBackground(activity.getResources().getDrawable(activity.getResources().getIdentifier("img_cardlayout_spell", "drawable", activity.getPackageName())));
        }
    }

    public void MergeCardInfosOnOtherView(View cardOnBoard, Card cardToMerge) {

        TextView tv_cardAttack = cardOnBoard.findViewById(R.id.tv_cardAttack);
        TextView tv_cardDefense = cardOnBoard.findViewById(R.id.tv_cardDefense);
        TextView tv_cardSpecial = cardOnBoard.findViewById(R.id.tv_cardSpecial);

        int baseAttack = Integer.parseInt(tv_cardAttack.getText().toString());
        int baseDefense = Integer.parseInt(tv_cardDefense.getText().toString());

        String baseSpecial = tv_cardSpecial.getText().toString();
        tv_cardAttack.setText(Integer.toString(cardToMerge.getAttack() + Integer.parseInt(tv_cardAttack.getText().toString())));
        tv_cardDefense.setText(Integer.toString(cardToMerge.getDefense() + Integer.parseInt(tv_cardDefense.getText().toString())));
        if (!cardToMerge.getSpecial().equals("buff") && !cardToMerge.getSpecial().equals("")) {
            tv_cardSpecial.setText(cardToMerge.getSpecial());
        }

        if(Integer.parseInt(tv_cardAttack.getText().toString()) > baseAttack) {
            tv_cardAttack.setTextColor(0xffcb00ff);
        }
        if(Integer.parseInt(tv_cardDefense.getText().toString()) > baseDefense) {
            tv_cardDefense.setTextColor(0xffcb00ff);
        }
        if(!tv_cardSpecial.getText().equals(baseSpecial)) {
            tv_cardSpecial.setTextColor(0xffcb00ff);
        }
    }

    public View[] getCardLayouts() {
        return cardLayouts;
    }

    public TextView[] getTv_cardNames() {
        return tv_cardNames;
    }

}
