package com.example.tankmark1;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile extends ImageView {
    private double speed = 10;

    public Projectile(double x, double y) {
        super(new Image("projectile.png")); // Load your projectile image here
        setX(x);
        setY(y);
    }

    public void update() {
        setY(getY() - speed); // Move upward
        // Check for collisions with tanks or map boundaries
    }
}

