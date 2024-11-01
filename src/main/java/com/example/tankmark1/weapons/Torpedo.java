package com.example.tankmark1.weapons;

import com.example.tankmark1.Tank;

public class Torpedo extends Projectile {
    private static int damage = 20; // Define damage for Cannon
    public Torpedo(double x, double y, double dx, double dy, double angle, Tank owner) {
        super(x, y, "rocket.png", dx, dy, 2,angle,Torpedo.damage,owner); // Faster speed for a rocket
        setFitWidth(100); // Rockets might be larger
        setFitHeight(100);
    }

//    @Override
//    public void applySpecialEffect() {
//        // Implement an explosion effect or other specific behavior
//    }
}
