package com.example.tankmark1.map;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

class DestructibleObject extends ImageView {
    private int health;

    public DestructibleObject(String imagePath, int width, int height) {
        super(new Image(imagePath, width, height, true, true));
        this.health = 50; // Set initial health for the destructible object
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            destroy();
        }
    }

    private void destroy() {
        setVisible(false); // Make the object disappear when destroyed
        // Additional destruction logic if needed (e.g., animations, sound effects)
    }
}