package com.example.tankmark1;

import com.example.tankmark1.controllers.GameController;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TankGame extends Application {
    private Scene mainMenuScene;
    private Scene gameScene;
    private StartMenu startMenu;
    private Stage primaryStage;
    Rectangle2D screenBounds = Screen.getPrimary().getBounds();
    double screenWidth = screenBounds.getWidth();
    double screenHeight = screenBounds.getHeight();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Tank Combat");
        Image image=new Image("/titlebar_logo.png");
        primaryStage.getIcons().add(image);

        // Create and show the splash screen
        SplashScreen splashScreen = new SplashScreen();
        splashScreen.showSplash(primaryStage, () -> {
            // After the splash screen, show the StartMenu
            startMenu = new StartMenu(primaryStage, this);
            mainMenuScene = new Scene(startMenu, screenWidth, screenHeight);
            primaryStage.setScene(mainMenuScene);
            primaryStage.setMaximized(true); // Maximize the window
            primaryStage.show();
        });
    }

    // Method to start the game, creating a new GameController with selected settings
    public void startGame(int numPlayers, String selectedWeapon, String selectedMap, boolean soundOn,String level) {
        GameController gameController = new GameController(numPlayers, selectedWeapon, selectedMap, soundOn, this,level);
        gameScene = new Scene(gameController, screenWidth, screenHeight);

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
