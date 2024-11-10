package com.example.tankmark1.weapons;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Node;
import javafx.util.Duration;

public class Earthquake {
    private final Node node;
    private final double intensity;    // How much the node will shake
    private final double duration;     // Duration of the earthquake effect

    // Constructor for Earthquake effect
    public Earthquake(Node node, double intensity, double duration) {
        this.node = node;
        this.intensity = intensity;
        this.duration = duration;
    }

    // Start the earthquake effect
    public void startEarthquake() {
        // Initial position of the node
        double originalX = node.getTranslateX();
        double originalY = node.getTranslateY();

        // Timeline to shake the node in random positions
        Timeline timeline = new Timeline();
        timeline.setCycleCount((int) (duration * 10));  // Duration of shaking, 10 frames per second

        for (int i = 0; i < duration * 10; i++) {
            double shakeAmountX = Math.random() * intensity - (intensity / 2);  // Random X shake
            double shakeAmountY = Math.random() * intensity - (intensity / 2);  // Random Y shake

            // Add a KeyFrame with the new shake values at each iteration
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(i * 100), e -> {
                node.setTranslateX(originalX + shakeAmountX);
                node.setTranslateY(originalY + shakeAmountY);
            }));
        }

        // Reset the node position after the shaking ends
        timeline.setOnFinished(event -> {
            node.setTranslateX(originalX);
            node.setTranslateY(originalY);
        });

        // Play the shake animation
        timeline.play();
    }
}

