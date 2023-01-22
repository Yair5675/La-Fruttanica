package com.example.lafruttanica.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Abstract class for all objects in the game.
 */
public abstract class GameObject {
    protected double x; /* X position of the object */
    protected double y; /* Y position of the object */
    protected double velX; /* Velocity on the x axis */
    protected double velY; /* Velocity on the y axis */
    protected Bitmap image; /* The image of the object */

    public GameObject() {
        this.x = 0;
        this.y = 0;
        this.velX = 0;
        this.velY = 0;
        this.image = null;
    }

    public GameObject(double x, double y, double velX, double velY) {
        this.x = x;
        this.y = y;
        this.velX = velX;
        this.velY = velY;
        this.image = null;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, (float) this.x, (float) this.y, new Paint());
    }

    public abstract void update();

    public void move() {
        this.x += this.velX;
        this.y += this.velY;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public int getWidth() {
        return this.image.getWidth();
    }

    public int getHeight() {
        return this.image.getHeight();
    }

    public boolean collides(GameObject other) {
        return this.x < other.x + other.getWidth() &&
               this.x + this.getWidth() > other.x &&
               this.y < other.y + other.getHeight() &&
               this.y + this.getHeight() > other.y;
    }
}
