package com.example.tankmark1;

import com.example.tankmark1.map.*;
import javafx.scene.layout.Pane;

import java.util.List;

public class MapController {
    private String selectedMap;
    Pane pane;
    List<DestructibleObject> destructibleObjects;
    MapController(Pane pane, String selectedMap, List<DestructibleObject> destructibleObjects){
        this.selectedMap=selectedMap;
        this.pane=pane;
        this.destructibleObjects=destructibleObjects;
    }
    public void setUpMap() {
        Map currentMap;
        switch (selectedMap) {
            case "Forest" -> currentMap = new ForestMap();
            case "Desert" -> currentMap = new DesertMap();
            case "Tiles" -> currentMap = new TileMap();
            case "Maze" -> currentMap = new MazeMap();

            default -> currentMap = new SnowMap();
        }
        pane.getChildren().add(currentMap);

        // Add destructible objects to the list for collision checking
        if (currentMap instanceof ForestMap forestMap) {
            destructibleObjects.addAll(forestMap.getDestructibleObjects());
        }
        if (currentMap instanceof SnowMap snowMap) {
            destructibleObjects.addAll(snowMap.getDestructibleObjects());
        }
        if (currentMap instanceof TileMap tileMap) {
            destructibleObjects.addAll(tileMap.getDestructibleObjects());
        }

        if (currentMap instanceof MazeMap mazeMap) {
            destructibleObjects.addAll(mazeMap.getDestructibleObjects());
        }

        if (currentMap instanceof DesertMap dMap) {
            destructibleObjects.addAll(dMap.getDestructibleObjects());
        }
    }
}
