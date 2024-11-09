package com.example.tankmark1.map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.util.Random;
import javafx.scene.layout.Pane;

public class SnowfallEffect extends Pane {
    private final int SNOWFLAKE_COUNT = 100; // Number of snowflakes
    private final Random random = new Random();

    public SnowfallEffect(double width, double height) {
        setPrefSize(width, height);
        createSnowfall(width, height);
    }

    private void createSnowfall(double width, double height) {
        for (int i = 0; i < SNOWFLAKE_COUNT; i++) {
            Circle snowflake = new Circle(2 + random.nextDouble() * 3); // Snowflake size varies from 2-5 px
            snowflake.setFill(Color.AQUAMARINE);
            snowflake.setOpacity(0.7 + random.nextDouble() * 0.3); // Random transparency

            // Initial position at random x and at the top of the scene or slightly above
            snowflake.setTranslateX(random.nextDouble() * width);
            snowflake.setTranslateY(-random.nextDouble() * height);

            // Add to the pane
            getChildren().add(snowflake);

            // Animate snowflake to fall
            animateSnowflake(snowflake, height);
        }
    }

    private void animateSnowflake(Circle snowflake, double height) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> {
            // Move snowflake downwards by a small step each frame
            snowflake.setTranslateY(snowflake.getTranslateY() + 1 + random.nextDouble() * 2);

            // Add a slight horizontal drift
            snowflake.setTranslateX(snowflake.getTranslateX() + (random.nextDouble() - 0.5) * 1);

            // If snowflake reaches the bottom, reset it to the top at a new random x position
            if (snowflake.getTranslateY() > height) {
                snowflake.setTranslateY(-10);
                snowflake.setTranslateX(random.nextDouble() * getPrefWidth());
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE); // Loop the animation
        timeline.play();
    }
}
