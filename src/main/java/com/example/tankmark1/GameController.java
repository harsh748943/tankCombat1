package com.example.tankmark1;

import com.example.tankmark1.map.*;
import com.example.tankmark1.map.Map;
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
    private TankGame mainApp;
    private Tank tank1;
    private Tank tank2;
    private ProgressBar healthBar1, healthBar2;
    private Text healthText1, healthText2;
    private Text winnerText;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private Thread movementThread;
    private List<Boundary> boundaries; // Add this field
    private Thread projectileThread;

    public GameController(int numPlayers, String selectedWeapon, String selectedMap, boolean soundOn, TankGame mainApp) {
        this.numPlayers = numPlayers;
        this.selectedWeapon = selectedWeapon;
        this.selectedMap = selectedMap;
        this.soundOn = soundOn;
        this.mainApp = mainApp;

        setUpMap();
        setUpTanks();
        setUpHealthBars();
        startGame();
    }

    private void setUpHealthBars() {
        healthBar1 = createHealthBar();
        healthText1 = new Text("100%");

        HBox player1HealthBox = new HBox(5, healthBar1, healthText1);
        player1HealthBox.setLayoutX(10);
        player1HealthBox.setLayoutY(10);

        healthBar2 = createHealthBar();
        healthText2 = new Text("100%");

        HBox player2HealthBox = new HBox(5, healthText2, healthBar2);
        player2HealthBox.setLayoutX(650);
        player2HealthBox.setLayoutY(10);

        getChildren().addAll(player1HealthBox, player2HealthBox);
        winnerText = createWinnerText();
        getChildren().add(winnerText);
    }

    private ProgressBar createHealthBar() {
        ProgressBar healthBar = new ProgressBar(1);
        healthBar.setStyle("-fx-accent: green;");
        return healthBar;
    }

    private Text createWinnerText() {
        Text winnerText = new Text();
        winnerText.setFill(Color.RED);
        winnerText.setLayoutX(350);
        winnerText.setLayoutY(50);
        winnerText.setVisible(false);
        return winnerText;
    }

    public void updateHealthBars() {
        healthBar1.setProgress(tank1.getHealth() / 100.0);
        healthText1.setText((int) tank1.getHealth() + "%");
        healthBar2.setProgress(tank2.getHealth() / 100.0);
        healthText2.setText((int) tank2.getHealth() + "%");
    }

    private void checkForWin() {
        if (tank1.isDestroyed()) {
            endGame("Player 2 Wins!");
        } else if (tank2.isDestroyed()) {
            endGame("Player 1 Wins!");
        }
    }

    private void endGame(String winnerMessage) {
        interruptThreads();
        stopBackgroundMusic();
        displayWinnerMessage(winnerMessage);
        disableControls();
    }

    private void interruptThreads() {
        if (movementThread != null && movementThread.isAlive()) {
            movementThread.interrupt();
        }
        if (projectileThread != null && projectileThread.isAlive()) {
            projectileThread.interrupt();
        }
    }

    private void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }

    private void displayWinnerMessage(String winnerMessage) {
        Platform.runLater(() -> {
            winnerText.setText(winnerMessage + " Press Enter to exit.");
            winnerText.setVisible(true);
            setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    mainApp.returnToMenu();
                }
            });
        });
    }

    private void disableControls() {
        setOnKeyPressed(null);
        setOnKeyReleased(null);
    }

    private void setUpMap() {
        Map currentMap;
        switch (selectedMap) {
            case "Forest" -> currentMap = new ForestMap();
            case "Desert" -> currentMap = new DesertMap();
            default -> currentMap = new SnowMap();
        }
        boundaries = currentMap.getBoundaries(); // Initialize boundaries
        getChildren().add(currentMap);
    }

    private void setUpTanks() {
        if (numPlayers >= 1) {
            tank1 = new Tank(100, 100, "tank1.png", selectedWeapon);
            getChildren().add(tank1);
        }
        if (numPlayers >= 2) {
            tank2 = new Tank(700, 500, "tank2.png", selectedWeapon);
            getChildren().add(tank2);
        }
    }

    public void startGame() {
        if (soundOn) {
            playBackgroundMusic();
        }

        setOnKeyPressed(this::handleKeyPressed);
        setOnKeyReleased(this::handleKeyReleased);
        setFocusTraversable(true);
        requestFocus();

        startMovementThread();
        startProjectileThread();
    }

    private void startProjectileThread() {
        projectileThread = new Thread(() -> {
            while (true) {
                Platform.runLater(this::updateProjectiles);
                try {
                    Thread.sleep(16); // Update approximately 60 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        projectileThread.setDaemon(true);
        projectileThread.start();
    }

    private void updateProjectiles() {
        for (Projectile projectile : new ArrayList<>(projectiles)) {
            projectile.move();

            // Check collision with tank1, ensuring tank1 is not null
            if (tank1 != null && checkCollision(projectile, tank1) && projectile.getOwner() != tank1) {
                tank1.takeDamage(projectile.getDamage());
                updateHealthBars();
                removeProjectile(projectile);
                checkForWin();
            }

            // Check collision with tank2, ensuring tank2 is not null
            if (tank2 != null && checkCollision(projectile, tank2) && projectile.getOwner() != tank2) {
                tank2.takeDamage(projectile.getDamage());
                updateHealthBars();
                removeProjectile(projectile);
                checkForWin();
            }

            // Remove projectile if out of bounds
            if (isOutOfBounds(projectile)) {
                removeProjectile(projectile);
            }
        }
    }


    private boolean checkCollision(Projectile projectile, Tank tank) {
        if (tank == null) return false; // Add this check

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
        if (tank != null) { // Check that tank is not null before calling move
            Platform.runLater(() -> tank.move(dx, dy, boundaries)); // Pass boundaries to move method
        }
    }


    private void playBackgroundMusic() {
        if (mediaPlayer == null) {
            String musicFilePath = getClass().getResource("/background.mp3").toExternalForm();
            mediaPlayer = new MediaPlayer(new Media(musicFilePath));
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        mediaPlayer.play();
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
        Platform.runLater(() -> getChildren().add(projectile));
    }
}
