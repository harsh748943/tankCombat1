package com.example.tankmark1.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DesertMap extends Map {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 1000;
    private static final double MIN_DISTANCE = 50;
    private Random random = new Random();

    // Lists for desert elements
    private List<double[]> cacti = new ArrayList<>();
    private List<double[]> rocks = new ArrayList<>();
    private List<double[]> bushes = new ArrayList<>();
    private List<double[]> flowers = new ArrayList<>();

    // Images for desert elements
    private Image cactusImage, rockImage, bushImage, flowerImage;

    public DesertMap() {
        setPrefSize(WIDTH, HEIGHT);
        setStyle("-fx-background-color: brown;");

        // Load images for desert elements
        cactusImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\cactus.png");
        rockImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\cactus2.png");
        bushImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\cactusbushes.png");
        flowerImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\smallcactus.png");

        // Generate positions for cacti, rocks, bushes, and flowers
        generateDesertElements();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawDesertElements(gc); // Initial draw

        // Adding canvas to layout
        StackPane layout = new StackPane();
        layout.getChildren().add(canvas);
        getChildren().add(layout);
    }

    private void generateDesertElements() {
        // Populate cacti, rocks, bushes, and flowers with randomly generated positions
        for (int i = 0; i < 30; i++) cacti.add(generatePosition());
        for (int i = 0; i < 15; i++) rocks.add(generatePosition());
        for (int i = 0; i < 20; i++) bushes.add(generatePosition());
        for (int i = 0; i < 25; i++) flowers.add(generatePosition());
    }

    private double[] generatePosition() {
        double x, y;
        do {
            x = random.nextDouble() * (WIDTH - 50);
            y = random.nextDouble() * (HEIGHT - 50);
        } while (isOverlapping(x, y));
        return new double[]{x, y};
    }

    private void drawDesertElements(GraphicsContext gc) {
        // Draw background
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw cacti, rocks, bushes, and flowers
        drawElements(gc, cacti, cactusImage, 50, 50); // Cacti dimensions
        drawElements(gc, rocks, rockImage, 50, 50); // Rocks dimensions
        drawElements(gc, bushes, bushImage, 40, 40); // Bushes dimensions
        drawElements(gc, flowers, flowerImage, 40, 40); // Flowers dimensions
    }

    private void drawElements(GraphicsContext gc, List<double[]> elements, Image image, int width, int height) {
        for (double[] pos : elements) {
            gc.drawImage(image, pos[0], pos[1], width, height);
        }
    }

    private boolean isOverlapping(double x, double y) {
        return isOverlappingWithList(x, y, cacti) || isOverlappingWithList(x, y, rocks) ||
                isOverlappingWithList(x, y, bushes) || isOverlappingWithList(x, y, flowers);
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
}
