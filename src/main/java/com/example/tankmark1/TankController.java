package com.example.tankmark1;

import com.example.tankmark1.map.DestructibleObject;
import com.example.tankmark1.tanks.ComputerTank;
import com.example.tankmark1.tanks.Tank;
import javafx.scene.layout.Pane;

import java.util.List;

public class TankController {
    Pane pane;
    public Tank tank1;
    public Tank tank2;
    private boolean isRobot=false;
    private String selectedWeapon;
    private int numPlayers;
    private ComputerTank computerTank;
    private String level;
    private List<DestructibleObject> destructibleObjects;
    TankController(Pane pane,String selectedWeapon,int numPlayers,String level,List<DestructibleObject> destructibleObjects){
        this.pane=pane;
        this.selectedWeapon=selectedWeapon;
        this.numPlayers=numPlayers;
        this.level=level;
        this.destructibleObjects=destructibleObjects;
    }

    public void setUpTanks() {

        tank1 = new Tank(100, 100, "tank.png", selectedWeapon);
        pane.getChildren().add(tank1);
        pane.getChildren().add(tank1.getShieldCircle()); // Assuming you have a getter for shieldCircle


        tank2 = new Tank(1300, 700, "tank3.png", selectedWeapon);
        if(numPlayers>1) {
            pane.getChildren().add(tank2);
            pane.getChildren().add(tank2.getShieldCircle());

        }
        else {
            isRobot=true;
            computerTank=new ComputerTank(tank2,tank1,(GameController) pane,level,destructibleObjects);
            computerTank.setComputerControlled();

            pane.getChildren().add(tank2);
        }
    }
}
