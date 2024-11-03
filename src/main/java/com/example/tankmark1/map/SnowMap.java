package com.example.tankmark1.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SnowMap extends Map {
  public  static final int WIDTH = 800; // Updated width to match ForestMap
  public static final int HEIGHT = 600; // Updated height to match ForestMap
    private static final double MIN_DISTANCE = 50; // Maintain the same minimum distance
    private Random random = new Random();

    // Lists for snow elements
    private List<double[]> snowCoveredTreesType1 = new ArrayList<>();
    private List<double[]> snowCoveredTreesType2 = new ArrayList<>();
    private List<double[]> rocks = new ArrayList<>();
    private List<double[]> bushes = new ArrayList<>();
    private List<double[]> flowers = new ArrayList<>();

    // Images for snow elements
    private Image snowTreeImageType1, snowTreeImageType2, rockImage, bushImage, flowerImage;

    public SnowMap() {
        setPrefSize(WIDTH, HEIGHT);
        setStyle("-fx-background-color: white;");

        // Load images for snow elements
        snowTreeImageType1 = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowTree1.png");
        snowTreeImageType2 = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowTree2.png");
        rockImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowRock.png"); // Ensure you have this image
        bushImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowBushes.png");
        flowerImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\snowFlower.png");

        // Generate positions for snow-covered trees, rocks, bushes, and flowers
        generateSnowElements();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawSnowElements(gc); // Initial draw

        // Adding canvas to layout
        StackPane layout = new StackPane();
        layout.getChildren().add(canvas);
        getChildren().add(layout);
    }

    private void generateSnowElements() {
        // Populate snow-covered trees, rocks, bushes, and flowers with randomly generated positions
        for (int i = 0; i < 20; i++) snowCoveredTreesType1.add(generatePosition(50, 110)); // Snow trees type 1
        for (int i = 0; i < 20; i++) snowCoveredTreesType2.add(generatePosition(50, 110)); // Snow trees type 2
        for (int i = 0; i < 15; i++) rocks.add(generatePosition(50, 50)); // Rocks
        for (int i = 0; i < 15; i++) bushes.add(generatePosition(60, 40)); // Bushes
        for (int i = 0; i < 15; i++) flowers.add(generatePosition(30, 50)); // Flowers
    }

    private double[] generatePosition(int width, int height) {
        double x, y;
        do {
            x = random.nextDouble() * (WIDTH - width);
            y = random.nextDouble() * (HEIGHT - height);
        } while (isOverlapping(x, y, width, height));
        return new double[]{x, y};
    }

    private boolean isOverlapping(double x, double y, int width, int height) {
        return isOverlappingWithList(x, y, width, height, snowCoveredTreesType1) ||
                isOverlappingWithList(x, y, width, height, snowCoveredTreesType2) ||
                isOverlappingWithList(x, y, width, height, rocks) ||
                isOverlappingWithList(x, y, width, height, bushes) ||
                isOverlappingWithList(x, y, width, height, flowers);
    }

    private boolean isOverlappingWithList(double x, double y, int width, int height, List<double[]> elements) {
        for (double[] element : elements) {
            double elementX = element[0];
            double elementY = element[1];
            if (Math.sqrt(Math.pow(x - elementX, 2) + Math.pow(y - elementY, 2)) < MIN_DISTANCE) {
                return true;
            }
        }
        return false;
    }

    private void drawSnowElements(GraphicsContext gc) {
        // Draw background
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw snow-covered trees, rocks, bushes, and flowers
        drawElements(gc, snowCoveredTreesType1, snowTreeImageType1, 50, 110); // Snow trees type 1
        drawElements(gc, snowCoveredTreesType2, snowTreeImageType2, 50, 110); // Snow trees type 2
        drawElements(gc, rocks, rockImage, 50, 50); // Rocks
        drawElements(gc, bushes, bushImage, 60, 40); // Bushes
        drawElements(gc, flowers, flowerImage, 30, 50); // Flowers
    }

    private void drawElements(GraphicsContext gc, List<double[]> elements, Image image, int width, int height) {
        for (double[] pos : elements) {
            if (image != null) {
                gc.drawImage(image, pos[0], pos[1], width, height);
            } else {
                // Draw as gray rectangles if no image is provided
                gc.setFill(Color.GRAY);
                gc.fillRect(pos[0], pos[1], width, height);
            }
        }
    }

    // Method to retrieve the boundaries of the elements for collision detection
    public List<Boundary> getBoundaries() {
        List<Boundary> boundaries = new ArrayList<>();

        // Create boundaries for snow-covered trees type 1
        for (double[] pos : snowCoveredTreesType1) {
            boundaries.add(new Boundary(pos[0], pos[1], 50, 110)); // Assuming trees are 50x110
        }
        // Create boundaries for snow-covered trees type 2
        for (double[] pos : snowCoveredTreesType2) {
            boundaries.add(new Boundary(pos[0], pos[1], 50, 110)); // Assuming trees are 50x110
        }
        // Create boundaries for rocks
        for (double[] pos : rocks) {
            boundaries.add(new Boundary(pos[0], pos[1], 50, 50)); // Assuming rocks are 50x50
        }
        // Create boundaries for bushes
        for (double[] pos : bushes) {
            boundaries.add(new Boundary(pos[0], pos[1], 60, 40)); // Assuming bushes are 60x40
        }
        // Create boundaries for flowers
        for (double[] pos : flowers) {
            boundaries.add(new Boundary(pos[0], pos[1], 30, 50)); // Assuming flowers are 30x50
        }

        return boundaries;
    }
}
