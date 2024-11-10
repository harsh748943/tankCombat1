package com.example.tankmark1.weapons;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class WeaponExhibition {

    private final Map<String, String> weaponDescriptions = new HashMap<>();
    private final Map<String, Image> weaponImages = new HashMap<>();
    private final Map<String, Integer> weaponDamage = new HashMap<>();


    public WeaponExhibition() {
        // Initialize weapon descriptions
        weaponDescriptions.put("Cannon", "A standard explosive bomb with low impact but fast.");
        weaponDescriptions.put("Missile", "A powerful guided weapon with a long range.");
        weaponDescriptions.put("Laser", "A precise energy weapon with instant hit.");
        weaponDescriptions.put("Demon", "A Monster weapon with mass Destruction.");
        weaponDescriptions.put("Gamma-Blaster", "A Missile full of gamma radiation.");

        // Load weapon images (ensure these images are accessible in your resources)
        weaponImages.put("Cannon", new Image(getClass().getResource("/weapons_png/bomb.png").toExternalForm()));
        weaponImages.put("Missile", new Image(getClass().getResource("/weapons_png/rocket.png").toExternalForm()));
        weaponImages.put("Laser", new Image(getClass().getResource("/weapons_png/bigtorpedo.png").toExternalForm()));
        weaponImages.put("Demon", new Image(getClass().getResource("/weapons_png/demon.png").toExternalForm()));
        weaponImages.put("Gamma-Blaster", new Image(getClass().getResource("/weapons_png/gamma_blaster.png").toExternalForm()));

        // Define weapon damage
        weaponDamage.put("Cannon", 15);
        weaponDamage.put("Missile", 20);
        weaponDamage.put("Laser", 25);
        weaponDamage.put("Demon", 40);
        weaponDamage.put("Gamma-Blaster", 50);


    }

    public void showExhibition(Stage primaryStage) {
        // Create a new window (Stage) for the exhibition
        Stage exhibitionStage = new Stage();
        exhibitionStage.setTitle("Artillery");

        // Main layout

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(15));
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setStyle("-fx-background-color: #2c3e50;");


        // Title
        Label title = new Label("Artillery");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        // Display each weapon with its details
        for (String weaponName : weaponDescriptions.keySet()) {
            HBox weaponDisplay = createWeaponCard(weaponName);
            mainLayout.getChildren().add(weaponDisplay);
        }

        // Close button
        Button closeButton = new Button("Close Artillery");
        closeButton.setStyle("-fx-background-color: #ff5722; -fx-text-fill: white;");
        closeButton.setOnAction(e -> exhibitionStage.close());

        // Add title and close button to layout
        mainLayout.getChildren().addAll(title, closeButton);
        ScrollPane scrollPane = new ScrollPane(mainLayout);
        scrollPane.setFitToWidth(true);
        // Create and set the scene
        Scene scene = new Scene(scrollPane, 700, 700);
        exhibitionStage.setScene(scene);
        exhibitionStage.initOwner(primaryStage);
        exhibitionStage.show();
    }

    private HBox createWeaponCard(String weaponName) {
        // Weapon card container
        HBox weaponContainer = new HBox(15);
        weaponContainer.setAlignment(Pos.CENTER_LEFT);
        weaponContainer.setPadding(new Insets(10));
        weaponContainer.setStyle("-fx-background-color: #34495e; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Weapon icon
        ImageView weaponIcon = new ImageView(weaponImages.get(weaponName));
        weaponIcon.setFitWidth(150);
        weaponIcon.setFitHeight(100);

        // Weapon details (name, description, damage, special property)
        VBox weaponDetails = new VBox(5);
        Label weaponTitle = new Label(weaponName);
        weaponTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        weaponTitle.setTextFill(Color.ORANGE);

        Label weaponDescription = new Label(weaponDescriptions.get(weaponName));
        weaponDescription.setFont(Font.font("Arial", 14));
        weaponDescription.setTextFill(Color.LIGHTGRAY);
        weaponDescription.setWrapText(true);

        Label weaponDamageLabel = new Label("Damage: " + weaponDamage.get(weaponName));
        weaponDamageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        weaponDamageLabel.setTextFill(Color.RED);



        // Add name, description, damage, and special property to details box
        weaponDetails.getChildren().addAll(weaponTitle, weaponDescription, weaponDamageLabel);

        // Add icon and details to main container
        weaponContainer.getChildren().addAll(weaponIcon, weaponDetails);

        return weaponContainer;
    }
}
