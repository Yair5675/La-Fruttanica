package com.example.lafruttanica.gameobjects;

import android.graphics.Canvas;

/**
 * Abstract class for all objects in the game.
 */
public abstract class GameObject {
    protected double x; /* X position of the object */
    protected double y; /* Y position of the object */
    protected double velX; /* Velocity on the x axis */
    protected double velY; /* Velocity on the y axis */


    public GameObject(double x, double y, double velX, double velY) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
    }

    public abstract void draw(Canvas canvas);

    public abstract void update();

    public void move() {
        this.x += this.velX;
        this.y += this.velY;
    }
}
