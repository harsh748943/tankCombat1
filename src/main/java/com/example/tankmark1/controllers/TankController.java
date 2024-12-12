package com.example.tankmark1.controllers;

import com.example.tankmark1.map.DestructibleObject;
import com.example.tankmark1.tanks.ComputerTank;
import com.example.tankmark1.tanks.Tank;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TankController {
    Pane pane;

    ArrayList<Tank>tanks;
    private boolean isRobot=false;
    private String selectedWeapon;
    private int numPlayers;
    private ComputerTank computerTank;
    private String level;
    GameController gameController;
    private List<DestructibleObject> destructibleObjects;
    ProjectileController projectileController;
    Set<KeyCode> pressedKeys;
    TankController(Pane pane, String selectedWeapon, int numPlayers, String level, List<DestructibleObject> destructibleObjects, GameController gameController, ArrayList<Tank>tanks, Set<KeyCode> pressedKeys,ProjectileController projectileController){
        this.pane=pane;
        this.selectedWeapon=selectedWeapon;
        this.numPlayers=numPlayers;
        this.level=level;
        this.destructibleObjects=destructibleObjects;
        this.gameController=gameController;
        this.tanks=tanks;
        this.pressedKeys=pressedKeys;
        this.projectileController=projectileController;
    }

    public void setUpTanks() {

        tanks.add( new Tank(100, 100, "tank.png", selectedWeapon));
        //allTanks.add(tank1);
        gameController.getChildren().add(tanks.get(0));
        gameController.getChildren().add(tanks.get(0).getShieldCircle()); // Assuming you have a getter for shieldCircle


        tanks.add(new Tank(1300, 700, "tank3.png", selectedWeapon));
       // allTanks.add(tanks.get(1));
        if(numPlayers>1) {
            gameController.getChildren().add(tanks.get(1));
            gameController.getChildren().add(tanks.get(1).getShieldCircle());

        }
        else {
            isRobot=true;
            computerTank=new ComputerTank(tanks.get(1),tanks.get(0),gameController,level,destructibleObjects,projectileController);
            computerTank.setComputerControlled();

            gameController.getChildren().add(tanks.get(1));
        }
    }


    public void moveTanks() {

        if (pressedKeys.contains(KeyCode.Q)) tanks.get(0).rotate(-5); // Rotate counterclockwise
        if (pressedKeys.contains(KeyCode.E)) tanks.get(0).rotate(5); // Rotate clockwise

        if (pressedKeys.contains(KeyCode.NUMPAD7)) tanks.get(1).rotate(-5); // Rotate counterclockwise
        if (pressedKeys.contains(KeyCode.NUMPAD9)) tanks.get(1).rotate(5); // Rotate clockwise
        if (pressedKeys.contains(KeyCode.P)) tanks.get(1).rotate(5);

        if (tanks.get(0) != null) {
            if (pressedKeys.contains(KeyCode.W)) updateTankPosition(tanks.get(0), 0, -1);
            if (pressedKeys.contains(KeyCode.S)) updateTankPosition(tanks.get(0), 0, 1);
            if (pressedKeys.contains(KeyCode.A)) updateTankPosition(tanks.get(0), -1, 0);
            if (pressedKeys.contains(KeyCode.D)) updateTankPosition(tanks.get(0), 1, 0);
            if (pressedKeys.contains(KeyCode.SPACE)) tanks.get(0).shoot(projectileController);
        }
        if (tanks.get(1) != null&&!isRobot) {
            if (pressedKeys.contains(KeyCode.UP)) updateTankPosition(tanks.get(1), 0, -1);
            if (pressedKeys.contains(KeyCode.DOWN)) updateTankPosition(tanks.get(1), 0, 1);
            if (pressedKeys.contains(KeyCode.LEFT)) updateTankPosition(tanks.get(1), -1, 0);
            if (pressedKeys.contains(KeyCode.RIGHT)) updateTankPosition(tanks.get(1), 1, 0);
            if (pressedKeys.contains(KeyCode.ENTER)) tanks.get(1).shoot(projectileController);
        }
    }

    private void updateTankPosition(Tank tank, double dx, double dy) {
        Platform.runLater(() -> tank.move(dx, dy,destructibleObjects));
        tank.updateShieldPosition(); // Update the shield position when the tank moves
    }
}
