package com.example.tankmark1.map;

import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;

public abstract class Map extends Pane {
    public Map() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Set the Pane size to the full screen dimensions
        setPrefSize(screenBounds.getWidth(), screenBounds.getHeight());
    }
}
