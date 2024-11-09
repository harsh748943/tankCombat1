package com.example.tankmark1.weapons;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;


public class Explosion extends ImageView {
    private final int columns;  // Number of columns in sprite sheet
    private final int rows;     // Number of rows in sprite sheet
    private final int totalFrames;
    private final int frameWidth;
    private final int frameHeight;
    private int currentFrame = 0;
    private MediaPlayer mediaPlayer;

    // Constructor for explosion animation with sound, image, and earthquake effect
    public Explosion(double x, double y, String spriteSheetPath, String soundFilePath,
                     int columns, int rows, int frameWidth, int frameHeight, Node shakeNode,boolean isEarthQuake) {

        // Load explosion sprite sheet
        Image spriteSheet = new Image(getClass().getResource(spriteSheetPath).toExternalForm());
        setImage(spriteSheet);
        setX(x);
        setY(y);
        setFitWidth(frameWidth);
        setFitHeight(frameHeight);

        this.columns = columns;
        this.rows = rows;
        this.totalFrames = columns * rows;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;

        // Initially set the viewport to the first frame to avoid showing unwanted frames
        setViewport(new Rectangle2D(0, 0, frameWidth, frameHeight));

        // Play sound if a valid path is provided
        if (soundFilePath != null && !soundFilePath.isEmpty()) {
            playSoundEffect(soundFilePath);
        }

        // Create and start the earthquake effect
        if(isEarthQuake) {
            Earthquake earthquake = new Earthquake(shakeNode, 5.0, .3);  // Shake intensity and duration
            earthquake.startEarthquake();  // Trigger earthquake effect
        }
        // Timeline for animating frames
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), e -> nextFrame()));
        timeline.setCycleCount(totalFrames);  // Total number of frames to animate
        timeline.setOnFinished(event -> {
            this.setVisible(false);  // Hide after animation finishes
            System.out.println("Explosion animation finished");
        });
        timeline.play();
    }

    private void nextFrame() {
        int column = currentFrame % columns; // Calculate the column of the frame
        int row = currentFrame / columns;    // Calculate the row of the frame

        // Calculate the x and y offsets based on the current frame's position
        double xOffset = column * frameWidth;
        double yOffset = row * frameHeight;

        // Update the viewport to display the next frame
        setViewport(new Rectangle2D(xOffset, yOffset, frameWidth, frameHeight));

        // Increment the frame counter for the next frame
        currentFrame++;
    }

    // Method to play explosion sound effect
    private void playSoundEffect(String soundFilePath) {
        Media sound = new Media(getClass().getResource(soundFilePath).toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
