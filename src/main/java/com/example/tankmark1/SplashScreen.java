package com.example.tankmark1;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Paths;

public class SplashScreen {
    public void showSplash(Stage primaryStage, Runnable afterSplash) {
        // Create an image view with a splash image
        ImageView splashImage = new ImageView(new Image(getClass().getResource("/splash.jpg").toExternalForm()));

        // Make the splash image cover the initial stage dimensions while preserving the aspect ratio
        splashImage.setPreserveRatio(true);
        splashImage.setFitWidth(1260);  // Set a preferred width for the splash image
        splashImage.setFitHeight(720);  // Set a preferred height for the splash image

        // Load the audio file and create a media player
        Media splashSound = new Media(getClass().getResource("/explosion_sound/splash_sound.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(splashSound);

        // Play the sound when the splash screen appears
        mediaPlayer.play();

        // Create a layout for the splash screen
        StackPane splashLayout = new StackPane(splashImage);
        Scene splashScene = new Scene(splashLayout, 1260, 720);  // Set the scene size to match the splash image

        primaryStage.setScene(splashScene);
        primaryStage.show();

        // Set a fade-out transition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(4), splashImage);
        fadeTransition.setFromValue(1.0);  // Start fully opaque
        fadeTransition.setToValue(0.0);    // End fully transparent
        fadeTransition.setOnFinished(event -> {
            mediaPlayer.stop();           // Stop the sound after the transition
            Platform.runLater(afterSplash);  // Run after splash is gone
        });

        // Start the transition after a delay
        Platform.runLater(() -> fadeTransition.play());
    }
}
