package com.example.tankmark1.controllers;

import com.example.tankmark1.map.DestructibleObject;
import com.example.tankmark1.tanks.Tank;
import com.example.tankmark1.weapons.Explosion;
import com.example.tankmark1.weapons.Projectile;
import javafx.application.Platform;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class ProjectileController {
    private List<Projectile> projectiles ;
    private GameController gameController;
    private List<DestructibleObject> destructibleObjects;
    public ArrayList<Tank>tanks;
    HealthController healthController;

      ProjectileController(List<Projectile> projectiles,GameController gameController,List<DestructibleObject> destructibleObjects,ArrayList<Tank>tanks,HealthController healthController){
          this.projectiles=projectiles;
          this.gameController=gameController;
          this.destructibleObjects=destructibleObjects;
          this.tanks=tanks;
          this.healthController=healthController;

      }


    public void startProjectileThread() {
        new Thread(() -> {
            while (true) {
                Platform.runLater(this::updateProjectiles);
                try {
                    Thread.sleep(16); // Update approximately 60 FPS
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }).start();
    }
    public void updateProjectiles() {
        for (Projectile projectile : new ArrayList<>(projectiles)) {
            projectile.move();


            // Check for collisions with other projectiles
            for (Projectile other : new ArrayList<>(projectiles)) {
                if (projectile != other && projectile.collidesWith(other)) {
                    // Create an explosion at the point of collision
                    double collisionX = (projectile.getX() + other.getX()) / 2;
                    double collisionY = (projectile.getY() + other.getY()) / 2;

                    double adjustedX = collisionX - (256 / 2);  // Center the explosion
                    double adjustedY =collisionY - (256 / 2); // Center the explosion
                    Explosion explosion = new Explosion(
                            adjustedX, adjustedY,
                            "/explosion_sprite_Sheets/explosionL1.png",
                            "/explosionSound.mp3",
                            8,
                            7,
                            256,
                            256,256,256,
                            gameController, true
                    );
                    Platform.runLater(() -> gameController.getChildren().add(explosion));


                    // Remove both projectiles on collision
                    removeProjectile(projectile);
                    removeProjectile(other);
                    break;
                }
            }



            for (DestructibleObject destructible : new ArrayList<>(destructibleObjects)) {
                if (checkCollisionForObject(projectile, destructible)) {
                    destructible.takeDamage(projectile.getDamage());


                    createExplosion(projectile);

                    // Remove projectile after collision
                    removeProjectile(projectile);

                    // Remove destructible object from list and scene if destroyed
                    if (destructible.getHealth() <= 0) {
                        removeDestructibleObject(destructible);
                    }

                    break;
                }
            }




            if (tanks.get(0) != null && checkCollision(projectile, tanks.get(0)) && projectile.getOwner() != tanks.get(0)) {
                if (tanks.get(0).isShieldActive()) {
                    // Reduce shield strength instead of health
                    tanks.get(0).takeDamageToS(projectile.getDamage());
                    if (tanks.get(0).getShieldStrength() <= 0) {
                        tanks.get(0).deactivateShield(); // Deactivate shield if its strength reaches zero
                    }
                } else {
                    // If shield is not active, apply damage directly to health
                    tanks.get(0).takeDamageToS(projectile.getDamage());
                }
//                updatesb();
                healthController.updateHealthBars();
                removeProjectile(projectile);
                healthController.checkForWin();

                createExplosion(projectile);
            }

            if (tanks.get(1) != null && checkCollision(projectile, tanks.get(1)) && projectile.getOwner() != tanks.get(1)) {
                if (tanks.get(1).isShieldActive()) {
                    // Reduce shield strength instead of health
                    tanks.get(1).takeDamageToS(projectile.getDamage());
                    if (tanks.get(1).getShieldStrength() <= 0) {
                        tanks.get(1).deactivateShield(); // Deactivate shield if its strength reaches zero
                    }
                } else {
                    // If shield is not active, apply damage directly to health
                    tanks.get(1).takeDamageToS(projectile.getDamage());
                }
//                updatesb();
                healthController.updateHealthBars();
                removeProjectile(projectile);
                healthController.checkForWin();

                createExplosion(projectile);
            }


            if (isOutOfBounds(projectile)) {
                removeProjectile(projectile);
            }
        }
    }

    private void createExplosion(Projectile projectile) {
        double collisionX = (projectile.getX() + projectile.getX()) / 2;
        double collisionY = (projectile.getY() + projectile.getY()) / 2;

        double adjustedX = collisionX - (128/ 2);  // Center the explosion
        double adjustedY =collisionY - (128 / 2); // Center the explosion
//        double adjustedX = projectile.getX() - 128/2;
//        double adjustedY = projectile.getY() - 128/2;
        Explosion explosion = new Explosion(
                adjustedX, adjustedY,
                projectile.getExplosionImagePath(),
                projectile.getExplosionSoundPath(),
                projectile.getExplosionFrameColomn(),
                projectile.getExplosionFrameRow(),
                projectile.getExplosionFrameWidth(),
                projectile.getExplosionFrameHeight(),
                128,
                128,
                gameController, true
        );
        Platform.runLater(() -> gameController.getChildren().add(explosion));
    }

    private boolean checkCollisionForObject(Projectile projectile, DestructibleObject destructible) {
        return projectile.getBoundsInParent().intersects(destructible.getBoundsInParent());
    }

    private void removeProjectile(Projectile projectile) {
        projectiles.remove(projectile);
        gameController.getChildren().remove(projectile);
    }

    public void removeDestructibleObject(DestructibleObject destructibleObject) {
        Platform.runLater(() -> {
            gameController.getChildren().remove(destructibleObject); // Remove from the scene graph
            destructibleObjects.remove(destructibleObject); // Remove from tracking list
        });
    }

    private boolean checkCollision(Projectile projectile, Tank tank) {
        Rectangle projectileBounds = new Rectangle(projectile.getX(), projectile.getY(), projectile.getFitWidth(), projectile.getFitHeight());
        Rectangle tankBounds = new Rectangle(tank.getX(), tank.getY(), tank.getFitWidth(), tank.getFitHeight());
        return projectileBounds.getBoundsInParent().intersects(tankBounds.getBoundsInParent());
    }

    private boolean isOutOfBounds(Projectile projectile) {
        return projectile.getX() < 0 || projectile.getY() < 0 || projectile.getX() > gameController.getWidth() || projectile.getY() > gameController.getHeight();
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
        Platform.runLater(() -> gameController.getChildren().add(projectile));
    }
}
