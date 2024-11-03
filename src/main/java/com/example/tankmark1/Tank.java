package com.example.tankmark1;

import com.example.tankmark1.map.Boundary;
import com.example.tankmark1.weapons.Cannon;
import com.example.tankmark1.weapons.Missile;
import com.example.tankmark1.weapons.Projectile;
import com.example.tankmark1.weapons.Torpedo;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.List;

public class Tank extends ImageView {
    private double speed = 5;
    private String weapon;
    private long lastShotTime; // Time when the last shot was fired
    private static final long SHOOT_DELAY = 1000; // Minimum time delay between shots (ms)
    private int health = 100; // Health attribute

    // Tank's boundary (hitbox)
    private Boundary hitbox;

    public Tank(double x, double y, String imagePath, String weapon) {
        super(new Image(imagePath));
        setX(x);
        setY(y);
        setFitWidth(50);
        setFitHeight(50);
        setPreserveRatio(true);
        this.weapon = weapon;
        this.lastShotTime = 0; // Initialize last shot time

        // Initialize the tank's boundary (hitbox) based on its dimensions and position
        this.hitbox = new Boundary(x, y, getFitWidth(), getFitHeight());
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

    // Method to get tank's hitbox
    public Boundary getHitbox() {
        return hitbox;
    }

    public void move(double dx, double dy, List<Boundary> staticBoundaries) {
        System.out.println("Moving with dx: " + dx + ", dy: " + dy + ", speed: " + speed);

        // Calculate new position
        double newX = getX() + dx * speed;
        double newY = getY() + dy * speed;

        // Create a temporary hitbox for the proposed new position
        Boundary newHitbox = new Boundary(newX, newY, getFitWidth(), getFitHeight());

        // Check for collisions with static elements
        boolean collisionDetected = false;
        for (Boundary boundary : staticBoundaries) {
            if (newHitbox.intersects(boundary)) {
                collisionDetected = true;
                break;
            }
        }

        // Update tank position if no collision
        if (!collisionDetected) {
            setX(newX);
            setY(newY);
            // Update the tank's hitbox to the new position
            hitbox = newHitbox;
            // Rotate tank according to movement direction
            rotateToDirection(dx, dy);
        }
    }

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
                    projectileSpeed = 8;
                    projectileSize = 30;
                    projectile = new Cannon(getX() + 25, getY(), dx * projectileSpeed, dy * projectileSpeed, angle, this);
                    playShootingSound("/laserSound.wav"); // Play sound

                    break;
                case "Missile":
                    projectileImagePath = "rocket.png"; // Path for missile image
                    projectileSpeed = 1;
                    projectileSize = 50;
                    projectile = new Missile(getX() + 25, getY(), dx * projectileSpeed, dy * projectileSpeed, angle, this);
                    playShootingSound("/levelBossTorpedo.wav"); // Play sound
                    break;
                case "Laser":
                    projectileImagePath = "laser.png"; // Path for laser image
                    projectileSpeed = 4;
                    projectileSize = 50;
                    projectile = new Torpedo(getX() + 25, getY(), dx * projectileSpeed, dy * projectileSpeed, angle, this);
                    playShootingSound("/levelBossRocket.wav"); // Play sound
                    break;
                default:
                    projectileImagePath = "default.png"; // Fallback image
                    projectileSpeed = 10;
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
}
