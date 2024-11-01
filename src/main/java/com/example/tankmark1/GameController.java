package com.example.tankmark1;

import com.example.tankmark1.map.DesertMap;
import com.example.tankmark1.map.ForestMap;
import com.example.tankmark1.map.Map;
import com.example.tankmark1.map.SnowMap;
import com.example.tankmark1.weapons.Projectile;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.*;

public class GameController extends Pane {
    private int numPlayers;
    private String selectedWeapon;
    private String selectedMap;
    private boolean soundOn;
    private MediaPlayer mediaPlayer;
    public Tank tank1;
    public Tank tank2;
    private ProgressBar healthBar1, healthBar2;
    private Text winnerText;
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
        setUpHealthBars();
        startGame();
    }

    private void setUpHealthBars() {
        healthBar1 = new ProgressBar(1);
        healthBar1.setStyle("-fx-accent: green;");
        healthBar2 = new ProgressBar(1);
        healthBar2.setStyle("-fx-accent: green;");

        HBox healthBars = new HBox(10, healthBar1, healthBar2);
        healthBars.setLayoutX(10);
        healthBars.setLayoutY(10);
        this.getChildren().add(healthBars);

        winnerText = new Text();
        winnerText.setFill(Color.RED);
        winnerText.setLayoutX(350);
        winnerText.setLayoutY(50);
        winnerText.setVisible(false);
        this.getChildren().add(winnerText);
    }

    public void updateHealthBars() {
        healthBar1.setProgress(tank1.getHealth() / 100.0);
        healthBar2.setProgress(tank2.getHealth() / 100.0);
    }

    public void checkForWin() {
        if (tank1.isDestroyed()) {
            winnerText.setText("Player 2 Wins!");
            winnerText.setVisible(true);
        } else if (tank2.isDestroyed()) {
            winnerText.setText("Player 1 Wins!");
            winnerText.setVisible(true);
        }
    }

    private void setUpMap() {
        Map currentMap;
        switch (selectedMap) {
            case "Forest" -> currentMap = new ForestMap();
            case "Desert" -> currentMap = new DesertMap();
            default -> currentMap = new SnowMap();
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
        startProjectileThread();
    }

    private void startProjectileThread() {
        new Thread(() -> {
            while (true) {
                Platform.runLater(this::updateProjectiles);
                try {
                    Thread.sleep(16); // Update approximately 60 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

    private void updateProjectiles() {
        for (Projectile projectile : new ArrayList<>(projectiles)) {
            projectile.move();

            if (tank1 != null && checkCollision(projectile, tank1) && projectile.getOwner() != tank1) {
                tank1.takeDamage(projectile.getDamage());
                updateHealthBars();
                removeProjectile(projectile);
                checkForWin();
            }

            if (tank2 != null && checkCollision(projectile, tank2) && projectile.getOwner() != tank2) {
                tank2.takeDamage(projectile.getDamage());
                updateHealthBars();
                removeProjectile(projectile);
                checkForWin();
            }

            if (isOutOfBounds(projectile)) {
                removeProjectile(projectile);
            }
        }
    }

    private boolean checkCollision(Projectile projectile, Tank tank) {
        Rectangle projectileBounds = new Rectangle(projectile.getX(), projectile.getY(), projectile.getFitWidth(), projectile.getFitHeight());
        Rectangle tankBounds = new Rectangle(tank.getX(), tank.getY(), tank.getFitWidth(), tank.getFitHeight());
        return projectileBounds.getBoundsInParent().intersects(tankBounds.getBoundsInParent());
    }

    private boolean isOutOfBounds(Projectile projectile) {
        return projectile.getX() < 0 || projectile.getY() < 0 || projectile.getX() > getWidth() || projectile.getY() > getHeight();
    }

    private void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
        getChildren().remove(projectile);
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
        movementThread.setDaemon(true);
        movementThread.start();
    }

    private void moveTanks() {
        if (tank1 != null) {
            if (pressedKeys.contains(KeyCode.W)) updateTankPosition(tank1, 0, -1);
            if (pressedKeys.contains(KeyCode.S)) updateTankPosition(tank1, 0, 1);
            if (pressedKeys.contains(KeyCode.A)) updateTankPosition(tank1, -1, 0);
            if (pressedKeys.contains(KeyCode.D)) updateTankPosition(tank1, 1, 0);
            if (pressedKeys.contains(KeyCode.SPACE)) tank1.shoot(this);
        }
        if (tank2 != null) {
            if (pressedKeys.contains(KeyCode.UP)) updateTankPosition(tank2, 0, -1);
            if (pressedKeys.contains(KeyCode.DOWN)) updateTankPosition(tank2, 0, 1);
            if (pressedKeys.contains(KeyCode.LEFT)) updateTankPosition(tank2, -1, 0);
            if (pressedKeys.contains(KeyCode.RIGHT)) updateTankPosition(tank2, 1, 0);
            if (pressedKeys.contains(KeyCode.ENTER)) tank2.shoot(this);
        }
    }

    private void updateTankPosition(Tank tank, double dx, double dy) {
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

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
        Platform.runLater(() -> getChildren().add(projectile));
    }
}
