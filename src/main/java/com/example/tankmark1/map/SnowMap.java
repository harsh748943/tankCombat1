package com.example.tankmark1.map;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SnowMap extends Map {
    public SnowMap() {
        setStyle("-fx-background-color: snow;"); // Load your snow background image

        addDestructibleObjects();
    }




    private void addDestructibleObjects() {

        // Create trees

        createDestructibleObject("safed ped.png", 80, 100, 500, 300);

        createDestructibleObject("safed sikhi lakdi.png", 80, 100, 400, 600);

        createDestructibleObject("sukhi lakdi.png", 80, 100, 1000, 350);

        createDestructibleObject("barf wala ped.png", 80, 100, 700, 500);

        createDestructibleObject("barf wala ped.png", 80, 100, 1100, 700);

        createDestructibleObject("barf wala ped.png", 80, 100, 900, 50);

        // Create rocks

        createDestructibleObject("badi sukhi lakdi.png", 100, 100, 1400, 250);

        createDestructibleObject("orange ped.png", 100, 100, 100, 600);

        createDestructibleObject("orange ped 2.png", 100, 100, 375, 100);

        createDestructibleObject("rock.png", 100, 100, 700, 250);


        // Create down forest objects

//        for (int i = 0; i < 16; i++) {
//
//            createDestructibleObject("down forest.png", 200, 100, 1 + (i * 95), 692);
//
//        }

        for (int i = 0; i < 16; i++) {

            createDestructibleObject("upper forest.png", 200, 100, 1 + (i * 95), 1);

        }

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