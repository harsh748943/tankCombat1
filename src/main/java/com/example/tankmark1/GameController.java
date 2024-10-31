package com.example.tankmark1;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.io.File;
import java.util.*;


import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;

import java.util.HashSet;
import java.util.Set;

public class GameController extends Pane {
    private int numPlayers;
    private String selectedWeapon;
    private String selectedMap;
    private boolean soundOn;
    private MediaPlayer mediaPlayer;
    public Tank tank1;
    public Tank tank2;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private Thread movementThread;

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
        if (soundOn) {
            playBackgroundMusic();
        }

        this.setOnKeyPressed(this::handleKeyPressed);
        this.setOnKeyReleased(this::handleKeyReleased);

        this.setFocusTraversable(true);
        this.requestFocus();

        startMovementThread();
    }

    private void handleKeyPressed(KeyEvent event) {
        pressedKeys.add(event.getCode());
    }

    private void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());
    }

    private void startMovementThread() {
        movementThread = new Thread(() -> {
            while (true) {
                moveTanks();
                try {
                    Thread.sleep(16); // Roughly 60 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        movementThread.setDaemon(true); // Daemon thread will not prevent the application from exiting
        movementThread.start();
    }

    private void moveTanks() {
        if (tank1 != null) {
            if (pressedKeys.contains(KeyCode.W)) {
                updateTankPosition(tank1, 0, -1); // Up
            }
            if (pressedKeys.contains(KeyCode.S)) {
                updateTankPosition(tank1, 0, 1);  // Down
            }
            if (pressedKeys.contains(KeyCode.A)) {
                updateTankPosition(tank1, -1, 0); // Left
            }
            if (pressedKeys.contains(KeyCode.D)) {
                updateTankPosition(tank1, 1, 0);  // Right
            }

            if (pressedKeys.contains(KeyCode.SPACE)) {
                tank1.shoot(this);  // Right
            }
        }
        if (tank2 != null) {
            if (pressedKeys.contains(KeyCode.UP)) {
                updateTankPosition(tank2, 0, -1);   // Up
            }
            if (pressedKeys.contains(KeyCode.DOWN)) {
                updateTankPosition(tank2, 0, 1);  // Down
            }
            if (pressedKeys.contains(KeyCode.LEFT)) {
                updateTankPosition(tank2, -1, 0); // Left
            }
            if (pressedKeys.contains(KeyCode.RIGHT)) {
                updateTankPosition(tank2, 1, 0); // Right
            }
            if (pressedKeys.contains(KeyCode.ENTER)) {
                tank2.shoot(this);  // Right
            }
        }
        // Update projectiles' positions
        for (Projectile projectile : new ArrayList<>(projectiles)) {
            // Assuming projectiles move upwards; adjust as necessary
            projectile.move(); // Change direction if needed

            // Check if the projectile goes out of bounds
            if (projectile.getY() < 0 || projectile.getX() < 0 || projectile.getX() > getWidth()) {
                // Remove projectile safely on the JavaFX Application Thread
                Platform.runLater(() -> {
                    projectiles.remove(projectile); // Remove from the list
                    getChildren().remove(projectile); // Remove from the UI
                });
            }
        }
    }

    private void updateTankPosition(Tank tank, double dx, double dy) {
        // Use Platform.runLater to safely update the UI
        Platform.runLater(() -> tank.move(dx, dy));
    }

    private void playBackgroundMusic() {
        if (mediaPlayer == null) {
            String musicFilePath = getClass().getResource("/background.mp3").toExternalForm();
            Media sound = new Media(musicFilePath);
            mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        mediaPlayer.play();
    }
    // In GameController class
    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);

        // Use Platform.runLater to ensure this runs on the JavaFX Application Thread
        Platform.runLater(() -> getChildren().add(projectile));
    }

}
