package com.example.tankmark1.map;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileMap extends Map {

    public TileMap() {
        setStyle("-fx-background-color: black;"); // Load your desert background image

    addDestructibleObjects();
}
    private void addDestructibleObjects() {

        createDestructibleObject("grass.png", 30, 70, 300, 250);
        createDestructibleObject("mud.png", 30, 70, 300, 300);

        createDestructibleObject("mud.png", 30, 70, 300, 450);

        createDestructibleObject("grass.png", 30, 70, 300, 350);

        createDestructibleObject("grass.png", 30, 70, 300, 400);
        createDestructibleObject("grass.png", 30, 70, 300, 500);



        createDestructibleObject("mudLeft.png", 30, 70, 600, 10);

        createDestructibleObject("grass.png", 30, 70, 600, 60);

        createDestructibleObject("mudLeft.png", 30, 70, 600, 110);

        createDestructibleObject("grass.png", 30, 70, 600, 160);
        createDestructibleObject("mudLeft.png", 30, 70, 600, 210);

        createDestructibleObject("grass.png", 30, 70, 600, 260);
        createDestructibleObject("mudLeft.png", 30, 70, 600, 310);

        createDestructibleObject("grass.png", 30, 70, 600, 360);




        createDestructibleObject("mud.png", 30, 70, 800, 400);

        createDestructibleObject("grass.png", 30, 70, 800, 450);

        createDestructibleObject("mud.png", 30, 70, 800, 500);

        createDestructibleObject("grass.png", 30, 70, 800, 550);

        createDestructibleObject("mud.png", 30, 70, 800, 600);

        createDestructibleObject("grass.png", 30, 70, 800, 650);
        createDestructibleObject("mud.png", 30, 70, 800, 700);

        createDestructibleObject("grass.png", 30, 70, 800, 750);



        createDestructibleObject("mud.png", 30, 70, 1100, 10);

        createDestructibleObject("grass.png", 30, 70, 1100, 60);
        createDestructibleObject("mud.png", 30, 70, 1100, 110);

        createDestructibleObject("grass.png", 30, 70, 1100, 160);

        createDestructibleObject("mud.png", 30, 70, 1100, 210);

        createDestructibleObject("grass.png", 30, 70, 1100, 260);



        createDestructibleObject("mud.png", 30, 70, 1300, 400);

        createDestructibleObject("grass.png", 30, 70, 1300, 450);
        createDestructibleObject("mud.png", 30, 70, 1300, 500);

        createDestructibleObject("grass.png", 30, 70, 1300, 550);

        createDestructibleObject("mud.png", 30, 70, 1300, 600);

        createDestructibleObject("grass.png", 30, 70, 1300, 650);

//        for (int i = 0; i < 16; i++) {
//
//            createDestructibleObject("down forest.png", 200, 100, 1 + (i * 95), 692);
//
//        }
//
//        for (int i = 0; i < 16; i++) {
//
//            createDestructibleObject("upper forest.png", 200, 100, 1 + (i * 95), 1);
//
//        }

    }

    public List<DestructibleObject> getDestructibleObjects() {
        return getChildren().stream()
                .filter(node -> node instanceof DestructibleObject)
                .map(node -> (DestructibleObject) node)
                .collect(Collectors.toList());
    }


    private List<DestructibleObject> destructibleList = new ArrayList<>();

    private void createDestructibleObject(String imagePath, int width, int height, double x, double y) {
        try {
            URL imageUrl = getClass().getResource("/" + imagePath);
            if (imageUrl == null) {
                throw new IllegalArgumentException("Image not found: " + imagePath);
            }
            DestructibleObject object = new DestructibleObject(imageUrl.toExternalForm(), width, height, destructibleList);
            object.setX(x);
            object.setY(y);
            destructibleList.add(object);
            getChildren().add(object);
        } catch (Exception e) {
            System.err.println("Failed to load image: " + imagePath + " - " + e.getMessage());
        }
    }

}



