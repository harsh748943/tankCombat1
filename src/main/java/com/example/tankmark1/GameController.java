package com.example.tankmark1;

import com.example.tankmark1.map.*;
import com.example.tankmark1.map.Map;
import com.example.tankmark1.weapons.Explosion;
import com.example.tankmark1.weapons.Projectile;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
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
    private TankGame mainApp;  // Reference to the main application
    public Tank tank1;
    public Tank tank2;
    public Tank tank3;
    private ProgressBar healthBar1, healthBar2;
    private Text healthText1, healthText2;
    private Text winnerText;
    private Set<KeyCode> pressedKeys = new HashSet<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<DestructibleObject> destructibleObjects = new ArrayList<>();

    private Thread movementThread;
    private boolean isRobot=false;

    public GameController(int numPlayers, String selectedWeapon, String selectedMap, boolean soundOn, TankGame mainApp) {
        this.numPlayers = numPlayers;
        this.selectedWeapon = selectedWeapon;
        this.selectedMap = selectedMap;
        this.soundOn = soundOn;
        this.mainApp = mainApp;  // Assign mainApp reference

        setUpMap();
        setUpTanks();
        setUpHealthBars();
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

    private void checkForWin() {
        if (tank1.isDestroyed()) {
            // Show destroyed image for tank1
            tank1.setImage(new Image("/tankBlast.png"));
            endGame("Player 2 Wins!");
        } else if (tank2.isDestroyed()) {
            // Show destroyed image for tank2
            tank2.setImage(new Image("/tankBlast1.png"));
            endGame("Player 1 Wins!");

        }
    }

    private void endGame(String winnerMessage) {
        // Stop movement and projectile threads
        if (movementThread != null && movementThread.isAlive()) {
            movementThread.interrupt();
        }

        // Stop background music if playing
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }

        // Display winner message and await Enter key to return to menu
        Platform.runLater(() -> {
            winnerText.setText(winnerMessage + " Press Enter to exit.");
            winnerText.setVisible(true);

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


        tank2 = new Tank(1300, 700, "tank3.png", selectedWeapon);
        if(numPlayers>1) {
            this.getChildren().add(tank2);

        }

        if (numPlayers >= 2) {
            tank2 = new Tank(1400, 700, "tank3.png", selectedWeapon);

        else {
            isRobot=true;
            setComputerControlledTank(tank2);

            this.getChildren().add(tank2);
        }




//        if (numPlayers == 1) {
//            tank1 = new Tank(100, 100, "tank.png", selectedWeapon);
//            this.getChildren().add(tank1);
//            tank3 = new Tank(1300, 650, "tank3.png", selectedWeapon);
//            // If only one player is selected, control tank2 with computer AI
//
//
//        }
    }
    private void setComputerControlledTank(Tank computerTank) {
        new Thread(() -> {
            Random random = new Random();
            long lastShootTime = 0;
            long lastMoveTime = 0;

            while (true) {
                try {
                    long currentTime = System.currentTimeMillis();
                    boolean shouldMove = currentTime - lastMoveTime > 100;  // Move every 0.1 second (smooth)

                    boolean shouldShoot = currentTime - lastShootTime > 2000;  // Shoot every 2 seconds

                    boolean tank1Destroyed = tank1 == null || tank1.isDestroyed();
                    if (tank1Destroyed) {
                        shouldShoot = false;
                    }

                    if (shouldMove) {
                        final double[] dx = {0}, dy = {0};  // Final dx and dy for lambda expression

                        if (!tank1Destroyed && tank1 != null) {
                            // Calculate direction to tank1
                            double angleToTank1 = Math.toDegrees(Math.atan2(
                                    tank1.getY() - computerTank.getY(),
                                    tank1.getX() - computerTank.getX()
                            ));

                            // Distance from tank1 to tank2
                            double distanceToTank1 = Math.sqrt(
                                    Math.pow(tank1.getX() - computerTank.getX(), 2) +
                                            Math.pow(tank1.getY() - computerTank.getY(), 2)
                            );

                            if (distanceToTank1 < 200) {  // If tank1 is very close, dodge
                                // Move in a random direction
                                dx[0] = random.nextDouble() * 1 - 1;  // Random between -1 and 1
                                dy[0] = random.nextDouble() * 1 - 1;
                            } else {
                                // Otherwise, move towards tank1
                                double radians = Math.toRadians(angleToTank1);
                                dx[0] = Math.cos(radians) * 3;  // Fast movement (speed = 4)
                                dy[0] = Math.sin(radians) * 3;
                            }

                            // Rotate the tank to face tank1
                            Platform.runLater(() -> computerTank.setRotate(angleToTank1));
                        } else {
                            // Random movement if tank1 is destroyed or far away
                            dx[0] = random.nextDouble() * 1- 1;  // Random between -1 and 1
                            dy[0] = random.nextDouble() * 1 - 1;
                        }

                        // Move the tank using the updated dx and dy values
                        Platform.runLater(() -> computerTank.move(dx[0], dy[0], destructibleObjects));
                        lastMoveTime = currentTime;  // Update last move time
                    }

                    if (shouldShoot && tank1 != null && !tank1Destroyed) {
                        // Shoot after every 2 seconds
                        Platform.runLater(() -> computerTank.shoot(this));
                        lastShootTime = currentTime;  // Update last shoot time
                    }

                    // Sleep to allow for smoother movement and shooting
                    Thread.sleep(50);  // 50ms delay to make the movement smooth

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
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


            // Check for collisions with other projectiles
            for (Projectile other : new ArrayList<>(projectiles)) {
                if (projectile != other && projectile.collidesWith(other)) {
                    // Create an explosion at the point of collision
                    // If you find that the explosion is misaligned, adjust like this:
                    double adjustedX = projectile.getX() - (256 / 2);  // Center the explosion
                    double adjustedY = projectile.getY() - (256 / 2); // Center the explosion
                    Explosion explosion = new Explosion(
                            adjustedX, adjustedY,  // Position of the explosion
                            "/explosionL1.png",  // Path to explosion sprite sheet
                            "/explosionSound.mp3",  // Path to explosion sound file
                            8, 7,  // Columns and rows of the sprite sheet
                            256, 256,  // Frame width and height
                            this,// The node to shake during the explosion
                            true
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

                    // Create explosion effect
                    double adjustedX = projectile.getX() - 40;  // Adjust as needed
                    double adjustedY = projectile.getY() - 40;  // Adjust as needed
                    Explosion explosion = new Explosion(
                            adjustedX, adjustedY,
                            "/torpedoHitL1.png",
                            "/explosionSound.mp3",
                            5, 2,
                            80, 80,
                            this,
                            true
                    );
                    Platform.runLater(() -> getChildren().add(explosion));

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
                tank1.takeDamage(projectile.getDamage());
                updateHealthBars();
                removeProjectile(projectile);
                checkForWin();
                double adjustedX = projectile.getX() - (80 / 2);  // Center the explosion
                double adjustedY = projectile.getY() - (80/ 2); // Center the explosion
                Explosion explosion = new Explosion(
                        adjustedX, adjustedY,  // Position of the explosion
                        "/torpedoHitL1.png",  // Path to explosion sprite sheet
                        "/explosionSound.mp3",  // Path to explosion sound file
                        5, 2,  // Columns and rows of the sprite sheet
                        80, 80,  // Frame width and height
                        this,// The node to shake during the explosion
                        true
                );
                Platform.runLater(() -> getChildren().add(explosion));
            }

            if (tank2 != null && checkCollision(projectile, tank2) && projectile.getOwner() != tank2) {
                tank2.takeDamage(projectile.getDamage());
                updateHealthBars();
                removeProjectile(projectile);
                checkForWin();
                double adjustedX = projectile.getX() - (80 / 2);  // Center the explosion
                double adjustedY = projectile.getY() - (80 / 2); // Center the explosion
                Explosion explosion = new Explosion(
                        adjustedX, adjustedY,  // Position of the explosion
                        "/torpedoHitL1.png",  // Path to explosion sprite sheet
                        "/explosionSound.mp3",  // Path to explosion sound file
                        5, 2,  // Columns and rows of the sprite sheet
                        80, 80,  // Frame width and height
                        this,// The node to shake during the explosion
                        true
                );
                Platform.runLater(() -> getChildren().add(explosion));
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

    public void removeDestructibleObject(DestructibleObject destructibleObject) {
        Platform.runLater(() -> {
            getChildren().remove(destructibleObject); // Remove from the scene graph
            destructibleObjects.remove(destructibleObject); // Remove from tracking list
        });
    }

}