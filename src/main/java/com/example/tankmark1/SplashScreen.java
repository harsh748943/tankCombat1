package com.example.tankmark1;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashScreen {
    public void showSplash(Stage primaryStage, Runnable afterSplash) {
        // Create an image view with a splash image
        ImageView splashImage = new ImageView(new Image(getClass().getResource("/splash.jpg").toExternalForm()));

        // Make the splash image cover the full screen
        splashImage.setPreserveRatio(true);
        splashImage.setFitWidth(primaryStage.getWidth());
        splashImage.setFitHeight(primaryStage.getHeight());

        // Create a layout for the splash screen
        StackPane splashLayout = new StackPane(splashImage);
        Scene splashScene = new Scene(splashLayout, primaryStage.getWidth(), primaryStage.getHeight());

        primaryStage.setScene(splashScene);
        primaryStage.show();

        // Set a fade-out transition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(7), splashImage);
        fadeTransition.setFromValue(1.0);  // Start fully opaque
        fadeTransition.setToValue(1.0);    // End fully transparent
        fadeTransition.setOnFinished(event -> Platform.runLater(afterSplash));  // Run after splash is gone

        // Start the transition after a delay (e.g., 3 seconds)
        Platform.runLater(() -> fadeTransition.play());
    }
}
