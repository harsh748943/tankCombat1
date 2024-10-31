package com.example.tankmark1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Tank extends ImageView {
    private double speed = 5;
    private String weapon;

    public Tank(double x, double y, String imagePath, String weapon) {
        super(new Image(imagePath));
        setX(x);
        setY(y);
        setFitWidth(50);
        setFitHeight(50);
        setPreserveRatio(true);
        this.weapon = weapon;
    }

    public void move(double dx, double dy) {
        System.out.println("Moving with dx: " + dx + ", dy: " + dy + ", speed: " + speed);
        double newX = getLayoutX() + dx * speed;
        double newY = getLayoutY() + dy * speed;

        // Ensure the tank stays within the game area boundaries
        if (newX >= 0 && newX <= 750) { // Adjust based on game area width and tank size
            setLayoutX(newX);
        }
        if (newY >= 0 && newY <= 550) { // Adjust based on game area height and tank size
            setLayoutY(newY);
        }
    }


    public void shoot() {
        Projectile projectile = new Projectile(getX() + 25, getY());
        // Add the projectile to the game (handle projectile logic)
    }
}

