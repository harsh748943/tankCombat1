package com.example.tankmark1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TankGame extends Application {
    private Scene mainMenuScene;
    private Scene gameScene;
    private StartMenu startMenu;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Initialize the main menu and set up the initial scene
        startMenu = new StartMenu(primaryStage, this);
        mainMenuScene = new Scene(startMenu, 1260, 720);

        primaryStage.setTitle("Tank Game");
        primaryStage.setScene(mainMenuScene);
        primaryStage.show();
    }

    // Method to start the game, creating a new GameController with selected settings
    public void startGame(int numPlayers, String selectedWeapon, String selectedMap, boolean soundOn) {
        GameController gameController = new GameController(numPlayers, selectedWeapon, selectedMap, soundOn, this);
        gameScene = new Scene(gameController, 1260, 720);

        primaryStage.setScene(gameScene);
        gameController.startGame();
    }

    // Method to return to the main menu
    public void returnToMenu() {
        primaryStage.setScene(mainMenuScene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
