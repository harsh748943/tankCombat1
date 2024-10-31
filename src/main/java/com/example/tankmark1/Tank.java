package com.example.tankmark1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tank extends ImageView {
    private double speed = 5;
    private String weapon;

    private long lastShotTime; // Time when the last shot was fired
    private static final long SHOOT_DELAY = 1000;

    public Tank(double x, double y, String imagePath, String weapon) {
        super(new Image(imagePath));
        setX(x);
        setY(y);
        setFitWidth(50);
        setFitHeight(50);
        setPreserveRatio(true);
        this.weapon = weapon;
        this.lastShotTime = 0; // Initialize last shot time
    }

    public void move(double dx, double dy) {
        System.out.println("Moving with dx: " + dx + ", dy: " + dy + ", speed: " + speed);
        double newX = getX() + dx * speed;
        double newY = getY() + dy * speed;

        // Ensure the tank stays within the game area boundaries
        if (newX >= 0 && newX <= 750) { // Adjust based on game area width and tank size
            setX(newX);
        }
        if (newY >= 0 && newY <= 550) { // Adjust based on game area height and tank size
            setY(newY);
        }

        // Rotate tank according to movement direction
        rotateToDirection(dx, dy);
    }

    private void rotateToDirection(double dx, double dy) {
        // Calculate angle based on dx, dy
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        setRotate(angle);
    }

    public void shoot(GameController gameController) {
        String projectileImagePath;
        long currentTime = System.currentTimeMillis();
        // Check if enough time has passed since the last shot
        if (currentTime - lastShotTime >= SHOOT_DELAY) {
            lastShotTime = currentTime; // Update last shot time

            // Determine the projectile image based on the selected weapon
            switch (weapon) {
                case "Cannon":
                    projectileImagePath = "cannonball.png"; // Path for cannonball image
                    break;
                case "Missile":
                    projectileImagePath = "missile.png"; // Path for missile image
                    break;
                case "Laser":
                    projectileImagePath = "laser.png"; // Path for laser image
                    break;
                default:
                    projectileImagePath = "laser.png"; // Fallback image
                    break;
            }
            // Calculate direction based on the tank's rotation
            double angle = Math.toRadians(getRotate()); // Convert to radians
            double dx = Math.cos(angle); // Calculate x direction
            double dy = Math.sin(angle); // Calculate y direction


            // Create the projectile
            Projectile projectile = new Projectile(getX() + 25, getY(), projectileImagePath, dx, dy);

//        // Calculate the direction based on the tank's rotation
//        double directionX = Math.cos(Math.toRadians(getRotate()));
//        double directionY = Math.sin(Math.toRadians(getRotate()));

            // Add the projectile to the game
            gameController.addProjectile(projectile);
        }
    }
}

