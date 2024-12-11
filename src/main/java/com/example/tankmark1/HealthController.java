package com.example.tankmark1;

import com.example.tankmark1.tanks.Tank;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class HealthController {
  Pane pane;
    private ProgressBar healthBar1, healthBar2;
    private Text healthText1, healthText2;
    private Text winnerText;
    public Tank tank1;
    public Tank tank2;
    HealthController(Pane pane,Tank tank1,Tank tank2)
    {
        this.pane=pane;
        this.tank1=tank1;
        this.tank2=tank2;
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
        double health1 = tank1.getHealth();
        healthBar1.setProgress(health1 / 100.0);
        healthText1.setText((int) health1 + "%");

        // Update Player 2 health bar and text
        double health2 = tank2.getHealth();
        healthBar2.setProgress(health2 / 100.0);
        healthText2.setText((int) health2 + "%");
    }
}
