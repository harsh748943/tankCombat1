package com.example.tankmark1.weapons;

import com.example.tankmark1.Tank;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile extends ImageView {
    private double speed = 1; // Speed of the projectile
    private Tank owner; // Reference to the tank that fired this projectile
    private double dx; // Change in x direction
    private double dy; // Change in y direction
    private int damage;

    public Projectile(double x, double y, String imagePath, double dx, double dy,double speed,double angle,int damage,Tank owner) {
        super(new Image(imagePath));
        setX(x);
        setY(y);
        this.owner=owner;
        this.dx = dx;
        this.dy = dy;
        this.speed=speed;
        setFitWidth(100); // Adjust size as necessary
        setFitHeight(100);
        setPreserveRatio(true);
        this.damage=damage;
        setRotate(Math.toDegrees(angle));
    }

    public void move() {
        // Update position based on direction
        setX(getX() + dx * speed);
        setY(getY() + dy * speed);
    }
    public int getDamage() {
        return damage;
    }
    public Tank getOwner() {
        return owner; // Getter for the owner
    }
    // public abstract void applySpecialEffect();

    public boolean collidesWith(Projectile other) {
        return this.getBoundsInParent().intersects(other.getBoundsInParent());
    }

}
