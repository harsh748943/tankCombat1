package com.example.tankmark1.map;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MazeMap extends Map {
    public MazeMap() {
        setStyle("-fx-background-color: DARKRED;"); // Load your snow background image




        addDestructibleObjects();
    }




    private void addDestructibleObjects() {

        // Create trees

        createDestructibleObject("m.png", 60, 90, 200, 140);
        createDestructibleObject("m.png", 60, 90, 265, 140);
        createDestructibleObject("m.png", 60, 90, 330, 140);
        createDestructibleObject("m.png", 60, 90, 395, 140);
        createDestructibleObject("m.png", 60, 90, 460, 140);
        createDestructibleObject("m.png", 60, 90, 525, 140);
        createDestructibleObject("m.png", 60, 90, 590, 140);


        createDestructibleObject("m.png", 60, 90, 915, 140);
        createDestructibleObject("m.png", 60, 90, 980, 140);
        createDestructibleObject("m.png", 60, 90, 1045, 140);
        createDestructibleObject("m.png", 60, 90, 1110, 140);
        createDestructibleObject("m.png", 60, 90, 1175, 140);
        createDestructibleObject("m.png", 60, 90, 1240, 140);
        createDestructibleObject("m.png", 60, 90, 1305, 140);


        createDestructibleObject("magic.png", 90, 60, 200, 185);
        createDestructibleObject("magic.png", 90, 60, 200, 250);
        createDestructibleObject("magic.png", 90, 60, 200, 315);
        createDestructibleObject("magic.png", 90, 60, 200, 380);
        createDestructibleObject("magic.png", 90, 60, 200, 445);
        createDestructibleObject("magic.png", 90, 60, 200, 510);
        createDestructibleObject("magic.png", 90, 60, 200, 575);
//        createDestructibleObject("magic.png", 90, 60, 200, 640);






        createDestructibleObject("m.png", 60, 90, 200, 640);
        createDestructibleObject("m.png", 60, 90, 265, 640);
        createDestructibleObject("m.png", 60, 90, 330, 640);
        createDestructibleObject("m.png", 60, 90, 395, 640);
        createDestructibleObject("m.png", 60, 90, 460, 640);
        createDestructibleObject("m.png", 60, 90, 525, 640);
        createDestructibleObject("m.png", 60, 90, 590, 640);


        createDestructibleObject("m.png", 60, 90, 915, 640);
        createDestructibleObject("m.png", 60, 90, 980, 640);
        createDestructibleObject("m.png", 60, 90, 1045, 640);
        createDestructibleObject("m.png", 60, 90, 1110, 640);
        createDestructibleObject("m.png", 60, 90, 1175, 640);
        createDestructibleObject("m.png", 60, 90, 1240, 640);
        createDestructibleObject("m.png", 60, 90, 1305, 640);





        createDestructibleObject("magic.png", 90, 60, 1325, 185);
        createDestructibleObject("magic.png", 90, 60, 1325, 250);
        createDestructibleObject("magic.png", 90, 60, 1325, 315);
        createDestructibleObject("magic.png", 90, 60, 1325, 380);
        createDestructibleObject("magic.png", 90, 60, 1325, 445);
        createDestructibleObject("magic.png", 90, 60, 1325, 510);
        createDestructibleObject("magic.png", 90, 60, 1325, 575);






        //under ka box

        createDestructibleObject("m.png", 50, 30, 360, 315);
        createDestructibleObject("m.png", 50, 30, 425, 315);
        createDestructibleObject("m.png", 50, 30, 490, 315);
        createDestructibleObject("m.png", 50, 30, 550, 315);
        createDestructibleObject("m.png", 50, 30, 615, 315);
        createDestructibleObject("m.png", 50, 30, 680, 315);
        createDestructibleObject("m.png", 50, 30, 745, 315);
        createDestructibleObject("m.png", 50, 30, 810, 315);
        createDestructibleObject("m.png", 50, 30, 875, 315);
        createDestructibleObject("m.png", 50, 30, 940, 315);
        createDestructibleObject("m.png", 50, 30, 1005, 315);
        createDestructibleObject("m.png", 50, 30, 1070, 315);
        createDestructibleObject("m.png", 50, 30, 1135, 315);


        createDestructibleObject("m.png", 50, 30, 360, 490);
        createDestructibleObject("m.png", 50, 30, 425, 490);
        createDestructibleObject("m.png", 50, 30, 490, 490);
        createDestructibleObject("m.png", 50, 30, 550, 490);
        createDestructibleObject("m.png", 50, 30, 615, 490);
        createDestructibleObject("m.png", 50, 30, 680, 490);
        createDestructibleObject("m.png", 50, 30, 745, 490);
        createDestructibleObject("m.png", 50, 30, 810, 490);
        createDestructibleObject("m.png", 50, 30, 875, 490);
        createDestructibleObject("m.png", 50, 30, 940, 490);
        createDestructibleObject("m.png", 50, 30, 1005, 490);
        createDestructibleObject("m.png", 50, 30, 1070, 490);
        createDestructibleObject("m.png", 50, 30, 1135, 490);





//        createDestructibleObject("magic.png", 90, 60, 200, 705);
//        createDestructibleObject("magic.png", 90, 60, 200, 770);
//        createDestructibleObject("magic.png", 90, 60, 200, 835);
//        createDestructibleObject("magic.png", 90, 60, 200, 900);

//        createDestructibleObject("safed sikhi lakdi.png", 80, 100, 400, 600);
//
//        createDestructibleObject("sukhi lakdi.png", 80, 100, 1000, 350);
//
//        createDestructibleObject("barf wala ped.png", 80, 100, 700, 500);
//
//        createDestructibleObject("barf wala ped.png", 80, 100, 1100, 700);
//
//        createDestructibleObject("barf wala ped.png", 80, 100, 900, 50);
//
//        // Create rocks
//
//        createDestructibleObject("badi sukhi lakdi.png", 100, 100, 1200, 250);
//
//        createDestructibleObject("orange ped.png", 100, 100, 120, 600);
//
//        createDestructibleObject("orange ped 2.png", 100, 100, 375, 100);
//
//        createDestructibleObject("rock.png", 100, 100, 700, 250);
//
//
//        // Create down forest objects
//
////        for (int i = 0; i < 16; i++) {
////
////            createDestructibleObject("down forest.png", 200, 100, 1 + (i * 95), 692);
////
////        }
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

