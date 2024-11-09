package com.example.tankmark1.weapons;

import com.example.tankmark1.Tank;

public class Missile extends Projectile {
    private static int damage = 40; // Define damage for Cannon
    public Missile(double x, double y, double dx, double dy, double angle, Tank owner) {
        super(x, y, "/weapons_png/rocket.png", dx, dy, 5,angle,Missile.damage,owner,"/explosion_sprite_Sheets/big_explosionL3.png","/explosionSound.mp3",4,7,256,256,30,30); // High-speed projectile for a laser
        setFitWidth(10); // Smaller size for a laser
        setFitHeight(10);
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
