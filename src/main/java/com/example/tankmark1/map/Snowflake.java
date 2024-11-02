package com.example.tankmark1.map;

public class Snowflake {
    private double x;
    private double y;
    private double speed; // Speed of the snowflake
    private double size;  // Size of the snowflake

    public Snowflake(double x, double y, double speed, double size) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.size = size;
    }

    public void update() {
        y += speed; // Move the snowflake down by its speed
        if (y > SnowMap.HEIGHT) {
            y = -size; // Reset snowflake to the top if it falls below the screen
            x = Math.random() * SnowMap.WIDTH; // Random horizontal position
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }
}
