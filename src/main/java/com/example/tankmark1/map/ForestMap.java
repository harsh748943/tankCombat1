package com.example.tankmark1.map;

import javafx.scene.image.ImageView;

public class ForestMap extends Map {
    public ForestMap() {
        setStyle("-fx-background-color: lightgreen;"); // Optional background color

        // Add destructible objects to the map
        addDestructibleObjects();
    }

    private void addDestructibleObjects() {
        ImageView tree = new DestructibleObject("tree1.png", 100, 150);
        tree.setX(200); // Position the tree at x=200
        tree.setY(300); // Position the tree at y=300

        getChildren().add(tree); // Add the tree to the map
    }
}
