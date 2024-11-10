package com.example.tankmark1.weapons;

import com.example.tankmark1.tanks.Tank;

public class Demon extends Projectile {
    private static int damage = 40; // Define damage for Cannon

    public Demon(double x, double y, double dx, double dy, double angle, Tank owner) {
        super(x, y, "/weapons_png/demon.png", dx, dy, 5, angle, Demon.damage, owner, "/explosion_sprite_Sheets/big_explosionL3.png", "/explosionSound.mp3", 4, 7, 256, 256, 30, 30); // High-speed projectile for a laser

    }
}
