package com.example.tankmark1.map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;
import javafx.scene.layout.Pane;

public class DustEffect extends Pane {
    private final int DUST_PARTICLE_COUNT = 50; // Number of dust particles
    private final Random random = new Random();

    public DustEffect(double width, double height) {
        setPrefSize(width, height);
        createDustParticles(width, height);
    }

    private void createDustParticles(double width, double height) {
        for (int i = 0; i < DUST_PARTICLE_COUNT; i++) {
            // Dust particle represented as a small circle
            Circle dustParticle = new Circle(2 + random.nextDouble() * 3); // Particle size varies from 2-5 px
            dustParticle.setFill(Color.BLACK); // Dusty desert color
            dustParticle.setOpacity(0.4 + random.nextDouble() * 0.3); // Random transparency between 0.4 and 0.7

            // Initial position on the left side with a random y-position
            dustParticle.setTranslateX(-random.nextDouble() * width);
            dustParticle.setTranslateY(random.nextDouble() * height);

            // Add the particle to the pane
            getChildren().add(dustParticle);

            // Animate the dust particle to move horizontally from left to right
            animateDustParticle(dustParticle, width, height);
        }
    }

    private void animateDustParticle(Circle dustParticle, double width, double height) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            // Move the particle to the right with a small random speed
            dustParticle.setTranslateX(dustParticle.getTranslateX() + 1 + random.nextDouble() * 2);

            // Add a slight vertical drift
            dustParticle.setTranslateY(dustParticle.getTranslateY() + (random.nextDouble() - 0.5) * 0.5);

            // If dust particle reaches the right side, reset it to the left at a random y position
            if (dustParticle.getTranslateX() > width) {
                dustParticle.setTranslateX(-10); // Reset to the left edge
                dustParticle.setTranslateY(random.nextDouble() * height); // New random height position
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Loop the animation
        timeline.play();
    }
}
