package com.example.tankmark1;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class StartMenu extends GridPane {
    private ComboBox<String> playerSelector;
    private ComboBox<String> weaponSelector;
    private ComboBox<String> mapSelector;
    private CheckBox soundCheckbox;

    public StartMenu(Stage primaryStage) {
        setPadding(new Insets(20));
        setVgap(10);
        setHgap(10);

        // Player selection
        Label playerLabel = new Label("Number of Players:");
        playerSelector = new ComboBox<>();
        playerSelector.getItems().addAll("1", "2");
        playerSelector.setValue("1");

        // Weapon selection
        Label weaponLabel = new Label("Select Weapon:");
        weaponSelector = new ComboBox<>();
        weaponSelector.getItems().addAll("Cannon", "Missile", "Laser");
        weaponSelector.setValue("Cannon");

        // Map selection
        Label mapLabel = new Label("Select Map:");
        mapSelector = new ComboBox<>();
        mapSelector.getItems().addAll("Forest", "Desert", "Snow");
        mapSelector.setValue("Forest");

        // Sound toggle
        soundCheckbox = new CheckBox("Sound On/Off");
        soundCheckbox.setSelected(true); // Default is sound on

        // Start game button
        Button startButton = new Button("Start Game");
        startButton.setOnAction(e -> startGame(primaryStage));

        // Adding components to the layout
        add(playerLabel, 0, 0);
        add(playerSelector, 1, 0);
        add(weaponLabel, 0, 1);
        add(weaponSelector, 1, 1);
        add(mapLabel, 0, 2);
        add(mapSelector, 1, 2);
        add(soundCheckbox, 0, 3, 2, 1);
        add(startButton, 0, 4, 2, 1);
    }

    private void startGame(Stage primaryStage) {
        System.out.println("Start game");
        int numPlayers = Integer.parseInt(playerSelector.getValue());
        String selectedWeapon = weaponSelector.getValue();
        String selectedMap = mapSelector.getValue();
        boolean soundOn = soundCheckbox.isSelected();

        // Pass these settings to the game controller
        GameController gameController = new GameController(numPlayers, selectedWeapon, selectedMap, soundOn);
        Scene scene = new Scene(gameController, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tank Game - " + selectedMap);
    }
}
