package com.example.tankmark1.weapons;

import com.example.tankmark1.Tank;

public class Missile extends Projectile {
    private static int damage = 40; // Define damage for Cannon
    public Missile(double x, double y, double dx, double dy, double angle, Tank owner) {
        super(x, y, "bigtorpedo.png", dx, dy, 5,angle,Missile.damage,owner); // High-speed projectile for a laser
        setFitWidth(30); // Smaller size for a laser
        setFitHeight(30);
    }
//    public int getDamage() {
//        return damage;
//    }
//
//    @Override
//    public void applySpecialEffect() {
//        // You could add effects like piercing through objects
//    }
}
