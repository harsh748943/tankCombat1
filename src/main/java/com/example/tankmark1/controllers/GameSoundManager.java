package com.example.tankmark1.controllers;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameSoundManager {
    private MediaPlayer mediaPlayer;
    private  MediaPlayer tukTukSound;

    public void loadSounds() {
        this.mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/music/background.mp3").toExternalForm()));
        //this.tukTukSound = new AudioClip(getClass().getResource("/tukTuk.mp3").toExternalForm());
    }




    public void playBackgroundMusic() {
        if (mediaPlayer == null) {
            String musicFilePath = getClass().getResource("/music/background.mp3").toExternalForm();
            Media sound = new Media(musicFilePath);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        mediaPlayer.play();
    }

    public void playTukTuk() {
        if (tukTukSound == null) {
            String musicFilePath = getClass().getResource("/music/tuktuk.mp3").toExternalForm();
            Media sound = new Media(musicFilePath);
            tukTukSound = new MediaPlayer(sound);
            tukTukSound.setCycleCount(MediaPlayer.INDEFINITE);
        }
        tukTukSound.play();
    }

    public void stopMusic(){
        mediaPlayer.stop();
    }

    public void stopTukTuk(){
        tukTukSound.stop();
    }
}
