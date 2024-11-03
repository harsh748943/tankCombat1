package com.example.tankmark1.map;

import javafx.scene.layout.Pane;
import java.util.List;

public abstract class Map extends Pane {

    public Map() {
        setPrefSize(1260, 720);
    }

    /**
     * Abstract method to get the boundaries of the map.
     * Each subclass should implement this to return the specific boundaries.
     *
     * @return a list of Boundary objects representing the map's boundaries
     */
    public abstract List<Boundary> getBoundaries();
}
