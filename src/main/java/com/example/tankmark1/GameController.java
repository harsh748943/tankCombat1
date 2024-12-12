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
    public GameSoundManager soundManager;
    private boolean soundOn;
    private String level;
    private TankGame mainApp;  // Reference to the main application
    public Tank tank1;
    public Tank tank2;

    private Set<KeyCode> pressedKeys = new HashSet<>();
    private List<Projectile> projectiles = new ArrayList<>();
    private List<DestructibleObject> destructibleObjects = new ArrayList<>();
    public boolean gameOver[] = {false};
    private Text countdownText;  // Countdown text display ke liye
    public boolean countdownComplete = false;  // Game tabhi start hoga jab countdown complete hoga
    private Thread movementThread;
    private boolean isRobot=false;
    private ComputerTank computerTank;
    private List<Tank> allTanks=new ArrayList<>();
    HealthController healthController;
    MapController mapController;
    TankController tankController;

    public GameController(int numPlayers, String selectedWeapon, String selectedMap, boolean soundOn, TankGame mainApp,String level) {
        this.numPlayers = numPlayers;
        this.selectedWeapon = selectedWeapon;
        this.selectedMap = selectedMap;
        this.soundOn = soundOn;
        this.mainApp = mainApp;  // Assign mainApp reference
        this.level=level;
        soundManager = new GameSoundManager();
         mapController=new MapController(this,selectedMap,destructibleObjects);

        mapController.setUpMap();
        tankController=new TankController(this,selectedWeapon,numPlayers,level,destructibleObjects);

        setUpTanks();
        healthController=new HealthController(this,tank1,tank2, soundManager,movementThread,mainApp,gameOver);
        healthController.setUpHealthBars();
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





    private void setUpTanks() {

        tank1 = new Tank(100, 100, "tank.png", selectedWeapon);
        allTanks.add(tank1);
        this.getChildren().add(tank1);
        this.getChildren().add(tank1.getShieldCircle()); // Assuming you have a getter for shieldCircle


            tank2 = new Tank(1300, 700, "tank3.png", selectedWeapon);
            allTanks.add(tank2);
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
                    double collisionX = (projectile.getX() + other.getX()) / 2;
                    double collisionY = (projectile.getY() + other.getY()) / 2;

                    double adjustedX = collisionX - (256 / 2);  // Center the explosion
                    double adjustedY =collisionY - (256 / 2); // Center the explosion
                    Explosion explosion = new Explosion(
                            adjustedX, adjustedY,
                            "/explosion_sprite_Sheets/explosionL1.png",
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
//                updatesb();
                healthController.updateHealthBars();
                removeProjectile(projectile);
                healthController.checkForWin();

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
//                updatesb();
                healthController.updateHealthBars();
                removeProjectile(projectile);
                healthController.checkForWin();

               createExplosion(projectile);
            }


            if (isOutOfBounds(projectile)) {
                removeProjectile(projectile);
            }
        }
    }


    private void createExplosion(Projectile projectile) {
        double collisionX = (projectile.getX() + projectile.getX()) / 2;
        double collisionY = (projectile.getY() + projectile.getY()) / 2;

        double adjustedX = collisionX - (128/ 2);  // Center the explosion
        double adjustedY =collisionY - (128 / 2); // Center the explosion
//        double adjustedX = projectile.getX() - 128/2;
//        double adjustedY = projectile.getY() - 128/2;
        Explosion explosion = new Explosion(
                adjustedX, adjustedY,
                projectile.getExplosionImagePath(),
                projectile.getExplosionSoundPath(),
                projectile.getExplosionFrameColomn(),
                projectile.getExplosionFrameRow(),
                projectile.getExplosionFrameWidth(),
                projectile.getExplosionFrameHeight(),
                128,
                128,
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

        if (pressedKeys.contains(KeyCode.NUMPAD7)) tank2.rotate(-5); // Rotate counterclockwise
        if (pressedKeys.contains(KeyCode.NUMPAD9)) tank2.rotate(5); // Rotate clockwise
        if (pressedKeys.contains(KeyCode.P)) tank2.rotate(5);

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


}