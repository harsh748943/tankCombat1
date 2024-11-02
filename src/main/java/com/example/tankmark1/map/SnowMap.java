package com.example.tankmark1.map;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnowMap extends Map {
    public static final int WIDTH = 1200;
    public static final int HEIGHT = 800;
    private static final double MIN_DISTANCE = 50;
    private Random random = new Random();

    // Lists for snow elements and trees
    private List<double[]> snowCoveredTreesType1 = new ArrayList<>();
    private List<double[]> snowCoveredTreesType2 = new ArrayList<>();
    private List<double[]> rocks = new ArrayList<>();
    private List<double[]> bushes = new ArrayList<>();
    private List<double[]> flowers = new ArrayList<>();
    private List<Snowflake> snowflakes = new ArrayList<>(); // List to hold snowflakes

    // Images for static elements
    private Image snowTreeImageType1, snowTreeImageType2, bushImage, flowerImage;

    public SnowMap() {
        setPrefSize(WIDTH, HEIGHT);

        // Load images for static elements
        snowTreeImageType1 = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowTree1.png");
        snowTreeImageType2 = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowTree2.png");
        bushImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowBhuses.png");
        flowerImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowflower.png");

        // Generate positions for snow-covered trees, bushes, and flowers
        generateSnowElements();
        generateSnowflakes(); // Generate initial snowflakes

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        // Start the snowfall animation
        startSnowfallAnimation(gc);

        // Adding canvas to layout
        StackPane layout = new StackPane();
        layout.getChildren().add(canvas);
        getChildren().add(layout);
    }

    private void generateSnowElements() {
        // Populate snow-covered trees, bushes, and flowers with randomly generated positions
        for (int i = 0; i < 20; i++) snowCoveredTreesType1.add(generatePosition());
        for (int i = 0; i < 20; i++) snowCoveredTreesType2.add(generatePosition());
        for (int i = 0; i < 15; i++) rocks.add(generatePosition());
        for (int i = 0; i < 20; i++) bushes.add(generatePosition());
        for (int i = 0; i < 25; i++) flowers.add(generatePosition());
    }

    private void generateSnowflakes() {
        // Create snowflakes with random initial positions, speeds, and sizes
        for (int i = 0; i < 100; i++) { // Number of snowflakes
            double x = random.nextDouble() * WIDTH;
            double y = random.nextDouble() * HEIGHT;
            double speed = 1 + random.nextDouble() * 3; // Random speed for each snowflake
            double size = 5 + random.nextDouble() * 15; // Random size for each snowflake
            snowflakes.add(new Snowflake(x, y, speed, size));
        }
    }

    private double[] generatePosition() {
        double x, y;
        do {
            x = random.nextDouble() * (WIDTH - 50);
            y = random.nextDouble() * (HEIGHT - 50);
        } while (isOverlapping(x, y));
        return new double[]{x, y};
    }

    private void drawSnowElements(GraphicsContext gc) {
        // Draw a solid white background to represent snow
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw snow-covered trees (two types), rocks, bushes, and flowers
        drawElements(gc, snowCoveredTreesType1, snowTreeImageType1, 50, 110); // Snow-covered trees type 1
        drawElements(gc, snowCoveredTreesType2, snowTreeImageType2, 50, 110); // Snow-covered trees type 2
        drawElements(gc, rocks, null, 50, 50); // Placeholder for rocks if needed
        drawElements(gc, bushes, bushImage, 60, 40); // Bushes
        drawElements(gc, flowers, flowerImage, 30, 50); // Flowers

        // Draw snowflakes as circles
        for (Snowflake snowflake : snowflakes) {
            gc.setFill(Color.WHITE); // Set color for snowflakes
            gc.fillOval(snowflake.getX(), snowflake.getY(), snowflake.getSize(), snowflake.getSize()); // Draw snowflake
        }
    }

    private void drawElements(GraphicsContext gc, List<double[]> elements, Image image, int width, int height) {
        for (double[] pos : elements) {
            if (image != null) {
                gc.drawImage(image, pos[0], pos[1], width, height); // Draw images for trees, bushes, and flowers
            }
        }
    }

    private boolean isOverlapping(double x, double y) {
        return isOverlappingWithList(x, y, snowCoveredTreesType1) ||
                isOverlappingWithList(x, y, snowCoveredTreesType2) ||
                isOverlappingWithList(x, y, rocks) ||
                isOverlappingWithList(x, y, bushes) ||
                isOverlappingWithList(x, y, flowers);
    }

    private boolean isOverlappingWithList(double x, double y, List<double[]> elements) {
        for (double[] element : elements) {
            double elementX = element[0];
            double elementY = element[1];
            if (Math.sqrt(Math.pow(x - elementX, 2) + Math.pow(y - elementY, 2)) < MIN_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    private void startSnowfallAnimation(GraphicsContext gc) {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                // Update snowflakes
                for (Snowflake snowflake : snowflakes) {
                    snowflake.update();
                }
                // Draw elements
                drawSnowElements(gc);
            }
        };
        timer.start();
    }
}
