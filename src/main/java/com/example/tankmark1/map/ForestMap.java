package com.example.tankmark1.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForestMap extends Map {
    private static final int WIDTH = 1200;
    private static final int HEIGHT = 800;
    private static final double MIN_DISTANCE = 50;
    private Random random = new Random();

    // Lists for forest elements
    private List<double[]> trees = new ArrayList<>();
    private List<double[]> rocks = new ArrayList<>();
    private List<double[]> bushes = new ArrayList<>();
    private List<double[]> flowers = new ArrayList<>();

    // Images for forest elements
    private Image treeImage, rockImage, bushImage, flowerImage;

    public ForestMap() {
        setPrefSize(WIDTH, HEIGHT);
        setStyle("-fx-background-color: green;");

        // Load images for forest elements
        treeImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\tree.png");
        rockImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\rock.png");
        bushImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\bushes.png");
        flowerImage = new Image("file:C:\\Users\\hp\\IdeaProjects\\tankCombat1\\src\\main\\resources\\flower.png");

        // Generate positions for trees, rocks, bushes, and flowers
        generateForestElements();

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawForestElements(gc); // Initial draw

        // Adding canvas to layout
        StackPane layout = new StackPane();
        layout.getChildren().add(canvas);
        getChildren().add(layout);
    }

    private void generateForestElements() {
        // Populate trees, rocks, bushes, and flowers with randomly generated positions
        for (int i = 0; i < 30; i++) trees.add(generatePosition());
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

    private void drawForestElements(GraphicsContext gc) {
        // Draw background
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw trees, rocks, bushes, and flowers
        drawElements(gc, trees, treeImage, 50, 60);  // Trees dimensions
        drawElements(gc, rocks, rockImage, 50, 40);  // Rocks dimensions
        drawElements(gc, bushes, bushImage, 50, 30);  // Bushes dimensions
        drawElements(gc, flowers, flowerImage, 20, 40); // Flowers dimensions
    }

    private void drawElements(GraphicsContext gc, List<double[]> elements, Image image, int width, int height) {
        for (double[] pos : elements) {
            gc.drawImage(image, pos[0], pos[1], width, height);
        }
    }

    private boolean isOverlapping(double x, double y) {
        return isOverlappingWithList(x, y, trees) || isOverlappingWithList(x, y, rocks) ||
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

    // Method to retrieve the boundaries of the elements for collision detection
    public List<Boundary> getBoundaries() {
        List<Boundary> boundaries = new ArrayList<>();

        // Create boundaries for trees
        for (double[] pos : trees) {
            boundaries.add(new Boundary(pos[0], pos[1], 50, 60)); // Assuming trees are 50x60
        }
        // Create boundaries for rocks
        for (double[] pos : rocks) {
            boundaries.add(new Boundary(pos[0], pos[1], 50, 40)); // Assuming rocks are 50x40
        }
        // Create boundaries for bushes
        for (double[] pos : bushes) {
            boundaries.add(new Boundary(pos[0], pos[1], 50, 30)); // Assuming bushes are 50x30
        }
        // Create boundaries for flowers
        for (double[] pos : flowers) {
            boundaries.add(new Boundary(pos[0], pos[1], 20, 40)); // Assuming flowers are 20x40
        }

        return boundaries;
    }
}
