package com.example.tankmark1.weapons;

import com.example.tankmark1.tanks.Tank;

public class Cannon extends Projectile {
    private static int damage = 20; // Define damage for Cannon
    public Cannon(double x, double y, double dx, double dy, double angle, Tank owner) {
        super(x, y, "/weapons_png/bomb.png", dx, dy, 5,angle,Cannon.damage,owner,"/explosion_sprite_Sheets/torpedoHitL1.png","/explosionSound.mp3",5,2,80,80,25,25); // Faster speed for a rocket
        // Slower speed for a heavier projectile
    }
//
//    @Override
//    public void applySpecialEffect() {
//        // No special effect for cannonball, but you could add one if needed
//    }
}
