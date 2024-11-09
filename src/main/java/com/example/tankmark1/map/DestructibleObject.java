package com.example.tankmark1.map;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.List;

public class DestructibleObject extends ImageView {
    private int health;
    private boolean isDestroyed = false;
    private List<DestructibleObject> destructibleList;

    public DestructibleObject(String imagePath, int width, int height, List<DestructibleObject> destructibleList) {
        super(new Image(imagePath, width, height, true, true));
        this.health = 50; // Set initial health for the destructible object
        this.destructibleList = destructibleList;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            destroy();
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }


    private void destroy() {
        isDestroyed = true;
        setVisible(false); // Make the object disappear when destroyed

        Platform.runLater(() -> {
            // Remove from the destructible list
            destructibleList.remove(this);

            // Remove from the parent container's children list if the parent is a Pane or similar container
            if (getParent() instanceof javafx.scene.layout.Pane pane) {
                pane.getChildren().remove(this);
            }
        });

        // Additional destruction logic if needed (e.g., animations, sound effects)
    }
}