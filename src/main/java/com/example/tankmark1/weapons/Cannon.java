package com.example.tankmark1.weapons;

import com.example.tankmark1.Tank;

public class Cannon extends Projectile {
    private static int damage = 20; // Define damage for Cannon
    public Cannon(double x, double y, double dx, double dy, double angle, Tank owner) {
        super(x, y, "cannonball.png", dx, dy, 2,angle,Cannon.damage,owner); // Slower speed for a heavier projectile
    }
//
//    @Override
//    public void applySpecialEffect() {
//        // No special effect for cannonball, but you could add one if needed
//    }
}
