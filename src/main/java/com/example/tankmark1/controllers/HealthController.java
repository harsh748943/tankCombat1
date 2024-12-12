package com.example.tankmark1.controllers;

import com.example.tankmark1.TankGame;
import com.example.tankmark1.tanks.Tank;
import javafx.application.Platform;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class HealthController {
  Pane pane;
    private ProgressBar healthBar1, healthBar2;
    private Text healthText1, healthText2;
    private Text winnerText;

    GameSoundManager gameSoundManager;
    private Thread movementThread;
    private TankGame mainApp;
    boolean gameOver[];
    ArrayList<Tank>tanks;
    //HealthController(){};
    HealthController(Pane pane, ArrayList<Tank>tanks, GameSoundManager gameSoundManager, Thread movementThread, TankGame mainApp, boolean gameOver[])
    {
        this.pane=pane;

        this.gameSoundManager=gameSoundManager;
        this.movementThread=movementThread;
        this.mainApp=mainApp;
        this.gameOver=gameOver;
        this.tanks=tanks;
    }

    public void setUpHealthBars() {
        // Player 1 Label
        Text player1Label = new Text("Player 1");
        player1Label.setFont(new Font(18));
        player1Label.setFill(Color.RED);
        player1Label.setLayoutX(10);  // Position above Player 1's health bar
        player1Label.setLayoutY(25);

        // Player 1 Health Bar
        healthBar1 = new ProgressBar(1);
        healthBar1.setStyle("-fx-accent: red;");
        healthText1 = new Text("100%");
        HBox player1HealthBox = new HBox(5, healthBar1, healthText1);
        player1HealthBox.setLayoutX(10);
        player1HealthBox.setLayoutY(40);

        // Player 2 Label
        Text player2Label = new Text("Player 2");
        player2Label.setFont(new Font(18));
        player2Label.setFill(Color.BLUE);
        player2Label.setLayoutX(1400);  // Position above Player 2's health bar
        player2Label.setLayoutY(25);

        // Player 2 Health Bar
        healthBar2 = new ProgressBar(1);
        healthBar2.setStyle("-fx-accent: BLUE;");
        healthText2 = new Text("100%");
        HBox player2HealthBox = new HBox(5, healthText2, healthBar2);
        player2HealthBox.setLayoutX(1400);
        player2HealthBox.setLayoutY(40);

        // Add to the scene
        pane.getChildren().addAll(player1Label, player1HealthBox, player2Label, player2HealthBox);

        // Winner Text
        winnerText = new Text();
        winnerText.setFill(Color.RED);
        winnerText.setLayoutX(350);
        winnerText.setLayoutY(50);
        winnerText.setVisible(false);
        pane.getChildren().add(winnerText);
    }

    public void updateHealthBars() {
        // Update Player 1 health bar and text
        double health1 = tanks.get(0).getHealth();
        healthBar1.setProgress(health1 / 100.0);
        healthText1.setText((int) health1 + "%");

        // Update Player 2 health bar and text
        double health2 = tanks.get(1).getHealth();
        healthBar2.setProgress(health2 / 100.0);
        healthText2.setText((int) health2 + "%");
    }

    public void checkForWin() {
        if (tanks.get(0).isDestroyed()) {
            // Show destroyed image for tank1
            tanks.get(0).setImage(new Image("/tankBlast.png"));
            showWinningMessage("Player 2 Wins!","Press Enter to exit.");
            endGame();
        } else if (tanks.get(1).isDestroyed()) {
            // Show destroyed image for tank2
            tanks.get(1).setImage(new Image("/tankBlast1.png"));
            showWinningMessage("Player 1 Wins!","Press Enter to exit.");
            endGame();
        }
    }

    private void showWinningMessage(String message,String message1) {
        Text winningMessage = new Text(message);
        Text winningMessage1 = new Text(message1);
        winningMessage.setStyle(
                "-fx-font-size: 55px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #000000; " +  // Green color for the text
                        "-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.75), 10, 0.5, 0, 2); " +  // Shadow effect
                        "-fx-background-color: rgba(255, 0, 0, 0.5); " +  // Semi-transparent dark background
                        "-fx-padding: 20; " +
                        "-fx-background-radius: 10; "
        );
        winningMessage1.setStyle(
                "-fx-font-size: 25px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #FF0000;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255,0,0,0.75), 10, 0.5, 0, 2); " +  // Shadow effect
                        "-fx-background-color: rgba(255, 0, 0, 0.5); " +  // Semi-transparent dark background
                        "-fx-padding: 20; " +
                        "-fx-background-radius: 10; "
        );

        // Set the position to center
        winningMessage.setX(600);  // X-coordinate for centering
        winningMessage.setY(400);  // Y-coordinate for centering
        winningMessage1.setX(670);  // X-coordinate for centering
        winningMessage1.setY(470);  // Y-coordinate for centering


        // Optional: Add the winning message to the root or main pane
        pane.getChildren().add(winningMessage);
        pane.getChildren().add(winningMessage1);
    }

    public void endGame() {
        // Set game over flag
        gameOver[0] = true;

        // Stop movement and projectile threads
        if (movementThread != null && movementThread.isAlive()) {
            movementThread.interrupt();
        }

        gameSoundManager.stopMusic();

        // Display winner message and await Enter key to return to menu
        Platform.runLater(() -> {
            // Disable tank controls to prevent further movement
            pane.setOnKeyPressed(null);
            pane.setOnKeyReleased(null);

            // Set a listener for the Enter key to return to the menu
            pane.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    mainApp.returnToMenu();
                }
            });
        });
    }

}
