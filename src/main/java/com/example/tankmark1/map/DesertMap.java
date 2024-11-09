package com.example.tankmark1.map;

public class DesertMap extends Map {
    public DesertMap() {
        setStyle("-fx-background-color: sandybrown;"); // Load your desert background image

        // Create and add dust effect with map dimensions
        DustEffect dustEffect = new DustEffect(1500, 1200);
        getChildren().add(dustEffect);
    }
}
