package com.example.tankmark1;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.File;


public class GameController extends Pane {
    private int numPlayers;
    private String selectedWeapon;
    private String selectedMap;
    private boolean soundOn;
    private MediaPlayer mediaPlayer;
    public Tank tank1;
    public Tank tank2;

    public GameController(int numPlayers, String selectedWeapon, String selectedMap, boolean soundOn) {
        this.numPlayers = numPlayers;
        this.selectedWeapon = selectedWeapon;
        this.selectedMap = selectedMap;
        this.soundOn = soundOn;

        setUpMap();
        setUpTanks();
        startGame();
    }

    private void setUpMap() {
        Map currentMap;
        if (selectedMap.equals("Forest")) {
            currentMap = new ForestMap();
        } else if (selectedMap.equals("Desert")) {
            currentMap = new DesertMap();
        } else {
            currentMap = new SnowMap();
        }
        this.getChildren().add(currentMap);
    }

    private void setUpTanks() {
        if (numPlayers >= 1) {
            tank1 = new Tank(100, 100, "tank1.png", selectedWeapon);
            this.getChildren().add(tank1);
        }
        if (numPlayers >= 2) {

            tank2 = new Tank(700, 500, "tank2.png", selectedWeapon);
            this.getChildren().add(tank2);
        }
    }

    public void startGame() {
        // Initialize game loop, event handlers, etc.
        if (soundOn) {
            playBackgroundMusic();

        }

        // Add key event handlers
        this.setOnKeyPressed(this::handleKeyPressed);
       // this.setOnKeyReleased(this::handleKeyReleased);

        // Request focus so key events are captured
        this.setFocusTraversable(true);
        this.requestFocus();
    }


    private void handleKeyPressed(KeyEvent event) {
        KeyCode code = event.getCode();
        System.out.println("Key pressed: " + event.getCode());
        if (tank1 != null) {
            // Player 1 Controls (WASD)
            if (code == KeyCode.W) tank1.move(0, -1); // Up
            if (code == KeyCode.S) tank1.move(0, 1);  // Down
            if (code == KeyCode.A) tank1.move(-1, 0); // Left
            if (code == KeyCode.D) tank1.move(1, 0);  // Right
        }
        if (tank2 != null) {
            // Player 2 Controls (Arrow Keys)
            if (code == KeyCode.UP) tank2.move(0, -1);   // Up
            if (code == KeyCode.DOWN) tank2.move(0, 1);  // Down
            if (code == KeyCode.LEFT) tank2.move(-1, 0); // Left
            if (code == KeyCode.RIGHT) tank2.move(1, 0); // Right
        }
    }


    private void playBackgroundMusic() {
        if(mediaPlayer==null) {
            String musicFilePath = getClass().getResource("/background.mp3").toExternalForm(); // Adjust path if needed
            Media sound = new Media(musicFilePath);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the music
        }
        mediaPlayer.play();
    }
}

