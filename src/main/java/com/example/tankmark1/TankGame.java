package com.example.tankmark1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TankGame extends Application {
    @Override
    public void start(Stage primaryStage) {
        StartMenu startMenu = new StartMenu(primaryStage);
        Scene scene = new Scene(startMenu, 800, 600);

        primaryStage.setTitle("Tank Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
