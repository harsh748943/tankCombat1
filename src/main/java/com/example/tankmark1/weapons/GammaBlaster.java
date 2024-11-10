package com.example.tankmark1.weapons;

import com.example.tankmark1.tanks.Tank;

public class GammaBlaster extends Projectile {
    private static int damage = 50; // Define damage for Cannon

    public GammaBlaster(double x, double y, double dx, double dy, double angle, Tank owner) {
        super(x, y, "/weapons_png/demon.png", dx, dy, 5, angle, GammaBlaster.damage, owner, "/explosion_sprite_Sheets/big_explosionL2.png", "/explosionSound.mp3", 4, 7, 256, 256, 30, 30); // High-speed projectile for a laser

    }
}
