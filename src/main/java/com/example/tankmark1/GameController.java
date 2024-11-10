package com.example.tankmark1;

import com.example.tankmark1.map.*;
import com.example.tankmark1.map.Map;
import com.example.tankmark1.tanks.ComputerTank;
import com.example.tankmark1.tanks.Tank;
import com.example.tankmark1.weapons.Explosion;
import com.example.tankmark1.weapons.Projectile;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.*;

public class GameController extends Pane {
    private int numPlayers;
    private String selectedWeapon;
    private String selectedMap;
    private GameSoundManager soundManager;
    private boolean soundOn;
    private String level;
    private MediaPlayer mediaPlayer;
    private TankGame mainApp;  // Reference to the main application
    public Tank tank1;
    public Tank tank2;
    private ProgressBar healthBar1, healthBar2;

    private ProgressBar sb1, sb2;
    private Text st1, st2;

    private Text healthText1, healthText2;
    private Text winnerText;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<DestructibleObject> destructibleObjects = new ArrayList<>();
    public boolean gameOver = false;
    private Text countdownText;  // Countdown text display ke liye
    public boolean countdownComplete = false;  // Game tabhi start hoga jab countdown complete hoga
    private Thread movementThread;
    private boolean isRobot=false;
    private ComputerTank computerTank;


    public GameController(int numPlayers, String selectedWeapon, String selectedMap, boolean soundOn, TankGame mainApp,String level) {
        this.numPlayers = numPlayers;
        this.selectedWeapon = selectedWeapon;
        this.selectedMap = selectedMap;
        this.soundOn = soundOn;
        this.mainApp = mainApp;  // Assign mainApp reference
        this.level=level;
        soundManager = new GameSoundManager();

        setUpMap();
        setUpTanks();
        setsb();
        setUpHealthBars();
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
    private void setUpHealthBars() {
        healthBar1 = new ProgressBar(1);
        healthBar1.setStyle("-fx-accent: red;");
        healthText1 = new Text("100%");

        // Position Player 1’s health on the left
        HBox player1HealthBox = new HBox(5, healthBar1, healthText1);
        player1HealthBox.setLayoutX(10);
        player1HealthBox.setLayoutY(10);

        healthBar2 = new ProgressBar(1);
        healthBar2.setStyle("-fx-accent: BLUE;");
        healthText2 = new Text("100%");

        // Position Player 2’s health on the right
        HBox player2HealthBox = new HBox(5, healthText2, healthBar2);
        player2HealthBox.setLayoutX(1400); // Adjust based on scene width
        player2HealthBox.setLayoutY(10);
        this.getChildren().addAll(player1HealthBox,player2HealthBox);

        winnerText = new Text();
        winnerText.setFill(Color.RED);
        winnerText.setLayoutX(350);
        winnerText.setLayoutY(50);
        winnerText.setVisible(false);
        this.getChildren().add(winnerText);
    }

    private void setsb() {
        sb1 = new ProgressBar(1);
        sb1.setStyle("-fx-accent: black;");
        st1 = new Text("100%");

        // Position Player 1’s health on the left
        HBox player1sb = new HBox(5, sb1, st1);
        player1sb.setLayoutX(10);
        player1sb.setLayoutY(10);

        sb2 = new ProgressBar(1);
        sb2.setStyle("-fx-accent: black;");
        st2 = new Text("100%");

        // Position Player 2’s health on the right
        HBox player2sb = new HBox(5, st2, sb2);
        player2sb.setLayoutX(1400); // Adjust based on scene width
        player2sb.setLayoutY(10);
        this.getChildren().addAll(player1sb,player2sb);

        winnerText = new Text();
        winnerText.setFill(Color.RED);
        winnerText.setLayoutX(350);
        winnerText.setLayoutY(50);
        winnerText.setVisible(false);
        this.getChildren().add(winnerText);
    }

    public void updatesb() {
        // Update Player 1 health bar and text
        double s1 = tank1.getHealth();
        sb1.setProgress(s1 / 100.0);
        st1.setText((int) s1 + "%");

        // Update Player 2 health bar and text
        double s2 = tank2.getHealth();
        sb2.setProgress(s2 / 100.0);
       st2.setText((int) s2 + "%");
    }

    public void updateHealthBars() {
        // Update Player 1 health bar and text
        double health1 = tank1.getHealth();
        healthBar1.setProgress(health1 / 100.0);
        healthText1.setText((int) health1 + "%");

        // Update Player 2 health bar and text
        double health2 = tank2.getHealth();
        healthBar2.setProgress(health2 / 100.0);
        healthText2.setText((int) health2 + "%");
    }

    public void checkForWin() {
        if (tank1.isDestroyed()) {
            // Show destroyed image for tank1
            tank1.setImage(new Image("/tankBlast.png"));
            showWinningMessage("Player 2 Wins!","Press Enter to exit.");
            endGame();
        } else if (tank2.isDestroyed()) {
            // Show destroyed image for tank2
            tank2.setImage(new Image("/tankBlast1.png"));
            showWinningMessage("Player 1 Wins!","Press Enter to exit.");
            endGame();
        }
    }
    private void showWinningMessage(String message,String message1) {
        Text winningMessage = new Text(message);
        Text winningMessage1 = new Text(message1);
        winningMessage.setStyle(
                "-fx-font-size: 55px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #000000; " +  // Green color for the text
                        "-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.75), 10, 0.5, 0, 2); " +  // Shadow effect
                        "-fx-background-color: rgba(255, 0, 0, 0.5); " +  // Semi-transparent dark background
                        "-fx-padding: 20; " +
                        "-fx-background-radius: 10; "
        );
        winningMessage1.setStyle(
                "-fx-font-size: 25px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #FF0000;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.75), 10, 0.5, 0, 2); " +  // Shadow effect
                        "-fx-background-color: rgba(255, 0, 0, 0.5); " +  // Semi-transparent dark background
                        "-fx-padding: 20; " +
                        "-fx-background-radius: 10; "
        );

        // Set the position to center
        winningMessage.setX(600);  // X-coordinate for centering
        winningMessage.setY(400);  // Y-coordinate for centering
        winningMessage1.setX(670);  // X-coordinate for centering
        winningMessage1.setY(470);  // Y-coordinate for centering


        // Optional: Add the winning message to the root or main pane
        this.getChildren().add(winningMessage);
        this.getChildren().add(winningMessage1);
    }


    private void endGame() {
        // Set game over flag
        gameOver = true;

        // Stop movement and projectile threads
        if (movementThread != null && movementThread.isAlive()) {
            movementThread.interrupt();
        }

        soundManager.stopMusic();

        // Display winner message and await Enter key to return to menu
        Platform.runLater(() -> {
            // Disable tank controls to prevent further movement
            this.setOnKeyPressed(null);
            this.setOnKeyReleased(null);

            // Set a listener for the Enter key to return to the menu
            this.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    mainApp.returnToMenu();
                }
            });
        });
    }


    private void setUpMap() {
        Map currentMap;
        switch (selectedMap) {
            case "Forest" -> currentMap = new ForestMap();
            case "Desert" -> currentMap = new DesertMap();
            case "Tiles" -> currentMap = new TileMap();
            case "Maze" -> currentMap = new MazeMap();

            default -> currentMap = new SnowMap();
        }
        this.getChildren().add(currentMap);

        // Add destructible objects to the list for collision checking
        if (currentMap instanceof ForestMap forestMap) {
            destructibleObjects.addAll(forestMap.getDestructibleObjects());
        }
        if (currentMap instanceof SnowMap snowMap) {
            destructibleObjects.addAll(snowMap.getDestructibleObjects());
        }
        if (currentMap instanceof TileMap tileMap) {
            destructibleObjects.addAll(tileMap.getDestructibleObjects());
        }

        if (currentMap instanceof MazeMap mazeMap) {
            destructibleObjects.addAll(mazeMap.getDestructibleObjects());
        }
    }


    private void setUpTanks() {
        tank1 = new Tank(100, 100, "tank.png", selectedWeapon);
        this.getChildren().add(tank1);
        this.getChildren().add(tank1.getShieldCircle()); // Assuming you have a getter for shieldCircle


            tank2 = new Tank(1300, 700, "tank3.png", selectedWeapon);
            if(numPlayers>1) {
                this.getChildren().add(tank2);
                this.getChildren().add(tank2.getShieldCircle());

            }
            else {
                isRobot=true;
                computerTank=new ComputerTank(tank2,tank1,this,level,destructibleObjects);
                computerTank.setComputerControlled();

                this.getChildren().add(tank2);
            }
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
            startProjectileThread();
        }
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


            // Check for collisions with other projectiles
            for (Projectile other : new ArrayList<>(projectiles)) {
                if (projectile != other && projectile.collidesWith(other)) {
                    // Create an explosion at the point of collision
                    // If you find that the explosion is misaligned, adjust like this:
                    double adjustedX = projectile.getX() - (128 / 2);  // Center the explosion
                    double adjustedY = projectile.getY() - (128 / 2); // Center the explosion
                    Explosion explosion = new Explosion(
                            adjustedX, adjustedY,
                            "/explosionL1.png",
                            "/explosionSound.mp3",
                            8,
                            7,
                            256,
                           256,256,256,
                            this, true
                    );
                    Platform.runLater(() -> getChildren().add(explosion));


                    // Remove both projectiles on collision
                    removeProjectile(projectile);
                    removeProjectile(other);
                    break;
                }
            }



            for (DestructibleObject destructible : new ArrayList<>(destructibleObjects)) {
                if (checkCollisionForObject(projectile, destructible)) {
                    destructible.takeDamage(projectile.getDamage());


                    createExplosion(projectile);

                    // Remove projectile after collision
                    removeProjectile(projectile);

                    // Remove destructible object from list and scene if destroyed
                    if (destructible.getHealth() <= 0) {
                        removeDestructibleObject(destructible);
                    }

                    break;
                }
            }




            if (tank1 != null && checkCollision(projectile, tank1) && projectile.getOwner() != tank1) {
                if (tank1.isShieldActive()) {
                    // Reduce shield strength instead of health
                    tank1.takeDamageToS(projectile.getDamage());
                    if (tank1.getShieldStrength() <= 0) {
                        tank1.deactivateShield(); // Deactivate shield if its strength reaches zero
                    }
                } else {
                    // If shield is not active, apply damage directly to health
                    tank1.takeDamageToS(projectile.getDamage());
                }
                updatesb();
                updateHealthBars();
                removeProjectile(projectile);
                checkForWin();

                createExplosion(projectile);
            }

            if (tank2 != null && checkCollision(projectile, tank2) && projectile.getOwner() != tank2) {
                if (tank2.isShieldActive()) {
                    // Reduce shield strength instead of health
                    tank2.takeDamageToS(projectile.getDamage());
                    if (tank2.getShieldStrength() <= 0) {
                        tank2.deactivateShield(); // Deactivate shield if its strength reaches zero
                    }
                } else {
                    // If shield is not active, apply damage directly to health
                    tank2.takeDamageToS(projectile.getDamage());
                }
                updatesb();
                updateHealthBars();
                removeProjectile(projectile);
                checkForWin();

               createExplosion(projectile);
            }


            if (isOutOfBounds(projectile)) {
                removeProjectile(projectile);
            }
        }
    }


    private void createExplosion(Projectile projectile) {
        double adjustedX = projectile.getX() - projectile.getExplosionFrameWidth()/2;
        double adjustedY = projectile.getY() - projectile.getExplosionFrameHeight()/2;
        Explosion explosion = new Explosion(
                adjustedX, adjustedY,
                projectile.getExplosionImagePath(),
                projectile.getExplosionSoundPath(),
                projectile.getExplosionFrameColomn(),
                projectile.getExplosionFrameRow(),
                projectile.getExplosionFrameWidth(),
                projectile.getExplosionFrameHeight(),
                projectile.getExplosionFrameWidth(),
                projectile.getExplosionFrameHeight(),
                this, true
        );
        Platform.runLater(() -> getChildren().add(explosion));
    }


    private boolean checkCollision(Projectile projectile, Tank tank) {
        Rectangle projectileBounds = new Rectangle(projectile.getX(), projectile.getY(), projectile.getFitWidth(), projectile.getFitHeight());
        Rectangle tankBounds = new Rectangle(tank.getX(), tank.getY(), tank.getFitWidth(), tank.getFitHeight());
        return projectileBounds.getBoundsInParent().intersects(tankBounds.getBoundsInParent());
    }

    private boolean checkCollisionForObject(Projectile projectile, DestructibleObject destructible) {
        return projectile.getBoundsInParent().intersects(destructible.getBoundsInParent());
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
        if (event.getCode() == KeyCode.R) {

            // Toggle shield for Tank1

            tank1.getShieldCircle().setVisible(!tank1.getShieldCircle().isVisible());
            tank1.activateShield();
        } else if (event.getCode() == KeyCode.U) {

            // Toggle shield for Tank2

            tank2.getShieldCircle().setVisible(!tank2.getShieldCircle().isVisible());
            tank2.activateShield();

        }
    }

    private void handleKeyReleased(KeyEvent event) {
        pressedKeys.remove(event.getCode());

        if (event.getCode() == KeyCode.R) {
            tank1.deactivateShield(); // Example: Player 1 deactivates the shield with 'R'
        }
        if (event.getCode() == KeyCode.U) {
            tank2.deactivateShield();  // Example: Player 2 deactivates the shield with 'R'
        }
    }

    private void startMovementThread() {
        movementThread = new Thread(() -> {
            while (true) {
                if (countdownComplete) {  // Only move tanks after countdown is complete
                    moveTanks();
                    tank1.updateShieldStatus();  // Check if shield needs to be deactivated
                    tank2.updateShieldStatus();
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


    private void moveTanks() {

        if (pressedKeys.contains(KeyCode.Q)) tank1.rotate(-5); // Rotate counterclockwise
        if (pressedKeys.contains(KeyCode.E)) tank1.rotate(5); // Rotate clockwise

        if (pressedKeys.contains(KeyCode.I)) tank2.rotate(-5); // Rotate counterclockwise
        if (pressedKeys.contains(KeyCode.P)) tank2.rotate(5); // Rotate clockwise

        if (tank1 != null) {
            if (pressedKeys.contains(KeyCode.W)) updateTankPosition(tank1, 0, -1);
            if (pressedKeys.contains(KeyCode.S)) updateTankPosition(tank1, 0, 1);
            if (pressedKeys.contains(KeyCode.A)) updateTankPosition(tank1, -1, 0);
            if (pressedKeys.contains(KeyCode.D)) updateTankPosition(tank1, 1, 0);
            if (pressedKeys.contains(KeyCode.SPACE)) tank1.shoot(this);
        }
        if (tank2 != null&&!isRobot) {
            if (pressedKeys.contains(KeyCode.UP)) updateTankPosition(tank2, 0, -1);
            if (pressedKeys.contains(KeyCode.DOWN)) updateTankPosition(tank2, 0, 1);
            if (pressedKeys.contains(KeyCode.LEFT)) updateTankPosition(tank2, -1, 0);
            if (pressedKeys.contains(KeyCode.RIGHT)) updateTankPosition(tank2, 1, 0);
            if (pressedKeys.contains(KeyCode.ENTER)) tank2.shoot(this);
        }
    }



    private void updateTankPosition(Tank tank, double dx, double dy) {
        Platform.runLater(() -> tank.move(dx, dy,destructibleObjects));
        tank.updateShieldPosition(); // Update the shield position when the tank moves
    }


    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
        Platform.runLater(() -> getChildren().add(projectile));
    }

    public void removeDestructibleObject(DestructibleObject destructibleObject) {
        Platform.runLater(() -> {
            getChildren().remove(destructibleObject); // Remove from the scene graph
            destructibleObjects.remove(destructibleObject); // Remove from tracking list
        });
    }

    public Tank getTank1() {
        return tank1;
    }

    public Tank getTank2() {
        return tank2;
    }



    public  List<DestructibleObject> getDestructibleObjects() {
        return destructibleObjects;
    }
}