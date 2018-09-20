package com.v41.tp3mobile;

import android.content.Context;
import android.media.MediaPlayer;
import java.util.Random;

public class SoundManager {

    private Context context;

    private MediaPlayer introMusicPlayer;
    private MediaPlayer bossSelectMusicPlayer;
    private MediaPlayer tradeMusicPlayer;

    private MediaPlayer fightMusicPlayer;
    private MediaPlayer bossUltimateSoundPlayer;
    private MediaPlayer bossHitSoundPlayer;

    private MediaPlayer buttonClickPlayer;
    private MediaPlayer cardPlacingPlayer;
    private MediaPlayer cardHitPlayer;


    public SoundManager(Context context) {
        this.context = context;

        introMusicPlayer = new MediaPlayer();
        bossSelectMusicPlayer = new MediaPlayer();
        tradeMusicPlayer = new MediaPlayer();

        fightMusicPlayer = new MediaPlayer();
        bossUltimateSoundPlayer = new MediaPlayer();
        bossHitSoundPlayer = new MediaPlayer();

        buttonClickPlayer = new MediaPlayer();
        cardPlacingPlayer = new MediaPlayer();
        cardHitPlayer = new MediaPlayer();
    }

    public void playButtonClickSound() {
        buttonClickPlayer.reset();
        buttonClickPlayer = MediaPlayer.create(context, R.raw.sound_buttonclick);
        buttonClickPlayer.setLooping(false);
        buttonClickPlayer.setVolume(0.5f, 0.5f);
        buttonClickPlayer.start();
    }

    public void playFightButtonClickSound() {
        buttonClickPlayer.reset();
        buttonClickPlayer = MediaPlayer.create(context, R.raw.sound_fight_buttonclick);
        buttonClickPlayer.setLooping(false);
        buttonClickPlayer.start();
    }

    public void playBossUltimateSound() {
        bossUltimateSoundPlayer.reset();
        bossUltimateSoundPlayer = MediaPlayer.create(context, R.raw.sound_boss_ultimate);
        bossUltimateSoundPlayer.setLooping(false);
        bossUltimateSoundPlayer.start();
    }

    public void playIntroMusic() {
        introMusicPlayer = MediaPlayer.create(context, R.raw.sound_mainmenu_music);
        introMusicPlayer.setVolume(0.4f, 0.4f);
        introMusicPlayer.start();
    }

    public void playBossSelectMusic() {
        bossSelectMusicPlayer = MediaPlayer.create(context, R.raw.sound_bossselection_music);
        bossSelectMusicPlayer.setVolume(0.4f, 0.4f);
        bossSelectMusicPlayer.start();
    }

    public void stopBossSelectMusic() {
        bossSelectMusicPlayer.stop();
        bossSelectMusicPlayer.release();
    }

    public void playTradeMusic() {
        tradeMusicPlayer = MediaPlayer.create(context, R.raw.sound_trade_music);
        tradeMusicPlayer.setVolume(0.4f, 0.4f);
        tradeMusicPlayer.start();
    }

    public void stopTradeMusic() {
        tradeMusicPlayer.stop();
        tradeMusicPlayer.release();
    }

    public void playBossHitSound(String path) {
        bossHitSoundPlayer.reset();
        bossHitSoundPlayer = MediaPlayer.create(context, context.getResources().getIdentifier(path, "raw", context.getPackageName()));
        bossHitSoundPlayer.setLooping(false);
        bossHitSoundPlayer.start();
    }

    public void playFightMusic(String path) {
        fightMusicPlayer = MediaPlayer.create(context, context.getResources().getIdentifier(path, "raw", context.getPackageName()));
        fightMusicPlayer.setVolume(0.4f, 0.4f);
        fightMusicPlayer.start();
    }

    public void stopFightMusic() {
        fightMusicPlayer.stop();
        fightMusicPlayer.release();
    }

    public void stopIntroMusic() {
        introMusicPlayer.stop();
        introMusicPlayer.release();
    }

    public void playCardPlacingSound() {
        Random random = new Random();
        int rnd = random.nextInt(4) + 1;

        switch (rnd) {
            case 1:
                cardPlacingPlayer = MediaPlayer.create(context, R.raw.sound_card_place_1);
                break;
            case 2:
                cardPlacingPlayer = MediaPlayer.create(context, R.raw.sound_card_place_2);
                break;
            case 3:
                cardPlacingPlayer = MediaPlayer.create(context, R.raw.sound_card_place_3);
                break;
            case 4:
                cardPlacingPlayer = MediaPlayer.create(context, R.raw.sound_card_place_4);
                break;
        }

        cardPlacingPlayer.start();
    }

    public void playCardHitSound() {
        Random random = new Random();
        int rnd = random.nextInt(2) + 1;

        cardHitPlayer.reset();

        switch (rnd) {
            case 1:
                cardHitPlayer = MediaPlayer.create(context, R.raw.sound_card_hit_1);
                break;
            case 2:
                cardHitPlayer = MediaPlayer.create(context, R.raw.sound_card_hit_2);
                break;
        }

        cardHitPlayer.setLooping(false);
        cardHitPlayer.start();
    }

    public void resetAllSounds() {
        bossUltimateSoundPlayer.reset();
        bossHitSoundPlayer.reset();

        buttonClickPlayer.reset();
        cardPlacingPlayer.reset();
        cardHitPlayer.reset();

    }
}
