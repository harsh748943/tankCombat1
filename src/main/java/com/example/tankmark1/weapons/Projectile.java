package com.example.tankmark1.weapons;

import com.example.tankmark1.tanks.Tank;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Projectile extends ImageView {
    private double speed = 1;
    private Tank owner;
    private double dx;
    private double dy;
    private int damage;
    private String explosionImagePath;
    private String explosionSoundPath;
    private int explosionFrameCount;
    private int explosionFrameWidth;
    private int explosionFrameHeight;
    private int explosionFrameColomn;
    private int explosionFrameRow;

    public Projectile(double x, double y, String imagePath, double dx, double dy, double speed, double angle, int damage, Tank owner,
                      String explosionImagePath, String explosionSoundPath, int explosionFrameColomn,int  explosionFrameRow, int explosionFrameWidth, int explosionFrameHeight,int width,int height) {
        super(new Image(Projectile.class.getResource(imagePath).toExternalForm()));
        setX(x);
        setY(y);
        this.owner = owner;
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
        setFitWidth(width);
        setFitHeight(height);
        setPreserveRatio(true);
        this.damage = damage;
        setRotate(Math.toDegrees(angle));

        // Explosion properties
        this.explosionImagePath = explosionImagePath;
        this.explosionSoundPath = explosionSoundPath;
        this.explosionFrameColomn = explosionFrameColomn;
        this.explosionFrameRow = explosionFrameRow;
        this.explosionFrameWidth = explosionFrameWidth;
        this.explosionFrameHeight = explosionFrameHeight;
    }

    public int getDamage() {
        return damage;
    }

    public Tank getOwner() {
        return owner;
    }

    public String getExplosionImagePath() {
        return explosionImagePath;
    }

    public String getExplosionSoundPath() {
        return explosionSoundPath;
    }

    public int getExplosionFrameCount() {
        return explosionFrameCount;
    }

    public int getExplosionFrameWidth() {
        return explosionFrameWidth;
    }

    public int getExplosionFrameHeight() {
        return explosionFrameHeight;
    }

    public boolean collidesWith(Projectile other) {
        return this.getBoundsInParent().intersects(other.getBoundsInParent());
    }

    public void move() {
        setX(getX() + dx * speed);
        setY(getY() + dy * speed);
    }

    public int getExplosionFrameColomn() {
        return explosionFrameColomn;
    }

    public int getExplosionFrameRow() {
        return explosionFrameRow;
    }
}
