package com.example.tankmark1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile extends ImageView {
    private double speed = 10; // Speed of the projectile

    private double dx; // Change in x direction
    private double dy; // Change in y direction


    public Projectile(double x, double y, String imagePath, double dx, double dy) {
        super(new Image(imagePath));
        setX(x);
        setY(y);
        this.dx = dx;
        this.dy = dy;
        setFitWidth(10); // Adjust size as necessary
        setFitHeight(10);
        setPreserveRatio(true);
    }

    public void move() {
        // Update position based on direction
        setX(getX() + dx * speed);
        setY(getY() + dy * speed);
    }
}
