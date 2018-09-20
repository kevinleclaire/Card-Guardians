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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BossSelectionActivity extends AppCompatActivity {

    private LinearLayout bossesGallery;
    private ArrayList<Boss> bosses;

    private Boss selectedBoss;

    private Animation shake;

    private Properties properties;

    private SoundManager soundManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_boss_selection);

        soundManager = new SoundManager(this);
        properties = ((Properties) this.getApplication());

        shake = AnimationUtils.loadAnimation(this,R.anim.shake);

        Intent intent = getIntent();
        if (intent.hasExtra("BOSS_LIST")) {
            bosses = (ArrayList<Boss>)intent.getSerializableExtra("BOSS_LIST");
        }

        initView();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();

    }

    @Override
    protected void onResume() {
        super.onResume();
        soundManager.playBossSelectMusic();
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundManager.stopBossSelectMusic();
    }

    private void initView() {

        bossesGallery = findViewById(R.id.id_gallery);

        for (int i = 0; i < bosses.size(); i++) {

            View bossView = LayoutInflater.from(this).inflate(R.layout.boss_layout, bossesGallery, false);

            TextView bossName = bossView.findViewById(R.id.boss_name);
            ImageView bossImg = bossView.findViewById(R.id.boss_img);

            if (bosses.get(i).getId() <= properties.getPlayerInfo().getLevel()){

                bossImg.setImageDrawable(getResources().getDrawable(getResources().getIdentifier(bosses.get(i).getRoundImgPath(), "drawable", getPackageName())));
                bossName.setText(bosses.get(i).getName());

                final Boss indexBoss = bosses.get(i);
                bossView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(selectedBoss != null) {
                            UnselectBossView(bossesGallery.getChildAt(selectedBoss.getId()));
                        }
                        selectedBoss = indexBoss;
                        SelectBossView(view);
                        view.startAnimation(shake);
                    }
                });
            }
            else{

                bossImg.setImageDrawable(getDrawable(R.drawable.round_boss_locked));
                bossName.setText("Locked");
            }


            bossesGallery.addView(bossView);
        }
    }

    public void onClickFightButton(View view) {

        if(selectedBoss != null) {
            if(properties.getPlayerInfo().getPlayingDeck() != null) {
                if (properties.getPlayerInfo().getPlayingDeck().getCards().size() == 20) {
                    Intent intent = new Intent(this, GameActivity.class);

                    intent.putExtra("BOSS", selectedBoss);

                    startActivity(intent);
                } else {
                    Toast.makeText(this, "You need at least 20 cards in your playing deck", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "You need to set your playing deck", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Please, select a boss", Toast.LENGTH_LONG).show();
        }
        soundManager.playFightButtonClickSound();
    }

    private void SelectBossView(View bossView){
        bossView.findViewById(R.id.boss_name).setBackgroundColor(Color.parseColor(selectedBoss.getcolor()));
    }

    private void UnselectBossView(View bossView){
        bossView.findViewById(R.id.boss_name).setBackgroundColor(Color.WHITE);
        bossView.clearAnimation();
    }

    public void onClickMainMenuButton(View view) {
        soundManager.playButtonClickSound();
        finish();
    }
}
