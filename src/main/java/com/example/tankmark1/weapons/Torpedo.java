package com.example.tankmark1.weapons;

import com.example.tankmark1.Tank;

public class Torpedo extends Projectile {
    private static int damage = 20; // Define damage for Cannon
    public Torpedo(double x, double y, double dx, double dy, double angle, Tank owner) {
        super(x, y, "/weapons_png/bigtorpedo.png", dx, dy, 5,angle,Torpedo.damage,owner,"/explosion_sprite_Sheets/big_explosionL1.png","/explosionSound.mp3",8,7,256,256,50,50); // Faster speed for a rocket

    }

//    @Override
//    public void applySpecialEffect() {
//        // Implement an explosion effect or other specific behavior
//    }
}
