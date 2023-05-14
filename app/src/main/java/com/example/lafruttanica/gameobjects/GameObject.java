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
    protected final int SCREEN_WIDTH; /* The width of the screen */
    protected final int SCREEN_HEIGHT; /* The height of the screen */
    private static final double GRAVITY = 4; /* The gravity enacted on the player */
    private static final double MAX_SPEED = 70; /* The maximum possible speed of the player */

    // To make the game compatible for every device, the movement of objects will be multiplied by this ratio:
    protected final double screenRatioX;
    protected final double screenRatioY;

    public GameObject(final int SCREEN_WIDTH, final int SCREEN_HEIGHT) {
        this.x = 0;
        this.y = 0;
        this.velX = 0;
        this.velY = 0;
        this.image = null;
        this.screenRatioX = 1080.0 / SCREEN_WIDTH;
        this.screenRatioY = 2186.0 / SCREEN_HEIGHT;
        this.SCREEN_WIDTH = SCREEN_WIDTH;
        this.SCREEN_HEIGHT = SCREEN_HEIGHT;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(this.image, (float) this.x, (float) this.y, new Paint());
    }

    public abstract void update();

    protected void move() {
        // Making sure the object's x-velocity doesn't exceeds the maximum allowed speed:
        if (this.velX > 0)
            this.velX = Math.min(this.velX, MAX_SPEED * screenRatioX);
        else
            this.velX = Math.max(this.velX, -MAX_SPEED * screenRatioX);

        // Making sure the object's y-velocity doesn't exceeds the maximum allowed speed:
        if (this.velY > 0)
            this.velY = Math.min(this.velY, MAX_SPEED * screenRatioY);
        else
            this.velY = Math.max(this.velY, -MAX_SPEED * screenRatioY);

        // Applying gravity:
        this.fall();

        // Updating the object's coordinates:
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

    /**
     * Function for collision detecting between two objects.
     */
    public boolean collides(GameObject other) {
        return this.x < other.x + other.getWidth() &&
               this.x + this.getWidth() > other.x &&
               this.y < other.y + other.getHeight() &&
               this.y + this.getHeight() > other.y;
    }

    /**
     * Applies gravity to the object.
     */
    protected void fall() {
        this.velY += GRAVITY * screenRatioY;
    }
}
