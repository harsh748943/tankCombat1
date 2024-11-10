
package com.example.tankmark1.tanks;

import com.example.tankmark1.GameController;
import com.example.tankmark1.map.DestructibleObject;
import com.example.tankmark1.weapons.Cannon;
import com.example.tankmark1.weapons.Missile;
import com.example.tankmark1.weapons.Projectile;
import com.example.tankmark1.weapons.Torpedo;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;

import java.util.List;

public class Tank extends ImageView {



    private double shieldStrength = 100;  // Initial shield strength
    private boolean isShieldActive = false;
    private long shieldActivatedTime;
    private final long shieldDuration = 5000; // Shield duration in milliseconds



    // Add getters and setters for these attributes
    public boolean isShieldActive() { return isShieldActive; }
    public double getShieldStrength() { return shieldStrength; }


    private double speed = 3;
    private String weapon;
    private long lastShotTime; // Time when the last shot was fired
    private static final long SHOOT_DELAY = 1000; // Minimum time delay between shots (ms)
    private int health = 100; // Health attribute


    // Margins in pixels (2 cm converted to pixels)
    private static final double MARGIN = 0.5 / 2.54 * 96; // 2 cm to pixels (assuming 96 DPI)
    private Circle shieldCircle;
    public Tank(double x, double y, String imagePath, String weapon) {
        super(new Image(imagePath));
        setX(x);
        setY(y);
        setFitWidth(100);
        setFitHeight(100);
        setPreserveRatio(true);
        this.weapon = weapon;
        this.lastShotTime = 0; // Initialize last shot time


        // Create the shield circle

        shieldCircle = new Circle(getFitWidth() / 2 + 10); // Adjust size as needed

        shieldCircle.setFill(Color.TRANSPARENT);

        shieldCircle.setStroke(Color.BLUE); // Outline color for shield

        shieldCircle.setStrokeWidth(4);

        shieldCircle.setVisible(false); // Hidden by default


        // Set the circle's initial position

        updateShieldPosition();

    }

    public Circle getShieldCircle() {
        return shieldCircle;
    }



    // Activate the shield
    public void activateShield() {

        if (!isShieldActive) {

            isShieldActive = true;

            shieldActivatedTime = System.currentTimeMillis();

            shieldCircle.setVisible(true); // Show the shield

        }

    }


    public void deactivateShield() {

        isShieldActive = false;

        shieldStrength = 100;  // Reset shield strength for next use

        shieldCircle.setVisible(false); // Hide the shield

    }

    // Method to update shield status based on time
    // Method to update shield status based on time
    public void updateShieldStatus() {
        if (isShieldActive && (System.currentTimeMillis() - shieldActivatedTime > shieldDuration || shieldStrength <= 0)) {
            deactivateShield();
        }
    }



    public void updateShieldPosition() {
        shieldCircle.setCenterX(getX() + getFitWidth() / 2);
        shieldCircle.setCenterY(getY() + getFitHeight() / 2);
    }

    // Take damage method modified to consider shield
    // Method to take damage and factor in the shield
    public void takeDamageToS(double damage) {
        if (isShieldActive) {
            shieldStrength -= damage; // Shield absorbs damage
            if (shieldStrength <= 0) {
                deactivateShield(); // Deactivate shield if it's depleted
            }
        } else {
            health -= damage; // No shield, damage goes directly to health
            if (health < 0) health = 0; // Prevent health from going below 0
        }
    }






    public int getHealth() {
        return health;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }


//    public Circle createShieldCircle() {
//        Circle shieldCircle = new Circle(getFitWidth() / 2 + 10);
//        shieldCircle.setFill(Color.TRANSPARENT);
//        shieldCircle.setStroke(Color.BLUE); // Outline color for shield
//        shieldCircle.setStrokeWidth(4);
//        shieldCircle.setVisible(false); // Hidden by default
//        return shieldCircle;
//    }


    public void move(double dx, double dy, List<DestructibleObject> destructibleObjects) {
        System.out.println("Moving with dx: " + dx + ", dy: " + dy + ", speed: " + speed);
        double newX = getX() + dx * speed;
        double newY = getY() + dy * speed;

        // Get the full screen dimensions
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // Calculate usable width and height
        double usableWidth = screenWidth - (2 * MARGIN);
        double usableHeight = screenHeight - (2 * MARGIN);

        // Check for collision with destructible objects
        boolean collisionDetected = false;
        for (DestructibleObject destructible : destructibleObjects) {
            if (destructible.getHealth() > 0) { // Only check collision if object is not destroyed
                Rectangle tankBounds = new Rectangle(newX, newY, getFitWidth(), getFitHeight());
                if (tankBounds.getBoundsInParent().intersects(destructible.getBoundsInParent())) {
                    collisionDetected = true;
                    break;
                }
            }
        }

        if (!collisionDetected) {
            // Ensure the tank stays within the game area boundaries
            if (newX >= MARGIN && newX <= usableWidth - 100 + MARGIN) { // Adjust for tank width
                setX(newX);
            }
            if (newY >= MARGIN && newY <= usableHeight - 100 + MARGIN) { // Adjust for tank height
                setY(newY);
            }

            // Rotate tank according to movement direction
            rotateToDirection(dx, dy);
        }}

    private void rotateToDirection(double dx, double dy) {
        // Calculate angle based on dx, dy
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        setRotate(angle);
    }

    public void shoot(GameController gameController) {
        long currentTime = System.currentTimeMillis();
        // Check if enough time has passed since the last shot
        if (currentTime - lastShotTime >= SHOOT_DELAY) {
            lastShotTime = currentTime; // Update last shot time

            // Set projectile properties based on the selected weapon
            String projectileImagePath;
            double projectileSpeed;
            double projectileSize;
            Projectile projectile;
            // Calculate direction based on the tank's rotation
            double angle = Math.toRadians(getRotate()); // Convert to radians
            double dx = Math.cos(angle); // Calculate x direction
            double dy = Math.sin(angle); // Calculate y direction

            switch (weapon) {
                case "Cannon":
                    projectileImagePath = "cannonball.png"; // Path for cannonball image
                    projectileSpeed = 4;
                    projectileSize = 30;
                    projectile = new Cannon(getX() + 25, getY(), dx * projectileSpeed, dy * projectileSpeed, angle, this);
                    playShootingSound("/laserSound.wav"); // Play sound
                    break;
                case "Missile":
                    projectileImagePath = "rocket.png"; // Path for missile image
                    projectileSpeed = 3;
                    projectileSize = 50;
                    projectile = new Missile(getX() + 25, getY(), dx * projectileSpeed, dy * projectileSpeed, angle, this);
                    playShootingSound("/levelBossTorpedo.wav"); // Play sound
                    break;
                case "Laser":
                    projectileImagePath = "laser.png"; // Path for laser image
                    projectileSpeed = 3;
                    projectileSize = 50;
                    projectile = new Torpedo(getX() + 25, getY(), dx * projectileSpeed, dy * projectileSpeed, angle, this);
                    playShootingSound("/levelBossRocket.wav"); // Play sound
                    break;
                default:
                    projectileImagePath = "default.png"; // Fallback image
                    projectileSpeed = 2;
                    projectileSize = 20;
                    projectile = new Missile(getX() + 25, getY(), dx * projectileSpeed, dy * projectileSpeed, angle, this);
                    playShootingSound("/laserSound.wav"); // Play sound
                    break;
            }

            // Create the projectile with specific properties
            projectile.setFitWidth(projectileSize);
            projectile.setFitHeight(projectileSize);

            // Add the projectile to the game
            gameController.addProjectile(projectile);
        }
    }


    private void playShootingSound(String soundFileName) {
        String soundPath = getClass().getResource(soundFileName).toExternalForm();
        Media shotSound = new Media(soundPath);
        MediaPlayer mediaPlayer = new MediaPlayer(shotSound);
        mediaPlayer.setVolume(0.5); // Adjust volume if necessary
        mediaPlayer.play();
    }

    public void rotate(double angleDelta) {
        // Adjust the tank's rotation angle by the specified delta
        setRotate(getRotate() + angleDelta);
    }
}
