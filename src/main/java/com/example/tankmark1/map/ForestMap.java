package com.example.tankmark1.map;

import javafx.scene.image.ImageView;

public class ForestMap extends Map { public ForestMap() { setStyle("-fx-background-color: forestgreen;"); // Optional background color

    // Add destructible objects to the map
    addDestructibleObjects();
}


    private void addDestructibleObjects() {

        // Create trees

        createDestructibleObject("tree1.png", 80, 100, 500, 300);

        createDestructibleObject("tree1.png", 80, 100, 400, 600);

        createDestructibleObject("tree1.png", 80, 100, 1000, 350);

        createDestructibleObject("tree1.png", 80, 100, 700, 500);


        // Create rocks

        createDestructibleObject("rock.png", 100, 100, 1400, 250);

        createDestructibleObject("rock.png", 100, 100, 100, 600);

        createDestructibleObject("rock.png", 100, 100, 375, 100);

        createDestructibleObject("rock.png", 100, 100, 700, 250);


        // Create down forest objects

        for (int i = 0; i < 16; i++) {

            createDestructibleObject("down forest.png", 200, 100, 1 + (i * 95), 692);

        }

        for (int i = 0; i < 16; i++) {

            createDestructibleObject("upper forest.png", 200, 100, 1 + (i * 95), 1);

        }

    }


    private void createDestructibleObject(String imagePath, int width, int height, double x, double y) {

        DestructibleObject object = new DestructibleObject(imagePath, width, height);

        object.setX(x);

        object.setY(y);

        getChildren().add(object);

    }}