package com.example.tankmark1.map;// Tree.java
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class Tree extends ImageView {
    private int health = 100;

    public Tree(double x, double y, String imagePath) {
        super(Tree.class.getResource("/" + imagePath).toExternalForm());
        setX(x);
        setY(y);
        setFitWidth(50);  // Adjust size as needed
        setFitHeight(50);
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            destroy();
        }
    }

    private void destroy() {
        if (getParent() instanceof Pane) {
            ((Pane) getParent()).getChildren().remove(this);
        }
    }
}
