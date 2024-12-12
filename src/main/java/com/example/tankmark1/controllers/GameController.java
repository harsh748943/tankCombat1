package com.example.tankmark1.controllers;

import com.example.tankmark1.*;
import com.example.tankmark1.map.*;
import com.example.tankmark1.tanks.Tank;
import com.example.tankmark1.weapons.Projectile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;

public class GameController extends Pane {
    private int numPlayers;
    private String selectedWeapon;
    private String selectedMap;
    public GameSoundManager soundManager;
    private boolean soundOn;
    private String level;
    private TankGame mainApp;  // Reference to the main application
    public ArrayList<Tank>tanks;


    private Set<KeyCode> pressedKeys = new HashSet<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<DestructibleObject> destructibleObjects = new ArrayList<>();
    public boolean gameOver[] = {false};
    private Text countdownText;  // Countdown text display ke liye
    public boolean countdownComplete = false;  // Game tabhi start hoga jab countdown complete hoga
    private Thread movementThread;
    private List<Tank> allTanks=new ArrayList<>();
    HealthController healthController;
    MapController mapController;
    TankController tankController;
    ProjectileController projectileController;

    public GameController(int numPlayers, String selectedWeapon, String selectedMap, boolean soundOn, TankGame mainApp,String level) {
        this.numPlayers = numPlayers;
        this.selectedWeapon = selectedWeapon;
        this.selectedMap = selectedMap;
        this.soundOn = soundOn;
        this.mainApp = mainApp;  // Assign mainApp reference
        this.level=level;
        this.tanks=new ArrayList<>();
        soundManager = new GameSoundManager();
         mapController=new MapController(this,selectedMap,destructibleObjects);

        mapController.setUpMap();
        healthController=new HealthController(this,tanks, soundManager,movementThread,mainApp,gameOver);
        healthController.setUpHealthBars();
        projectileController=new ProjectileController(projectiles,this,destructibleObjects,tanks,healthController);
        tankController=new TankController(this,selectedWeapon,numPlayers,level,destructibleObjects,this,tanks,pressedKeys,projectileController);

        tankController.setUpTanks();


        startCountdown();
    }

    private void startCountdown() {

        countdownText = new Text();
        countdownText.setFont(new Font(50));
        countdownText.setFill(Color.RED);
        countdownText.setLayoutX(750); // Countdown text ko center mein rakhne ke liye
        countdownText.setLayoutY(400);
        countdownText.setStyle(
                "-fx-font-size: 48px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #FF5722; " +  // Bright orange color
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 10, 0.5, 0, 2); " +
                        "-fx-background-color: rgba(255, 255, 255, 0.3); " +  // Further reduced opacity for background
                        "-fx-background-radius: 15; " +  // Rounded corners for the box
                        "-fx-border-color: #FF5722; " +  // Border color matching the text color
                        "-fx-border-width: 4; " +  // Border width
                        "-fx-padding: 20; " +  // Padding inside the box
                        "-fx-alignment: center; " // Text centered inside the box
        );

        this.getChildren().add(countdownText);

        // Play tuk tuk sound at each step of the countdown
        Timeline countdown = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    countdownText.setText("3");
                    soundManager.playTukTuk();  // Play tuk tuk sound
                }),
                new KeyFrame(Duration.seconds(2), event -> {
                    countdownText.setText("2");
                }),
                new KeyFrame(Duration.seconds(3), event -> {
                    countdownText.setText("1");
                }),
                new KeyFrame(Duration.seconds(4), event -> {
                    countdownText.setLayoutX(620); // Countdown text ko center mein rakhne ke liye
                    countdownText.setLayoutY(400);
                    countdownText.setText("Let's Attack!!");  // Display "आक्रमण"
         soundManager.stopTukTuk();
                    Timeline removeText = new Timeline(
                            new KeyFrame(Duration.seconds(1), e -> {
                                this.getChildren().remove(countdownText); // Remove countdown text after 1 second
                                countdownComplete = true; // Mark countdown as complete
                                startGame(); // Start the game
                            })
                    );
                    removeText.setCycleCount(1);
                    removeText.play();
                })
        );
        countdown.setCycleCount(1);
        countdown.play();
    }


    public void startGame() {
        if (soundOn) {
            soundManager.playBackgroundMusic();
        }

        this.setOnKeyPressed(this::handleKeyPressed);
        this.setOnKeyReleased(this::handleKeyReleased);

        this.setFocusTraversable(true);
        this.requestFocus();
        if (countdownComplete) {  // Countdown complete hone ke baad hi threads start honge
            startMovementThread();
            projectileController.startProjectileThread();
        }
    }


    private void handleKeyPressed(KeyEvent event) {
        pressedKeys.add(event.getCode());
        if (event.getCode() == KeyCode.R) {

            // Toggle shield for Tank1

            tanks.get(0).getShieldCircle().setVisible(!tanks.get(0).getShieldCircle().isVisible());
            tanks.get(0).activateShield();
        } else if (event.getCode() == KeyCode.U) {

            // Toggle shield for Tank2

            tanks.get(1).getShieldCircle().setVisible(!tanks.get(1).getShieldCircle().isVisible());
            tanks.get(1).activateShield();

        }
    }

    private void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());

        if (event.getCode() == KeyCode.R) {
            tanks.get(0).deactivateShield(); // Example: Player 1 deactivates the shield with 'R'
        }
        if (event.getCode() == KeyCode.U) {
            tanks.get(1).deactivateShield();  // Example: Player 2 deactivates the shield with 'R'
        }
    }

    private void startMovementThread() {
        movementThread = new Thread(() -> {
            while (true) {
                if (countdownComplete) {  // Only move tanks after countdown is complete
                    tankController.moveTanks();
                    tanks.get(0).updateShieldStatus();  // Check if shield needs to be deactivated
                    tanks.get(1).updateShieldStatus();
                }
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
}