package com.example.tankmark1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TankGame extends Application {
    @Override
    public void start(Stage primaryStage) {
//        GameController gameController = new GameController(2, "Rocket", "Forest", true);
        //Scene scene = new Scene(gameController, 800, 600);
        StartMenu startMenu = new StartMenu(primaryStage);
        Scene scene = new Scene(startMenu, 800, 600);

        primaryStage.setTitle("Tank Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Call startGame after showing the scene to ensure it has focus
        //gameController.startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
