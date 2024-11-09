package com.example.tankmark1.tanks;

import com.example.tankmark1.GameController;
import com.example.tankmark1.map.DestructibleObject;
import javafx.application.Platform;

import java.util.List;
import java.util.Random;

public class ComputerTank {
    private Tank computerTank;
    private Tank tank1;
    private double moveSpeed;
    private long shootInterval;
    private boolean isRunning = true;
    private GameController gameController;
    private List<DestructibleObject> destructibleObjects;


    public ComputerTank(Tank computerTank, Tank targetTank, GameController gameController, String level, List<DestructibleObject> destructibleObjects) {
        this.computerTank = computerTank;
        this.tank1 = targetTank;
        setDifficulty(level);
        this.gameController=gameController;
        this.destructibleObjects=destructibleObjects;


    }

    private void setDifficulty(String level) {
        switch (level) {
            case "Easy":
                moveSpeed = 2;
                shootInterval = 3000;
                break;
            case "Hard":
                moveSpeed = 5.5;
                shootInterval = 500;
                break;
            default:
                moveSpeed = 3.5;
                shootInterval = 1500;
                break;
        }
    }

    public void setComputerControlled() {
        new Thread(() -> {
            Random random = new Random();
            long lastShootTime = 0;
            long lastMoveTime = 0;





            while (!gameController.gameOver) {  // Check game over status
                try {
                    // Wait until countdown is complete before starting tank actions
                    if (!gameController.countdownComplete) {
                        Thread.sleep(100); // Small sleep to prevent tight looping
                        continue; // Skip the rest of the loop if countdown is not complete
                    }

                    long currentTime = System.currentTimeMillis();
                    boolean shouldMove = currentTime - lastMoveTime > 100;  // Move every 0.1 second (smooth)
                    boolean shouldShoot = currentTime - lastShootTime > shootInterval;  // Adjusted shooting interval

                    boolean tank1Destroyed = tank1 == null || tank1.isDestroyed();
                    if (tank1Destroyed) {
                        shouldShoot = false;
                    }

                    if (shouldMove) {
                        final double[] dx = {0}, dy = {0};  // Final dx and dy for lambda expression

                        if (!tank1Destroyed && tank1 != null) {
                            // Calculate direction to tank1
                            double angleToTank1 = Math.toDegrees(Math.atan2(
                                    tank1.getY() - computerTank.getY(),
                                    tank1.getX() - computerTank.getX()
                            ));

                            // Distance from tank1 to tank2
                            double distanceToTank1 = Math.sqrt(
                                    Math.pow(tank1.getX() - computerTank.getX(), 2) +
                                            Math.pow(tank1.getY() - computerTank.getY(), 2)
                            );

                            if (distanceToTank1 < 200) {  // If tank1 is very close, dodge
                                // Move in a random direction
                                dx[0] = random.nextDouble() * 1 - 1;  // Random between -1 and 1
                                dy[0] = random.nextDouble() * 1 - 1;
                            } else {
                                // Otherwise, move towards tank1
                                double radians = Math.toRadians(angleToTank1);
                                dx[0] = Math.cos(radians) * moveSpeed;  // Move with adjusted speed
                                dy[0] = Math.sin(radians) * moveSpeed;
                            }

                            // Rotate the tank to face tank1
                            Platform.runLater(() -> computerTank.setRotate(angleToTank1));
                        } else {
                            // Random movement if tank1 is destroyed or far away
                            dx[0] = random.nextDouble() * 1 - 1;  // Random between -1 and 1
                            dy[0] = random.nextDouble() * 1 - 1;
                        }

                        // Move the tank using the updated dx and dy values
                        Platform.runLater(() -> computerTank.move(dx[0], dy[0], destructibleObjects));
                        lastMoveTime = currentTime;  // Update last move time
                    }

                    if (shouldShoot && tank1 != null && !tank1Destroyed) {
                        // Shoot after the specified interval
                        Platform.runLater(() -> computerTank.shoot(gameController));
                        lastShootTime = currentTime;  // Update last shoot time
                    }

                    // Sleep to allow for smoother movement and shooting
                    Thread.sleep(50);  // 50ms delay to make the movement smooth

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }

}

