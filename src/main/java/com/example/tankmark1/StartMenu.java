package com.example.tankmark1;

import com.example.tankmark1.weapons.WeaponExhibition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.ListCell;

public class StartMenu extends GridPane {
    private ComboBox<String> playerSelector;
    private ComboBox<String> weaponSelector;
    private ComboBox<String> mapSelector;
    private CheckBox soundCheckbox;

    public StartMenu(Stage primaryStage, TankGame mainApp) {
        // Set padding, spacing, and background image
        setPadding(new Insets(20));
        setVgap(15);
        setHgap(15);

        // Load and set the background image
        BackgroundImage backgroundImage = new BackgroundImage(
                new javafx.scene.image.Image("TankBG4.jpg"),  // Load the image without preset dimensions
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(
                        BackgroundSize.AUTO,
                        BackgroundSize.AUTO,
                        false,
                        false,
                        true,   // Scale width to fit screen
                        true    // Scale height to fit screen
                )
        );
        setBackground(new Background(backgroundImage));

        // Title styling
        Label title = new Label("Tank Game - Main Menu");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        // Player selection
        Label playerLabel = new Label("Number of Players:");
        playerLabel.setTextFill(Color.WHITE);
        playerLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        playerSelector = new ComboBox<>();
        playerSelector.getItems().addAll("1", "2");
        playerSelector.setValue("1");

        // Style ComboBox and ensure white text for selected item
        playerSelector.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-color: #333333;" +
                        "-fx-text-fill: #FFFFFF;" +               // White text color for the selected item
                        "-fx-border-color: #777777;" +
                        "-fx-border-width: 1;"
        );

        // Ensure white text for selected item in dropdown
        playerSelector.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setStyle("-fx-text-fill: white;");  // White text for selected item
                }
            }
        });

        // Weapon selection
        Label weaponLabel = new Label("Select Weapon:");
        weaponLabel.setTextFill(Color.WHITE);
        weaponLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        weaponSelector = new ComboBox<>();
        weaponSelector.getItems().addAll("Cannon", "Missile", "Laser");
        weaponSelector.setValue("Cannon");

        // Style ComboBox and ensure white text for selected item
        weaponSelector.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-color: #333333;" +
                        "-fx-text-fill: #FFFFFF;" +               // White text color for the selected item
                        "-fx-border-color: #777777;" +
                        "-fx-border-width: 1;"
        );

        // Ensure white text for selected item in dropdown
        weaponSelector.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setStyle("-fx-text-fill: white;");  // White text for selected item
                }
            }
        });

        // Map selection
        Label mapLabel = new Label("Select Map:");
        mapLabel.setTextFill(Color.WHITE);
        mapLabel.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        mapSelector = new ComboBox<>();
        mapSelector.getItems().addAll("Forest", "Desert", "Snow");
        mapSelector.setValue("Forest");

        // Style ComboBox and ensure white text for selected item
        mapSelector.setStyle(
                "-fx-font-family: Arial, sans-serif;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-color: #333333;" +
                        "-fx-text-fill: #FFFFFF;" +               // White text color for the selected item
                        "-fx-border-color: #777777;" +
                        "-fx-border-width: 1;"
        );

        // Ensure white text for selected item in dropdown
        mapSelector.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null) {
                    setText(item);
                    setStyle("-fx-text-fill: white;");  // White text for selected item
                }
            }
        });

        // Sound toggle
        soundCheckbox = new CheckBox("Sound On/Off");
        soundCheckbox.setSelected(true); // Default is sound on
        soundCheckbox.setTextFill(Color.WHITE);
        soundCheckbox.setFont(Font.font("Arial", 14));

        // Start game button
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-background-color: #ff5722; -fx-text-fill: white; -fx-font-weight: bold;");
        startButton.setOnAction(e -> mainApp.startGame(
                Integer.parseInt(playerSelector.getValue()),
                weaponSelector.getValue(),
                mapSelector.getValue(),
                soundCheckbox.isSelected()
        ));
        // In your StartMenu class
        Button exhibitionButton = new Button("Weapon Exhibition");
        exhibitionButton.setOnAction(e -> new WeaponExhibition().showExhibition(primaryStage));

// Add the button to the layout
        add(exhibitionButton, 0, 6, 2, 1);


        // Adding components to the layout
        add(title, 0, 0, 2, 1);
        add(playerLabel, 0, 1);
        add(playerSelector, 1, 1);
        add(weaponLabel, 0, 2);
        add(weaponSelector, 1, 2);
        add(mapLabel, 0, 3);
        add(mapSelector, 1, 3);
        add(soundCheckbox, 0, 4, 2, 1);
        add(startButton, 0, 5, 2, 1);

        // Center alignment for items
        setAlignment(Pos.CENTER);
    }
}